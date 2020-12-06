/*
 * 作成日: 2003/10/27
 *
 */
package com.nullfish.lib.vfs.impl.local.manipulation;

import java.io.File;
import java.util.Date;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.impl.local.LocalFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractSetAttributeManipulation;


/**
 * @author shunji
 *
 */
public class LocalSetAttributeManipulation extends AbstractSetAttributeManipulation {
	public LocalSetAttributeManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractSetAttributeManipulation#doSetAttribute(com.sexyprogrammer.lib.vfs.VFile, com.sexyprogrammer.lib.vfs.FileAttribute)
	 */
	public void doSetAttribute(VFile file, FileAttribute attr) {
		File jFile = ((LocalFile)file).getFile();

		Date date = (Date)attr.getAttribute(FileAttribute.DATE);
		if(date != null) {
			jFile.setLastModified(date.getTime());		
		}
	}
}
