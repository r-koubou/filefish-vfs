/*
 * 作成日: 2003/10/27
 *
 */
package com.nullfish.lib.vfs.impl.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.UserInfo;
import com.nullfish.lib.vfs.UserInfoManager;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileSystemNotExistsException;
import com.nullfish.lib.vfs.exception.ManipulationStoppedException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.exception.WrongUserException;
import com.nullfish.lib.vfs.impl.AbstractFileSystem;
import com.nullfish.lib.vfs.impl.ftp.util.FTPClientPool;

/**
 * @author shunji
 * 
 */
public class FTPFileSystem extends AbstractFileSystem {
	private String host;

	private int port;

	private UserInfo userInfo;

	FTPClient ftp = new FTPClient();

	private FTPClientPool pool;

	private FileName workDir;
	
	private String encode;
	
	/**
	 * 拡張子で自動でASCIIモード転送を行うかの設定
	 */
	public static final String TRANSFER_MODE = "ftp_transfer_mode";

	/**
	 * バイナリー転送
	 */
	public static final String TRANSFER_MODE_BINARY = "ftp_binary";

	/**
	 * ASCII転送
	 */
	public static final String TRANSFER_MODE_ASCII = "ftp_ascii";

	/**
	 * 拡張子によりASCII転送
	 */
	public static final String TRANSFER_MODE_EXTENSION = "ftp_extension";
	
	/**
	 * ASCIIモード転送を行うファイル名の拡張子
	 */
	public static final String CONFIG_ASCII_MODE_EXTENSION = "ftp_ascii_mode_extension";
	
	/**
	 * パッシブ接続
	 */
	public static final String CONFIG_PASSIVE = "ftp_passive_transfer";
	
	/**
	 * プロキシ使用の設定
	 */
	public static final String CONFIG_USE_PROXY = "use_ftp_proxy";
	
	/**
	 * プロキシホスト名
	 */
	public static final String CONFIG_PROXY_HOST = "ftp_proxy_host";
	
	/**
	 * プロキシポート番号
	 */
	public static final String CONFIG_PROXY_PORT = "ftp_proxy_port";
	
	/**
	 * ファイル名の文字コード
	 */
	public static final String CONFIG_ENCODE = "ftp_encode";
	
	/**
	 * FTPClientの使用中フラグ
	 */
	private boolean ftpClientOcupied = false;
	
