/*
 * 作成日: 2003/11/18
 */
package com.nullfish.lib.vfs.impl.tar.manipulation;

import org.apache.tools.tar.TarEntry;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.DefaultFileAttribute;
import com.nullfish.lib.vfs.impl.tar.TARFile;
import com.nullfish.lib.vfs.impl.tar.TARFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetAttributesManipulation;


/**
 * @author shunji
 *
 */
public class TARGetAttributesManipulation
	extends AbstractGetAttributesManipulation {

	public TARGetAttributesManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractInitAttributesManipulation#doGetAttribute(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public FileAttribute doGetAttribute(VFile file) throws VFSException {
		TARFile tarFile = (TARFile)file;
		TarEntry entry = tarFile.getTarEntry();
		if(file.isRoot()) {
			return new DefaultFileAttribute(true, -1, null, FileType.DIRECTORY, ((TARFileSystem)file.getFileSystem()).getTotalSpace(), 0);
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
		
		return new DefaultFileAttribute(true, entry.getSize(), entry.getModTime(), type, ((TARFileSystem)file.getFileSystem()).getTotalSpace(), 0);
	}

}
