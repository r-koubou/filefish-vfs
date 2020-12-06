package com.nullfish.lib.vfs.impl.smbwin;

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

	public SMBFileName(
		String host,
		String[] fileNames,
		FileName baseFileName) {

		super(SCHEME, baseFileName, fileNames, UserInfo.NO_INPUT, host, -1, "", "");
	}

	/**
	 * 絶対パスを返す。
	 * 
	 * @see com.nullfish.lib.vfs.FileName#doGetAbsolutePath()
	 */
	public String doGetAbsolutePath() {
		return toUNCPath();
	}

	/**
	 * ユーザー情報抜きのパスを返す。
	 */
	public String getSecurePath() {
		//return toSecureJCIFSPath();
		return toUNCPath();

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

		return new SMBFileName(host, path, baseFileName);
	}

	/**
	 * パスのセパレータ文字を返す。
	 * @return
	 */
	public String getSeparator() {
		return "\\";
	}
}
