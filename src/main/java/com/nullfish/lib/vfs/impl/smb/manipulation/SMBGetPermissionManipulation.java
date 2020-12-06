/*
 * 作成日: 2003/10/17
 *
 */
package com.nullfish.lib.vfs.impl.smb.manipulation;

import java.net.UnknownHostException;

import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.WrongUserException;
import com.nullfish.lib.vfs.impl.smb.SMBFile;
import com.nullfish.lib.vfs.impl.smb.SMBFileSystem;
import com.nullfish.lib.vfs.impl.smb.SMBPermission;
import com.nullfish.lib.vfs.impl.smb.SMBUtilities;
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
		try {
			SmbFile f = ((SMBFile)file).getFileAsFile();
			if (!f.exists()) {
				throw new FileNotExistsException(file);
			}
	
			Permission rtn = new SMBPermission();
	
			rtn.setPermission(
				PermissionType.READABLE,
				ClassFileAccess.ALL,
				f.canRead());
			rtn.setPermission(
				PermissionType.WRITABLE,
				ClassFileAccess.ALL,
				f.canWrite());
			
((SMBFileSystem)file.getFileSystem()).cacheAuthentication(f);
			return rtn;
		} catch (UnknownHostException e) {
			throw new FileNotExistsException(e, file);
		} catch (SmbAuthException e) {
			throw new WrongUserException(file.getFileName().getUserInfo(), e);
		} catch (SmbException e) {
			throw SMBUtilities.getVFSException(e, file);
		}
	}
}
