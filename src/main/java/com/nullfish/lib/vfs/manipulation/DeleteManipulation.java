/*
 * 作成日: 2003/10/06
 *
 */
package com.nullfish.lib.vfs.manipulation;

import com.nullfish.lib.vfs.Manipulation;

/**
 * ファイル削除クラス
 * @author Shunji Yamaura
 */
public interface DeleteManipulation extends Manipulation {
	/**
	 * 操作名
	 */
	public static final String NAME = "delete_file";
	
	/**
	 * 削除失敗時方針をセットする。
	 * prepare前に実行すること。
	 * 
	 * @param policy
	 */
	public abstract void setDeleteFailurePolicy(DeleteFailurePolicy policy);
	
}
