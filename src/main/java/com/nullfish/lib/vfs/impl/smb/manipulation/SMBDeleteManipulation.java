package com.nullfish.lib.vfs.impl.smb.manipulation;

import java.net.UnknownHostException;

import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileSystemNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.WrongUserException;
import com.nullfish.lib.vfs.impl.smb.SMBFile;
import com.nullfish.lib.vfs.impl.smb.SMBFileSystem;
import com.nullfish.lib.vfs.impl.smb.SMBUtilities;
import com.nullfish.lib.vfs.manipulation.abst.AbstractDeleteManipulation;


/**
 * ローカルファイルの削除操作クラス
 * 
 * @author Shunji Yamaura
 */
public class SMBDeleteManipulation extends AbstractDeleteManipulation {
	public SMBDeleteManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abs.AbstractDeleteManipulation#deleteFile(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public void doDeleteFile(VFile file) throws VFSException {
		try {
			SmbFile smbFile = ((SMBFile)file).getFileAsFile();
			smbFile.delete();
((SMBFileSystem)file.getFileSystem()).cacheAuthentication(smbFile);
		} catch (SmbAuthException e) {
			throw new WrongUserException(file.getFileName().getUserInfo(), e);
		} catch (SmbException e) {
			throw SMBUtilities.getVFSException(e, file);
		} catch (UnknownHostException e) {
			throw new FileSystemNotExistsException(file.getFileSystem());
		}
	}


	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abs.AbstractDeleteManipulation#deleteDirectory(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public void doDeleteDirectory(VFile file) throws VFSException {
		doDeleteFile(file);
	}
}
