/*
 * Created on 2004/09/17
 *
 */
package com.nullfish.lib.vfs.impl.ftp.util;

import java.util.ArrayList;
import java.util.List;

import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.ftp.FTPFileSystem;

/**
 * @author shunji
 *
 */
public class FTPClientPool {
	private FTPFileSystem fileSystem;
	
	private List pool = new ArrayList();

	private String encode;
	
	public FTPClientPool(FTPFileSystem fileSystem, String encode) {
		this.fileSystem = fileSystem;
		this.encode = encode;
	}
	
	public String getEncode() {
		return encode;
	}

	public synchronized void removeClient(VFSFTPClient client) {
		pool.remove(client);
	}
	
	public synchronized void returnClient(VFSFTPClient client) {
		pool.add(client);
	}
	
	public synchronized VFSFTPClient getConnection() throws VFSException {
		VFSFTPClient client = null;
		boolean active = false;
		if(pool.size() > 0) {
			client = (VFSFTPClient)pool.get(0);
			client.save();
			
			pool.remove(0);
			try {
				active = client.sendNoOp();
				if(!active) {
					fileSystem.connect(client);
				}

				return client;
			} catch (Exception e) {
				active = false;
			}
		}
		
		if(!active) {
			client = new VFSFTPClient(this);
			fileSystem.initEncoding(client);
			fileSystem.connect(client);
		}
		client.save();
		
		return client;
	}
	
	public boolean isEncodeSame(String encode) {
		if(this.encode == null && encode == null) {
			return true;
		}
		
		if(this.encode != null && encode != null) {
			return this.encode.equals(encode);
		}
		
		return false;
	}
}
