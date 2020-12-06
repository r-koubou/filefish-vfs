/*
 * 作成日: 2003/10/30
 *
 */
package com.nullfish.lib.vfs.impl.ftp.manipulation;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileDeletionException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.impl.ftp.FTPFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractDeleteManipulation;


/**
 * @author shunji
 *
 */
public class FTPDeleteManipulation extends AbstractDeleteManipulation {
	private FTPClient ftp = null;
	/**
	 * コンストラクタ
	 * @param file
	 */
	public FTPDeleteManipulation(VFile file) {
		super(file);
	}

	/**
	 * ファイルを削除する。
	 * @see com.nullfish.lib.vfs.manipulation.abs.AbstractDeleteManipulation#deleteFile(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public void doDeleteFile(VFile file) throws VFSException {
		FTPFileSystem fileSystem = (FTPFileSystem)file.getFileSystem();
		FileName rootName = fileSystem.getWorkDir();

		try {
			ftp = ((FTPFileSystem) file.getFileSystem()).getFTPClient(this);

			if (!ftp.deleteFile(rootName.resolveRelation( file.getFileName()))) {
				throw new FileDeletionException(file);
			}
		} catch (IOException e) {
			throw new VFSIOException("Failed to delete " + file.getAbsolutePath(), e);
		} finally {
			fileSystem.releaseFTPClient();
		}
	}

	/**
	 * ディレクトリを削除する。
	 * @see com.nullfish.lib.vfs.manipulation.abs.AbstractDeleteManipulation#deleteDirectory(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public void doDeleteDirectory(VFile file) throws VFSException {
		FTPFileSystem fileSystem = (FTPFileSystem)file.getFileSystem();
		FileName rootName = fileSystem.getWorkDir();

		try {
			ftp = ((FTPFileSystem) file.getFileSystem()).getFTPClient(this);
			if (!ftp.removeDirectory(rootName.resolveRelation( file.getFileName()))) {
				throw new FileDeletionException(file);
			}
		} catch (IOException e) {
			throw new VFSIOException("Failed to delete " + file.getAbsolutePath(), e);
		} finally {
			fileSystem.releaseFTPClient();
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
