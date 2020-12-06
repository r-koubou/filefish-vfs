/*
 * 作成日: 2003/11/07
 *
 */
package com.nullfish.lib.vfs.impl.filelist;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.AbstractFileSystem;

/**
 */
public abstract class AbstractFileListFileSystem extends AbstractFileSystem {

	boolean opened = false;

	protected long totalSpace = 0;

	private Set openedFileSystems = new HashSet();
	
	public AbstractFileListFileSystem(VFS vfs, VFile baseFile) {
		super(vfs);
		parentFileSystem = baseFile.getFileSystem();
		rootName = new FileListFileName(new String[0], baseFile.getFileName());
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
		VFile baseFile = getBaseFile();
		baseFile.getFileSystem().open(manipulation);

		if (!opened) {
			init();
		}
		opened = true;
	}
	
	/**
	 * 初期化する。
	 */
	public abstract void init() throws VFSException;

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#close()
	 */
	public void doClose(Manipulation manipulation) throws VFSException {
		opened = false;
		closeFileList();
		
		Iterator ite = openedFileSystems.iterator();
		while(ite.hasNext()) {
			((FileSystem)ite.next()).removeUser(this);
		}
	}
	
	public abstract void closeFileList() throws VFSException;  

	public boolean isOpened() {
		return opened;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#createFileSystem()
	 */
	public void createFileSystem() throws VFSException {
		VFile baseFile = getBaseFile();
		if (!baseFile.exists()) {
			baseFile.createFile();
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#getFile(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public VFile getFile(FileName fileName) {
		return new FileListFile(this, fileName);
	}

	public final List getFiles(Manipulation manipulation) throws VFSException {
		List rtn = doGetFiles(manipulation);
		for(int i=0; i<rtn.size(); i++) {
			FileSystem fileSystem = ((VFile)rtn.get(i)).getFileSystem();
			fileSystem.registerUser(this);
			openedFileSystems.add(fileSystem);
		}
		
		return rtn;
	}

	public abstract List doGetFiles(Manipulation manipulation) throws VFSException;

	public boolean isLocal() {
		return getBaseFile().getFileSystem().isLocal();
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
