/*
 * 作成日: 2003/11/04
 *
 */
package com.nullfish.lib.vfs.impl.ftp.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;

/**
 * @author shunji
 *
 */
public class FTPOutputStream extends BufferedOutputStream {

	FTPClient ftp;

	
	public FTPOutputStream(OutputStream out, FTPClient ftp) {
		super(out);
		this.ftp = ftp;
	}
	
	public void close() throws IOException {
		super.close();
		Runnable disconnector = new Runnable() {
			public void run() {
				try {
					ftp.completePendingCommand();
				} catch (IOException e) {
				}
				try {
					ftp.disconnect();
				} catch (IOException e) {
				}
			}
		};
	
		AutoInterruptionThread thread = new AutoInterruptionThread(disconnector, 60000);
		thread.start();
	}
}
