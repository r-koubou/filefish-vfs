/*
 * 作成日: 2003/11/18
 *
 */
package com.nullfish.lib.vfs.impl.commons_http.manipulation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.exception.WrongPathException;
import com.nullfish.lib.vfs.impl.DefaultFileAttribute;
import com.nullfish.lib.vfs.impl.commons_http.CommonsHTTPFile;
import com.nullfish.lib.vfs.impl.commons_http.CommonsHTTPFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetAttributesManipulation;

/**
 * @author shunji
 * 
 */
public class CommonsHTTPGetAttributesManipulation extends
		AbstractGetAttributesManipulation {

	private GetMethod getMethod;

	private Object lockObj = new Object();
	
	public CommonsHTTPGetAttributesManipulation(VFile file) {
		super(file);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractInitAttributesManipulation#doGetAttribute(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public FileAttribute doGetAttribute(VFile file) throws VFSException {
		try {
			return getAttribute(file.getURI());
		} catch (URISyntaxException e) {
			throw new WrongPathException(file.getAbsolutePath());
		}
	}

	public FileAttribute getAttribute(URI uri) throws VFSException {
		try {
			HttpClient client = ((CommonsHTTPFileSystem)file.getFileSystem()).getHttpClient();
			
			getMethod = CommonsHTTPFile.createGetMethod(uri, true);
			int response = client.executeMethod(getMethod);
			
			if (response >= 400) {
				return new DefaultFileAttribute(false, 0, null, FileType.NOT_EXISTS);
			}

			boolean redirect = (response >= 300 && response <= 399);
			if (redirect) {
				String location = getMethod.getResponseHeader("Location").getValue();
				return getAttribute(uri.resolve(location));
			}
			
			Header lengthHeader = getMethod.getResponseHeader("content-length");
			Header dateHeader = getMethod.getResponseHeader("last-modified");
			return new DefaultFileAttribute(true, 
					lengthHeader != null ? Integer.parseInt(lengthHeader.getValue()) : 0,
					dateHeader != null ? getHeaderFieldDate(dateHeader.getValue()) : new Date(), 
					uri.toString().endsWith(
							"/") ? FileType.DIRECTORY : FileType.FILE);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new WrongPathException(file.getAbsolutePath());
		} catch (URISyntaxException e) {
			throw new WrongPathException(file.getAbsolutePath());
		} catch (IOException e) {
			throw new VFSIOException("IOException opening "
					+ file.getAbsolutePath(), e);
		} finally {
			synchronized(lockObj) {
				if (getMethod != null) {
					getMethod.abort();
					getMethod.releaseConnection();
					getMethod = null;
				}
			}
		}

	}
	    
	private Date getHeaderFieldDate(String value) {
	try {
    	    return new Date(value);
    	} catch (Exception e) { }
    	return new Date();
	}
	
	public synchronized void doStop() {
		getWorkThread().interrupt();
		synchronized (lockObj) {
			if (getMethod != null) {
				getMethod.abort();
				getMethod.releaseConnection();
				getMethod = null;
			}
		}
	}
}
