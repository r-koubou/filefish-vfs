package com.nullfish.lib.vfs.impl.root.manipulation;

import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.root.RootPermission;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation;

public class RootFileGetPermissionManipulation extends
		AbstractGetPermissionManipulation {

	public RootFileGetPermissionManipulation(VFile file) {
		super(file);
	}

	public Permission doGetPermission(VFile file) throws VFSException {
		return new RootPermission();
	}

}
