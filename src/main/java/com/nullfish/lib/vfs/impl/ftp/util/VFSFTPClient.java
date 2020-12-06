/*
 * Created on 2004/09/17
 *
 */
package com.nullfish.lib.vfs.impl.ftp.util;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.net.ftp.FTPClient;


public class VFSFTPClient extends FTPClient {
	FTPClientPool pool;
	
	Timer disconnectTimer;
	
	public VFSFTPClient(FTPClientPool pool) {
		super();
		this.pool = pool;
	}
	
	public void save() {
		if(disconnectTimer != null) {
			disconnectTimer.cancel();
		}
	}
	
	public void disconnect() throws IOException {
		pool.returnClient(this);
		disconnectTimer = new Timer();
		disconnectTimer.schedule(new Disconnecter(), 30000);
	}
	
	public void reallyDisconnect() throws IOException {
		super.disconnect();
	}
	
	private class Disconnecter extends TimerTask {
		public void run() {
			pool.removeClient(VFSFTPClient.this);
			try {
				reallyDisconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
