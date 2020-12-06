/*
 * 作成日: 2003/11/14
 * 
 */
package com.nullfish.lib.vfs.manipulation.abst;


import java.text.MessageFormat;

import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.FileUtil;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.UpdateManager;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.ManipulationStoppedException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.exception.VFSSystemException;
import com.nullfish.lib.vfs.exception.WrongOverwriteException;
import com.nullfish.lib.vfs.exception.WrongPathException;
import com.nullfish.lib.vfs.manipulation.CopyFileManipulation;
import com.nullfish.lib.vfs.manipulation.MoveManipulation;
import com.nullfish.lib.vfs.manipulation.OverwritePolicy;
import com.nullfish.lib.vfs.tag_db.TagDataBase;
import com.nullfish.lib.vfs.tag_db.command.FileMovedCommand;
import com.nullfish.lib.vfs.tag_db.command.FileRecursiveMovedCommand;


/**
 * @author shunji
 * 
 */
public abstract class AbstractMoveManipulation
	extends AbstractManipulation
	implements MoveManipulation {

	/**
	 * リネーム先
	 */
	protected VFile dest;

	OverwritePolicy policy;

	/**
	 * メッセージのパラメータ
	 */
	protected VFile[] messageParam;
	
	/**
	 * 経過フォーマット
	 */
	private MessageFormat progressFormat;
	
	/**
	 * パーミッションのコピーを行うかのフラグ
	 */
	private boolean copiesPermission = true;

	/**
	 * コピー→削除の移動を行わないかのフラグ
	 */
	private boolean noCopy = false;
	
	/**
	 * コンストラクタ
	 * 
	 * @param file
	 */
	public AbstractMoveManipulation(VFile file) {
		super(file);
		messageParam = new VFile[1];
		messageParam[0] = file;
	}

	/**
	 * 改名先をセットする。
	 * 
	 * @see com.nullfish.lib.vfs.manipulation.RenameManipulation#setDest(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public void setDest(VFile dest) {
		this.dest = dest;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#execute(com.sexyprogrammer.lib.vfs.VFSUser)
	 */
	public void doExecute() throws VFSException {
		boolean movingStarted = false;
		try {
			//	属性キャッシュの削除
			file.clearFileAttribute();

			if (!file.exists(this)) {
				throw new FileNotExistsException(file);
			}
			
			//	子孫ファイルか確認する。
			checkDescendant();

			boolean overwriting = dest.exists(this);
			FileType fileType = file.getType(this);
			FileType destType = dest.getType(this);
			if (overwriting) {
				if (!fileType.equals(destType)) {
					throw new WrongOverwriteException(
						file,
						dest,
						WrongOverwriteException.DIFFERENT_FILE_TYPE);
				}

				if (fileType.equals(FileType.FILE)) {
					while (true) {
						int answer;
						if(policy != null) {
							policy.setFiles(file, dest);
							answer = policy.getOverwritePolicy(file, dest);
						} else {
							answer = OverwritePolicy.OVERWRITE;
						}

						if (answer == OverwritePolicy.NO_OVERWRITE) {
							return;
						} else if (answer == OverwritePolicy.STOP_ALL) {
							throw new ManipulationStoppedException(this);
						} else if (answer == OverwritePolicy.OVERWRITE) {
							break;
						} else if (answer == OverwritePolicy.NEW_DEST) {
							dest = policy.getNewDestination();
							if (!dest.exists()) {
								break;
							}
						} else {
							throw new VFSSystemException(
								"Wrong overwrite policy : " + answer);
						}
					}
				}
			}

			FileSystem fromFileSystem = file.getFileSystem();
			FileSystem destFileSystem = dest.getFileSystem();

			movingStarted = true;

			if (fromFileSystem.equals(destFileSystem)) {
				try {
					if (!doMoveFile(file, dest)) {
						throw new Exception();
					}

					TagDataBase tagDataBase = file.getFileSystem().getVFS().getTagDataBase();
					if(tagDataBase != null) {
						tagDataBase.addCommand(new FileMovedCommand(file, dest));
						tagDataBase.addCommand(new FileRecursiveMovedCommand(file, dest));
					}

					setFinished(true);
					UpdateManager.getInstance().fileDeleted(file);
					UpdateManager.getInstance().fileCreated(dest);
					return;
				} catch (Exception e) {
					if(noCopy) {
						throw new VFSIOException();
					}
				}
			}
			
			if(fileType == FileType.FILE) {
				CopyFileManipulation copyManipulation = FileUtil.prepareCopyTo(file, dest, policy, copiesPermission, this);
				copyManipulation.start();
				file.delete(this);
			} else {
				VFile[] children = file.getChildren(this);
				for(int i=0; i<children.length; i++) {
					MoveManipulation moveManipulation = FileUtil.prepareMoveTo(children[i], dest.getChild(children[i].getName()), policy, this);
					moveManipulation.start();
				}
				
				if(file.getChildren(this).length == 0) {
					file.delete(this);
				}
			}
			
			setFinished(true);

			UpdateManager.getInstance().fileDeleted(file);
			if(overwriting) {
				UpdateManager.getInstance().fileChanged(dest);
			} else {
				UpdateManager.getInstance().fileCreated(dest);
			}
		} finally {
			//	属性キャッシュの削除
			file.clearFileAttribute();
			dest.clearFileAttribute();
			
			if (movingStarted && !isFinished()) {
				if (file.exists(this) && dest.exists(this) && dest.isFile(this)) {
					try {
						dest.delete(this);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 移動先が子孫ファイルか確認し、もしそうなら例外を投げる。
	 * 
	 * @throws WrongPathException
	 */
	private void checkDescendant() throws WrongPathException {
		//	移動先が子孫ファイルなら中止
		VFile temp = dest.getParent();
		while(true) {
			if(temp == null) {
				break;
			}
			
			if(temp.equals(file)) { 
				throw new WrongPathException(dest.getAbsolutePath());
			}
			
			temp = temp.getParent();
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#getProgress()
	 */
	public long getProgress() {
		if (!isPrepared()) {
			return PROGRESS_INDETERMINED;
		}

		Manipulation current = getCurrentManipulation();
		if (current != this) {
			return current.getProgress();
		}

		return isFinished() ? 1 : 0;
	}
	
	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#getProgressMax()
	 */
	public long getProgressMax() {
		Manipulation current = getCurrentManipulation();
		if (current != this) {
			return current.getProgressMax();
		}

		return 1;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.manipulation.RenameManipulation#setOverwritePolicy(com.sexyprogrammer.lib.vfs.manipulation.OverwritePolicy)
	 */
	public void setOverwritePolicy(OverwritePolicy policy) {
		this.policy = policy;
	}

	public abstract boolean doMoveFile(VFile from, VFile dest)
		throws VFSException;

	/**
	 * 作業経過メッセージを取得する。
	 * 
	 * @return
	 */
	public String getProgressMessage() {
		Manipulation current = getCurrentManipulation();
		
		if (current != this) {
			return current.getProgressMessage();
		} else {
			if (progressFormat == null) {
				progressFormat = new MessageFormat(progressMessages
						.getString(MoveManipulation.NAME));
			}
			return progressFormat.format(messageParam);
		}
	}
	
	public void setCopiesPermission(boolean bool) {
		copiesPermission = bool;
	}

	/**
	 * パーティション内移動に失敗した際にコピー→削除での移動を行わないかを指定する。
	 * @param noCopy	trueなら行わない。
	 */
	public void setNoCopy(boolean noCopy) {
		this.noCopy = noCopy;
	}
}
