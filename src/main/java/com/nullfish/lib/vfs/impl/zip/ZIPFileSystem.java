/*
 * 作成日: 2003/11/07
 *
 */
package com.nullfish.lib.vfs.impl.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileFactory;
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


/**
 * @author shunji
 *
 */
public class ZIPFileSystem extends AbstractFileSystem {

	private List entries = new ArrayList();

	private Map nameFileMap = new HashMap();

	private UserInfo userInfo;

	boolean opened = false;

	Map threadStreamMap = new HashMap();

	
	public ZIPFileSystem(VFS vfs, VFile baseFile, UserInfo userInfo) {
		super(vfs);
		parentFileSystem = baseFile.getFileSystem();
		rootName = new ZIPFileName(new String[0], baseFile.getFileName(), userInfo);
	}

	public VFile getBaseFile() {
		return parentFileSystem.getFile(rootName.getBaseFileName());
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#open()
	 */
	public void doOpen(Manipulation manipulation) throws VFSException {
		VFile baseFile = getBaseFile();
		baseFile.getFileSystem().open(manipulation);
		if(!opened) {
			initFileTree(baseFile.getInputStream());
		}
		opened = true;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#close()
	 */
	public void doClose(Manipulation manipulation) throws VFSException {
		opened = false;
		entries.clear();
		nameFileMap.clear();
		
		Iterator ite = threadStreamMap.values().iterator();
		while(ite.hasNext()) {
			((ZIPInputStreamHolder)ite.next()).close();
		}
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
		return ZIPFile.getInstance(this, (ZIPFileName)fileName);
	}
	
	/*
	 * ファイル名に対応するファイルインスタンスが既に存在するなら返す。
	 */
	ZIPFile getFileCache(ZIPFileName fileName) {
		return (ZIPFile)nameFileMap.get(fileName);
	}
	
	/*
	 * ファイル名に対応するファイルのキャッシュを登録する。
	 */
	void addFileCache(ZIPFileName name, ZIPFile file) {
		nameFileMap.put(name, file);
	}
	
	public InputStream getInputStream(ZIPFile file) throws VFSException {
		Thread currentThread = Thread.currentThread();
		ZIPInputStreamHolder holder = (ZIPInputStreamHolder)threadStreamMap.get(currentThread);
		if(holder == null) {
			holder = new ZIPInputStreamHolder(this);
			threadStreamMap.put(currentThread, holder);
		}
		
		return holder.getInputStream(file);
	}

	/**
	 * アーカイブ内のエントリ情報からファイルツリーを初期化する。
	 * @param is
	 */
	private void initFileTree(InputStream is) throws VFSException {
		ZipInputStream zis = null;
		try {
			zis = new ZipInputStream(is);
			ZipEntry entry = zis.getNextEntry();
			
			while (entry != null) {
				FileName name = rootName.resolveFileName(entry.getName(), FileType.DIRECTORY);
				ZIPFile file = ZIPFile.getInstance(this, (ZIPFileName)name);
				file.setZipEntry(entry);
				
				FileType fileType;
				if (entry.isDirectory()) {
					fileType = FileType.DIRECTORY;
				} else {
					fileType = FileType.FILE;
				}

				FileAttribute attr =
					new DefaultFileAttribute(
						true,
						entry.getSize(),
						new Date(entry.getTime()),
						fileType);
				file.setInitialAttribute(attr);

				entries.add(entry);
				nameFileMap.put(name, file);

				initFileParent(file);
				
				entry = zis.getNextEntry();
			}
		} catch (IOException e) {
			throw new VFSIOException(
				"Failed to open a ZIP file : "
					+ FileFactory.interpretPath(rootName), e);
		} finally {
			try {
				zis.close();
			} catch (Exception e) {
			}
		}
	}
	
	private void initFileParent(ZIPFile file) throws VFSException {
		while(!file.isRoot()) {
			FileName name = file.getFileName().getParent();
			ZIPFile parentFile = (ZIPFile) nameFileMap.get(name);
			if(parentFile == null) {
				parentFile = ZIPFile.getInstance(this, (ZIPFileName)name);
			}
			
			if(parentFile.getInitialAttribute() == null) {
				FileAttribute attr =
					new DefaultFileAttribute(
						true,
						-1,
						new Date(),
						FileType.DIRECTORY);
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

	public boolean isShellCompatible() {
		return false;
	}
}
