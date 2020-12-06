/*
 * 作成日: 2003/10/19
 *
 */
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
import com.nullfish.lib.vfs.manipulation.abst.AbstractMoveManipulation;


/**
 * 
 * @author Shunji Yamaura
 */
public class SMBMoveManipulation extends AbstractMoveManipulation {
	/**
	 * コンストラクタ
	 * @param file
	 */
	public SMBMoveManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractMoveManipulation#moveFile(com.sexyprogrammer.lib.vfs.VFile, com.sexyprogrammer.lib.vfs.VFile)
	 */
	public boolean doMoveFile(VFile from, VFile dest) throws VFSException {
		try {
			SmbFile fromFile; 
			try {
				fromFile = ((SMBFile)from).getFileAsFile();
			} catch (UnknownHostException e) {
				throw new FileSystemNotExistsException(from.getFileSystem()); 
			}
			
			SmbFile destFile;
			try {
				destFile = ((SMBFile)dest).getFileAsFile();
			} catch (UnknownHostException e) {
				throw new FileSystemNotExistsException(dest.getFileSystem()); 
			}
			
			if (!dest.exists(this)) {
				dest.getParent().createDirectory(this);
			}
			fromFile.renameTo(destFile);
((SMBFileSystem)file.getFileSystem()).cacheAuthentication(fromFile);
((SMBFileSystem)dest.getFileSystem()).cacheAuthentication(destFile);
			return true;
		} catch (SmbAuthException e) {
			throw new WrongUserException(from.getFileName().getUserInfo(), e);
		} catch (SmbException e) {
			throw SMBUtilities.getVFSException(e, file);
		}
	}
}
