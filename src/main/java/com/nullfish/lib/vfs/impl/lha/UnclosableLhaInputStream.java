/*
 * Created on 2003/12/08
 *
 */
package com.nullfish.lib.vfs.impl.lha;

import java.io.IOException;
import java.io.InputStream;

import jp.gr.java_conf.dangan.util.lha.LhaInputStream;

/**
 * @author shunji
 *
 */
public class UnclosableLhaInputStream extends LhaInputStream {

	/**
	 * @param in
	 */
	public UnclosableLhaInputStream(InputStream in) {
		super(in);
	}

	public void close() throws IOException {
		closeEntry();
	}
	
	public void closeReally() throws IOException {
		super.close();
	}
}
