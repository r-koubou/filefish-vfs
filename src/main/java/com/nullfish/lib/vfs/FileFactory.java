package com.nullfish.lib.vfs;

import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.WrongPathException;

/**
 * パス文字列からファイルを生成するクラス。
 * ファイルクラス一つにつき一つ存在する。
 * 
 * @author shunji
 */
public abstract class FileFactory {

	/**
	 * ファイルシステムマネージャ
	 */
	protected VFS vfs;


	/**
	 * ファイルのパスを接続するパイプ文字
	 */
	public static final char PIPE = '|';

	/**
	 * パスがこのクラスで解釈可能か判定する。
	 * 
	 * @param path	パス	
	 * @return	解釈可能ならtrueを返す。
	 */
	public abstract boolean isInterpretable(String path);

	/**
	 * ファイル名がこのクラスで解釈できるか判定する。
	 * @param fileName	ファイル名
	 * @return	解釈できるならtrueを返す。
	 */
	public abstract boolean isBelongingFileName(FileName fileName);

	/**
	 * パスからファイル名を生成する。
	 * @param path	パス
	 * @return	ファイル名
	 */
	public FileName interpretFileName(String path) throws VFSException {
		int pipeIndex = path.lastIndexOf(PIPE);
		
		if (pipeIndex == -1) {
			return interpretFileName(path, null);
		} else if(pipeIndex == path.length() - 1) {
			return interpretFileName(path.substring(0, pipeIndex));
		} else {
			String basePath = path.substring(0, pipeIndex);
			VFile baseFile = getFileSystemManager().getFile(basePath);
			String thisPath = path.substring(pipeIndex + 1);
			return interpretFileName(thisPath, baseFile.getFileName());
		}
	}

	/**
	 * ファイル名クラスから絶対パスを取得する。
	 * @param fileName	ファイル名
	 * @return	絶対パス文字列
	 */
	public static String interpretPath(FileName fileName) {
		StringBuffer buffer = new StringBuffer();
		FileName baseName = fileName.getBaseFileName();
		while(baseName != null) {
			buffer.insert(0, baseName.getAbsolutePath() + FileFactory.PIPE);
			baseName = baseName.getBaseFileName();
		}
		
		buffer.append(fileName.getAbsolutePath());

		return buffer.toString();
	}
	
	/**
	 * ファイル名クラスからユーザー情報抜きの絶対パスを取得する。
	 * @param fileName
	 * @return	ユーザー情報抜きの絶対パス
	 */
	public static String interpretSecurePath(FileName fileName) {
		StringBuffer buffer = new StringBuffer();
		FileName baseName = fileName.getBaseFileName();
		while(baseName != null) {
			buffer.insert(0, baseName.getSecurePath() + FileFactory.PIPE);
			baseName = baseName.getBaseFileName();
		}
		
		buffer.append(fileName.getSecurePath());

		return buffer.toString();
	}
	
	/**
	 * パスからファイル名を生成する。
	 * @param path	パス
	 * @param baseFileName 基準ファイル名
	 * @return	ファイル名
	 */
	public abstract FileName interpretFileName(
		String path,
		FileName baseFileName)
		throws WrongPathException;

	/**
	 * ファイル名からファイルシステムを取得する。
	 * @param fileName	ファイル名
	 * @return	ファイルシステム
	 */
	public abstract FileSystem interpretFileSystem(FileName fileName)
		throws WrongPathException;

	/**
	 * パスからファイルを生成する。
	 * 
	 * @param path	パス
	 * @return	ファイル
	 */
	public VFile interpretFile(String path) throws VFSException {
		FileName fileName = interpretFileName(path);
		return interpretFile(fileName);
	}

	/**
	 * ファイル名からファイルを生成する。
	 * @param fileName	ファイル名
	 * @return	ファイル
	 */
	public VFile interpretFile(FileName fileName) throws WrongPathException {
		VFS manager = getFileSystemManager();
		FileName rootName = fileName.getRoot();
		FileSystem fileSystem = manager.getFileSystem(rootName);
		
		if(fileSystem == null) {
			fileSystem = interpretFileSystem(rootName);
			manager.addFileSystem(fileSystem);
		}
		
		return fileSystem.getFile(fileName);
	}

	/**
	 * ファイルシステム管理を行うVFSクラスを取得する。
	 * @return	VFSクラス
	 */
	public VFS getFileSystemManager() {
		return vfs;
	}

	/**
	 * ファイルシステム管理を行うVFSクラスをセットする。
	 * @param manager	VFSクラス
	 */
	public void setFileSystemManager(VFS manager) {
		vfs = manager;
	}

	/**
	 * ファイルが解釈可能であれば、内部のルートディレクトリを取得する。
	 * もしも解釈不可能な場合は、nullを返す。
	 * @param file
	 * @return
	 */
	public final VFile getInnerRoot(VFile file) {
		try {
			FileName innerRootName = doGetInnerRootName(file);
			if(innerRootName == null) {
				return null;
			}
			
			return interpretFile(innerRootName);
		} catch (WrongPathException e) {
			return null;
		}
	}

	/**
	 * ファイルに内部ファイルシステムが存在する場合、そのルートディレクトリを取得する。
	 * @param file
	 * @return
	 */
	public abstract FileName doGetInnerRootName(VFile file);
	
	/**
	 * 設定が変更された際に呼び出される。
	 *
	 */
	public abstract void configChanged();
	
	/**
	 * ファイルシステムのルートを返す。
	 * @return
	 */
	public abstract VFile[] listRoots();
}
