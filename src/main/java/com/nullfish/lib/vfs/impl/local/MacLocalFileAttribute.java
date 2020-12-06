/*
 * Created on 2004/06/09
 *
 */
package com.nullfish.lib.vfs.impl.local;

import java.io.IOException;
import java.util.Date;

import jp.ne.anet.kentkt.fastfile.FastFile;

import com.apple.eio.FileManager;
import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.VFS;

/**
 * @author shunji
 * 
 */
public class MacLocalFileAttribute implements FileAttribute {

	private LocalFile file;
	
	private FastFile javaFile;

	protected long length = -1;
	protected boolean lenghtInited = false;
	
	protected Date date;
	protected boolean dateInited = false;

	protected boolean exists;
	protected boolean existenceInited = false;

	protected FileType fileType;
	protected boolean fileTypeInited = false;

	protected int creator;
	protected boolean creatorInited;
	
	protected int type;
	protected boolean typeInited = false;

	
	
	/**
	 * ファイルクリエータ
	 */
	public static final String CREATOR = "creator";
	
	/**
	 * ファイルタイプ
	 */
	public static final String TYPE = "type";
	
	public static final Object[] ATTRIBUTES_DEFAULT = {
		LENGTH,
		FILE_TYPE,
		DATE,
		EXISTS,
		CREATOR,
		TYPE
	};

	public static final Object[] ATTRIBUTES_JAVA6 = {
		LENGTH,
		FILE_TYPE,
		DATE,
		EXISTS,
		TOTAL_SPACE,
		FREE_SPACE,
		CREATOR,
		TYPE
	};

	public MacLocalFileAttribute(LocalFile file) {
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
		} else if (TOTAL_SPACE.equals(key)) {
			return new Long(file.getTotalSpace());
		} else if (FREE_SPACE.equals(key)) {
			return new Long(file.getFreeSpace());
		} else if (CREATOR.equals(key)) {
			return new Integer(creator);
		} else if (TYPE.equals(key)) {
			return new Integer(type);
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

	public int getCreator() {
		if(creatorInited) {
			try {
				creator = FileManager.getFileCreator(file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public int getType() {
		if(typeInited) {
			try {
				type = FileManager.getFileType(file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return type;
	}
	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}
}
