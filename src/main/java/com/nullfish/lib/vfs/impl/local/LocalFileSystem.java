package com.nullfish.lib.vfs.impl.local;

import java.util.HashMap;
import java.util.Map;

import jp.ne.anet.kentkt.fastfile.FastFile;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.impl.AbstractFileSystem;


/**
 * 
 * @author Shunji Yamaura
 */
public class LocalFileSystem extends AbstractFileSystem {
	/*
	 * ドライブ名
	 * a-z か / のみ
	 */
	private char drive;
	
	/*
	 * ドライブ名とファイルシステムのマップ
	 */
	private static Map driveMap = new HashMap();
	
	private FastFile rootJavaFile;

	/**
	 * コンストラクタ
	 * @param drive
	 */
	public LocalFileSystem(VFS vfs, char drive) {
		super(vfs);
		this.drive = drive;
		
		if(drive == '/') {
			rootJavaFile = new FastFile("/");
		} else {
			rootJavaFile = new FastFile(drive + ":\\");
		}
		
		rootName = new LocalFileName(new String[0], null, null, Character.toString(drive));
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
		
		return new LocalFile(this, fileName);
	}

	/**
	 * Javaのファイルクラスでルートディレクトリを取得する。
	 * @return
	 */
	private FastFile getRootDir() {
		return rootJavaFile;
	}
	
	public boolean isLocal() {
		return true;
	}
	
	public boolean isShellCompatible() {
		return true;
	}
	
	/**
	 * Javaのバージョンが6以上かを判定する（パーミッション設定の可否判定のため）
	 * @return
	 */
	public static boolean aboveJava6() {
		return VFS.JAVA6_OR_LATER;
	}
}
