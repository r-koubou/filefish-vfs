package com.nullfish.lib.vfs.impl.smb.manipulation;

import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileSystemNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSSystemException;
import com.nullfish.lib.vfs.exception.WrongUserException;
import com.nullfish.lib.vfs.impl.smb.SMBFile;
import com.nullfish.lib.vfs.impl.smb.SMBFileSystem;
import com.nullfish.lib.vfs.impl.smb.SMBUtilities;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetOutputStreamManipulation;


/**
 * 
 * @author Shunji Yamaura
 */
public class SMBGetOutputStreamManipulation
	extends AbstractGetOutputStreamManipulation {

	public SMBGetOutputStreamManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetOutputStreamManipulation#initOutputStream(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public OutputStream doGetOutputStream(VFile file) throws VFSException {
		return getOutputStreamImpl(file, false);
	}

	/* (non-Javadoc)
	 * @see com.nullfish.lib.vfs.manipulation.abst.AbstractGetOutputStreamManipulation#doGetAppendOutputStream(com.nullfish.lib.vfs.VFile)
	 */
	public OutputStream doGetAppendOutputStream(VFile file) throws VFSException {
		return getOutputStreamImpl(file, true);
	}
	
	private OutputStream getOutputStreamImpl(VFile file, boolean append) throws VFSException {
		try {
			VFile parent = file.getParent();
			
			if(!parent.exists(this)) {
				parent.createDirectory(this);
			}
			
			SmbFile smbFile = ((SMBFile)file).getFileAsFile();
			OutputStream rtn = new SmbFileOutputStream(smbFile, append);
((SMBFileSystem)file.getFileSystem()).cacheAuthentication(smbFile);
			return rtn;
		} catch (SmbAuthException e) {
			throw new WrongUserException(file.getFileName().getUserInfo(), e);
		} catch (SmbException e) {
			throw SMBUtilities.getVFSException(e, file);
		} catch (MalformedURLException e) {
			throw new VFSSystemException(e);
		} catch (UnknownHostException e) {
			throw new FileSystemNotExistsException(e, file.getFileSystem());
		}
	}
}
