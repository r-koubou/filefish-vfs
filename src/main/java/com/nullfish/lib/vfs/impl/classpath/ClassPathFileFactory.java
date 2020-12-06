package com.nullfish.lib.vfs.impl.classpath;

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
 * クラスパス内ファイルのパスを解釈するクラス。
 * 
 * @author Shunji Yamaura
 */
public class ClassPathFileFactory extends FileFactory {
	/**
	 * パスが解釈可能ならtrueを返す。
	 * @see com.nullfish.lib.vfs.FileFactory#isInterpretable(java.lang.String)
	 */
	public boolean isInterpretable(String path) {
		try {
			if (path.startsWith("classpath://")) {
				URI uri = new URI(path);
				return true;
			}
		} catch (URISyntaxException e) {
			//	例外を条件判定に使ってはいけませんよ、皆さん。
		}
		return false;
	}

	/**
	 * ファイル名がクラスパスファイルの物か判定する。
	 * @see com.nullfish.lib.vfs.FileFactory#isBelongingFileName(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public boolean isBelongingFileName(FileName fileName) {
		return ClassPathFileName.SCHEME.equals(fileName.getScheme());
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

			return new ClassPathFileName(
				pathArray);
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
		return ClassPathFileSystem.getFileSystem(getFileSystemManager());
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

	public VFile[] listRoots() {
		// TODO Auto-generated method stub
		return null;
	}
}
