package com.nullfish.lib.vfs.impl.rar;

import java.util.ArrayList;
import java.util.List;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFile;

import de.innosystec.unrar.rarfile.FileHeader;


public class RARFile extends VFile {
	private static final ManipulationFactory provider =
		new RARFileManipulationFactory();

	List children = new ArrayList();

	
	FileHeader header;
	
	/**
	 * コンストラクタ
	 * ファイルインスタンスの生成を制御するため、プライベートになっている。
	 * インスタンス生成にはgetInstanceメソッドを使用する。
	 * @param fileSystem
	 * @param fileName
	 */
	private RARFile(FileSystem fileSystem, FileName fileName) {
		super(fileSystem, fileName);
	}

	/**
	 * ファイルインスタンスを取得する。
	 * 
	 * @param fileSystem	ファイルシステム
	 * @param fileName		ファイル名
	 * @return				指定されたファイルシステム、ファイル名のファイル
	 */
	public static RARFile getInstance(RARFileSystem fileSystem, RARFileName fileName) {
		RARFile rtn = fileSystem.getFileCache(fileName);
		if(rtn == null) {
			rtn = new RARFile(fileSystem, fileName);
			fileSystem.addFileCache(fileName, rtn);
		}
		
		return rtn;
	}
	
	/* (非 Javadoc)
	 * @see org.sexyprogrammer.lib.vfs.VFile#getManipulationFactory()
	 */
	public ManipulationFactory getManipulationFactory() {
		return provider;
	}
	
	/**
	 * 子ファイルを追加する。
	 * @param file
	 */
	public void addChildFile(VFile file) {
		if(!children.contains(file)) { 
			children.add(file);
		}
	}
	
	/**
	 * 子ファイルを取得する。
	 */
	public List getChildFiles() {
		return children;
	}
	
	
	public void setInitialAttribute(FileAttribute attributes) {
		this.attributes = attributes;
	}
	
	public FileAttribute getInitialAttribute() {
		return attributes;
	}
	
	public FileHeader getFileHeader() {
		return header;
	}
	
	public void setFileHeader(FileHeader header) {
		this.header = header;
	}
}
