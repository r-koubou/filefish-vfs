package com.nullfish.lib.vfs;

import com.nullfish.lib.vfs.exception.VFSException;

/**
 * ファイルシステムの抽象クラス。
 * 
 * @author shunji
 */

public interface FileSystem {
	/**
	 * ファイルシステムを開く
	 * @throws VFSException
	 */
	public void open(Manipulation manipulation) throws VFSException;

	/**
	 * ファイルシステムを閉じる。
	 * @throws VFSException
	 */
	public void close(Manipulation manipulation) throws VFSException;
	
	/**
	 * ファイルシステムが開かれているか判定する。
	 * @return	ファイルシステムが開かれているならtrueを返す。
	 */
	public boolean isOpened();
	
	/**
	 * ファイルシステムを生成する。
	 * @throws VFSException
	 */
	public void createFileSystem() throws VFSException;
	
	/**
	 * 指定されたファイル名のファイルインスタンスを取得する。
	 * @param fileName	ファイル
	 * @return
	 */
	public VFile getFile(FileName fileName);

	/**
	 * ファイルシステムの基準ファイルを取得する。
	 * @return	基準ファイル
	 */
	public VFile getMountPoint();

	/**
	 * ファイルシステムのルートを取得する。
	 * @return	ルート
	 */
	public FileName getRootName();

	/**
	 * このファイルシステムを生成したVFSを取得する。
	 * @return	生成したVFS
	 */
	public VFS getVFS();
	
	/**
	 * このファイルシステムがローカルファイルシステムならtrueを返す。
	 * @return
	 */
	public boolean isLocal();
	
	/**
	 * このファイルシステムがシェルで解釈可能ならtrueを返す。
	 * @return
	 */
	public boolean isShellCompatible();
	
	/**
	 * このファイルシステムに使用者を登録する。
	 * @param user
	 */
	public void registerUser(Object user);
	
	/**
	 * このファイルシステムの使用者を削除する。
	 * @param user
	 */
	public void removeUser(Object user);
	
	/**
	 * 使用中ならtrueを返す。
	 * @return
	 */
	public boolean isInUse();
}
