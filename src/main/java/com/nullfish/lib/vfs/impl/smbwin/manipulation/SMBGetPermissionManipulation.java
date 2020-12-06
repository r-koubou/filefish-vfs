/*
 * 作成日: 2003/10/17
 *
 */
package com.nullfish.lib.vfs.impl.smbwin.manipulation;

import java.io.File;

import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.smbwin.SMBFile;
import com.nullfish.lib.vfs.impl.smbwin.SMBPermission;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation;
import com.nullfish.lib.vfs.permission.ClassFileAccess;
import com.nullfish.lib.vfs.permission.PermissionType;


/**
 * 
 * @author Shunji Yamaura
 */
public class SMBGetPermissionManipulation
	extends AbstractGetPermissionManipulation {

	public SMBGetPermissionManipulation(VFile file) {
		super(file);
	}


	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation#initPermission(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public Permission doGetPermission(VFile file) throws VFSException {
		File f = ((SMBFile)file).getFile();
		if (!f.exists()) {
			throw new FileNotExistsException(file);
		}

		Permission rtn = new SMBPermission((SMBFile)file);

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
