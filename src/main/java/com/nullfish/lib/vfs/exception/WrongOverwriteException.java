/*
 * 作成日: 2003/10/15
 *
 */
package com.nullfish.lib.vfs.exception;

import com.nullfish.lib.vfs.VFile;

/**
 * 上書き例外クラス
 * 
 * @author Shunji Yamaura
 */
public class WrongOverwriteException extends VFSException {
	public static final String NAME = "wrong_overwrite";

	/**
	 * 元ファイル
	 */
	private VFile from;

	/**
	 * 先ファイル
	 */
	private VFile to;

	
	/**
	 * 失敗理由
	 */
	private int reason;
	
	/**
	 * ファイル種類が異なる
	 */
	public static final int DIFFERENT_FILE_TYPE = 1;
	
	/**
	 * コンストラクタ
	 * @param from
	 * @param to
	 */
	public WrongOverwriteException(VFile from, VFile to, int reason) {
		this.from = from;
		this.to = to;
		this.reason = reason;
	}

	/**
	 * @return
	 */
	public VFile getFrom() {
		return from;
	}

	/**
	 * @return
	 */
	public int getReason() {
		return reason;
	}

	/**
	 * @return
	 */
	public VFile getTo() {
		return to;
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
				from,
				to
		};
		return rtn;
	}
}
