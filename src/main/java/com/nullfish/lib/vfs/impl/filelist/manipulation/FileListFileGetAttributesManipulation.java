/*
 * 作成日: 2003/11/18
 *
 */
package com.nullfish.lib.vfs.impl.filelist.manipulation;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.DefaultFileAttribute;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetAttributesManipulation;


/**
 * @author shunji
 *
 */
public class FileListFileGetAttributesManipulation
	extends AbstractGetAttributesManipulation {

	public FileListFileGetAttributesManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractInitAttributesManipulation#doGetAttribute(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public FileAttribute doGetAttribute(VFile file) throws VFSException {
		VFile baseFile = file.getFileSystem().getMountPoint();
		if(file.isRoot()) {
			return new DefaultFileAttribute(baseFile.exists(), baseFile.getLength(), baseFile.getTimestamp(), FileType.DIRECTORY, baseFile.getLength(), 0);
		} else {
			return new DefaultFileAttribute(false, -1, null, FileType.NOT_EXISTS);
		}
	}

}
