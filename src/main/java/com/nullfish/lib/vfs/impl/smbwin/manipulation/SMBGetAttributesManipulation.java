package com.nullfish.lib.vfs.impl.smbwin.manipulation;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.smbwin.SMBFile;
import com.nullfish.lib.vfs.impl.smbwin.SMBFileAttribute;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetAttributesManipulation;


/**
 * SMBファイルの属性初期化、取得操作クラス
 * @author Shunji Yamaura
 */
public class SMBGetAttributesManipulation
	extends AbstractGetAttributesManipulation {
	
	public SMBGetAttributesManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractInitAttributesManipulation#initFileAttribute(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public FileAttribute doGetAttribute(VFile file) throws VFSException {
		//	通常ここで属性取得をしてしまうが、SMBFileAtributeで遅延取得をしている。
		return new SMBFileAttribute((SMBFile)file);
	}
}
