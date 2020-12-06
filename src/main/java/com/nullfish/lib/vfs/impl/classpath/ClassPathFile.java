/*
 * 作成日: 2003/10/08
 *
 */
package com.nullfish.lib.vfs.impl.classpath;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFile;


/**
 * クラスパス内ファイルクラス
 * 
 * @author Shunji Yamaura
 */
public class ClassPathFile extends VFile {
	private static ClassPathFileManipulationFactory provider =
		new ClassPathFileManipulationFactory();

	/**
	 * コンストラクタ
	 * @param fileSystem
	 * @param fileName
	 */
	public ClassPathFile(FileSystem fileSystem, FileName fileName) {
		super(fileSystem, fileName);
	}
	
	/**
	 * ローカルファイル操作クラス提供クラスを取得する。
	 * @see com.nullfish.lib.vfs.VFile#getManipulationFactory()
	 */
	public ManipulationFactory getManipulationFactory() {
		return provider;
	}
}
