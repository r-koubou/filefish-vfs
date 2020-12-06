/*
 * 作成日: 2003/11/07
 *
 */
package com.nullfish.lib.vfs.impl.http;


import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.permission.ClassFileAccess;
import com.nullfish.lib.vfs.permission.FileAccess;
import com.nullfish.lib.vfs.permission.PermissionType;


/**
 * @author shunji
 *
 */
public class HTTPPermission implements Permission {
	
	/*
	 * パーミッション属性名
	 */
	private static final PermissionType[] keys = {
		PermissionType.READABLE
	};
	
	/**
	 * アクセス種別
	 */
	private static final FileAccess[] access = {
		ClassFileAccess.ALL
	};
	
	public HTTPPermission() {
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Permission#getTypes()
	 */
	public PermissionType[] getTypes() {
		return keys;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Permission#getAccess()
	 */
	public FileAccess[] getAccess() {
		return access;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Permission#getValue(java.lang.String, java.lang.String)
	 */
	public boolean hasPermission(PermissionType name, FileAccess access) {
		return PermissionType.READABLE.equals(name);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Permission#getOwner()
	 */
	public String getOwner() {
		return null;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Permission#setOwner(java.lang.String)
	 */
	public void setOwner(String owner) {
	}

	/**
	 * グループを取得する
	 * @return
	 */
	public String getGroup() {
		return null;
	}
	
	/**
	 * グループをセットする。
	 * @param owner
	 */
	public void setGroup(String group) {
	}
	
	/**
	 * パーミッションを設定する。
	 * @see com.nullfish.lib.vfs.Permission#setPermission(java.lang.String, com.sexyprogrammer.lib.vfs.FileAccess, boolean)
	 */
	public void setPermission(PermissionType name, FileAccess access, boolean value) {
		//	何もしない。HTTPだから読み込み可能なだけ。
	}

	/**
	 * 他のパーミッションの内容を取り込む。
	 * @see com.nullfish.lib.vfs.Permission#importPermission(com.sexyprogrammer.lib.vfs.Permission)
	 */
	public void importPermission(Permission permission) {
		//	何もしない。HTTPだから読み込み可能なだけ。
	}
	
	/**
	 * パーミッションの文字列表現を取得する。
	 * @return
	 */
	public String getPermissionString() {
		return "r";
	}

	/**
	 * パーミッション文字列から初期化する。
	 *
	 */
	public void initFromString(String pemissionStr) {
		//	何もしない（できない）
	}
	
	/**
	 * パーミッションの編集可否を返す
	 * @param name
	 * @param access
	 * @return
	 */
	public boolean isEditable(PermissionType name, FileAccess access) {
		return false;
	}
}
