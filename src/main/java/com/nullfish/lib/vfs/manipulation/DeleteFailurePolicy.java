package com.nullfish.lib.vfs.manipulation;

import com.nullfish.lib.vfs.VFile;

/**
 * ファイルの削除時に失敗した場合、リトライするか、無視するか、
 * 全て中止するか判断するクラスのインターフェイス。
 * 
 * @author shunji
 */

public interface DeleteFailurePolicy {
	/**
	 * リトライする。
	 */
	public static final int RETRY = 1;
	
	/**
	 * 無視する。
	 */
	public static final int IGNORE = 2;
	
	/**
	 * 全ての操作を中止する。
	 */
	public static final int FAIL = 99;
	
	/**
	 * 削除失敗時動作確認を行う。
	 * リトライするならRETRY、無視するならしないならIGNORE、
	 * 動作を全て中止するならFAIL、
	 * 
	 * @param srcFile	上書き元ファイル
	 * @return
	 */ 
	public int getDeleteFailurePolicy(VFile file);
}
