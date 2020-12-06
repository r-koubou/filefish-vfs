/*
 * Created on 2004/06/09
 *
 */
package com.nullfish.lib.vfs.impl.smbwin;

import java.util.Date;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileType;

/**
 * @author shunji
 * 
 */
public class SMBFileAttribute implements FileAttribute {

	private SMBFile file;
	
	protected long length = -1;
	protected boolean lenghtInited = false;
	
	protected Date date;
	protected boolean dateInited = false;

	protected boolean exists;
	protected boolean existenceInited = false;

	protected FileType fileType;

	protected boolean fileTypeInited = false;

	public SMBFileAttribute(SMBFile file) {
		this.file = file;
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
			if (!file.getFile().exists()) {
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
		return ATTRIBUTES;
	}
	
	/**
	 * ファイルシステムの全容量を取得する。
	 * @return	全容量。取得不可能な場合は-1
	 */
	public long getTotalSpace() {
		return -1;
	}
	
	/**
	 * ファイルシステムの空き容量を取得する。
	 * @return	空き容量。取得不可能な場合は-1
	 */
	public long getFreeSpace() {
		return -1;
	}
}
