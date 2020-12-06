/*
 * Created on 2004/04/09
 *
 */
package com.nullfish.lib.vfs.permission;

/**
 * パーミッション種別を表すクラス。
 * 読み込み、書き込み、実行などの権限を表す。
 * 
 * @author shunji
 */
public class PermissionType {
	//	パーミッション種別名称
	private String name;
	
	/**
	 * 読み込み権限
	 */
	public static final PermissionType READABLE = new PermissionType("readable");
	
	/**
	 * 書き込み権限
	 */
	public static final PermissionType WRITABLE = new PermissionType("writable");
	
	/**
	 * 実行権限
	 */
	public static final PermissionType EXECUTABLE = new PermissionType("executable");
	
	/**
	 * コンストラクタ。
	 * @param name
	 */
	private PermissionType(String name) {
		this.name = name;
	}
	
	/**
	 * オブジェクトが等しいならtrueを返す。
	 */
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		
		if(o.getClass() != getClass()) {
			return false;
		}
		
		return name.equals(((PermissionType)o).name);
	}
		
	/**
	 * ハッシュ値を返す。
	 */
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * パーミッション種別名称を取得する。
	 * @return
	 */
	public String getName() {
		return name;
	}
}
