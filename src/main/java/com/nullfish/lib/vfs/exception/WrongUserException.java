/*
 * 作成日: 2003/10/03
 *
 */
package com.nullfish.lib.vfs.exception;

import com.nullfish.lib.vfs.UserInfo;

/**
 * @author shunji
 *
 */
public class WrongUserException extends VFSException {
	public static final String NAME = "wrong_user";

	UserInfo user;

	
	public WrongUserException() {
		super();
	}
	
	public WrongUserException(UserInfo user) {
		super();
		this.user = user;
	}
	
	public WrongUserException(UserInfo user, Throwable cause) {
		super(cause);
		this.user = user;
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
				user.getInfo(UserInfo.USER)
		};
		return rtn;
	}
}
