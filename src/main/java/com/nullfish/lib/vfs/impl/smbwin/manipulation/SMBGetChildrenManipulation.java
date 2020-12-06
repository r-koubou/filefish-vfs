/*
 * 作成日: 2003/10/09
 * 
 */
package com.nullfish.lib.vfs.impl.smbwin.manipulation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.smbwin.SMBFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetChildrenManipulation;


/**
 * @author Shunji Yamaura
 */
public class SMBGetChildrenManipulation
	extends AbstractGetChildrenManipulation {
	public SMBGetChildrenManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetChildrenManipulation#initChildrenInfo(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public VFile[] doGetChildren(VFile file) throws VFSException {
		if(!file.exists(this)) {
			throw new FileNotExistsException(file);
		}
		
		SMBFile lfile = (SMBFile) file;
		File[] javaFiles = lfile.getFile().listFiles();
		if (javaFiles == null) {
			return new SMBFile[0];
		}

		FileName fileName = file.getFileName();
		FileSystem fileSystem = file.getFileSystem();

		FileName childName;
		List filesList = new ArrayList();
		for (int i = 0; javaFiles != null && i < javaFiles.length; i++) {
			if(!javaFiles[i].equals(".") && !javaFiles[i].equals("..")) {
				childName = fileName.createChild(javaFiles[i].getName());
				filesList.add(new SMBFile(fileSystem, childName, javaFiles[i]));
			}
		}
		VFile[] rtn = new SMBFile[javaFiles.length];
		rtn = (VFile[]) filesList.toArray(rtn);

		return rtn;
	}
}
