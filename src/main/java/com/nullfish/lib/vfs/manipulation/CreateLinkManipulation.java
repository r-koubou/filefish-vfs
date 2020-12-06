/*
 * 作成日: 2003/10/06
 *
 */
package com.nullfish.lib.vfs.manipulation;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFile;

/**
 * リンク生成操作クラス
 * @author Shunji Yamaura
 */
public interface CreateLinkManipulation extends Manipulation {
	/**
	 * 操作名
	 */
	public static final String NAME = "create_link";
	
	/**
	 * リンク先を指定する。
	 * @param file
	 */
	public abstract void setDest(VFile file);
}
