package com.nullfish.lib.vfs.impl.local.manipulation;

import java.io.File;
import java.io.IOException;

import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.UpdateManager;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileCreationException;
import com.nullfish.lib.vfs.exception.ManipulationNotAvailableException;
import com.nullfish.lib.vfs.exception.ManipulationStoppedException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSSystemException;
import com.nullfish.lib.vfs.impl.local.LocalFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractCreateFileManipulation;


/**
 * ローカルファイル生成クラス
 * @author Shunji Yamaura
 */
public class LocalCreateFileManipulation extends AbstractCreateFileManipulation {
	public LocalCreateFileManipulation(VFile file) {
		super(file);
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractCreateFileManipulation#createFile()
	 */
	public void doCreateFile() throws VFSException {
		if(isStopped()) {
			throw new ManipulationStoppedException(this);
		}
		
		try{
			LocalFile lfile = (LocalFile)file;
			File jFile = lfile.getFile();
			File parent = jFile.getParentFile();
			
			if(!parent.exists()) {
				if(!parent.mkdirs()) {
					throw new FileCreationException(file);
				}
				UpdateManager.getInstance().fileCreated(lfile.getParent());
			}
			
			FileType fileType = getFileType();
			
			if(fileType.equals(FileType.FILE)) {
				if(!lfile.getFile().createNewFile()) {
					throw new FileCreationException(file);
				}
			} else if(fileType.equals(FileType.DIRECTORY)) {
				if(!lfile.getFile().mkdirs()) {
					throw new FileCreationException(file);
				}
			} else if(fileType.equals(FileType.LINK)) {
				throw new ManipulationNotAvailableException(file, "creating link");
			} else {
				throw new VFSSystemException("Wrong FileType : " + fileType);
			}
		} catch (IOException e) {
			throw new FileCreationException(file);
		}
	}
}
