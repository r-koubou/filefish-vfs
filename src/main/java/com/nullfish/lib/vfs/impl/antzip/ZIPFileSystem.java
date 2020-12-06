/*
 * 作成日: 2003/11/07
 *
 */
package com.nullfish.lib.vfs.impl.antzip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.UserInfo;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.impl.AbstractFileSystem;
import com.nullfish.lib.vfs.impl.DefaultFileAttribute;
import com.nullfish.lib.vfs.impl.local.LocalFile;

/**
 */
public class ZIPFileSystem extends AbstractFileSystem {

	private List entries = new ArrayList();

	private Map nameFileMap = new HashMap();

	private UserInfo userInfo;

	boolean opened = false;

	private long totalSpace = 0;
	
	/**
	 * ローカルのテンポラリファイル
	 */
	private File tempFile;

	/**
	 * ファイルのローカルコピーを使用するかどうかのフラグ
	 */
	private boolean usesLocalCopy = false;

	private ZipFile zipFile;
	
	/**
	 * ZIPファイルのアーカイブ内のエンコーディング
	 */
	public static final String CONFIG_FILE_ENCODING = "zip_encoding";
	
	public ZIPFileSystem(VFS vfs, VFile baseFile, UserInfo userInfo) {
		super(vfs);
		parentFileSystem = baseFile.getFileSystem();
		rootName = new ZIPFileName(new String[0], baseFile.getFileName(),
				userInfo);
	}

	public VFile getBaseFile() {
		return parentFileSystem.getFile(rootName.getBaseFileName());
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#open()
	 */
	public void doOpen(Manipulation manipulation) throws VFSException {
		try {
			VFile baseFile = getBaseFile();
			baseFile.getFileSystem().open(manipulation);
	
			String encoding = (String)getVFS().getConfiguration().getDefaultConfig(CONFIG_FILE_ENCODING);
			encoding = encoding != null ? encoding : System.getProperty("file.encoding");
			
			if (!opened) {
				if (baseFile instanceof LocalFile) {
					usesLocalCopy = false;
					zipFile = new ZipFile( ((LocalFile) baseFile).getFile(), encoding);
				} else {
					usesLocalCopy = true;
					tempFile = TemporaryFileUtil.getInstance().getTemporaryFile(
							baseFile, manipulation).getFile();
					zipFile = new ZipFile(tempFile, encoding);
				}
	
				initFileTree(zipFile);
			}
			opened = true;
		} catch (IOException e) {
			try {
				if(zipFile != null) {
					zipFile.close();
				}
				if(usesLocalCopy) {
					tempFile.delete();
				}
			} catch (Exception ex) {}
			throw new VFSIOException(e);
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#close()
	 */
	public void doClose(Manipulation manipulation) throws VFSException {
		opened = false;
		entries.clear();
		nameFileMap.clear();
		try {
			if(zipFile != null) {
				zipFile.close();
			}
	
			if(usesLocalCopy && tempFile != null) {
				tempFile.delete();
			}
		} catch (IOException e) {e.printStackTrace();}
	}

	public boolean isOpened() {
		return opened;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#createFileSystem()
	 */
	public void createFileSystem() throws VFSException {
		VFile baseFile = getBaseFile();
		if (!baseFile.exists()) {
			baseFile.createFile();
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#getFile(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public VFile getFile(FileName fileName) {
		return ZIPFile.getInstance(this, (ZIPFileName) fileName);
	}

	/*
	 * ファイル名に対応するファイルインスタンスが既に存在するなら返す。
	 */
	ZIPFile getFileCache(ZIPFileName fileName) {
		return (ZIPFile) nameFileMap.get(fileName);
	}

	/*
	 * ファイル名に対応するファイルのキャッシュを登録する。
	 */
	void addFileCache(ZIPFileName name, ZIPFile file) {
		nameFileMap.put(name, file);
	}

	public InputStream getInputStream(ZIPFile file) throws VFSException {
		try {
			return zipFile.getInputStream( file.getZipEntry() );
		} catch (IOException e) {
			throw new VFSIOException(e);
		}
	}

	/**
	 * アーカイブ内のエントリ情報からファイルツリーを初期化する。
	 * 
	 * @param zipFile
	 */
	private void initFileTree(ZipFile zipFile) throws VFSException {
		Enumeration entriesEnum = zipFile.getEntries();
		while (entriesEnum.hasMoreElements()) {
			ZipEntry entry = (ZipEntry)entriesEnum.nextElement();
			entries.add(entry);
			if(!entry.isDirectory()) {
				totalSpace += entry.getSize();
			}
		}
		
		for (int i=0; i<entries.size(); i++) {
			ZipEntry entry = (ZipEntry)entries.get(i);
			
			FileName name = rootName.resolveFileName(entry.getName(),
					FileType.DIRECTORY);
			ZIPFile file = ZIPFile.getInstance(this, (ZIPFileName) name);
			file.setZipEntry(entry);

			FileType fileType;
			if (entry.isDirectory()) {
				fileType = FileType.DIRECTORY;
			} else {
				fileType = FileType.FILE;
			}

			FileAttribute attr = new DefaultFileAttribute(true, entry
					.getSize(), new Date(entry.getTime()), fileType, totalSpace, 0);
			file.setInitialAttribute(attr);
			nameFileMap.put(name, file);

			initFileParent(file);
		}
	}

	private void initFileParent(ZIPFile file) throws VFSException {
		while (!file.isRoot()) {
			FileName name = file.getFileName().getParent();
			ZIPFile parentFile = (ZIPFile) nameFileMap.get(name);
			if (parentFile == null) {
				parentFile = ZIPFile.getInstance(this, (ZIPFileName) name);
			}

			if (parentFile.getInitialAttribute() == null) {
				FileAttribute attr = new DefaultFileAttribute(true, -1,
						new Date(), FileType.DIRECTORY, totalSpace, 0);
				parentFile.setInitialAttribute(attr);
				nameFileMap.put(name, parentFile);
			}

			parentFile.addChildFile(file);

			file = parentFile;
		}
	}

	public List getEntries() {
		return entries;
	}

	public boolean isLocal() {
		return getBaseFile().getFileSystem().isLocal();
	}

	/**
	 * このファイルシステムがシェルで解釈可能ならtrueを返す。
	 * @return
	 */
	public boolean isShellCompatible() {
		return false;
	}
	
	public long getTotalSpace() {
		return totalSpace;
	}

}
