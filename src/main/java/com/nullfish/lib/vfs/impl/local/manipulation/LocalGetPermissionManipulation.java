/*
 * 作成日: 2003/10/17
 *
 */
package com.nullfish.lib.vfs.impl.local.manipulation;

import java.io.File;

import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.local.LocalFile;
import com.nullfish.lib.vfs.impl.local.LocalPermission;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation;
import com.nullfish.lib.vfs.permission.ClassFileAccess;
import com.nullfish.lib.vfs.permission.PermissionType;


/**
 * 
 * @author Shunji Yamaura
 */
public class LocalGetPermissionManipulation
	extends AbstractGetPermissionManipulation {

	public LocalGetPermissionManipulation(VFile file) {
		super(file);
	}


	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation#initPermission(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public Permission doGetPermission(VFile file) throws VFSException {
		File f = ((LocalFile)file).getFile();
		if (!f.exists()) {
			throw new FileNotExistsException(file);
		}

		Permission rtn = new LocalPermission((LocalFile)file);

		rtn.setPermission(
			PermissionType.READABLE,
			ClassFileAccess.ALL,
			f.canRead());
		rtn.setPermission(
			PermissionType.WRITABLE,
			ClassFileAccess.ALL,
			f.canWrite());
		
		return rtn;
	}
}
