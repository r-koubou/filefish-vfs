/*
 * Created on 2004/06/09
 *
 */
package com.nullfish.lib.vfs.impl.local;

import java.io.File;
import java.util.Date;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.VFS;

/**
 * @author shunji
 * 
 */
public class LocalFileAttribute implements FileAttribute {

	private LocalFile file;
	
	private File javaFile;

	protected long length = -1;
	protected boolean lenghtInited = false;
	
	protected Date date;
	protected boolean dateInited = false;

	protected boolean exists;
	protected boolean existenceInited = false;

	protected FileType fileType;
	protected boolean fileTypeInited = false;

	public static final Object[] ATTRIBUTES = {
		LENGTH,
		FILE_TYPE,
		DATE,
		EXISTS,
		TOTAL_SPACE,
		FREE_SPACE
	};

	public LocalFileAttribute(LocalFile file) {
		this.file = file;
		javaFile = file.getFile();
	}

	/**
	 * ファイル長を取得する。
	 * 
	 * @see com.nullfish.lib.vfs.FileAttribute#getLength()
	 */
	public long getLength() {
		if (!lenghtInited) {
			length = file.getFile().length();
			lenghtInited = true;
		}

		return length;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileAttribute#getDate()
	 */
	public Date getDate() {
		if (!dateInited) {
			date = new Date(file.getFile().lastModified());
			dateInited = true;
		}

		return date;
	}


	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileAttribute#isExists()
	 */
	public boolean isExists() {
		if(!existenceInited) {
			exists = file.getFile().exists();
			existenceInited = true;
		}
		
		return exists;
	}

	public FileType getFileType() {
		if (!fileTypeInited) {
			if(!javaFile.exists()) {
				fileType = FileType.NOT_EXISTS;
			} else if (file.getFile().isFile()) {
				fileType = FileType.FILE;
			} else {
				fileType = FileType.DIRECTORY;
			}
			fileTypeInited = true;
		}
		return fileType;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileAttribute#getAttribute(java.lang.Object)
	 */
	public Object getAttribute(Object key) {
		if (FILE_TYPE.equals(key)) {
			return getFileType();
		} else if (LENGTH.equals(key)) {
			return new Long(getLength());
		} else if (DATE.equals(key)) {
			return getDate();
		} else if (TOTAL_SPACE.equals(key)) {
			return new Long(file.getTotalSpace());
		} else if (FREE_SPACE.equals(key)) {
			return new Long(file.getFreeSpace());
		} else {
			return null;
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileAttribute#getAttributeKeys()
	 */
	public Object[] getAttributeKeys() {
		if(VFS.JAVA6_OR_LATER) {
			return ATTRIBUTES;
		} else {
			return FileAttribute.ATTRIBUTES;
		}
	}
	
	
	/**
	 * ファイルシステムの全容量を取得する。
	 * @return	全容量。取得不可能な場合は-1
	 */
	public long getTotalSpace() {
		return VFS.JAVA6_OR_LATER ? file.getTotalSpace() : -1;
	}
	
	/**
	 * ファイルシステムの空き容量を取得する。
	 * @return	空き容量。取得不可能な場合は-1
	 */
	public long getFreeSpace() {
		return VFS.JAVA6_OR_LATER ? file.getFreeSpace() : -1;
	}
}
