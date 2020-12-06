/*
 * Created on 2004/04/10
 *
 */
package com.nullfish.lib.vfs.manipulation;

import java.util.Date;

import com.nullfish.lib.vfs.Manipulation;


/**
 * 
 * @author shunji
 *
 */
public interface SetTimestampManipulation extends Manipulation {
	/**
	 * 操作名
	 */
	public static final String NAME = "set_timestamp";
	
	/**
	 * 日付をセットする。
	 * @param date
	 */
	public void setDate(Date date);
}
