package com.nullfish.lib.vfs.exception;

import com.nullfish.lib.vfs.VFile;

/**
 * 
 * @author Shunji Yamaura
 */
public class FileNotExistsException extends VFSException {
	public static final String NAME ="file_not_exists";

	VFile file;

	public FileNotExistsException(VFile file) {
		super();
		this.file = file;
	}
	
	public FileNotExistsException(String message, VFile file) {
		super(message);
		this.file = file;
	}
	
	public FileNotExistsException(String message, Throwable cause, VFile file) {
		super(message, cause);
		this.file = file;
	}
	
	public FileNotExistsException(Throwable cause, VFile file) {
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
	
	public String toString() {
		return getClass().toString() + ":" + file.getAbsolutePath();
	}
}
