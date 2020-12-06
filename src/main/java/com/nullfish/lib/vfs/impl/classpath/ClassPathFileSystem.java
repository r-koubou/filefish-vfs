package com.nullfish.lib.vfs.impl.classpath;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.impl.AbstractFileSystem;


/**
 * 
 * @author Shunji Yamaura
 */
public class ClassPathFileSystem extends AbstractFileSystem {
	private static ClassPathFileSystem instance;
	
	/**
	 * コンストラクタ
	 * @param drive
	 */
	private ClassPathFileSystem(VFS vfs) {
		super(vfs);
		
		rootName = new ClassPathFileName(new String[0]);
	}
	
	/**
	 * ファイルシステムのインスタンスを取得する
	 * @param drive
	 * @return
	 */
	public static ClassPathFileSystem getFileSystem(VFS vfs) throws IllegalArgumentException {
		if(instance == null) {
			instance = new ClassPathFileSystem(vfs);
		}
		
		return instance;
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#open()
	 */
	public void doOpen(Manipulation manipulation) {
		//	何もしない
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#close()
	 */
	public void doClose(Manipulation manipulation) {
		//	何もしない
	}

	/**
	 * 
	 */
	public boolean isOpened() {
		return true;
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#createFileSystem()
	 */
	public void createFileSystem() {
		//	何もしない
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#getFile(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public VFile getFile(FileName fileName) {
		return new ClassPathFile(this, fileName);
	}

	public boolean isLocal() {
		return true;
	}
	
	public boolean isShellCompatible() {
		return false;
	}
	
}
