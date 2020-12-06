package com.nullfish.lib.vfs.impl.classpath;

import java.net.URI;
import java.net.URISyntaxException;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.UserInfo;


/**
 * クラスパス内ファイル名クラス
 * @author Shunji Yamaura
 */
public class ClassPathFileName extends FileName {
	URI uri = null;
	
	/**
	 * スキーム
	 */
	public static final String SCHEME = "classpath";

	public ClassPathFileName(
		String[] fileNames) {
		super(
			SCHEME,
			null,
			fileNames,
			null,
			null,
			0,
			null,
			null);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileName#getAbsolutePath()
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
		return getAbsolutePath();
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileName#getURI()
	 */
	public URI getURI() throws URISyntaxException {
		if(uri == null) {
			StringBuffer pathBuffer = new StringBuffer("/");
			for(int i=0; i<path.length; i++) {
				pathBuffer.append(path[i]);
				if(i != path.length - 1) {
					pathBuffer.append("/");
				}
			}
		
			uri = new URI(SCHEME, host, pathBuffer.toString(), null, null);
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
		return new ClassPathFileName(path);
	}

	/**
	 * パスのセパレータ文字を返す。
	 * @return
	 */
	public String getSeparator() {
		return SEPARATOR;
	}
}
