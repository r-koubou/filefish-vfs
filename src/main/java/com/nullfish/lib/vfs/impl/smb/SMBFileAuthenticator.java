package com.nullfish.lib.vfs.impl.smb;

import jcifs.smb.NtlmAuthenticator;
import jcifs.smb.NtlmPasswordAuthentication;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.UserInfo;
import com.nullfish.lib.vfs.UserInfoManager;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.exception.VFSException;

public class SMBFileAuthenticator extends NtlmAuthenticator {
	protected NtlmPasswordAuthentication getNtlmPasswordAuthentication() {
		try {
			UserInfoManager manager = VFS.getUserInfoManager();
			if (manager == null) {
				return null;
			}
	
			UserInfo defaultUser = new UserInfo();
			defaultUser.setInfo(UserInfo.USER, "");
			defaultUser.setInfo(UserInfo.PASSWORD, "");
			FileName rootName = VFS.getInstance().getFile(getRequestingURL()).getFileSystem().getRootName();
			UserInfo user = manager.getUserInfo(rootName, defaultUser);
			if (user == null) {
				return null;
			}
	
			String userName = (String) user.getInfo(UserInfo.USER);
			String domain = "";
	
			int delim = userName.indexOf(";");
			if (delim != -1) {
				domain = userName.substring(0, delim);
				userName = userName.substring(delim);
			}
	
			return new NtlmPasswordAuthentication(domain, userName, (String) user
					.getInfo(UserInfo.PASSWORD));
		} catch (VFSException e) {
			return null;
		}
	}
}
