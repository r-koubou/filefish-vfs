package com.nullfish.lib.vfs;

public interface UserInfoManager {
	/**
	 * ユーザー情報を問い合わせる。
	 * もしも中止する場合はnullを返す。
	 * @param fileName
	 * @param defaultInfo
	 * @return
	 */
	public UserInfo getUserInfo(FileName fileName, UserInfo defaultInfo);
	
	/**
	 * 受け取ったユーザー情報が正しかったことを通知する。
	 * @param userInfo
	 * @param fileName
	 */
	public void noticeUserInfoCollect(UserInfo userInfo, FileName fileName);
	
	/**
	 * 受け取ったユーザー情報が正しくなかったことを通知する。
	 * @param userInfo
	 * @param fileName
	 */
	public void noticeUserInfoIncollect(UserInfo userInfo, FileName fileName);
}
