/*
 * 作成日: 2003/11/18
 *
 */
package com.nullfish.lib.vfs.impl.tar.manipulation;

import java.io.InputStream;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.tar.TARFile;
import com.nullfish.lib.vfs.impl.tar.TARFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation;


/**
 * @author shunji
 *
 */
public class TARGetInputStreamManipulation
	extends AbstractGetInputStreamManipulation {
	public TARGetInputStreamManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation#doGetInputStream(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public InputStream doGetInputStream(VFile file) throws VFSException {
		TARFileSystem fileSystem = (TARFileSystem)file.getFileSystem();
		return fileSystem.getInputStream((TARFile)file, this);
	}

}
