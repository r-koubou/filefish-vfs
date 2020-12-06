package com.nullfish.lib.vfs.impl.root.manipulation;

import java.util.Date;

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
public class RootFileGetAttributesManipulation
	extends AbstractGetAttributesManipulation {

	public RootFileGetAttributesManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractInitAttributesManipulation#doGetAttribute(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public FileAttribute doGetAttribute(VFile file) throws VFSException {
		return new DefaultFileAttribute(true, 0, new Date(), FileType.DIRECTORY, 0, 0);
	}
}
