/*
 * 作成日: 2003/11/07
 *
 */
package com.nullfish.lib.vfs.impl.antzip;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.zip.ZipEntry;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFile;


/**
 * @author shunji
 *
 */
public class ZIPFile extends VFile {
	private static final ManipulationFactory provider =
		new ZIPFileManipulationFactory();

	List children = new ArrayList();

	
	ZipEntry entry;
	
	/**
	 * コンストラクタ
	 * ファイルインスタンスの生成を制御するため、プライベートになっている。
	 * インスタンス生成にはgetInstanceメソッドを使用する。
	 * @param fileSystem
	 * @param fileName
	 */
	private ZIPFile(FileSystem fileSystem, FileName fileName) {
		super(fileSystem, fileName);
	}

	/**
	 * ファイルインスタンスを取得する。
	 * 
	 * @param fileSystem	ファイルシステム
	 * @param fileName		ファイル名
	 * @return				指定されたファイルシステム、ファイル名のファイル
	 */
	public static ZIPFile getInstance(ZIPFileSystem fileSystem, ZIPFileName fileName) {
		ZIPFile rtn = fileSystem.getFileCache(fileName);
		if(rtn == null) {
			rtn = new ZIPFile(fileSystem, fileName);
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
	
	public ZipEntry getZipEntry() {
		return entry;
	}
	
	public void setZipEntry(ZipEntry entry) {
		this.entry = entry;
	}
}
