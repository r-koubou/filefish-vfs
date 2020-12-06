/*
 * 作成日: 2003/10/27
 *
 */
package com.nullfish.lib.vfs.impl.ftp;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.nullfish.lib.vfs.Configuration;
import com.nullfish.lib.vfs.FileFactory;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.UserInfo;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.WrongPathException;


/**
 * @author shunji
 *
 */
public class FTPFileFactory extends FileFactory {
	/**
	 * パスが解釈可能ならtrueを返す。
	 * @see com.nullfish.lib.vfs.FileFactory#isInterpretable(java.lang.String)
	 */
	public boolean isInterpretable(String path) {
		try {
			if (path.startsWith("ftp://")) {
				URI uri = new URI(path);
				return true;
			}
		} catch (URISyntaxException e) {
			//	例外を条件判定に使ってはいけませんよ、皆さん。
		}
		return false;
	}

	/**
	 * ファイル名がFTPファイルの物か判定する。
	 * @see com.nullfish.lib.vfs.FileFactory#isBelongingFileName(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public boolean isBelongingFileName(FileName fileName) {
		return FTPFileName.SCHEME.equals(fileName.getScheme());
	}

	/**
	 * パスを解釈して、ファイル名を返す。
	 * 
	 * @see com.nullfish.lib.vfs.FileFactory#interpretFileName(java.lang.String)
	 */
	public FileName interpretFileName(String path, FileName baseFileName)
		throws WrongPathException {
		try {
			URI uri = new URI(path);

			//	ユーザー
			UserInfo userInfo;
			String user = null;
			String password = null;
			String userInfoStr = uri.getUserInfo();
			if (userInfoStr != null) {
				StringTokenizer tokenizer =
					new StringTokenizer(userInfoStr, ":");
				if (tokenizer.hasMoreTokens()) {
					user = tokenizer.nextToken();
				}

				if (tokenizer.hasMoreTokens()) {
					password = tokenizer.nextToken();
				}

				userInfo = new UserInfo();
				userInfo.setInfo(UserInfo.USER, user);
				userInfo.setInfo(UserInfo.PASSWORD, password);
			} else {
				userInfo = UserInfo.NO_INPUT;
			}

			//	ホスト
			String host = uri.getHost();

			//	ポート
			int port = uri.getPort();

			//	パス
			String[] pathArray;
			String pathStr = uri.getPath();

			if (pathStr != null) {
				List pathList = new ArrayList();
				StringTokenizer tokenizer =
					new StringTokenizer(pathStr, FileName.SEPARATOR);
				while (tokenizer.hasMoreTokens()) {
					pathList.add(tokenizer.nextToken());
				}

				pathArray = new String[pathList.size()];
				pathArray = (String[]) pathList.toArray(pathArray);
			} else {
				pathArray = new String[0];
			}

			return new FTPFileName(
				host,
				port,
				pathArray,
				baseFileName,
				userInfo);
		} catch (URISyntaxException e) {
			throw new WrongPathException(path);
		}
	}

	/**
	 * ファイル名からファイルシステムを取得する。
	 * 
	 * @see com.nullfish.lib.vfs.FileFactory#interpretFileSystem(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public FileSystem interpretFileSystem(FileName fileName) {
		return new FTPFileSystem(
			getFileSystemManager(),
			fileName.getHost(),
			fileName.getPort(),
			fileName.getUserInfo());
	}

	/* (non-Javadoc)
	 * @see com.nullfish.lib.vfs.FileFactory#getInnerRoot(com.nullfish.lib.vfs.VFile)
	 */
	public FileName doGetInnerRootName(VFile file) {
		return null;
	}
	
	
	/**
	 * 設定が変更された際に呼び出される。
	 *
	 */
	public void configChanged() {
		VFS vfs = getFileSystemManager();
		Configuration config = vfs.getConfiguration();

		Boolean usesProxy = (Boolean) config
				.getDefaultConfig(FTPFileSystem.CONFIG_USE_PROXY);
		if (usesProxy != null && usesProxy.booleanValue()) {
			String proxyHost = (String) config
					.getDefaultConfig(FTPFileSystem.CONFIG_PROXY_HOST);
			Integer proxyPort = (Integer) config
					.getDefaultConfig(FTPFileSystem.CONFIG_PROXY_PORT);
			if (proxyHost != null) {
				System.getProperties().put("socksProxyHost", proxyHost);
			} else {
				System.getProperties().put("socksProxyHost", "");
			}

			if (proxyHost != null && proxyPort != null) {
				System.getProperties().put("socksProxyPort",
						proxyPort.toString());
			} else {
				System.getProperties().put("socksProxyPort", "");
			}
		} else {
			System.getProperties().put("socksProxyHost", "");
			System.getProperties().put("socksProxyPort", "");
		}
	}
	
	/**
	 * ファイルシステムのルートを返す。
	 * @return
	 */
	public VFile[] listRoots() {
		FileSystem[] fileSystems = getFileSystemManager().getFileSystem();
		List ftps = new ArrayList();
		for(int i=0; i<fileSystems.length; i++) {
			if(fileSystems[i] instanceof FTPFileSystem) {
				try {
					ftps.add(getFileSystemManager().getFile(fileSystems[i].getRootName()));
				} catch (WrongPathException e) {
					// ありえない
					e.printStackTrace();
				}
			}
		}
		VFile[] rtn = new VFile[ftps.size()];
		return (VFile[])ftps.toArray(rtn);
	}
}
