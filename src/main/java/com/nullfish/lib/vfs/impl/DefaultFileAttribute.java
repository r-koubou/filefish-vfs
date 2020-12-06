/*
 * 作成日: 2003/10/18
 *
 */
package com.nullfish.lib.vfs.impl;

import java.util.Date;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileType;


/**
 * 
 * @author Shunji Yamaura
 */
public class DefaultFileAttribute implements FileAttribute {
	protected long length = -1;

	protected Date date;

	protected boolean exists;

	protected FileType fileType;

	public long totalSpace;
	
	public long freeSpace;

	public DefaultFileAttribute(
		boolean exists,
		long length,
		Date date,
		FileType fileType) {
		this(exists, length, date, fileType, -1, -1);
	}
	public DefaultFileAttribute(
			boolean exists,
			long length,
			Date date,
			FileType fileType,
			long totalSpace,
			long freeSpace) {
		this.exists = exists;
		this.length = length;
		this.date = date;
		this.fileType = fileType;
		this.totalSpace = totalSpace;
		this.freeSpace = freeSpace;
	}

	/**
	 * ファイル長を取得する。
	 * @see com.nullfish.lib.vfs.FileAttribute#getLength()
	 */
	public long getLength() {
		return length;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileAttribute#getDate()
	 */
	public Date getDate() {
		return date;
	}


	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileAttribute#isExists()
	 */
	public boolean isExists() {
		return exists;
	}

	public FileType getFileType() {
		return fileType;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileAttribute#getAttribute(java.lang.Object)
	 */
	public Object getAttribute(Object key) {
		if (FILE_TYPE.equals(key)) {
			return fileType;
		} else if (LENGTH.equals(key)) {
			return new Long(length);
		} else if (DATE.equals(key)) {
			return date;
		} else {
			return null;
		}
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.FileAttribute#getAttributeKeys()
	 */
	public Object[] getAttributeKeys() {
		return ATTRIBUTES;
	}


	/**
	 * ファイルシステムの全容量を取得する。
	 * @return	全容量。取得不可能な場合は-1
	 */
	public long getTotalSpace() {
		return totalSpace;
	}
	
	/**
	 * ファイルシステムの空き容量を取得する。
	 * @return	空き容量。取得不可能な場合は-1
	 */
	public long getFreeSpace() {
		return freeSpace;
	}
}
