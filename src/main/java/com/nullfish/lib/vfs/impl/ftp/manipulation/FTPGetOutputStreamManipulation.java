/*
 * 作成日: 2003/11/03
 *
 */
package com.nullfish.lib.vfs.impl.ftp.manipulation;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.ManipulationNotAvailableException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.impl.ftp.FTPFile;
import com.nullfish.lib.vfs.impl.ftp.FTPFileSystem;
import com.nullfish.lib.vfs.impl.ftp.util.FTPOutputStream;
import com.nullfish.lib.vfs.manipulation.GetOutputStreamManipulation;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetOutputStreamManipulation;

/**
 * @author shunji
 *
 */
public class FTPGetOutputStreamManipulation extends
		AbstractGetOutputStreamManipulation {

	private FTPClient ftp;

	public FTPGetOutputStreamManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetOutputStreamManipulation#initOutputStream(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public OutputStream doGetOutputStream(VFile file) throws VFSException {
		try {
			VFile parent = file.getParent();
			if(!parent.exists(this)) {
				parent.createDirectory(this);
			}
			
			FTPFileSystem fileSystem = (FTPFileSystem)file.getFileSystem();
			FileName rootName = fileSystem.getWorkDir();
			ftp = fileSystem.getTempFTPClient((FTPFile)file);
System.out.println(ftp.getControlEncoding());
System.out.println(rootName.resolveRelation(file.getFileName()));
			OutputStream os = ftp.storeFileStream(rootName.resolveRelation(file.getFileName()));
			if(os == null) {
				System.out.println(ftp.getReplyString());
				throw new FileNotExistsException(file);
			}
			return new FTPOutputStream(os , ftp);
		} catch (IOException e) {
			throw new VFSIOException("IOException on " + file.getAbsolutePath(), e);
		} finally {
			setFinished(true);
		}
	}
	/**
	 * 追記書き込みストリームを取得する。
	 * 
	 * @see com.nullfish.lib.vfs.manipulation.abst.AbstractGetOutputStreamManipulation#doGetAppendOutputStream(com.nullfish.lib.vfs.VFile)
	 */
	public OutputStream doGetAppendOutputStream(VFile file) throws VFSException {
		throw new ManipulationNotAvailableException(file,
				GetOutputStreamManipulation.NAME_APPEND);
	}

	public synchronized void doStop() {
		getWorkThread().interrupt();
		if (ftp != null) {
			try {
				ftp.abort();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
