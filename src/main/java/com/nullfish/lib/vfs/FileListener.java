package com.nullfish.lib.vfs;

/**
 * ファイルシステムでのファイル生成、属性変更などのイベントのリスナ
 * 
 * @author shunji
 */
public interface FileListener {
	/**
	 * ファイルが更新された際に呼び出される
	 * @param file ファイル
	 */
	public void fileChanged(VFile file);

	/**
	 * ファイルが削除された際に呼び出される。
	 * @param file ファイル
	 */
	public void fileDeleted(VFile file);

	/**
	 * ファイルが生成された際に呼び出される。
	 * @param file ファイル
	 */
	public void fileCreated(VFile file);

}
