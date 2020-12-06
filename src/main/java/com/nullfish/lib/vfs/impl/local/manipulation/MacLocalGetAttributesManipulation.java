package com.nullfish.lib.vfs.impl.local.manipulation;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.local.LocalFile;
import com.nullfish.lib.vfs.impl.local.LocalFileAttribute;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetAttributesManipulation;


/**
 * ローカルファイルの属性初期化、取得操作クラス
 * @author Shunji Yamaura
 */
public class MacLocalGetAttributesManipulation
	extends AbstractGetAttributesManipulation {
		
	public MacLocalGetAttributesManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractInitAttributesManipulation#initFileAttribute(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public FileAttribute doGetAttribute(VFile file) throws VFSException {
		//	通常ここで属性取得をしてしまうが、LocalFileAtributeで遅延取得をしている。
		return new LocalFileAttribute((LocalFile)file);
	}
}
