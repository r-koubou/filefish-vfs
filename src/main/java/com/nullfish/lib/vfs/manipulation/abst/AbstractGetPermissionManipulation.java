/*
 * 作成日: 2003/11/13
 *
 */
package com.nullfish.lib.vfs.manipulation.abst;


import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.manipulation.GetPermissionManipulation;


/**
 * @author shunji
 *
 */
public abstract class AbstractGetPermissionManipulation
	extends AbstractManipulation
	implements GetPermissionManipulation {

	Permission rtn;

	public AbstractGetPermissionManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.GetPermissionManipulation#getPermission()
	 */
	public Permission getPermission() {
		return rtn;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#execute(com.sexyprogrammer.lib.vfs.VFSUser)
	 */
	public void doExecute() throws VFSException {
		if(!file.exists(this)) {
			throw new FileNotExistsException(file);
		}
		
		rtn = file.getPermissionCache();
		if(rtn != null) {
			return;
		}
		
		rtn = doGetPermission(file);
		
		file.setPermissionCache(rtn);
	}
	
	public abstract Permission doGetPermission(VFile file) throws VFSException;

	/**
	 * 作業経過メッセージを取得する。
	 * 
	 * @return
	 */
	public String getProgressMessage() {
		return "";
	}
}
