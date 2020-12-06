/*
 * 作成日: 2003/11/18
 *
 */
package com.nullfish.lib.vfs.impl.commons_http.manipulation;


import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.zip.ZIPPermission;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation;


/**
 * @author shunji
 *
 */
public class CommonsHTTPGetPermissionManipulation
	extends AbstractGetPermissionManipulation {
	
	static final ZIPPermission permission = new ZIPPermission();
	
	public CommonsHTTPGetPermissionManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation#doGetPermission(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public Permission doGetPermission(VFile file) throws VFSException {
		return permission;
	}
}
