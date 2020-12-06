package com.nullfish.lib.vfs;

import com.nullfish.lib.vfs.permission.FileAccess;
import com.nullfish.lib.vfs.permission.PermissionType;

/**
 * ファイルのパーミッションインターフェイス。
 * 実装クラスのパーミッションを変更しても、実際のファイルには反映されません。
 * 
 * @author Shunji Yamaura
 */

public interface Permission {
	/**
	 * ファイルパーミッションの属性名を取得する。
	 * @return	属性名の配列
	 */
	public PermissionType[] getTypes();
	
	/**
	 * アクセス種別を取得する。
	 * @return	アクセス種別の配列
	 */
	public FileAccess[] getAccess();
	
	/**
	 * ファイルパーミッションの属性値を取得する。
	 * @param name	パーミッション種類
	 * @param access	ユーザー種別
	 * @return		パーミッション値
	 */
	public boolean hasPermission(PermissionType name, FileAccess access);
	
	/**
	 * ファイルパーミッションの属性値をセットする
	 * @param name	パーミッション種類
	 * @param access	ユーザー種別
	 * @param value		パーミッション値
	 */
	public void setPermission(PermissionType name, FileAccess access, boolean value);

	/**
	 * オーナーを取得する
	 * @return	オーナー
	 */
	public String getOwner();

	/**
	 * オーナーをセットする。
	 * @param owner	オーナー
	 */
	public void setOwner(String owner);

	/**
	 * グループを取得する
	 * @return	グループ
	 */
	public String getGroup();

	/**
	 * グループをセットする。
	 * @param 	group グループ
	 */
	public void setGroup(String group);

	
	/**
	 * 他のパーミッションの値を取り込む。
	 * @param permission 
	 */
	public void importPermission(Permission permission);

	/**
	 * パーミッションの文字列表現を取得する。
	 * @return	パーミッション文字列
	 */
	public String getPermissionString();
	
	/**
	 * パーミッション文字列から初期化する。
	 *
	 */
	public void initFromString(String pemissionStr);
	
	/**
	 * パーミッションの編集可否を返す（実装予定）
	 * @param name
	 * @param access
	 * @return
	 */
	public boolean isEditable(PermissionType name, FileAccess access);
}
