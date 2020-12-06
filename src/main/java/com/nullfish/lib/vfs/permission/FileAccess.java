/*
 * Created on 2004/04/06
 *
 */
package com.nullfish.lib.vfs.permission;

/**
 * ファイルアクセスを表すインターフェイス。
 * equalsとhashCodeも実装すること。
 * 
 * @author shunji
 */

public interface FileAccess {

	/**
	 * 名称を取得する。
	 */
	public String getName();

}
