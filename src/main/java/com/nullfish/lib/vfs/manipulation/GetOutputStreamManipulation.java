/*
 * 作成日: 2003/10/06
 *
 */
package com.nullfish.lib.vfs.manipulation;

import java.io.OutputStream;

import com.nullfish.lib.vfs.Manipulation;

/**
 * 出力ストリーム取得クラス
 * @author Shunji Yamaura
 */

public interface GetOutputStreamManipulation extends Manipulation {
	/**
	 * 操作名
	 */
	public static final String NAME = "get_output_stream";

	/**
	 * 追記操作名
	 */
	public static final String NAME_APPEND = "get_output_stream_append";

	/**
	 * 出力ストリームを取得する。
	 * @return
	 */
	public abstract OutputStream getOutputStream();

	/**
	 * 追記書き込みを行なうか設定する。
	 * 
	 * @param append trueなら追記
	 */
	public abstract void setAppend(boolean append);
}
