/*
 * 作成日: 2003/10/08
 *
 */
package com.nullfish.lib.vfs.exception;

import com.nullfish.lib.vfs.VFile;

/**
 * 
 * @author Shunji Yamaura
 */
public class FileDeletionException extends VFSException {
	public static final String NAME = "file_deletion";

	VFile file;

	public FileDeletionException(VFile file) {
		super();
		this.file = file;
	}
	
	public FileDeletionException(String message, VFile file) {
		super(message);
		this.file = file;
	}
	
	public FileDeletionException(String message, Throwable cause, VFile file) {
		super(message, cause);
		this.file = file;
	}
	
	public FileDeletionException(Throwable cause, VFile file) {
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