	public FTPFileSystem(VFS vfs, String host, int port, UserInfo userInfo) {
		super(vfs);
		this.host = host;
		this.port = port;
		this.userInfo = userInfo;

		rootName = new FTPFileName(host, port, new String[0], null, userInfo);

		pool = new FTPClientPool(this, getEncode());
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#open()
	 */
	public void doOpen(Manipulation manipulation) throws VFSException {
		initEncoding(ftp);
		
		if(!ftp.isConnected()) {
			connect(ftp);
			
			try {
				workDir = rootName.resolveFileName(ftp.printWorkingDirectory(), FileType.DIRECTORY);
			} catch (IOException e) {
				throw new VFSIOException(e);
			}
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#close()
	 */
	public void doClose(Manipulation manipulation) throws VFSException {
		disconnect(ftp);
	}

	/**
	 *  
	 */
	public boolean isOpened() {
		return ftp.isConnected();
	}

	public void connect(FTPClient client) throws VFSException {
		UserInfoManager manager = VFS.getUserInfoManager();
		UserInfo currentUserInfo = null;
		try {
			if (port != -1) {
				client.connect(host, port);
			} else {
				client.connect(host);
			}

			int reply = client.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				throw new FileSystemNotExistsException(this);
			}

			currentUserInfo = this.userInfo;

			if(currentUserInfo == UserInfo.NO_INPUT) {
				if(manager != null) {
					UserInfo defaultInfo = new UserInfo();
					defaultInfo.setInfo(UserInfo.USER, "anonymous");
					defaultInfo.setInfo(UserInfo.PASSWORD, "");
					
					currentUserInfo = manager.getUserInfo(getRootName(), defaultInfo);
					if(currentUserInfo == null) {
						throw new ManipulationStoppedException(null);
					}
				}
			}
			
			String user = (String) currentUserInfo.getInfo(UserInfo.USER);
			String password = (String) currentUserInfo
					.getInfo(UserInfo.PASSWORD);

			if (!client.login(user != null && user.length() > 0 ? user
					: "anonymous", password != null ? password : "")) {
				client.disconnect();
				throw new WrongUserException(userInfo);
			}

			if (!client.setFileType(FTPClient.BINARY_FILE_TYPE)) {
				throw new VFSIOException(
						"Couldn't use binary transfer mode of FTP.");
			}
			
			if(manager != null) {
				manager.noticeUserInfoCollect(currentUserInfo, getRootName().getSecureName());
			}
			
			//	タイムアウトしないように設定
			client.setDataTimeout(Integer.MAX_VALUE);
		} catch (IOException e) {
			if(manager != null && currentUserInfo != null) {
				manager.noticeUserInfoIncollect(currentUserInfo, getRootName());
			}
			
			e.printStackTrace();
			throw new FileSystemNotExistsException(this);
		}
	}

	private void disconnect(FTPClient client) throws VFSException {
		if(!client.isConnected()) {
			return;
		}
		
		try {
			client.logout();
		} catch (IOException e) {
		} finally {
			try {
				client.disconnect();
			} catch (IOException e1) {
			}
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#createFileSystem()
	 */
	public void createFileSystem() {
		// 特に何もしない
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#getFile(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public VFile getFile(FileName fileName) {
		return new FTPFile(this, fileName);
	}

	/**
	 * FTP接続を取得する。
	 * FTP接続は一度に一つのスレッドからしか呼び出せない。
	 * FTP接続が不要になったら必ずreleaseFTPClientメソッドを呼ぶこと。
	 * 
	 * @return @throws
	 *         VFSException
	 */
	public synchronized FTPClient getFTPClient(Manipulation manipulation) throws VFSException {
		if(isConfigChanged()) {
			doClose(manipulation);
			doOpen(manipulation);
		}
		
		while(ftpClientOcupied) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		ftpClientOcupied = true;

		boolean active = false;

		try {
			if (!ftp.isConnected()) {
				active = false;
			} else {
				active = ftp.sendNoOp();
			}
		} catch (Exception e) {
			active = false;
		}

//		initEncoding(ftp);
		
		if (!active) {
			connect(ftp);
		}

		return ftp;
	}

	/**
	 * getFTPClientメソッドでを使用可能にする。
	 *
	 */
	public synchronized void releaseFTPClient() {
		ftpClientOcupied = false;
		notifyAll();
	}
	
	
	public FTPClient getTempFTPClient(FTPFile file) throws VFSException {
		if(!pool.isEncodeSame(getEncode())) {
			pool = new FTPClientPool(this, getEncode());
		}

		FTPClient rtn = pool.getConnection();
		initEncoding(rtn);
		try {
			file.configFtpClient(rtn);
		} catch (IOException e) {
			throw new VFSIOException(e);
		}
		
		return rtn;
	}

	public void initEncoding(FTPClient ftp) {
		String encode = (String)getVFS().getConfiguration().getConfig(this, FTPFileSystem.CONFIG_ENCODE);
		if(encode == null) {
			encode = (String)getVFS().getConfiguration().getDefaultConfig(FTPFileSystem.CONFIG_ENCODE);
		}
		if(encode != null) {
			ftp.setControlEncoding(encode);
		}
	}
	
	public boolean isConfigChanged() {
		String encode = null;
		try {
			encode = getEncode();
			
			if(this.encode == null && encode == null) {
				return false;
			}
			
			if(!(this.encode != null && encode != null)) {
				return true;
			}
			
			return !this.encode.equals(encode);
		} finally {
			this.encode = encode;
		}
	}
	
	private String getEncode() {
		String encode = (String)getVFS().getConfiguration().getConfig(this, FTPFileSystem.CONFIG_ENCODE);
		if(encode == null) {
			encode = (String)getVFS().getConfiguration().getDefaultConfig(FTPFileSystem.CONFIG_ENCODE);
		}
		
		return encode;
	}
	
	public boolean isLocal() {
		return false;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public FileName getWorkDir() {
		return workDir;
	}
	
	public boolean isShellCompatible() {
		return false;
	}
}
