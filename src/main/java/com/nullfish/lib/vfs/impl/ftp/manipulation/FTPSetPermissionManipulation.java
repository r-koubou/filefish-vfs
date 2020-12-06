package com.nullfish.lib.vfs.impl.ftp.manipulation;

import java.io.IOException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.exception.WrongPermissionException;
import com.nullfish.lib.vfs.impl.ftp.FTPFileSystem;
import com.nullfish.lib.vfs.impl.ftp.FTPPermission;
import com.nullfish.lib.vfs.manipulation.abst.AbstractSetPermissionManipulation;

public class FTPSetPermissionManipulation extends
		AbstractSetPermissionManipulation {

	private FTPClient ftp = null;

	/**
	 * コンストラクタ
	 * 
	 * @param file
	 */
	public FTPSetPermissionManipulation(VFile file) {
		super(file);
	}

	public void doSetPermission(VFile file, List values) throws VFSException {
		FTPFileSystem fileSystem = (FTPFileSystem) file.getFileSystem();
		FileName rootName = fileSystem.getWorkDir();

		try {
			FTPPermission permission = (FTPPermission) file.getPermission(this);
			for (int i = 0; i < values.size(); i++) {
				AccessTypeValueSet valueSet = (AccessTypeValueSet) values
						.get(i);
				permission.setPermission(valueSet.getType(), valueSet
						.getAccess(), valueSet.getValue());
			}

			ftp = ((FTPFileSystem) file.getFileSystem()).getFTPClient(this);
			if (!ftp.sendSiteCommand("chmod " + permission.getPermissionSum() + " " + rootName.resolveRelation(file.getFileName()))) {
				throw new WrongPermissionException(file);
			}
			file.clearPermission();
 		} catch (IOException e) {
			throw new VFSIOException("Failed to delete " + file.getAbsolutePath(), e);
		} finally {
			fileSystem.releaseFTPClient();
		}
	}
}
