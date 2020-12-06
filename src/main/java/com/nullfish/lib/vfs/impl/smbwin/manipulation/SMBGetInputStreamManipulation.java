package com.nullfish.lib.vfs.impl.smbwin.manipulation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.smbwin.SMBFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation;


/**
 * 
 * @author Shunji Yamaura
 */
public class SMBGetInputStreamManipulation
	extends AbstractGetInputStreamManipulation {
	
	public SMBGetInputStreamManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.GetInputStreamManipulation#getInputStream()
	 */
	public InputStream doGetInputStream(VFile file) throws VFSException {
		try {
			return new FileInputStream(((SMBFile)file).getFile());
		} catch (FileNotFoundException e) {
			throw new FileNotExistsException(file);
		}
	}
}
