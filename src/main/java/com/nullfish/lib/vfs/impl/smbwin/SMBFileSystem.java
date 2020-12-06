/*
 * 作成日: 2003/10/27
 *
 */
package com.nullfish.lib.vfs.impl.smbwin;


import java.io.File;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.impl.AbstractFileSystem;


/**
 * @author shunji
 *
 */
public class SMBFileSystem extends AbstractFileSystem {
	/*
	 * ホスト名
	 */
	private String host;

	/**
	 * このファイルシステムタイプの使用設定
	 */
	public static final String CONFIG_NATIVE_SMB_ENABLED = "native_smb_enabled";
	
	private File rootJavaFile;
	
	/**
	 * コンストラクタ
	 * @param drive
	 */
	public SMBFileSystem(VFS vfs, String host) {
		super(vfs);
		this.host = host;
		
		rootJavaFile = new File("\\\\" + host + "\\");
		
		rootName = new SMBFileName(host, new String[0], null);
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
		String[] path = fileName.getPath();
		
		return new SMBFile(this, fileName);
	}

	/**
	 * Javaのファイルクラスでルートディレクトリを取得する。
	 * @return
	 */
	private File getRootDir() {
		return rootJavaFile;
	}
	
	public boolean isLocal() {
		return false;
	}
	
	public boolean isShellCompatible() {
		return true;
	}
}
