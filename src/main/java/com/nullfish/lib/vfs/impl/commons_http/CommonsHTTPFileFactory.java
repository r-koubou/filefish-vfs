/*
 * 作成日: 2003/11/07
 * 
 */
package com.nullfish.lib.vfs.impl.commons_http;

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
 * @author shunji
 * 
 */
public class CommonsHTTPFileFactory extends FileFactory {

	/**
	 * パスが解釈可能ならtrueを返す。
	 * 
	 * @see com.nullfish.lib.vfs.FileFactory#isInterpretable(java.lang.String)
	 */
	public boolean isInterpretable(String path) {
		try {
			if (path.startsWith("http://") || path.startsWith("https://")) {
				URI uri = new URI(path);
				return true;
			}
			if (path.startsWith("ttp://")) {
				URI uri = new URI("h" + path);
				return true;
			}
		} catch (URISyntaxException e) {
			//	例外を条件判定に使ってはいけませんよ、皆さん。
		}
		return false;
	}

	/**
	 * ファイル名がHTTPファイルの物か判定する。
	 * 
	 * @see com.nullfish.lib.vfs.FileFactory#isBelongingFileName(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public boolean isBelongingFileName(FileName fileName) {
		return CommonsHTTPFileName.SCHEME.equals(fileName.getScheme())
			|| CommonsHTTPFileName.SCHEME_HTTPS.equals(fileName.getScheme());
	}

	/**
	 * パスを解釈して、ファイル名を返す。
	 * 
	 * @see com.nullfish.lib.vfs.FileFactory#interpretFileName(java.lang.String)
	 */
	public FileName interpretFileName(String path, FileName baseFileName)
		throws WrongPathException {
		try {
			if(path.startsWith("ttp://")) {
				path = "h" + path;
			}
			URI uri = new URI(path);

			//	ユーザー
			UserInfo userInfo;
			String password = null;
			String userInfoStr = uri.getUserInfo();
			if (userInfoStr != null) {
				StringTokenizer tokenizer =
					new StringTokenizer(userInfoStr, ":");
				if (tokenizer.hasMoreTokens()) {
					password = tokenizer.nextToken();
				}

				userInfo = new UserInfo();
				userInfo.setInfo(UserInfo.PASSWORD, password);
			} else {
				userInfo = UserInfo.NO_INPUT;
			}

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

			return new CommonsHTTPFileName(
				uri.getScheme(),
				uri.getHost(),
				uri.getPort(),
				pathArray,
				uri.getQuery(),
				uri.getFragment(),
				baseFileName,
				userInfo);
		} catch (URISyntaxException e) {
			throw new WrongPathException(path);
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.Interpreter#interpretFileSystem(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public FileSystem interpretFileSystem(FileName fileName)
		throws WrongPathException {
		return new CommonsHTTPFileSystem(
			getFileSystemManager(),
			fileName.getScheme(),
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
	public void configChanged() {}
	
	/**
	 * ファイルシステムのルートを返す。
	 * @return
	 */
	public VFile[] listRoots() {
		return null;
	}
}
