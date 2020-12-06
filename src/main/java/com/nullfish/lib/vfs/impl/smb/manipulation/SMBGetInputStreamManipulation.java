package com.nullfish.lib.vfs.impl.smb.manipulation;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileSystemNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSSystemException;
import com.nullfish.lib.vfs.exception.WrongUserException;
import com.nullfish.lib.vfs.impl.smb.SMBFile;
import com.nullfish.lib.vfs.impl.smb.SMBFileSystem;
import com.nullfish.lib.vfs.impl.smb.SMBUtilities;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation;


/**
 * 
 * @author Shunji Yamaura
 */
public class SMBGetInputStreamManipulation
	extends AbstractGetInputStreamManipulation {
		
	public SMBGetInputStreamManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.GetInputStreamManipulation#getInputStream()
	 */
	public InputStream doGetInputStream(VFile file) throws VFSException {
		try {
			SmbFile smbFile = ((SMBFile)file).getFileAsFile();
			InputStream rtn = new SmbFileInputStream(smbFile);
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
