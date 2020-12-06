package com.nullfish.lib.vfs.manipulation;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.Manipulation;

/**
 * ファイルの各種属性を初期化、取得する操作インターフェイス。
 * @author Shunji Yamaura
 */

public interface GetAttributesManipulation extends Manipulation {
	/**
	 * 操作名
	 */
	public static final String NAME = "init_attribute";

	/**
	 * ファイル属性を取得する。
	 * 
	 * @return
	 */
	public abstract FileAttribute getAttribute();

}
