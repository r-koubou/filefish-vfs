/*
 * 作成日: 2003/11/07
 *
 */
package com.nullfish.lib.vfs.impl.lha;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.dangan.util.lha.LhaFile;
import jp.gr.java_conf.dangan.util.lha.LhaHeader;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.impl.AbstractFileSystem;
import com.nullfish.lib.vfs.impl.DefaultFileAttribute;
import com.nullfish.lib.vfs.impl.antzip.TemporaryFileUtil;
import com.nullfish.lib.vfs.impl.local.LocalFile;


/**
 * @author shunji
 *
 */
public class LHAFileSystem extends AbstractFileSystem {

	private List entries = new ArrayList();

	private Map nameFileMap = new HashMap();

//	private UserInfo userInfo;

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

	private LhaFile lhaFile;
	
	public LHAFileSystem(VFS vfs, VFile baseFile) {
		super(vfs);
		parentFileSystem = baseFile.getFileSystem();
		rootName = new LHAFileName(new String[0], baseFile.getFileName());
	}

	public VFile getBaseFile() {
		return parentFileSystem.getFile(rootName.getBaseFileName());
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#open()
	 */
	public void doOpen(Manipulation manipulation) throws VFSException {
		try {
			VFile baseFile = getBaseFile();
			baseFile.getFileSystem().open(manipulation);
	
			if (!opened) {
				if (baseFile instanceof LocalFile) {
					usesLocalCopy = false;
					lhaFile = new LhaFile( ((LocalFile) baseFile).getFile() );
				} else {
					usesLocalCopy = true;
					tempFile = TemporaryFileUtil.getInstance().getTemporaryFile(
							baseFile, manipulation).getFile();
					lhaFile = new LhaFile(tempFile);
				}
	
				initFileTree(lhaFile);
			}
			opened = true;
		} catch (IOException e) {
			try {
				if(lhaFile != null) {
					lhaFile.close();
				}
				if(usesLocalCopy) {
					tempFile.delete();
				}
			} catch (Exception ex) {}
			throw new VFSIOException(e);
		}
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#close()
	 */
	public void doClose(Manipulation manipulation) throws VFSException {
		opened = false;
		entries.clear();
		nameFileMap.clear();
		try {
			if(lhaFile != null) {
				lhaFile.close();
			}
	
			if(usesLocalCopy && tempFile != null) {
				tempFile.delete();
			}
		} catch (Exception e) {e.printStackTrace(); }
	}

	public boolean isOpened() {
		return opened;
	}

	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#createFileSystem()
	 */
	public void createFileSystem() throws VFSException {
		VFile baseFile = getBaseFile();
		if(!baseFile.exists()) {
			baseFile.createFile();
		}
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#getFile(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public VFile getFile(FileName fileName) {
		return LHAFile.getInstance(this, (LHAFileName)fileName);
	}
	
	/*
	 * ファイル名に対応するファイルインスタンスが既に存在するなら返す。
	 */
	LHAFile getFileCache(LHAFileName fileName) {
		return (LHAFile)nameFileMap.get(fileName);
	}
	
	/*
	 * ファイル名に対応するファイルのキャッシュを登録する。
	 */
	void addFileCache(LHAFileName name, LHAFile file) {
		nameFileMap.put(name, file);
	}
	
	public InputStream getInputStream(LHAFile file) throws VFSException {
		return lhaFile.getInputStream( file.getLhaHeader() );
	}

	/**
	 * アーカイブ内のエントリ情報からファイルツリーを初期化する。
	 * @param is
	 */
	private void initFileTree(LhaFile lhaFile) throws VFSException {
		LhaHeader[] entriesArray = lhaFile.getEntries();
		
		for( int i=0; i< entriesArray.length; i++) { 
			totalSpace += entriesArray[i].getOriginalSize();
		}
		
		for( int i=0; i< entriesArray.length; i++) { 
			LhaHeader entry = entriesArray[i];
			
			String unixStylePath = entry.getPath().replace('\\', '/');
			FileName name = rootName.resolveFileName(unixStylePath,
					FileType.DIRECTORY);
			LHAFile file = LHAFile.getInstance(this, (LHAFileName) name);
			file.setLhaHeader(entry);

			FileType fileType;
			if (unixStylePath.endsWith(FileName.SEPARATOR)) {
				fileType = FileType.DIRECTORY;
			} else {
				fileType = FileType.FILE;
			}

			FileAttribute attr = new DefaultFileAttribute(true, entry
					.getOriginalSize(), entry.getLastModified(), fileType, totalSpace, 0);
			file.setInitialAttribute(attr);

			entries.add(entry);
			nameFileMap.put(name, file);

			initFileParent(file);
		}
	}
	
	private void initFileParent(LHAFile file) throws VFSException {
		while(!file.isRoot()) {
			FileName name = file.getFileName().getParent();
			LHAFile parentFile = (LHAFile) nameFileMap.get(name);
			if(parentFile == null) {
				parentFile = LHAFile.getInstance(this, (LHAFileName)name);
			}
			
			if(parentFile.getInitialAttribute() == null) {
				FileAttribute attr =
					new DefaultFileAttribute(
						true,
						-1,
						new Date(),
						FileType.DIRECTORY,
						totalSpace,
						0);
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

	public long getTotalSpace() {
		return totalSpace;
	}
	
	public boolean isShellCompatible() {
		return false;
	}
}
