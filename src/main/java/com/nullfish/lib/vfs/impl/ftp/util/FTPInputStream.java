/*
 * 作成日: 2003/11/04
 *
 */
package com.nullfish.lib.vfs.impl.ftp.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;

/**
 * ストリームが閉じたらFTP接続を切断する入力ストリーム
 * @author shunji
 *
 */
public class FTPInputStream extends BufferedInputStream {

	FTPClient ftp;

	
	/**
	 * コンストラクタ
	 * @param is
	 * @param ftp
	 */
	public FTPInputStream(InputStream in, FTPClient ftp) {
		super(in);
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
