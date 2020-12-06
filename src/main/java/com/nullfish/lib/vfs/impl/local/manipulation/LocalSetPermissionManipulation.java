package com.nullfish.lib.vfs.impl.local.manipulation;

import java.io.File;
import java.util.List;

import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.local.LocalFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractSetPermissionManipulation;
import com.nullfish.lib.vfs.permission.PermissionType;

public class LocalSetPermissionManipulation extends
		AbstractSetPermissionManipulation {

	/**
	 * コンストラクタ
	 * 
	 * @param file
	 */
	public LocalSetPermissionManipulation(VFile file) {
		super(file);
	}

	public void doSetPermission(VFile file, List values) throws VFSException {
		File jfile = ((LocalFile)file).getFile();
		for(int i=0; i<values.size(); i++) {
			AccessTypeValueSet set = (AccessTypeValueSet)values.get(i);
			if(!VFS.JAVA6_OR_LATER) {
				// Java6以上じゃない場合は何もしない。
			} else if(PermissionType.READABLE.equals(set.getType())) {
				jfile.setReadable(set.getValue());
			} else if(PermissionType.WRITABLE.equals(set.getType())) {
				jfile.setWritable(set.getValue());
			} else if(PermissionType.EXECUTABLE.equals(set.getType())) {
				jfile.setExecutable(set.getValue());
			}
		}
	}
}
