/*
 * 作成日: 2003/10/06
 *
 */
package com.nullfish.lib.vfs.manipulation;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFile;

/**
 * ファイルの子ファイルを取得するクラス。
 * 
 * @author Shunji Yamaura
 */
public interface GetChildrenManipulation extends Manipulation {
	/**
	 * 操作名
	 */
	public static final String NAME = "get_children";
	
	/**
	 * 子ファイルを取得する。
	 * @return
	 */
	public abstract VFile[] getChildren();
}
