/*
 * 作成日: 2003/10/06
 *
 */
package com.nullfish.lib.vfs.manipulation;

import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.Manipulation;

/**
 * ファイル生成操作クラス
 * @author Shunji Yamaura
 */
public interface CreateFileManipulation extends Manipulation {
	/**
	 * 操作名
	 */
	public static final String NAME = "create_file";
	
	public abstract void setType(FileType fileType);
}
