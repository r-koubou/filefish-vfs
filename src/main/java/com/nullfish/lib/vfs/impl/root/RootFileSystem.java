/*
 * 作成日: 2003/11/07
 *
 */
package com.nullfish.lib.vfs.impl.root;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.AbstractFileSystem;

/**
 */
public class RootFileSystem extends AbstractFileSystem {

	boolean opened = false;

	protected long totalSpace = 0;

	public RootFileSystem(VFS vfs) {
		super(vfs);
		parentFileSystem = null;
		rootName = RootFileName.getInstance();
	}

	public VFile getBaseFile() {
		return parentFileSystem.getFile(rootName.getBaseFileName());
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#open()
	 */
	public void doOpen(Manipulation manipulation) throws VFSException {
		opened = true;
	}
	
	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#close()
	 */
	public void doClose(Manipulation manipulation) throws VFSException {
		opened = false;
	}
	
	public boolean isOpened() {
		return opened;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#createFileSystem()
	 */
	public void createFileSystem() throws VFSException {
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#getFile(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public VFile getFile(FileName fileName) {
		return new RootFile(this, fileName);
	}

	public boolean isLocal() {
		return true;
	}

	/**
	 * このファイルシステムがシェルで解釈可能ならtrueを返す。
	 * @return
	 */
	public boolean isShellCompatible() {
		return false;
	}
	
	public long getTotalSpace() {
		return totalSpace;
	}
}
