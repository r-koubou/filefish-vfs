/*
 * 作成日: 2003/10/06
 *
 */
package com.nullfish.lib.vfs.exception;

import com.nullfish.lib.vfs.VFile;

/**
 * ファイル操作がファイルシステムによってサポートされてない場合に投げられる例外クラス。
 * 
 * @author Shunji Yamaura
 */
public class ManipulationNotAvailableException extends VFSException {
	public static final String NAME = "manipulation_not_available";

	/**
	 * ファイル
	 */
	VFile file;

	
	/**
	 * 操作種類
	 */
	String manipulation;
	
	public ManipulationNotAvailableException(VFile file, String manipulation) {
		super();
		this.file = file;
		this.manipulation = manipulation;
	}

	public VFile getFile() {
		return file;
	}

	public String getManipulation() {
		return manipulation;
	}

	

	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.exception.VFSException#getName()
	 */
	public String getName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.exception.VFSException#getErrorValues()
	 */
	public Object[] getErrorValues() {
		Object[] rtn = {
				file,
				manipulation
		};
		
		return rtn;
	}
}
