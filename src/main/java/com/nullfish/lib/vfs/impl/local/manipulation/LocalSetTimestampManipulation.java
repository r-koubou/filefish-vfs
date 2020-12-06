/*
 * Created on 2004/04/10
 *
 */
package com.nullfish.lib.vfs.impl.local.manipulation;

import java.io.File;
import java.util.Date;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.local.LocalFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractSetTimestampManipulation;


/**
 * @author shunji
 *
 */
public class LocalSetTimestampManipulation
		extends
			AbstractSetTimestampManipulation {
	public LocalSetTimestampManipulation(VFile file) {
		super(file);
	}
	
	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractSetTimestampManipulation#doSetLastModified(com.sexyprogrammer.lib.vfs.VFile, java.util.Date)
	 */
	public void doSetLastModified(VFile file, Date date) throws VFSException {
		File localFile = ((LocalFile)file).getFile();
		if(!localFile.exists()) {
			throw new FileNotExistsException(file);
		}
		localFile.setLastModified(date.getTime());
	}
}
