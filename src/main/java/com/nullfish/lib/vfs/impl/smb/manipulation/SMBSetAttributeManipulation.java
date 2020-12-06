/*
 * 作成日: 2003/10/27
 *
 */
package com.nullfish.lib.vfs.impl.smb.manipulation;


import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractSetAttributeManipulation;


/**
 * @author shunji
 *
 */
public class SMBSetAttributeManipulation extends AbstractSetAttributeManipulation {
	public SMBSetAttributeManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractSetAttributeManipulation#doSetAttribute(com.sexyprogrammer.lib.vfs.VFile, com.sexyprogrammer.lib.vfs.FileAttribute)
	 */
	public void doSetAttribute(VFile file, FileAttribute attr) {
	}
}
