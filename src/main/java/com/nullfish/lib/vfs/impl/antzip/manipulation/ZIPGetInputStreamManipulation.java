/*
 * 作成日: 2003/11/18
 *
 */
package com.nullfish.lib.vfs.impl.antzip.manipulation;

import java.io.InputStream;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.antzip.ZIPFile;
import com.nullfish.lib.vfs.impl.antzip.ZIPFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation;


/**
 * @author shunji
 *
 */
public class ZIPGetInputStreamManipulation
	extends AbstractGetInputStreamManipulation {
	public ZIPGetInputStreamManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation#doGetInputStream(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public InputStream doGetInputStream(VFile file) throws VFSException {
		ZIPFileSystem fileSystem = (ZIPFileSystem)file.getFileSystem();
		return fileSystem.getInputStream((ZIPFile)file);
	}

}
