package com.nullfish.lib.vfs.impl.local.manipulation;


import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileDeletionException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.local.LocalFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractDeleteManipulation;


/**
 * ローカルファイルの削除操作クラス
 * 
 * @author Shunji Yamaura
 */
public class LocalDeleteManipulation extends AbstractDeleteManipulation {
	public LocalDeleteManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abs.AbstractDeleteManipulation#deleteFile(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public void doDeleteFile(VFile file) throws VFSException {
		if(!((LocalFile)file).getFile().delete()) {
			throw new FileDeletionException(file);
		}
	}


	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abs.AbstractDeleteManipulation#deleteDirectory(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public void doDeleteDirectory(VFile file) throws VFSException {
		doDeleteFile(file);
	}
}
