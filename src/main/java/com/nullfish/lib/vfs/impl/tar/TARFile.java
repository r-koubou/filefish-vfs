/*
 * 作成日: 2003/11/07
 *
 */
package com.nullfish.lib.vfs.impl.tar;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.tar.TarEntry;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFile;


/**
 * @author shunji
 *
 */
public class TARFile extends VFile {
	private static final ManipulationFactory provider =
		new TARFileManipulationFactory();

	List children = new ArrayList();

	
	TarEntry entry;
	
	/**
	 * コンストラクタ
	 * ファイルインスタンスの生成を制御するため、プライベートになっている。
	 * インスタンス生成にはgetInstanceメソッドを使用する。
	 * @param fileSystem
	 * @param fileName
	 */
	private TARFile(FileSystem fileSystem, FileName fileName) {
		super(fileSystem, fileName);
	}

	/**
	 * ファイルインスタンスを取得する。
	 * 
	 * @param fileSystem	ファイルシステム
	 * @param fileName		ファイル名
	 * @return				指定されたファイルシステム、ファイル名のファイル
	 */
	public static TARFile getInstance(TARFileSystem fileSystem, TARFileName fileName) {
		TARFile rtn = fileSystem.getFileCache(fileName);
		if(rtn == null) {
			rtn = new TARFile(fileSystem, fileName);
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
	
	public TarEntry getTarEntry() {
		return entry;
	}
	
	public void setTarEntry(TarEntry entry) {
		this.entry = entry;
	}
}
