/*
 * 作成日: 2003/10/27
 *
 */
package com.nullfish.lib.vfs.impl.smb;


import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.UserInfo;
import com.nullfish.lib.vfs.UserInfoManager;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.AbstractFileSystem;


/**
 * @author shunji
 *
 */
public class SMBFileSystem extends AbstractFileSystem {
	private String host;

	private int port;

	private UserInfo userInfo;

	private NtlmPasswordAuthentication auth;
	
	private VFile rootFile = getFile(getRootName());
	
	/**
	 * デフォルトのパスのタイプ（URLかUNC）
	 */
	public static final String CONFIG_PATH_TYPE = "smb_path_type";
	
	/**
	 * URL
	 */
	public static final String PATH_TYPE_URL = "url";
	
	/**
	 * UNC
	 */
	public static final String PATH_TYPE_UNC = "unc";
	
	public SMBFileSystem(VFS vfs, String host, int port, UserInfo userInfo) {
		super(vfs);
		this.host = host;
		this.port = port;
		this.userInfo = userInfo;
		
		rootName = new SMBFileName(host, port, new String[0], null, userInfo);
		
		if(userInfo != UserInfo.NO_INPUT) {
			String user = (String)userInfo.getInfo(UserInfo.USER);
			int colonIndex = user.indexOf(';');
			if(colonIndex != -1) {
				String domain = user.substring(0, colonIndex);
				user = user.substring(colonIndex + 1);
				auth = new NtlmPasswordAuthentication(domain, user, (String)userInfo.getInfo(UserInfo.PASSWORD));
			} else {
				auth = new NtlmPasswordAuthentication("", user, (String)userInfo.getInfo(UserInfo.PASSWORD));
			}
		}
			
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#open()
	 */
	public void doOpen(Manipulation manipulation) throws VFSException {
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#close()
	 */
	public void doClose(Manipulation manipulation) throws VFSException {
		auth = null;
	}

	/**
	 * 
	 */
	public boolean isOpened() {
		return true;
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#createFileSystem()
	 */
	public void createFileSystem() {
		// 特に何もしない
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#getFile(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public VFile getFile(FileName fileName) {
		return new SMBFile(this, fileName);
	}
	
	public boolean isLocal() {
		return false;
	}
	
	public NtlmPasswordAuthentication getAuth() {
		return auth;
	}
	
	public void cacheAuthentication(SmbFile smbFile) {
		auth = (NtlmPasswordAuthentication)smbFile.getPrincipal();

		UserInfoManager manager = VFS.getUserInfoManager();
		if(manager != null && userInfo == UserInfo.NO_INPUT) {
			String domain = auth.getDomain();
			String user = auth.getUsername();
			String password = auth.getPassword();
			
			UserInfo userInfo = new UserInfo();
			if(domain != null && domain.length() > 0) {
				userInfo.setInfo(UserInfo.USER, domain + ";" + user);
			} else {
				userInfo.setInfo(UserInfo.USER, user);
			}
			userInfo.setInfo(UserInfo.PASSWORD, password);
			
			manager.noticeUserInfoCollect(userInfo, getRootName().getSecureName());
		}
	}
	
	public boolean isShellCompatible() {
		return false;
	}
}
