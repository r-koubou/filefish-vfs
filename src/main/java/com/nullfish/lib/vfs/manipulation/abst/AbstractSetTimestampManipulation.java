/*
 * Created on 2004/04/10
 *
 */
package com.nullfish.lib.vfs.manipulation.abst;

import java.util.Date;

import com.nullfish.lib.vfs.UpdateManager;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.manipulation.SetTimestampManipulation;


/**
 * @author shunji
 *
 */
public abstract class AbstractSetTimestampManipulation extends AbstractManipulation
		implements
			SetTimestampManipulation {

	Date date;

	
	/**
	 * コンストラクタ
	 * @param file
	 */
	public AbstractSetTimestampManipulation(VFile file) {
		super(file);
	}

	/**
	 * 日付をセットする。
	 * @see com.nullfish.lib.vfs.manipulation.SetTimestampManipulation#setDate(java.util.Date)
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractManipulation#doExecute()
	 */
	public void doExecute() throws VFSException {
		file.clearFileAttribute();
		doSetLastModified(file, date);
		UpdateManager.getInstance().fileChanged(file);
	}
	
	public abstract void doSetLastModified(VFile file, Date date) throws VFSException;

	/**
	 * 作業経過メッセージを取得する。
	 * 
	 * @return
	 */
	public String getProgressMessage() {
		return "";
	}
}
