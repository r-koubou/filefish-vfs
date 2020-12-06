/*
 * 作成日: 2003/10/09
 * 
 */
package com.nullfish.lib.vfs.impl.smb.manipulation;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.FileSystemNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.WrongUserException;
import com.nullfish.lib.vfs.impl.smb.SMBFile;
import com.nullfish.lib.vfs.impl.smb.SMBFileSystem;
import com.nullfish.lib.vfs.impl.smb.SMBUtilities;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetChildrenManipulation;


/**
 * @author Shunji Yamaura
 */
public class SMBGetChildrenManipulation
	extends AbstractGetChildrenManipulation {
	public SMBGetChildrenManipulation(VFile file) {
		super(file);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetChildrenManipulation#initChildrenInfo(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public VFile[] doGetChildren(VFile file) throws VFSException {
		try {
			if(!file.exists(this)) {
				throw new FileNotExistsException(file);
			}
			
			SMBFile vfile = (SMBFile) file;
			SmbFile smbFile = vfile.getFileAsDirectory();
			SmbFile[] smbFiles = smbFile.listFiles();
			if (smbFiles == null) {
				return new SMBFile[0];
			}

			FileName fileName = file.getFileName();
			FileSystem fileSystem = file.getFileSystem();

			List filesList = new ArrayList();
			FileName childName;
			for (int i = 0; smbFiles != null && i < smbFiles.length; i++) {
				String name = smbFiles[i].getName();
				if(name.endsWith("/")) {
					name = name.substring(0, name.length() - 1);
				}
				childName = fileName.createChild(name);
				SMBFile f = new SMBFile(fileSystem, childName, smbFiles[i]);
				if(!name.equals(".") && !name.equals("..")) {
					filesList.add(f);
				}
			}
((SMBFileSystem)file.getFileSystem()).cacheAuthentication(smbFile);
			VFile[] rtn = new SMBFile[smbFiles.length];
			rtn = (VFile[]) filesList.toArray(rtn);
			
			return rtn;
		} catch (SmbAuthException e) {
			throw new WrongUserException(file.getFileName().getUserInfo(), e);
		} catch (SmbException e) {
			throw SMBUtilities.getVFSException(e, file);
		} catch (UnknownHostException e) {
			throw new FileSystemNotExistsException(e, file.getFileSystem());
		}
	}
}
