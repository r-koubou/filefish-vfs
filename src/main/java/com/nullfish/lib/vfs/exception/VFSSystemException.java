package com.nullfish.lib.vfs.exception;

/**
 * 想定されてない実装があった場合に投げられる例外クラス。
 * 存在しないファイルタイプが指定された、等。
 * 
 * @author shunji
 */
public class VFSSystemException extends VFSException {
	public static final String NAME = "system_error";
	
	public VFSSystemException() {
		super();
	}
	
	public VFSSystemException(String message) {
		super(message);
	}
	
	public VFSSystemException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public VFSSystemException(Throwable cause) {
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
				getCause()
		};
		return rtn;
	}
}
