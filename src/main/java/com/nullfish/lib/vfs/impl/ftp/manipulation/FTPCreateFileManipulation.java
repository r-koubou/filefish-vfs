/*
 * 作成日: 2003/10/29
 *
 */
package com.nullfish.lib.vfs.impl.ftp.manipulation;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.UpdateManager;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileCreationException;
import com.nullfish.lib.vfs.exception.ManipulationNotAvailableException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSSystemException;
import com.nullfish.lib.vfs.impl.ftp.FTPFile;
import com.nullfish.lib.vfs.impl.ftp.FTPFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractCreateFileManipulation;


/**
 * @author shunji
 *
 */
public class FTPCreateFileManipulation extends AbstractCreateFileManipulation {
	private FTPClient ftp = null;
	
	public FTPCreateFileManipulation(VFile file) {
		super(file);
	}
	
	/**
	 * 
	 * @throws VFSException
	 */
	public void doCreateFile() throws VFSException {
		try{
			FTPFileSystem fileSystem = (FTPFileSystem)file.getFileSystem();
			FileName rootName = fileSystem.getWorkDir();
			ftp = fileSystem.getTempFTPClient((FTPFile)file);
			
			FTPFile parent = (FTPFile)file.getParent();
			
			if(!parent.exists()) {
				String parentPath = rootName.resolveRelation(parent.getFileName());
				ftp.makeDirectory(parentPath);
				UpdateManager.getInstance().fileCreated(parent);
			}
			
			FileType fileType = getFileType();
			if(fileType.equals(FileType.FILE)) {
				createFile(ftp, (FTPFile)file);
			} else if(fileType.equals(FileType.DIRECTORY)) {
				String path = rootName.resolveRelation(file.getFileName());
				ftp.makeDirectory(path);
			} else if(fileType.equals(FileType.LINK)) {
				throw new ManipulationNotAvailableException(file, "creating link");
			} else {
				throw new VFSSystemException("Wrong FileType : " + fileType);
			}
		} catch (VFSException e) {
			throw e;
		} catch (Exception e) {
			throw new FileCreationException(e, file);
		} finally {
			try {
				ftp.disconnect();
			} catch (Exception e) {
			}
		}
	}

	private void createFile(FTPClient ftp, FTPFile file) throws VFSException {
		OutputStream os = null;
		try {
			os = file.getOutputStream(this);
		} catch (Exception e) {
			throw new FileCreationException(e, file); 
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void doStop() {
		getWorkThread().interrupt();
		if(ftp != null) {
			try {
				ftp.abort();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
