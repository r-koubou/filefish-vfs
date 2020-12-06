/*
 * 作成日: 2003/11/03
 *
 */
package com.nullfish.lib.vfs.impl.ftp.manipulation;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.impl.ftp.FTPFile;
import com.nullfish.lib.vfs.impl.ftp.FTPFileSystem;
import com.nullfish.lib.vfs.impl.ftp.util.FTPInputStream;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation;


/**
 * @author shunji
 *
 */
public class FTPGetInputStreamManipulation extends AbstractGetInputStreamManipulation {
	private FTPClient ftp;
	
	public FTPGetInputStreamManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation#initInputStream(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public InputStream doGetInputStream(VFile file) throws VFSException {
		try {
			FTPFileSystem fileSystem = (FTPFileSystem)file.getFileSystem();
			FileName rootName = fileSystem.getWorkDir();
			ftp = fileSystem.getTempFTPClient((FTPFile)file);
			
			return new FTPInputStream(ftp.retrieveFileStream(rootName.resolveRelation(file.getFileName())), ftp);
		} catch (IOException e) {
			throw new VFSIOException(file.getAbsolutePath(), e);
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
