/*
 * 作成日: 2003/10/27
 * 
 */
package com.nullfish.lib.vfs.impl.commons_http;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

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
public class CommonsHTTPFileSystem extends AbstractFileSystem {
	private String scheme;

	private String host;

	private int port;

	private static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

	private UserInfo userInfo;

	/**
	 * プロキシーポート設定
	 */
	public static final String CONFIG_PROXY_PORT = "http_proxy_port";

	/**
	 * プロキシーサーバー設定
	 */
	public static final String CONFIG_PROXY_SERVER = "http_proxy_server";

	public CommonsHTTPFileSystem(VFS vfs, String scheme, String host, int port,
			UserInfo userInfo) {
		super(vfs);
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.userInfo = userInfo;

		rootName = new CommonsHTTPFileName(scheme, host, port, new String[0],
				null, null, null, userInfo);
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
		return new CommonsHTTPFile(this, fileName);
	}

	public boolean isLocal() {
		return false;
	}
	
	public HttpClient getHttpClient() {
		HttpClient rtn = new HttpClient(connectionManager);
		rtn.setHostConfiguration(getConfiguration());
		return rtn;
	}
	
	private HostConfiguration getConfiguration() {
		HostConfiguration rtn = new HostConfiguration();
		
		String proxyHost = (String)getVFS().getConfiguration().getDefaultConfig(CONFIG_PROXY_SERVER);
		Integer proxyPort = (Integer)getVFS().getConfiguration().getDefaultConfig(CONFIG_PROXY_PORT);
		
		if(proxyHost != null && proxyPort != null) {
			rtn.setProxy(proxyHost, proxyPort.intValue() );
		}
		
		return rtn;
	}

	public boolean isShellCompatible() {
		return false;
	}
}
