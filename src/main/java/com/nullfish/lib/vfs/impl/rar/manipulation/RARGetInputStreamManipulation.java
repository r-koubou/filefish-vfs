package com.nullfish.lib.vfs.impl.rar.manipulation;

import java.io.InputStream;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.rar.RARFile;
import com.nullfish.lib.vfs.impl.rar.RARFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation;


/**
 * @author shunji
 *
 */
public class RARGetInputStreamManipulation
	extends AbstractGetInputStreamManipulation {
	public RARGetInputStreamManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation#doGetInputStream(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public InputStream doGetInputStream(VFile file) throws VFSException {
		RARFileSystem fileSystem = (RARFileSystem)file.getFileSystem();
		return fileSystem.getInputStream((RARFile)file);
	}

}
