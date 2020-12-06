package com.nullfish.lib.vfs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserInfo {
	/**
	 * ユーザー
	 */
	public static final String USER = "user";
	
	/**
	 * パスワード
	 */
	public static final String PASSWORD = "password";

	Map infoMap = new HashMap();

	public static final UserInfo NO_INPUT = new UserInfo();
	
	public UserInfo() {
	}
	
	/**
	 * ユーザー情報をセットする。
	 * @param key
	 * @param value
	 */
	public void setInfo(String key, Object value) {
		infoMap.put(key, value);
	}
	
	/**
	 * ユーザー情報を取得する。
	 * @param key
	 * @return
	 */
	public Object getInfo(String key) {
		return infoMap.get(key);
	}
	
	/**
	 * キーのセットを取得する。
	 * @return
	 */
	public Set getKeys() {
		return infoMap.keySet();
	}
	
	public int hashCode() {
		return infoMap.hashCode();
	}
	
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		
		if(!getClass().equals(o.getClass())) {
			return false;
		}
		
		return infoMap.equals(((UserInfo)o).infoMap);
	}
	
	public String toString() {
		return "user:" + getInfo(USER) + " ,pass:" + getInfo(PASSWORD);
	}
}
