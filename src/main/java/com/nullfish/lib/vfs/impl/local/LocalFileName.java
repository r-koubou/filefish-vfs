package com.nullfish.lib.vfs.impl.local;

import java.io.File;
import java.net.URI;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.UserInfo;


/**
 * ローカルファイル名クラス
 * @author Shunji Yamaura
 */
public class LocalFileName extends FileName {
	/**
	 * スキーム
	 */
	public static final String SCHEME = "file";

	/**
	 * ファイルセパレータ
	 */
	private static String fileNameDevider = File.separator;
	
	public LocalFileName(
		String[] fileNames,
		FileName baseFileName,
		UserInfo userInfo,
		String drive) {

		super(
			SCHEME,
			baseFileName,
			fileNames,
			userInfo,
			drive.toUpperCase(),
			0,
			"",
			"");
	}

	/**
	 * 絶対パスを返す。
	 * @see com.nullfish.lib.vfs.FileName#doGetAbsolutePath()
	 */
	public String doGetAbsolutePath() {
		StringBuffer buffer = new StringBuffer();
		
		if(host != null && !host.equals("/")) {
			buffer.append(host);
			buffer.append(":");
		}
		
		buffer.append(fileNameDevider);
		
		String[] path = getPath();
		
		for(int i=0; i<path.length; i++) {
			buffer.append(path[i]);
			if(i != path.length - 1) {
				buffer.append(fileNameDevider);
			}
		}

		return buffer.toString();
	}

	/**
	 * ユーザー情報抜きのパスを返す。
	 */
	public String getSecurePath() {
		return getAbsolutePath(); 
	}
	
	/**
	 * URIを返す。
	 * @see com.nullfish.lib.vfs.FileName#getURI()
	 */
	public URI getURI() {
		StringBuffer buffer = new StringBuffer();
		
		if(host != null && !"/".equals(host)) {
			buffer.append(host);
			buffer.append(":");
		}
		
		buffer.append(SEPARATOR);
		
		String[] path = getPath();
		
		for(int i=0; i<path.length; i++) {
			buffer.append(path[i]);
			if(i != path.length - 1) {
				buffer.append(SEPARATOR);
			}
		}

//		try {
		return new File(buffer.toString()).toURI();
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
//			return null;
//		}
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
		return new LocalFileName(
			path,
			baseFileName,
			userInfo,
			host);
	}

	/**
	 * パスのセパレータ文字を返す。
	 * @return
	 */
	public String getSeparator() {
		return File.separator;
	}
}
