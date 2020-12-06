/*
 * 作成日: 2003/10/30
 * 
 */
package com.nullfish.lib.vfs.manipulation.abst;

import java.text.MessageFormat;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.UpdateManager;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.ManipulationStoppedException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.manipulation.DeleteFailurePolicy;
import com.nullfish.lib.vfs.manipulation.DeleteManipulation;
import com.nullfish.lib.vfs.tag_db.TagDataBase;
import com.nullfish.lib.vfs.tag_db.command.FileRemovedCommand;

/**
 * @author shunji
 * 
 */
public abstract class AbstractDeleteManipulation extends AbstractManipulation
		implements DeleteManipulation {

	int progress = 0;

	int maxProgress = -1;

	DeleteManipulation[] childManipulations;

	/**
	 * メッセージのパラメータ
	 */
	protected VFile[] messageParam;

	/**
	 * 経過フォーマット
	 */
	private MessageFormat progressFormat;

	/**
	 * 削除ポリシー
	 */
	protected DeleteFailurePolicy policy;
	
	/**
	 * コンストラクタ
	 * 
	 * @param file
	 */
	public AbstractDeleteManipulation(VFile file) {
		super(file);
		messageParam = new VFile[1];
		messageParam[0] = file;
	}

	/**
	 * 操作の前処理を行う。
	 */
	public void doPrepare() throws VFSException {
		if (file.isDirectory(this)) {
			//	ディレクトリの削除
			VFile[] children = file.getChildren(this);
			childManipulations = new DeleteManipulation[children.length];
			for (int i = 0; i < children.length; i++) {
					childManipulations[i] = children[i].getManipulationFactory()
							.getDeleteManipulation(children[i]);
					childManipulations[i].setDeleteFailurePolicy(policy);
					childManipulations[i].setParentManipulation(this);
					childManipulations[i].prepare();
			}
		}
	}

	/**
	 * ファイルを削除する。
	 *  
	 */
	public void doExecute() throws VFSException {
		for (int i = 0; childManipulations != null
				&& i < childManipulations.length; i++) {
			if (isStopped()) {
				throw new ManipulationStoppedException(this);
			}

			childManipulations[i].execute();
		}

		while(true) {
			try {
				if (file.isDirectory(this)) {
					doDeleteDirectory(file);
				} else {
					doDeleteFile(file);
				}
				
				//	属性キャッシュの削除
				file.clearFileAttribute();
		
				UpdateManager.getInstance().fileDeleted(file);
				
				TagDataBase tagDataBase = file.getFileSystem().getVFS().getTagDataBase();
				if(tagDataBase != null) {
					tagDataBase.addCommand(new FileRemovedCommand(file));
				}
				
				return;
			} catch (ManipulationStoppedException e) {
				throw e;
			} catch (VFSException e) {
				if(policy == null) {
					throw e;
				}
				
				switch(policy.getDeleteFailurePolicy(file)) {
				case DeleteFailurePolicy.IGNORE : return;
				case DeleteFailurePolicy.RETRY : continue;
				case DeleteFailurePolicy.FAIL : 
				default:
					throw e;
				}
			}
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileManipulation#getProgress()
	 */
	public long getProgress() {
		if (!isPrepared()) {
			return PROGRESS_INDETERMINED;
		}

		long progress = isFinished() ? 0 : 1;
		for (int i = 0; childManipulations != null
				&& i < childManipulations.length; i++) {
			progress += childManipulations[i].getProgress();
		}

		return progress;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileManipulation#getProgressMax()
	 */
	public long getProgressMax() {
		int progressMax = 1;
		for (int i = 0; childManipulations != null
				&& i < childManipulations.length; i++) {
			progressMax += childManipulations[i].getProgressMax();
		}

		return progressMax;
	}

	/**
	 * ファイルを削除する。
	 * 
	 * @param file
	 * @throws VFSException
	 */
	public abstract void doDeleteFile(VFile file) throws VFSException;

	/**
	 * ディレクトリを削除する。
	 * 
	 * @param file
	 * @throws VFSException
	 */
	public abstract void doDeleteDirectory(VFile file) throws VFSException;

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
						.getString(DeleteManipulation.NAME));
			}
			return progressFormat.format(messageParam);
		}
	}

	/**
	 * 削除方針をセットする。
	 * prepare前に実行すること。
	 * 
	 * @param policy
	 */
	public void setDeleteFailurePolicy(DeleteFailurePolicy policy) {
		this.policy = policy;
	}
	
}
