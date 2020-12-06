/*
 * 作成日: 2003/11/18
 *
 */
package com.nullfish.lib.vfs.impl.zipcloak.manipulation;

import java.util.Date;


import jp.hishidama.zip.ZipEntry;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.DefaultFileAttribute;
import com.nullfish.lib.vfs.impl.zipcloak.ZIPFile;
import com.nullfish.lib.vfs.impl.zipcloak.ZIPFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetAttributesManipulation;


/**
 * @author shunji
 *
 */
public class ZIPGetAttributesManipulation
	extends AbstractGetAttributesManipulation {

	public ZIPGetAttributesManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractInitAttributesManipulation#doGetAttribute(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public FileAttribute doGetAttribute(VFile file) throws VFSException {
		ZIPFile zfile = (ZIPFile)file;
		ZipEntry entry = zfile.getZipEntry();
		if(file.isRoot()) {
			return new DefaultFileAttribute(true, -1, null, FileType.DIRECTORY, ((ZIPFileSystem)file.getFileSystem()).getTotalSpace(), 0);
		}
		
		if(entry == null) {
			return new DefaultFileAttribute(false, -1, null, FileType.NOT_EXISTS);
		}
		
		FileType type;
		if(entry.isDirectory()) {
			type = FileType.DIRECTORY;
		} else {
			type = FileType.FILE;
		}
		
		return new DefaultFileAttribute(true, entry.getSize(), new Date(entry.getTime()), type, ((ZIPFileSystem)file.getFileSystem()).getTotalSpace(), 0);
	}

}
