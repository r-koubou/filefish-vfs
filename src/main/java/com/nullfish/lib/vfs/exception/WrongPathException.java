/*
 * 作成日: 2003/10/03
 *
 */
package com.nullfish.lib.vfs.exception;

/**
 * @author shunji
 *
 */
public class WrongPathException extends VFSException {
	public static final String NAME ="wrong_path";
	
	public WrongPathException() {
		super();
	}
	
	public WrongPathException(String message) {
		super(message);
	}
	
	public WrongPathException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public WrongPathException(Throwable cause) {
		super(cause);
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
				getMessage()
		};
		return rtn;
	}
}
