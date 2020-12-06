/*
 * 作成日: 2003/11/03
 *
 */
package com.nullfish.lib.vfs.impl.ftp.manipulation;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.ftp.FTPFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractMoveManipulation;


/**
 * @author shunji
 *
 */
public class FTPMoveManipulation extends AbstractMoveManipulation {
	private FTPClient ftp;
	
	/**
	 * コンストラクタ
	 * @param file
	 */
	public FTPMoveManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractMoveManipulation#moveFile(com.sexyprogrammer.lib.vfs.VFile, com.sexyprogrammer.lib.vfs.VFile)
	 */
	public boolean doMoveFile(VFile from, VFile dest) throws VFSException {
		FTPFileSystem fromFileSystem = (FTPFileSystem)file.getFileSystem();
		FileName fromRoot = fromFileSystem.getWorkDir();
			
		FTPFileSystem destFileSystem = (FTPFileSystem)dest.getFileSystem();
		FileName destRoot = destFileSystem.getWorkDir();

		String fromPath = fromRoot.resolveRelation(file.getFileName());
		String destPath = destRoot.resolveRelation(dest.getFileName());
					
		ftp = ((FTPFileSystem)fromFileSystem).getFTPClient(this);
		try {
			return ftp.rename(fromPath, destPath);
		} catch (IOException e) {
			return false;
		} finally {
			((FTPFileSystem)fromFileSystem).releaseFTPClient();
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
