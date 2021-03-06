/*
 * 作成日: 2003/11/18
 *
 */
package com.nullfish.lib.vfs.impl.http.manipulation;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.exception.WrongPathException;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation;


/**
 * @author shunji
 *
 */
public class HTTPGetInputStreamManipulation
	extends AbstractGetInputStreamManipulation {
	public HTTPGetInputStreamManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation#doGetInputStream(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public InputStream doGetInputStream(VFile file) throws VFSException {
		try {
			return file.getURI().toURL().openConnection().getInputStream();
		} catch (MalformedURLException e) {
			throw new WrongPathException(file.getAbsolutePath());
		} catch (IOException e) {
			throw new VFSIOException("IOException opening " + file.getAbsolutePath(), e);
		} catch (URISyntaxException e) {
			throw new WrongPathException(file.getAbsolutePath());
		}
	}

	public void doStop() {
		getWorkThread().interrupt();
	}
}
