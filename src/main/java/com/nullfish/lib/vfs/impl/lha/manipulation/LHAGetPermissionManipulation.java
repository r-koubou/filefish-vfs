/*
 * 作成日: 2003/11/18
 *
 */
package com.nullfish.lib.vfs.impl.lha.manipulation;


import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.lha.LHAPermission;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation;


/**
 * @author shunji
 *
 */
public class LHAGetPermissionManipulation
	extends AbstractGetPermissionManipulation {
	
	static final LHAPermission permission = new LHAPermission();
	
	public LHAGetPermissionManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation#doGetPermission(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public Permission doGetPermission(VFile file) throws VFSException {
		return permission;
	}
}
