package com.nullfish.lib.vfs.impl.filelist;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFile;


/**
 * @author shunji
 *
 */
public class FileListFile extends VFile {

	private static ManipulationFactory manipulationFactory = new FileListFileManipulationFactory();
	
	/**
	 * コンストラクタ
	 * ファイルインスタンスの生成を制御するため、プライベートになっている。
	 * インスタンス生成にはgetInstanceメソッドを使用する。
	 * @param fileSystem
	 * @param fileName
	 */
	public FileListFile(FileSystem fileSystem, FileName fileName) {
		super(fileSystem, fileName);
	}

	public void setInitialAttribute(FileAttribute attributes) {
		this.attributes = attributes;
	}
	
	public FileAttribute getInitialAttribute() {
		return attributes;
	}
	
	public ManipulationFactory getManipulationFactory() {
		return manipulationFactory;
	}
}
