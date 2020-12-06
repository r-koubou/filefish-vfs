/*
 * 作成日: 2003/10/09
 *
 */
package com.nullfish.lib.vfs.manipulation;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFile;

/**
 * ファイルのコピー操作
 * @author Shunji Yamaura
 */
public interface CopyFileManipulation extends Manipulation {
	/**
	 * 操作名
	 */
	public static final String NAME = "copy_file";
	
	/**
	 * コピー先をセットする。
	 * 
	 * @param dest
	 */
	public abstract void setDest(VFile dest);
	
	/**
	 * 上書き方針をセットする。
	 * prepare前に実行すること。
	 * 
	 * @param policy
	 */
	public abstract void setOverwritePolicy(OverwritePolicy policy);
	
	/**
	 * パーミッションのコピーを行うか指定する。
	 * デフォルトではtrue
	 * @param bool
	 */
	public abstract void setCopiesPermission(boolean bool);
}
