package com.nullfish.lib.vfs.impl.smbwin;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.nullfish.lib.vfs.FileFactory;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.UserInfo;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.WrongPathException;

/**
 * SMBファイルのパスを解釈するクラス。
 * 
 * @author Shunji Yamaura
 */
public class SMBFileFactory extends FileFactory {
	public static boolean isWindows = System.getProperty("os.name").indexOf("Windows") != -1;

	/**
	 * パスが解釈可能ならtrueを返す。
	 * @see com.nullfish.lib.vfs.FileFactory#isInterpretable(java.lang.String)
	 */
	public boolean isInterpretable(String path) {
		if(!isWindows) {
			return false;
		}
		
		Boolean enabled = (Boolean)vfs.getConfiguration().getDefaultConfig(SMBFileSystem.CONFIG_NATIVE_SMB_ENABLED);
		if(enabled != null && !enabled.booleanValue()) {
			return false;
		}
		
		return path.startsWith("\\\\");
	}

	/**
	 * ファイル名がSMBファイルの物か判定する。
	 * @see com.nullfish.lib.vfs.FileFactory#isBelongingFileName(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public boolean isBelongingFileName(FileName fileName) {
		if(!isWindows) {
			return false;
		}

		Boolean enabled = (Boolean)vfs.getConfiguration().getDefaultConfig(SMBFileSystem.CONFIG_NATIVE_SMB_ENABLED);
		if(enabled != null && !enabled.booleanValue()) {
			return false;
		}
		
		return SMBFileName.SCHEME.equals(fileName.getScheme());
	}

	/**
	 * パスを解釈して、ファイル名を返す。
	 * 
	 * @see com.nullfish.lib.vfs.FileFactory#interpretFileName(java.lang.String)
	 */
	public FileName interpretFileName(String path, FileName baseFileName)
	throws WrongPathException {
		try {
			if(path.startsWith("\\\\")) {
				return interpretWindowsName(path, baseFileName);
			}
			
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
				} else {
					user = "";
				}

				if (tokenizer.hasMoreTokens()) {
					password = tokenizer.nextToken();
				} else {
					password = "";
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

			return new SMBFileName(
					host,
					pathArray,
					baseFileName);
		} catch (URISyntaxException e) {
			throw new WrongPathException(path);
		}
	}

	/**
	 * Windows式パスを解釈する。
	 * @param path	
	 * @param baseFileName
	 * @return
	 */
	private FileName interpretWindowsName(String path, FileName baseFileName) {
		StringTokenizer tokenizer = new StringTokenizer(path, "\\");
		if(!tokenizer.hasMoreTokens()) {
			return new SMBFileName(
					"",
					new String[0],
					baseFileName);
		}
		
		//	ホスト
		String host = tokenizer.nextToken();
		
		//	パス
		List pathes = new ArrayList();
		while(tokenizer.hasMoreTokens()) {
			pathes.add(tokenizer.nextToken());
		}
		
		String[] pathArray = new String[pathes.size()];
		pathArray = (String[])pathes.toArray(pathArray);
		
		return new SMBFileName(
				host,
				pathArray,
				baseFileName);
	}
	
	/**
	 * ファイル名からファイルシステムを取得する。
	 * 
	 * @see com.nullfish.lib.vfs.FileFactory#interpretFileSystem(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public FileSystem interpretFileSystem(FileName fileName) {
		return new SMBFileSystem(
				getFileSystemManager(),
				fileName.getHost());
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
	public void configChanged() {}
	
	/**
	 * ファイルシステムのルートを返す。
	 * @return
	 */
	public VFile[] listRoots() {
		return null;
	}
}
