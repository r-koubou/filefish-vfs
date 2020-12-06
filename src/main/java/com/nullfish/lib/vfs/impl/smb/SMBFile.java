/*
 * 作成日: 2003/10/08
 *
 */
package com.nullfish.lib.vfs.impl.smb;

import java.net.MalformedURLException;
import java.net.UnknownHostException;

import jcifs.smb.SmbFile;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFile;

/**
 * SMBファイルクラス
 * 
 * @author Shunji Yamaura
 */
public class SMBFile extends VFile {
	/*
	 * ファイルとしてのSmbのファイルクラス
	 */
	private SmbFile file;

	/*
	 * ディレクトリとしてのとしてのSmbのファイルクラス
	 */
	private SmbFile directory;

	private static SMBFileManipulationFactory provider = new SMBFileManipulationFactory();

	/**
	 * コンストラクタ
	 * 
	 * @param fileSystem
	 * @param fileName
	 */
	public SMBFile(FileSystem fileSystem, FileName fileName) {
		super(fileSystem, fileName);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param fileSystem
	 * @param fileName
	 * @param file
	 */
	public SMBFile(FileSystem fileSystem, FileName fileName, SmbFile file) {
		super(fileSystem, fileName);
		this.file = file;
	}

	/**
	 * ローカルファイル操作クラス提供クラスを取得する。
	 * 
	 * @see com.nullfish.lib.vfs.VFile#getManipulationFactory()
	 */
	public ManipulationFactory getManipulationFactory() {
		return provider;
	}

	/**
	 * Smbのファイルオブジェクトをファイルとして取得する。
	 * 
	 * @return
	 */
	public SmbFile getFileAsFile() throws UnknownHostException {
		if (file == null) {
			try {
				file = new SmbFile(((SMBFileName) getFileName()).toJCIFSPath(),
						((SMBFileSystem) getFileSystem()).getAuth());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return file;
	}

	/**
	 * Smbのファイルオブジェクトをファイルとして取得する。
	 * 
	 * @return
	 */
	public SmbFile getFileAsDirectory() throws UnknownHostException {
		if (directory == null) {
			try {
				directory = new SmbFile(((SMBFileName) getFileName()).toJCIFSPath() + "/",
						((SMBFileSystem) getFileSystem()).getAuth());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return directory;
	}
}
