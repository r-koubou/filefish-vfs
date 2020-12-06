/*
 * 作成日: 2003/11/17
 *
 */
package com.nullfish.lib.vfs.manipulation.abst;


import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.UpdateManager;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.ManipulationStoppedException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.manipulation.SetAttributeManipulation;


/**
 * @author shunji
 *
 */
public abstract class AbstractSetAttributeManipulation
	extends AbstractManipulation
	implements SetAttributeManipulation {

	FileAttribute attr;

	/**
	 * 
	 * @param file
	 */
	public AbstractSetAttributeManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.SetAttributeManipulation#setAttribute(com.sexyprogrammer.lib.vfs.FileAttribute)
	 */
	public void setAttribute(FileAttribute attr) {
		this.attr = attr;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#execute()
	 */
	public void doExecute() throws VFSException {
		if(isStopped()) {
			throw new ManipulationStoppedException(this);
		}

		if(!file.exists()) {
			throw new FileNotExistsException(file);
		}
		
		doSetAttribute(file, attr);
		UpdateManager.getInstance().fileChanged(file);
	}
	
	public abstract void doSetAttribute(VFile file, FileAttribute attr) throws VFSException;

	/**
	 * 作業経過メッセージを取得する。
	 * 
	 * @return
	 */
	public String getProgressMessage() {
		return "";
	}
}
