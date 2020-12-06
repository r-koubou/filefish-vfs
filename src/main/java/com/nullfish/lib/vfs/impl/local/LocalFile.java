/*
 * 作成日: 2003/10/08
 *
 */
package com.nullfish.lib.vfs.impl.local;

import java.io.File;
import java.text.Normalizer;

import jp.ne.anet.kentkt.fastfile.FastFile;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;


/**
 * ローカルファイルクラス
 * 
 * @author Shunji Yamaura
 */
public class LocalFile extends VFile {
	/*
	 * Javaのファイルクラス
	 */
	private File file;

	private static LocalFileManipulationFactory provider =
		new LocalFileManipulationFactory();

	/**
	 * コンストラクタ
	 * @param fileSystem
	 * @param fileName
	 */
	public LocalFile(FileSystem fileSystem, FileName fileName) {
		this(fileSystem, fileName, null);
	}
	
	/**
	 * コンストラクタ
	 * @param fileSystem
	 * @param fileName
	 * @param file
	 */
	public LocalFile(FileSystem fileSystem, FileName fileName, File file) {
		super(fileSystem, fileName);
		this.file = file;

		//	パフォーマンス向上のために、Manipulation無しで
		//	属性をキャッシュしておく。
		setAttributeCache(new LocalFileAttribute(this));
		permission = new LocalPermission(this);
	}
	
	
	/**
	 * ローカルファイル操作クラス提供クラスを取得する。
	 * @see com.nullfish.lib.vfs.VFile#getManipulationFactory()
	 */
	public ManipulationFactory getManipulationFactory() {
		return provider;
	}

	/**
	 * Javaのファイルオブジェクトを取得する。
	 * @return
	 */
	public File getFile() {
		if (file == null) {
			//			file = new File(getFileName().getAbsolutePath());
			if(VFS.IS_MAC && VFS.JAVA6_OR_LATER) {
				file = new FastFile(Normalizer.normalize(getFileName().getAbsolutePath(), Normalizer.Form.NFD));
			} else {
				file = new FastFile(getFileName().getAbsolutePath());
			}
		}

		return file;
	}

	/**
	 * ファイルが等しいか判定する。
	 * 他のVFile継承クラスと異なり、java.io.Fileのequalsで判定している。
	 * これはWindowsの場合、大文字と小文字を区別しないため。
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null) {
			return false;
		}

		if (!getClass().equals(o.getClass())) {
			return false;
		}

		LocalFile otherFile = (LocalFile) o;
		
		if(!VFS.IS_MAC) {
			// Mac以外
			return this.getFile().equals(otherFile.getFile());
		} else {
			// Mac
			String thisPath = getAbsolutePath().toLowerCase();
			String otherPath = otherFile.getAbsolutePath().toLowerCase();
			
			return thisPath.equals(otherPath);
		}
	}
	
	/**
	 * このファイルのあるファイルシステムの全容量を取得する。
	 * @return
	 */
	public long getTotalSpace() {
		return getFile().getTotalSpace();
	}
	
	/**
	 * このファイルのあるファイルシステムの空き容量を取得する。
	 * @return
	 */
	public long getFreeSpace() {
		return getFile().getFreeSpace();
	}
	
	/**
	 * Macかどうか判定する。
	 * @return
	 */
	public static boolean isMac() {
		return VFS.IS_MAC;
	}
}
