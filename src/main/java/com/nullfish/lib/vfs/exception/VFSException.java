package com.nullfish.lib.vfs.exception;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * バーチャルファイルシステム用例外クラス
 * 
 * @author shunji
 */

public abstract class VFSException extends Exception {
	/**
	 * エラーメッセージのリソースバンドル
	 */
	protected final static ResourceBundle messages =
		ResourceBundle.getBundle("error_message");

	public VFSException() {
		super();
	}

	public VFSException(String message) {
		super(message);
	}

	public VFSException(String message, Throwable cause) {
		super(message, cause);
	}

	public VFSException(Throwable cause) {
		super(cause);
	}

	/**
	 * 例外の名称を取得する。
	 * @return
	 */
	public abstract String getName();

	
	/**
	 * エラーメッセージに出力するオブジェクトの配列を取得する。
	 * @return
	 */
	public abstract Object[] getErrorValues();
	
	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.exception.VFSException#getErrorMessage()
	 */
	public String getErrorMessage() {
		try {
			String origMessage = messages.getString(getName());
			Object[] values = getErrorValues();
			
			return MessageFormat.format(origMessage, values);
		} catch (MissingResourceException e) {
			return "";
		}
	}
}
