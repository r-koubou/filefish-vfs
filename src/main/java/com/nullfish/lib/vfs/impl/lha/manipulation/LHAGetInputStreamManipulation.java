/*
 * 作成日: 2003/11/18
 *
 */
package com.nullfish.lib.vfs.impl.lha.manipulation;

import java.io.InputStream;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.lha.LHAFile;
import com.nullfish.lib.vfs.impl.lha.LHAFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation;


/**
 * @author shunji
 *
 */
public class LHAGetInputStreamManipulation
	extends AbstractGetInputStreamManipulation {
	public LHAGetInputStreamManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation#doGetInputStream(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public InputStream doGetInputStream(VFile file) throws VFSException {
		LHAFileSystem fileSystem = (LHAFileSystem)file.getFileSystem();
		return fileSystem.getInputStream((LHAFile)file);
	}
}
