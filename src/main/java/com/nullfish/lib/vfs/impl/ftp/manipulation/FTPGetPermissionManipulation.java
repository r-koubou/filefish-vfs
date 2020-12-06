/*
 * 作成日: 2003/11/03
 *
 */
package com.nullfish.lib.vfs.impl.ftp.manipulation;


import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation;


/**
 * @author shunji
 *
 */
public class FTPGetPermissionManipulation
	extends AbstractGetPermissionManipulation {
	public FTPGetPermissionManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetPermissionManipulation#initPermission(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public Permission doGetPermission(VFile file) throws VFSException {
		if (!file.isRoot()) {
			VFile parent = file.getParent();
			VFile[] children = parent.getChildren(this);
			for (int i = 0; i < children.length; i++) {
				if (children[i].getName().equals(file.getName())) {
					//	FTPファイルはls時に属性、パーミッションを取得している。
					file.setAttributeCache(children[i].getAttribute(this));
					file.setPermissionCache(children[i].getPermissionCache());
					return children[i].getPermission();
				}
			}

		}

		return null;
	}
}
