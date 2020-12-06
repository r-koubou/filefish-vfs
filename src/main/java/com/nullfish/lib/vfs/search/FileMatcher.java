/*
 * Created on 2004/09/13
 *
 */
package com.nullfish.lib.vfs.search;

import com.nullfish.lib.vfs.VFile;

/**
 * ファイル検索の際の、ファイル判定クラスのインターフェイス
 * 
 * @author shunji
 */
public interface FileMatcher {
	/**
	 * ファイルが検索条件に一致するならtrueを返す。
	 * @param file
	 * @return
	 */
	public boolean matches(VFile file);
}
