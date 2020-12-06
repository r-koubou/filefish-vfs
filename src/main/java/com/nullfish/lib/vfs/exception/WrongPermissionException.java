package com.nullfish.lib.vfs.exception;

import com.nullfish.lib.vfs.VFile;

/**
 * 
 * @author Shunji Yamaura
 */
public class WrongPermissionException extends VFSException {
	public static final String NAME ="wrong_permission";

	VFile file;

	public WrongPermissionException(VFile file) {
		super();
		this.file = file;
	}
	
	public WrongPermissionException(String message, VFile file) {
		super(message);
		this.file = file;
	}
	
	public WrongPermissionException(String message, Throwable cause, VFile file) {
		super(message, cause);
		this.file = file;
	}
	
	public WrongPermissionException(Throwable cause, VFile file) {
		super(cause);
		this.file = file;
	}

	public VFile getFile() {
		return file;
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
				file
		};
		return rtn;
	}
}
