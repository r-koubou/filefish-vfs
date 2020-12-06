package com.nullfish.lib.vfs.impl.rar.manipulation;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.DefaultFileAttribute;
import com.nullfish.lib.vfs.impl.rar.RARFile;
import com.nullfish.lib.vfs.impl.rar.RARFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetAttributesManipulation;

import de.innosystec.unrar.rarfile.FileHeader;


public class RARGetAttributesManipulation
	extends AbstractGetAttributesManipulation {

	public RARGetAttributesManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractInitAttributesManipulation#doGetAttribute(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public FileAttribute doGetAttribute(VFile file) throws VFSException {
		RARFile rfile = (RARFile)file;
		FileHeader entry = rfile.getFileHeader();
		if(file.isRoot()) {
			return new DefaultFileAttribute(true, -1, null, FileType.DIRECTORY, ((RARFileSystem)file.getFileSystem()).getTotalSpace(), 0);
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
		
		return new DefaultFileAttribute(true, entry.getFullUnpackSize(), entry.getMTime(), type, ((RARFileSystem)file.getFileSystem()).getTotalSpace(), 0);
	}

}
