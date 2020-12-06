package com.nullfish.lib.vfs.impl.smb;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.UserInfo;


/**
 * ローカルファイル名クラス
 * 
 * @author Shunji Yamaura
 */
public class SMBFileName extends FileName {
	/**
	 * URI
	 */
	URI uri;

	/**
	 * スキーム
	 */
	public static final String SCHEME = "smb";

	/**
	 * ファイルセパレータ
	 */
	private static String fileNameDevider = File.separator;

	public SMBFileName(
		String host,
		int port,
		String[] fileNames,
		FileName baseFileName,
		UserInfo userInfo) {

		super(SCHEME, baseFileName, fileNames, userInfo, host, port, "", "");
	}

	/**
	 * 絶対パスを返す。
	 * 
	 * @see com.nullfish.lib.vfs.FileName#doGetAbsolutePath()
	 */
	public String doGetAbsolutePath() {
		//return toJCIFSPath();
		return toUNCPath();
	}

	/**
	 * jCIFS用のパスにして返す（URLエンコードしない）。
	 * @return
	 */
	public String toJCIFSPath() {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(SCHEME);
			sb.append("://");
			
			URI uri = getURI();
			if(uri.getUserInfo() != null && uri.getUserInfo().length() > 0) {
				sb.append(uri.getUserInfo());
				sb.append("@");
			}
			
			if(uri.getHost() == null || uri.getHost().length() == 0) {
				return sb.toString();
			}
			
			sb.append(uri.getHost());
			sb.append(uri.getPath());
			
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	/**
	 * ユーザー情報抜きのパスを返す。
	 */
	public String getSecurePath() {
		//return toSecureJCIFSPath();
		return toUNCPath();

	}

	/**
	 * ユーザー情報抜きで、jCIFS向けパスを返す。
	 * @return
	 */
	public String toSecureJCIFSPath() {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(SCHEME);
			sb.append("://");
			
			URI uri = getURI();
			if(uri.getHost() == null || uri.getHost().length() == 0) {
				return sb.toString();
			}
			
			sb.append(uri.getHost());
			sb.append(uri.getPath());
			
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	/**
	 * UNC形式のパスに変換する
	 * @return
	 */
	public String toUNCPath() {
		StringBuffer rtn = new StringBuffer();
		rtn.append("\\\\");
		rtn.append(getHost());
		rtn.append(getPathString().replace('/', '\\'));
		
		return rtn.toString();
	}
	
	/**
	 * URIを返す。
	 * 
	 * @see com.nullfish.lib.vfs.FileName#getURI()
	 */
	public URI getURI() throws URISyntaxException {
		if (uri == null) {
			String userInfoStr = null;
			if (userInfo != null) {
				String user = (String) userInfo.getInfo(UserInfo.USER);
				String password = (String) userInfo.getInfo(UserInfo.PASSWORD);

				if (user != null && password != null) {
					userInfoStr = user + ":" + password;
				}
			}

			StringBuffer pathBuffer = new StringBuffer();
			for (int i = 0; i < path.length; i++) {
				pathBuffer.append("/");
				pathBuffer.append(path[i]);
			}

			uri =
				new URI(
					SCHEME,
					userInfoStr,
					host,
					port,
					pathBuffer.toString(),
					null,
					null);
		}

		return uri;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileName#createFileName(com.sexyprogrammer.lib.vfs.FileName,
	 *      java.lang.String[], com.sexyprogrammer.lib.vfs.UserInfo,
	 *      java.lang.String, int, java.lang.String, java.lang.String)
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

		return new SMBFileName(host, port, path, baseFileName, userInfo);
	}

	/**
	 * パスのセパレータ文字を返す。
	 * @return
	 */
	public String getSeparator() {
		return SEPARATOR;
	}
}
