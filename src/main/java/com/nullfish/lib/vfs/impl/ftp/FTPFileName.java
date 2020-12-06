/*
 * 作成日: 2003/10/27
 *
 */
package com.nullfish.lib.vfs.impl.ftp;

import java.net.URI;
import java.net.URISyntaxException;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.UserInfo;


/**
 * @author shunji
 *
 */
public class FTPFileName extends FileName {
	URI uri = null;
	
	/**
	 * スキーム
	 */
	public static final String SCHEME = "ftp";

	public FTPFileName(
		String host,
		int port,
		String[] fileNames,
		FileName baseFileName,
		UserInfo userInfo) {
		super(
			SCHEME,
			baseFileName,
			fileNames,
			userInfo,
			host,
			port,
			null,
			null);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileName#doGetAbsolutePath()
	 */
	public String doGetAbsolutePath() {
		try {
			return getURI().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ユーザー情報抜きのパスを返す。
	 */
	public String getSecurePath() {
		try {
			URI orig = getURI();
			URI rtnUri = new URI(orig.getScheme(), null, orig.getHost(), orig.getPort(), orig.getPath(), orig.getQuery(), orig.getFragment());
			return rtnUri.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileName#getURI()
	 */
	public URI getURI() throws URISyntaxException {
		if(uri == null) {
			String userInfoStr = null;
			if(userInfo != null) {
				String user = (String)userInfo.getInfo(UserInfo.USER);
				String password = (String)userInfo.getInfo(UserInfo.PASSWORD);
			
				if(user != null && password != null) {
					userInfoStr = user + ":" + password;
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
		return new FTPFileName(host, port, path, baseFileName, userInfo);
	}

	/**
	 * パスのセパレータ文字を返す。
	 * @return
	 */
	public String getSeparator() {
		return SEPARATOR;
	}
}
