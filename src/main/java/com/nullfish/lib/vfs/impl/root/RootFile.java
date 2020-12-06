package com.nullfish.lib.vfs.impl.root;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFile;


/**
 * @author shunji
 *
 */
public class RootFile extends VFile {

	private static ManipulationFactory manipulationFactory = new RootManipulationFactory();
	
	/**
	 * コンストラクタ
	 * ファイルインスタンスの生成を制御するため、プライベートになっている。
	 * インスタンス生成にはgetInstanceメソッドを使用する。
	 * @param fileSystem
	 * @param fileName
	 */
	public RootFile(FileSystem fileSystem, FileName fileName) {
		super(fileSystem, fileName);
	}
	
	public ManipulationFactory getManipulationFactory() {
		return manipulationFactory;
	}
}
