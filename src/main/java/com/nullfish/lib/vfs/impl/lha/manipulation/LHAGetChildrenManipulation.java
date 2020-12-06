/*
 * 作成日: 2003/11/18
 *
 */
package com.nullfish.lib.vfs.impl.lha.manipulation;

import java.util.List;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.lha.LHAFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetChildrenManipulation;


/**
 * @author shunji
 *
 */
public class LHAGetChildrenManipulation
	extends AbstractGetChildrenManipulation {
	public LHAGetChildrenManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetChildrenManipulation#doGetChildren(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public VFile[] doGetChildren(VFile file) throws VFSException {
		if(!file.exists(this)) {
			throw new FileNotExistsException(file);
		}
		
		List filesList = ((LHAFile)file).getChildFiles();
		VFile[] rtn = new VFile[filesList.size()];
		return (VFile[])filesList.toArray(rtn);
	}
}
