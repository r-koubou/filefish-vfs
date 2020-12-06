/*
 * 作成日: 2003/11/01
 *
 */
package com.nullfish.lib.vfs.impl.ftp.manipulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.impl.ftp.FTPFile;
import com.nullfish.lib.vfs.impl.ftp.FTPFileSystem;
import com.nullfish.lib.vfs.impl.ftp.FTPPermission;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetChildrenManipulation;



/**
 * @author shunji
 *
 */
public class FTPGetChildrenManipulation extends AbstractGetChildrenManipulation {
	private FTPClient client = null;
	
	public FTPGetChildrenManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetChildrenManipulation#initChildrenInfo(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public VFile[] doGetChildren(VFile file) throws VFSException {
		FTPFile ffile = (FTPFile)file;
		FTPFileSystem fileSystem = (FTPFileSystem)file.getFileSystem();

		try {
			FileName rootName = fileSystem.getWorkDir();

			client = fileSystem.getFTPClient(this);
			
			org.apache.commons.net.ftp.FTPFile[] oroFTPFiles =
				client.listFiles(rootName.resolveRelation(file.getFileName()));

			if (oroFTPFiles == null) {
				//return new FTPFile[0];
				throw new FileNotExistsException(file);
			}

			FileName fileName = file.getFileName();

			List children = new ArrayList();
			for (int i = 0; i < oroFTPFiles.length; i++) {
				org.apache.commons.net.ftp.FTPFile oro = oroFTPFiles[i];
				if(oro == null) {
					continue;
				}
				FileAttribute attr = FTPFile.oro2FileAttribute(oro);
				FTPPermission permission = new FTPPermission();
				permission.converFromOroFTPFile(oro);

				FileName childName = fileName.createChild(oro.getName());
				if(childName.getName().equals(".")) {
					ffile.setAttributeCache(attr);
					ffile.setPermissionCache(permission);
				} else if(childName.getName().equals("..")) {
					if(ffile.getParent() != null) {
						ffile.getParent().setAttributeCache(attr);
						ffile.getParent().setPermissionCache(permission);
					}
				} else {
					children.add( new FTPFile(fileSystem, childName, attr, permission) );
				}
			}
			
			VFile[] rtn = new FTPFile[children.size()];
			rtn = (FTPFile[])children.toArray(rtn);
			return rtn;
		} catch (IOException e) {
			throw new VFSIOException("IOException on " + file.getAbsolutePath(), e);
		} finally {
			fileSystem.releaseFTPClient();
		}
	}
	
	public synchronized void doStop() {
		getWorkThread().interrupt();
		if(client != null) {
			try {
				client.abort();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
