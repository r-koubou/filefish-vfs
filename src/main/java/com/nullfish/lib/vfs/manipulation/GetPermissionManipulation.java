/*
 * 作成日: 2003/10/06
 *
 */
package com.nullfish.lib.vfs.manipulation;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.Permission;

/**
 * パーミッション取得インターフェイス
 * @author Shunji Yamaura
 */

public interface GetPermissionManipulation extends Manipulation {
	/**
	 * 操作名
	 */
	public static final String NAME = "get_permission";

	/**
	 * パーミッションを取得する。
	 * @return
	 */
	public abstract Permission getPermission();

}
