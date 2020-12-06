/*
 * 作成日: 2003/11/03
 *
 */
package com.nullfish.lib.vfs.impl.ftp.manipulation;



import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.DefaultFileAttribute;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetAttributesManipulation;


/**
 * @author shunji
 *
 */
public class FTPGetAttributeManipulation extends AbstractGetAttributesManipulation {
	public FTPGetAttributeManipulation(VFile file) {
		super(file);
	}
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractInitAttributesManipulation#initFileAttribute(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public FileAttribute doGetAttribute(VFile file) throws VFSException {
		if(!file.isRoot()) {
			VFile parent = file.getParent();
			VFile[] children = parent.getChildren(this);
			for(int i=0; i<children.length; i++) {
				if(children[i].getName().equals(file.getName())) {
					VFile thisFile = children[i];
					file.setAttributeCache(thisFile.getAttribute(this));
					file.setPermissionCache(thisFile.getPermissionCache());
					return thisFile.getAttribute();
				}
			}
			
			return new DefaultFileAttribute(false, -1, null, FileType.NOT_EXISTS);
		} else {
			return new DefaultFileAttribute(true, -1, null, FileType.DIRECTORY);
		}
			 
	}

}
