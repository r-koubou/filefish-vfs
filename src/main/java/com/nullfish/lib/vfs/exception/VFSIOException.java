/*
 * 作成日: 2003/10/03
 *
 */
package com.nullfish.lib.vfs.exception;

/**
 * IOExceptionをVFSExceptionにラップするクラス
 * @author shunji
 *
 */
public class VFSIOException extends VFSException {
	public static final String NAME = "io_exception";
	
	public VFSIOException() {
		super();
	}
	
	public VFSIOException(String message) {
		super(message);
	}
	
	public VFSIOException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public VFSIOException(Throwable cause) {
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
		};
		return rtn;
	}
}
