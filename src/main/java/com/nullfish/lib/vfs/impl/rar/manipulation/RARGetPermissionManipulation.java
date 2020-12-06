package com.nullfish.lib.vfs.impl.rar.manipulation;


import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.rar.RARPermission;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation;


public class RARGetPermissionManipulation
	extends AbstractGetPermissionManipulation {
	
	static final RARPermission permission = new RARPermission();
	
	public RARGetPermissionManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation#doGetPermission(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public Permission doGetPermission(VFile file) throws VFSException {
		return permission;
	}
}
