/*
 * 作成日: 2003/11/07
 *
 */
package com.nullfish.lib.vfs.impl.commons_http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFile;

/**
 * @author shunji
 *
 */
public class CommonsHTTPFile extends VFile {
	private static final ManipulationFactory provider =
		new CommonsHTTPFileManipulationFactory();

	/**
	 * コンストラクタ
	 * ファイルインスタンスの生成を制御するため、プライベートになっている。
	 * インスタンス生成にはgetInstanceメソッドを使用する。
	 * @param fileSystem
	 * @param fileName
	 */
	public CommonsHTTPFile(FileSystem fileSystem, FileName fileName) {
		super(fileSystem, fileName);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.VFile#getManipulationFactory()
	 */
	public ManipulationFactory getManipulationFactory() {
		return provider;
	}
	
	public static GetMethod createGetMethod(URI uri, boolean followRedirects) throws HttpException, IOException, URISyntaxException {
		GetMethod getMethod = new GetMethod(uri.toString());
		
		DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
		retryhandler.setRequestSentRetryEnabled(true);
		retryhandler.setRetryCount(5);
		getMethod.setMethodRetryHandler(retryhandler);

		getMethod.setFollowRedirects(followRedirects);
		
		return getMethod;
	}
}
