/*
 * Created on 2004/04/06
 *
 */
package com.nullfish.lib.vfs.permission;

/**
 * ユーザーごとのファイルアクセス。
 * 
 * @author shunji
 */
public class UserFileAccess implements FileAccess {
	private String user;
	
	int hash = -1;
	
	public UserFileAccess(String user) {
		this.user = user;
	}
	
	/**
	 * ハッシュ値を取得する。
	 */
	public int hashCode() {
		if(hash == -1) {
			hash = ("com.sexyprogrammer.lib.vfs.UserFileAccess:" + user).hashCode();
		}
		return hash;
	}

	/**
	 * オブジェクトが等しいか判定する。
	 */
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		
		if(o == null) {
			return false;
		}
		
		if(this.getClass() != o.getClass()) {
			return false;
		}
		
		UserFileAccess other = (UserFileAccess)o;
		return this.user.equals(other.user); 
	}
	
	/**
	 * 名称を取得する。
	 */
	public String getName() {
		return user;
	}
}
