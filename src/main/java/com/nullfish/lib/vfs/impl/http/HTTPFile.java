/*
 * 作成日: 2003/11/07
 *
 */
package com.nullfish.lib.vfs.impl.http;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFile;

/**
 * @author shunji
 *
 */
public class HTTPFile extends VFile {
	private static final ManipulationFactory provider =
		new HTTPFileManipulationFactory();

	/**
	 * コンストラクタ
	 * ファイルインスタンスの生成を制御するため、プライベートになっている。
	 * インスタンス生成にはgetInstanceメソッドを使用する。
	 * @param fileSystem
	 * @param fileName
	 */
	public HTTPFile(FileSystem fileSystem, FileName fileName) {
		super(fileSystem, fileName);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.VFile#getManipulationFactory()
	 */
	public ManipulationFactory getManipulationFactory() {
		return provider;
	}
}
