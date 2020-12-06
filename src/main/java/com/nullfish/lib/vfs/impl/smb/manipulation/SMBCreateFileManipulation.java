package com.nullfish.lib.vfs.impl.smb.manipulation;

import java.io.IOException;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.UpdateManager;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileCreationException;
import com.nullfish.lib.vfs.exception.ManipulationNotAvailableException;
import com.nullfish.lib.vfs.exception.ManipulationStoppedException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSSystemException;
import com.nullfish.lib.vfs.impl.smb.SMBFile;
import com.nullfish.lib.vfs.impl.smb.SMBFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractCreateFileManipulation;


/**
 * ローカルファイル生成クラス
 * @author Shunji Yamaura
 */
public class SMBCreateFileManipulation extends AbstractCreateFileManipulation {
	public SMBCreateFileManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractCreateFileManipulation#createFile()
	 */
	public void doCreateFile() throws VFSException {
		if(isStopped()) {
			throw new ManipulationStoppedException(this);
		}
		
		try{
			SMBFile vfile = (SMBFile)file;
			SmbFile smbFile = vfile.getFileAsFile();
			VFile parent = vfile.getParent();
			
			if(!parent.exists(this)) {
				parent.createDirectory(this);
				UpdateManager.getInstance().fileCreated(parent);
			}
			
			FileType fileType = getFileType();
			
			if(fileType.equals(FileType.FILE)) {
				SmbFileOutputStream os = null;
				try {
					os = new SmbFileOutputStream(smbFile);
				} finally {
					try {
						os.close();
					} catch (Exception e) {}
				}
			} else if(fileType.equals(FileType.DIRECTORY)) {
				smbFile.mkdirs();
			} else if(fileType.equals(FileType.LINK)) {
				throw new ManipulationNotAvailableException(file, "creating link");
			} else {
				throw new VFSSystemException("Wrong FileType : " + fileType);
			}
			
((SMBFileSystem)vfile.getFileSystem()).cacheAuthentication(smbFile);
		} catch (IOException e) {
			throw new FileCreationException(file);
		}
	}
}
