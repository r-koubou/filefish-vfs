/*
 * 作成日: 2003/10/19
 *
 */
package com.nullfish.lib.vfs.impl.local.manipulation;

import java.io.File;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.local.LocalFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractMoveManipulation;


/**
 * 
 * @author Shunji Yamaura
 */
public class LocalMoveManipulation extends AbstractMoveManipulation {
	/**
	 * コンストラクタ
	 * @param file
	 */
	public LocalMoveManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractMoveManipulation#moveFile(com.sexyprogrammer.lib.vfs.VFile, com.sexyprogrammer.lib.vfs.VFile)
	 */
	public boolean doMoveFile(VFile from, VFile dest) throws VFSException {
		File fromFile = ((LocalFile)from).getFile();
		File destFile = ((LocalFile)dest).getFile();

		if (!destFile.getParentFile().exists()
			&& !destFile.getParentFile().mkdirs()) {
			return false;
		}

		return fromFile.renameTo(destFile);
	}

}
