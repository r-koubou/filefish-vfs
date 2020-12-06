/*
 * 作成日: 2003/10/27
 * 
 */
package com.nullfish.lib.vfs.impl.http;


import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.UserInfo;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.AbstractFileSystem;


/**
 * @author shunji
 * 
 */
public class HTTPFileSystem extends AbstractFileSystem {
	private String scheme;
	
	private String host;

	private int port;

	private UserInfo userInfo;

	/**
	 * プロキシ使用の設定
	 */
	public static final String CONFIG_USE_PROXY = "use_http_proxy";
	
	/**
	 * プロキシホスト名
	 */
	public static final String CONFIG_PROXY_HOST = "http_proxy_host";
	
	/**
	 * プロキシポート番号
	 */
	public static final String CONFIG_PROXY_PORT = "http_proxy_port";
	
	public HTTPFileSystem(VFS vfs, String scheme, String host, int port, UserInfo userInfo) {
		super(vfs);
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.userInfo = userInfo;

		rootName =
			new HTTPFileName(
				scheme,
				host,
				port,
				new String[0],
				null,
				null,
				null,
				userInfo);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#open()
	 */
	public void doOpen(Manipulation manipulation) throws VFSException {
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#close()
	 */
	public void doClose(Manipulation manipulation) throws VFSException {
	}

	/**
	 *  
	 */
	public boolean isOpened() {
		return true;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#createFileSystem()
	 */
	public void createFileSystem() {
		// 特に何もしない
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#getFile(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public VFile getFile(FileName fileName) {
		return new HTTPFile(this, fileName);
	}
	
	public boolean isLocal() {
		return false;
	}
	
	public boolean isShellCompatible() {
		return false;
	}
}
