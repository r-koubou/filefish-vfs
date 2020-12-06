/*
 * 作成日: 2003/10/27
 *
 */
package com.nullfish.lib.vfs.manipulation;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.Manipulation;

/**
 * @author shunji
 *
 */
public interface SetAttributeManipulation extends Manipulation {
	/**
	 * 操作名
	 */
	public static final String NAME = "set_attribute";
	
	/**
	 * 属性をセットする。
	 * @param attr
	 */
	public abstract void setAttribute(FileAttribute attr);
}
