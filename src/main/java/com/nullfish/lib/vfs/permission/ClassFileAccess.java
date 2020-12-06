/*
 * Created on 2004/04/06
 *
 */
package com.nullfish.lib.vfs.permission;

/**
 * アクセスクラスによるファイルアクセス。
 * タイプセーフenumで、UNIXファイルシステムの3つのクラス。
 * （オーナー、グループ、一般）のインスタンスを持つ、
 * タイプセーフenumで実装されている。
 * 
 * 
 * @author shunji
 */
public class ClassFileAccess implements FileAccess {
	/**
	 * オーナー
	 */
	public static final ClassFileAccess OWNER = new ClassFileAccess("owner");
	
	/**
	 * グループ
	 */
	public static final ClassFileAccess GROUP = new ClassFileAccess("group");
	
	/**
	 * その他一般
	 */
	public static final ClassFileAccess ALL = new ClassFileAccess("normal");
	
	private String name;
	
	int hash = -1;
	
	/**
	 * コンストラクタ。
	 * @param name
	 */
	private ClassFileAccess(String name) {
		this.name = name;
	}
	
	/**
	 * ハッシュ値を取得する。
	 */
	public int hashCode() {
		if(hash == -1) {
			hash = ("com.sexyprogrammer.lib.vfs.ClassFileAccess:" + name).hashCode();
		}
		return hash;
	}

	/**
	 * オブジェクトが等しいか判定する。
	 */
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		
		return this == o;
	}

	/**
	 * 名称を取得する。
	 */
	public String getName() {
		return name;
	}

}
