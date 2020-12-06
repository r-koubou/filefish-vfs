package com.nullfish.lib.vfs.impl.filelist.manipulation;

import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.filelist.FileListPermission;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation;

public class FileListGetPermissionManipulation extends
		AbstractGetPermissionManipulation {

	public FileListGetPermissionManipulation(VFile file) {
		super(file);
	}

	public Permission doGetPermission(VFile file) throws VFSException {
		return new FileListPermission();
	}

}
