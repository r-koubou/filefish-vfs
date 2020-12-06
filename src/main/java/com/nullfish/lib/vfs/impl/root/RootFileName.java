/*
 * 作成日: 2003/11/07
 *
 */
package com.nullfish.lib.vfs.impl.root;

import java.net.URI;
import java.net.URISyntaxException;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.UserInfo;


/**
 * @author shunji
 *
 */
public class RootFileName extends FileName {
	/**
	 * スキーマ
	 */
	public static final String SCHEME = "root";
	
	URI uri = null;
	
	private static final RootFileName instance = new RootFileName();
	
	/**
	 * コンストラクタ
	 * @param fileNames
	 * @param baseFileName
	 */
	private RootFileName() {

		super(
			SCHEME,
			null,
			new String[0],
			null,
			"",
			-1,
			"",
			"");
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileName#doGetAbsolutePath()
	 */
	public String doGetAbsolutePath() {
		return "root:///";
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
			uri = new URI("root:///");
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
		return new RootFileName();
	}

	/**
	 * パスのセパレータ文字を返す。
	 * @return
	 */
	public String getSeparator() {
		return SEPARATOR;
	}

	public static RootFileName getInstance() {
		return instance;
	}
}
