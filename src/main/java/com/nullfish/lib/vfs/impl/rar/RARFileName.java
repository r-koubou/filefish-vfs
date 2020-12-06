package com.nullfish.lib.vfs.impl.rar;

import java.net.URI;
import java.net.URISyntaxException;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.UserInfo;


public class RARFileName extends FileName {
	/**
	 * スキーマ
	 */
	public static final String SCHEME = "rar";
	
	URI uri = null;
	
	/**
	 * コンストラクタ
	 * @param fileNames
	 * @param baseFileName
	 * @param userInfo
	 */
	public RARFileName(
		String[] fileNames,
		FileName baseFileName,
		UserInfo userInfo) {

		super(
			SCHEME,
			baseFileName,
			fileNames,
			userInfo,
			"",
			-1,
			"",
			"");
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileName#doGetAbsolutePath()
	 */
	public String doGetAbsolutePath() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(SCHEME);
		buffer.append("://");
		buffer.append(getPathString());
		return buffer.toString();
	}

	/**
	 * ユーザー情報抜きのパスを返す。
	 */
	public String getSecurePath() {
		return getAbsolutePath(); 
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileName#getURI()
	 */
	public URI getURI() throws URISyntaxException {
		if(uri == null) {
			String userInfoStr = null;
			if(userInfo != null) {
				String password = (String)userInfo.getInfo(UserInfo.PASSWORD);
			
				if(password != null) {
					userInfoStr = password;
				}
			}
		
			StringBuffer pathBuffer = new StringBuffer();
			for(int i=0; i<path.length; i++) {
				pathBuffer.append("/");
				pathBuffer.append(path[i]);
			}
		
			uri = new URI(SCHEME, userInfoStr, host, port, pathBuffer.toString(), null, null);
		}
		
		return uri;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileName#createFileName(com.sexyprogrammer.lib.vfs.FileName, java.lang.String[], com.sexyprogrammer.lib.vfs.UserInfo, java.lang.String, int, java.lang.String, java.lang.String)
	 */
	public FileName doCreateFileName(
		String scheme,
		FileName baseFileName,
		String[] path,
		UserInfo userInfo,
		String host,
		int port,
		String query,
		String fragment) {
		return new RARFileName(path, baseFileName, userInfo);
	}

	/**
	 * パスのセパレータ文字を返す。
	 * @return
	 */
	public String getSeparator() {
		return SEPARATOR;
	}
}
