package com.nullfish.lib.vfs.impl.local;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nullfish.lib.vfs.FileFactory;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.WrongPathException;


/**
 * ローカルファイルのパスを解釈するクラス。
 * 
 * @author Shunji Yamaura
 */
public class LocalFileFactory extends FileFactory {
	/**
	 * Windowsのファイルパスの正規表現
	 * これで合ってるのかなあ?
	 */
	public static final Pattern WINDOWS_PATH_PATTERN = Pattern.compile("^[A-Za-z]:[\\\\/][^?\";:<>*|]*");
	//public static final Pattern WINDOWS_PATH_PATTERN = Pattern.compile("^[A-Za-z]:\\\\.*");
	
	/**
	 * パスが解釈可能ならtrueを返す。
	 * @see com.nullfish.lib.vfs.FileFactory#isInterpretable(java.lang.String)
	 */
	public boolean isInterpretable(String path) {
		Matcher windowsMatcher = WINDOWS_PATH_PATTERN.matcher(path);
		if(windowsMatcher.matches()) {
			return true;
		}

		// UNIX形式
		if((path.startsWith("\\") || path.startsWith("/")) 
				&& (path.length() <= 1 || (path.length() > 1 && path.charAt(1) != '\\' && path.charAt(1) != '/'))) {
			return true;
		}
		
		try {
			if (path.startsWith("file://")) {
				URI uri = new URI(path);
				return true;
			}
		} catch (URISyntaxException e) {
			return false;
		}
		
		return false;
	}

	/**
	 * ファイル名がローカルファイルの物か判定する。
	 * @see com.nullfish.lib.vfs.FileFactory#isBelongingFileName(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public boolean isBelongingFileName(FileName fileName) {
		return "file".equals( fileName.getScheme() );
	}

	/**
	 * パスを解釈して、ファイル名を返す。
	 * @throws WrongPathException
	 * 
	 * @see com.nullfish.lib.vfs.FileFactory#interpretFileName(java.lang.String)
	 */
	public FileName interpretFileName(String path, FileName baseFileName) 
		throws WrongPathException {
		if(path.startsWith("file://")) {
			try {
				URI uri = new URI(path);
				File file = new File(uri);
				path = file.getAbsolutePath();
			} catch (URISyntaxException e) {
				throw new WrongPathException(path, e);
			}
		}
		
		Matcher windowsMatcher = WINDOWS_PATH_PATTERN.matcher(path);
		if(windowsMatcher.matches()) {
			return interpretWindowsFileName(path, baseFileName);
		} else {
			return interpretUnixFileName(path, baseFileName);
		}
	}

	/**
	 * ファイル名からファイルシステムを取得する。
	 * 
	 * @see com.nullfish.lib.vfs.FileFactory#interpretFileSystem(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public FileSystem interpretFileSystem(FileName fileName) {
		String host = fileName.getHost();
		if(host != null && host.length() > 0) {
			return new LocalFileSystem(getFileSystemManager(), host.charAt(0));
		} else {
			return new LocalFileSystem(getFileSystemManager(), '/');
		}
	}

	/*
	 * Windowsのファイルパスを解釈する。
	 */
	private FileName interpretWindowsFileName(String path, FileName baseFileName) {
		String drive = path.substring(0, 1);
		StringTokenizer tokenizer = new StringTokenizer(path.substring(2), "\\/");
		List pathList = new ArrayList();
		while(tokenizer.hasMoreTokens()) {
			pathList.add(tokenizer.nextToken());
		}
		
		String[] pathArray = new String[pathList.size()];
		pathArray = (String[])pathList.toArray(pathArray);
		
		return new LocalFileName(pathArray, baseFileName, null, drive);
	}

	/*
	 * UNIXのファイルパスを解釈する。
	 */
	private FileName interpretUnixFileName(String path, FileName baseFileName) {
		String drive = "/";
		StringTokenizer tokenizer = new StringTokenizer(path, "\\/");
		List pathList = new ArrayList();
		while(tokenizer.hasMoreTokens()) {
			pathList.add(tokenizer.nextToken());
		}
		
		String[] pathArray = new String[pathList.size()];
		pathArray = (String[])pathList.toArray(pathArray);
		
		return new LocalFileName(pathArray, baseFileName, null, drive);
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
		File[] roots = File.listRoots();
		VFile[] rtn = new VFile[roots.length];
		for(int i=0; i<roots.length; i++) {
			try {
				rtn[i] = getFileSystemManager().getFile(roots[i].getAbsolutePath());
			} catch (VFSException e) {
				// 発生しない
				e.printStackTrace();
			}
		}
		
		return rtn;
	}
}
