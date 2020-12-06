package com.nullfish.lib.vfs.exception;

/**
 * SqlExceptionをVFSExceptionにラップするクラス
 * @author shunji
 *
 */
public class VFSSqlException extends VFSException {
	public static final String NAME = "sql_exception";
	
	public VFSSqlException() {
		super();
	}
	
	public VFSSqlException(String message) {
		super(message);
	}
	
	public VFSSqlException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public VFSSqlException(Throwable cause) {
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
