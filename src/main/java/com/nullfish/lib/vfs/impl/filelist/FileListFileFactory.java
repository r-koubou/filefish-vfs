/*
 * 作成日: 2003/11/07
 *
 */
package com.nullfish.lib.vfs.impl.filelist;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.nullfish.lib.vfs.FileFactory;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.WrongPathException;

/**
 * @author shunji
 * 
 */
public class FileListFileFactory extends FileFactory {
	public static final String EXTENSION_FILE_LIST = "list";
	
	public static final String EXTENSION_SMART_FILE_LIST = "slist";
	
	/**
	 * パスが解釈可能ならtrueを返す。
	 * 
	 * @see com.nullfish.lib.vfs.FileFactory#isInterpretable(java.lang.String)
	 */
	public boolean isInterpretable(String path) {
		try {
			if (path.startsWith("list://")) {
				URI uri = new URI(path);
				return true;
			}
		} catch (URISyntaxException e) {
			// 例外を条件判定に使ってはいけませんよ、皆さん。
		}
		return false;
	}

	/**
	 * ファイル名がファイルリストの物か判定する。
	 * 
	 * @see com.nullfish.lib.vfs.FileFactory#isBelongingFileName(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public boolean isBelongingFileName(FileName fileName) {
		return FileListFileName.SCHEME.equals(fileName.getScheme());
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

			// パス
			String[] pathArray;
			String pathStr = uri.getPath();

			if (pathStr != null) {
				List pathList = new ArrayList();
				StringTokenizer tokenizer = new StringTokenizer(pathStr,
						FileName.SEPARATOR);
				while (tokenizer.hasMoreTokens()) {
					pathList.add(tokenizer.nextToken());
				}

				pathArray = new String[pathList.size()];
				pathArray = (String[]) pathList.toArray(pathArray);
			} else {
				pathArray = new String[0];
			}

			return new FileListFileName(pathArray, baseFileName);
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
		FileName baseFileName = fileName.getBaseFileName();
		if (baseFileName.getLowerExtension().equals(EXTENSION_FILE_LIST)) {
			return new FileListFileSystem(getFileSystemManager(), getFileSystemManager()
					.getFile(fileName.getBaseFileName()));
		} else if(baseFileName.getLowerExtension().equals(EXTENSION_SMART_FILE_LIST)) {
			return new SmartFileListFileSystem(getFileSystemManager(), getFileSystemManager()
					.getFile(fileName.getBaseFileName()));
		} else {
			throw new WrongPathException(fileName.getAbsolutePath());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nullfish.lib.vfs.FileFactory#getInnerRoot(com.nullfish.lib.vfs.VFile)
	 */
	public FileName doGetInnerRootName(VFile file) {
		String extension = file.getFileName().getLowerExtension();
		if (extension.equals("list") || extension.equals("slist")) {
			return new FileListFileName(new String[0], file.getFileName());
		}
		return null;
	}
	
	/**
	 * 設定が変更された際に呼び出される。
	 *
	 */
	public void configChanged() {}

	public VFile[] listRoots() {
		// TODO Auto-generated method stub
		return null;
	}
}
