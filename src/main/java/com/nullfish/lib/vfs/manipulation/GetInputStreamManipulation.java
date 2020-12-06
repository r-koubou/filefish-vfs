/*
 * 作成日: 2003/10/06
 *
 */
package com.nullfish.lib.vfs.manipulation;

import java.io.InputStream;

import com.nullfish.lib.vfs.Manipulation;

/**
 * 入力ストリームを取得する。
 * @author Shunji Yamaura
 */

public interface GetInputStreamManipulation extends Manipulation {
	/**
	 * 操作名
	 */
	public static final String NAME = "get_input_stream";

	public abstract InputStream getInputStream();

}
