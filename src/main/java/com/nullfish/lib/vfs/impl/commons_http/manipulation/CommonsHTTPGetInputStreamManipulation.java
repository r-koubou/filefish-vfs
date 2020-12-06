/*
 * 作成日: 2003/11/18
 *
 */
package com.nullfish.lib.vfs.impl.commons_http.manipulation;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.exception.WrongPathException;
import com.nullfish.lib.vfs.impl.commons_http.CommonsHTTPFile;
import com.nullfish.lib.vfs.impl.commons_http.CommonsHTTPFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation;

/**
 * @author shunji
 * 
 */
public class CommonsHTTPGetInputStreamManipulation extends
		AbstractGetInputStreamManipulation {
	private GetMethod getMethod;
	private HttpClient client;
	
	private Object synchroObj = new Object();
	
	public CommonsHTTPGetInputStreamManipulation(VFile file) {
		super(file);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation#doGetInputStream(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public InputStream doGetInputStream(VFile file) throws VFSException {
		try {
			client = ((CommonsHTTPFileSystem)file.getFileSystem()).getHttpClient();
			getMethod = CommonsHTTPFile.createGetMethod(file.getURI(), true);

			int response = client.executeMethod(getMethod);

			if (response >= 400) {
				throw new FileNotExistsException(file);
			}

			return new CmmonsHTTPInputStream(getMethod
					.getResponseBodyAsStream());
		} catch (MalformedURLException e) {
			throw new WrongPathException(file.getAbsolutePath());
		} catch (IOException e) {
			throw new VFSIOException("IOException opening "
					+ file.getAbsolutePath(), e);
		} catch (URISyntaxException e) {
			throw new WrongPathException(file.getAbsolutePath());
		}
	}

	private class CmmonsHTTPInputStream extends FilterInputStream {
		public CmmonsHTTPInputStream(InputStream is) {
			super(is);
		}

		public void close() throws IOException {
			synchronized(synchroObj) {
				if(getMethod != null) {
					try {
						getMethod.abort();
						getMethod.releaseConnection();
					} catch (Exception e) {
					} finally {
						getMethod = null;
					}
				}
			}
			super.close();
		}
	}
	
	public void doStop() {
		getWorkThread().interrupt();
		synchronized(synchroObj) {
			if(getMethod != null) {
				getMethod.abort();
				getMethod.releaseConnection();
				getMethod = null;
			}
		}
	}
}
