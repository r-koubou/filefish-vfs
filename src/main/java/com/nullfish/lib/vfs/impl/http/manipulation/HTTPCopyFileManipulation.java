package com.nullfish.lib.vfs.impl.http.manipulation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.text.MessageFormat;

import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.UpdateManager;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.ManipulationNotAvailableException;
import com.nullfish.lib.vfs.exception.ManipulationStoppedException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.exception.VFSSystemException;
import com.nullfish.lib.vfs.exception.WrongOverwriteException;
import com.nullfish.lib.vfs.manipulation.CopyFileManipulation;
import com.nullfish.lib.vfs.manipulation.CreateFileManipulation;
import com.nullfish.lib.vfs.manipulation.OverwritePolicy;
import com.nullfish.lib.vfs.manipulation.abst.AbstractManipulation;


/**
 * HTTPファイルシステム用ファイルコピー操作クラス。
 * 他と違い、ディレクトリでもファイルとしてコピーする。
 * 
 * @author Shunji Yamaura
 */
public class HTTPCopyFileManipulation
	extends AbstractManipulation
	implements CopyFileManipulation {
	/**
	 * ファイルサイズ
	 */
	protected int fileSize;

	/**
	 * コピー終了サイズ
	 */
	protected long copiedSize;

	/**
	 * 進行度最大値
	 */
	protected long max = 0;

	/**
	 * コピー先ファイル
	 */
	protected VFile dest;

	/**
	 * 上書き動作ポリシー
	 */
	protected OverwritePolicy policy;

	/**
	 * メッセージのパラメータ
	 */
	protected String[] messageParam;
	
	/**
	 * 経過フォーマット
	 */
	private MessageFormat progressFormat;
	
	/**
	 * パーミッションのコピーを行うかのフラグ
	 */
	private boolean copiesPermission = true;
	
	/**
	 * コンストラクタ
	 * 
	 * @param file
	 */
	public HTTPCopyFileManipulation(VFile file) {
		super(file);
		messageParam = new String[1];
		messageParam[0] = file.getName();
	}

	/**
	 * コピー先をセットする。
	 * 
	 * @see com.nullfish.lib.vfs.manipulation.CopyFileManipulation#setDest(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public void setDest(VFile dest) {
		this.dest = dest;
	}

	/**
	 * 操作の前処理を行う。 ファイル種類の判定、子ファイル、子操作の取得を行う。
	 */
	public void doPrepare() throws VFSException {
		if(!file.exists(this)) {
			throw new FileNotExistsException(file);
		}
		
		max = file.getLength(this);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#execute(com.sexyprogrammer.lib.vfs.VFSUser)
	 */
	public void doExecute() throws VFSException {
		boolean overwriting = dest.exists(this);
		if (overwriting) {
			if (!FileType.FILE.equals(dest.getType())) {
				throw new WrongOverwriteException(
					file,
					dest,
					WrongOverwriteException.DIFFERENT_FILE_TYPE);
			}

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

		dest.clearFileAttribute();
		dest.clearPermission();

		if (!overwriting && file.isDirectory(this)) {
			CreateFileManipulation createManipulation =
				dest.getManipulationFactory().getCreateFileManipulation(dest);
			createManipulation.setParentManipulation(this);
			createManipulation.setType(FileType.FILE);
			createManipulation.start();
		}
		
		if (isStopped()) {
			return;
		}

		copyFile(file, dest);

		setFinished(true);
		
		if(overwriting) {
			UpdateManager.getInstance().fileChanged(dest);
		} else {
			UpdateManager.getInstance().fileCreated(dest);
		}
	}

	/**
	 * ファイルをコピーする。コピー元、コピー先共にファイルが存在する必要がある。
	 * 
	 * @param from
	 *            コピー元
	 * @param to
	 *            コピー先
	 * @throws VFSException
	 */
	private void copyFile(VFile from, VFile to) throws VFSException {
		BufferedInputStream is = null;
		BufferedOutputStream os = null;

		boolean stopped = false;
		try {
			is = new BufferedInputStream(from.getInputStream());
			os = new BufferedOutputStream(to.getOutputStream());

			byte[] buffer = new byte[4096];

			int l = 0;

			while (true) {
				l = is.read(buffer);
				if (isStopped()) {
					throw new ManipulationStoppedException(this);
				}

				if (l < 0) {
					break;
				}

				os.write(buffer, 0, l);
				copiedSize += l;
			}

			//	パーミッションのコピー
			if(copiesPermission) {
				try {
					to.setPermission(from.getPermission(), this);
				} catch (ManipulationNotAvailableException e) {
					//	何もしない。
				}
			}

			//	タイムスタンプのコピー
			try {
				to.setTimestamp(from.getTimestamp(), this);
			} catch (ManipulationNotAvailableException e) {
				//	何もしない。
			}
		} catch (IOException e) {
			throw new VFSIOException(e);
		} catch (ManipulationStoppedException e) {
			stopped = true;
			throw e;
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				//e.printStackTrace();
			}
			try {
				os.flush();
			} catch (Exception e) {
				//e.printStackTrace();
			}
			try {
				os.close();
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			//	もしも中止されてたら、コピー先を削除する。
			if(stopped) {
				try {
					to.delete();
				} catch (Exception ex) {}
			}
			
			//	属性キャッシュの削除
			to.clearFileAttribute();
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

		if (isFinished()) {
			return max;
		}

		return copiedSize;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#getProgressMax()
	 */
	public long getProgressMax() {
		return max;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.manipulation.CopyFileManipulation#setOverwritePolicy(com.sexyprogrammer.lib.vfs.manipulation.OverwritePolicy)
	 */
	public void setOverwritePolicy(OverwritePolicy policy) {
		this.policy = policy;
	}

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
						.getString(CopyFileManipulation.NAME));
			}
			return progressFormat.format(messageParam);
		}
	}
	
	public void doStop() {
		getWorkThread().interrupt();
	}
	
	public void setCopiesPermission(boolean bool) {
		copiesPermission = bool;
	}
}
