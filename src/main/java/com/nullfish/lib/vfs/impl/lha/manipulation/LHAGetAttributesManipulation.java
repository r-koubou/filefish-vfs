/*
 * 作成日: 2003/11/18
 * 
 */
package com.nullfish.lib.vfs.impl.lha.manipulation;


import jp.gr.java_conf.dangan.util.lha.LhaHeader;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.DefaultFileAttribute;
import com.nullfish.lib.vfs.impl.lha.LHAFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetAttributesManipulation;


/**
 * @author shunji
 * 
 */
public class LHAGetAttributesManipulation
	extends AbstractGetAttributesManipulation {

	public LHAGetAttributesManipulation(VFile file) {
		super(file);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractInitAttributesManipulation#doGetAttribute(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public FileAttribute doGetAttribute(VFile file) throws VFSException {
		LHAFile zfile = (LHAFile) file;
		LhaHeader entry = zfile.getLhaHeader();
		if (file.isRoot()) {
			return new DefaultFileAttribute(true, -1, null, FileType.DIRECTORY);
		}

		if (entry == null) {
			return new DefaultFileAttribute(false, -1, null, FileType.NOT_EXISTS);
		}

		FileType type;
		if (entry.getPath().endsWith(FileName.SEPARATOR)
			|| entry.getPath().endsWith("\\")) {
			type = FileType.DIRECTORY;
		} else {
			type = FileType.FILE;
		}

		return new DefaultFileAttribute(
			true,
			entry.getOriginalSize(),
			entry.getLastModified(),
			type);
	}
}
