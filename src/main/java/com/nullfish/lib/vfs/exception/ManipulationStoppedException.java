package com.nullfish.lib.vfs.exception;

import com.nullfish.lib.vfs.Manipulation;

/**
 * 操作中止例外
 * 
 * @author Shunji Yamaura
 */
public class ManipulationStoppedException extends VFSException {
	public static final String NAME = "manipulation_stopped";

	Manipulation manipulation;

	
	public ManipulationStoppedException(Manipulation manipulation) {
		this.manipulation = manipulation;
	}

	public Manipulation getManipulation() {
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
				manipulation
		};
		return rtn;
	}
}
