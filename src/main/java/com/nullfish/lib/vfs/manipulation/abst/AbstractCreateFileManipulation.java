/*
 * 作成日: 2003/11/13
 *
 */
package com.nullfish.lib.vfs.manipulation.abst;


import java.text.MessageFormat;

import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.UpdateManager;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.manipulation.CreateFileManipulation;


/**
 * @author shunji
 *
 */
public abstract class AbstractCreateFileManipulation
	extends AbstractManipulation 
	implements CreateFileManipulation {
	
	/**
	 * メッセージのパラメータ
	 */
	protected VFile[] messageParam;
	
	/**
	 * 経過フォーマット
	 */
	private MessageFormat progressFormat;
	
	FileType fileType;

	public AbstractCreateFileManipulation(VFile file) {
		super(file);
		messageParam = new VFile[1];
		messageParam[0] = file;
	}

	/* (非 Javadoc)
	 * @see org.sexyprogrammer.lib.vfs.manipulation.CreateFileManipulation#setType(com.sexyprogrammer.lib.vfs.FileType)
	 */
	public void setType(FileType fileType) {
		this.fileType = fileType;
	}

	protected FileType getFileType() {
		return fileType;
	}

	/**
	 * ファイルを生成する。
	 * @see com.nullfish.lib.vfs.FileManipulation#execute(com.sexyprogrammer.lib.vfs.VFSUser)
	 */
	public void doExecute() throws VFSException {
		file.clearFileAttribute();
		doCreateFile();
		UpdateManager.getInstance().fileCreated(file);
	}

	public abstract void doCreateFile() throws VFSException;
	
	/**
	 * 作業経過メッセージを取得する。
	 * @return
	 */
	public String getProgressMessage() {
		if(progressFormat == null) {
			progressFormat = new MessageFormat(progressMessages.getString(CreateFileManipulation.NAME));
		}
		return progressFormat.format(messageParam);
	}
}
