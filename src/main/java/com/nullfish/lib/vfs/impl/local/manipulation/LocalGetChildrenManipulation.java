/*
 * 作成日: 2003/10/09
 *
 */
package com.nullfish.lib.vfs.impl.local.manipulation;

import java.io.File;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.local.LocalFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetChildrenManipulation;


/**
 * 
 * @author Shunji Yamaura
 */
public class LocalGetChildrenManipulation
	extends AbstractGetChildrenManipulation {
	public LocalGetChildrenManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetChildrenManipulation#initChildrenInfo(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public VFile[] doGetChildren(VFile file) throws VFSException {
		if(!file.exists(this)) {
			throw new FileNotExistsException(file);
		}
		
		LocalFile lfile = (LocalFile) file;
		File[] javaFiles = lfile.getFile().listFiles();
		if (javaFiles == null) {
			return new LocalFile[0];
		}

		FileName fileName = file.getFileName();
		FileSystem fileSystem = file.getFileSystem();

		VFile[] rtn = new LocalFile[javaFiles.length];
		FileName childName;
		for (int i = 0; javaFiles != null && i < javaFiles.length; i++) {
			childName = fileName.createChild(javaFiles[i].getName());
			rtn[i] = new LocalFile(fileSystem, childName, javaFiles[i]);
		}

		return rtn;
	}
}
