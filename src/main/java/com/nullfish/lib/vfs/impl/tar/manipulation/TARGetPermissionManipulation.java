package com.nullfish.lib.vfs.impl.tar.manipulation;


import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.tar.TARPermission;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation;


/**
 * @author shunji
 *
 */
public class TARGetPermissionManipulation
	extends AbstractGetPermissionManipulation {
	
	static final TARPermission permission = new TARPermission();
	
	public TARGetPermissionManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation#doGetPermission(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public Permission doGetPermission(VFile file) throws VFSException {
		return permission;
	}
}
