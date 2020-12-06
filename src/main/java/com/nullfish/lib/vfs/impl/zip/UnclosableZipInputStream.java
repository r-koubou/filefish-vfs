/*
 * Created on 2003/12/08
 *
 */
package com.nullfish.lib.vfs.impl.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * @author shunji
 *
 */
public class UnclosableZipInputStream extends ZipInputStream {

	/**
	 * @param in
	 */
	public UnclosableZipInputStream(InputStream in) {
		super(in);
	}

	public void close() throws IOException {
		closeEntry();
	}
	
	public void closeReally() throws IOException {
		super.close();
	}
}
