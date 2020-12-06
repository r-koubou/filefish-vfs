package com.nullfish.lib.vfs.manipulation;

import com.nullfish.lib.vfs.VFile;

/**
 * ファイルの上書き時に、実行するか、実行しないか、別ファイル名で実行するか、
 * 全て中止するか判断するクラスのインターフェイス。
 * 
 * @author shunji
 */

public interface OverwritePolicy {
	/**
	 * 上書きする。
	 */
	public static final int OVERWRITE = 1;
	
	/**
	 * 実行しない。
	 */
	public static final int NO_OVERWRITE = 2;
	
	/**
	 * 別のファイルにコピーする。
	 */
	public static final int NEW_DEST = 3;
	
	/**
	 * 全ての操作を中止する。
	 */
	public static final int STOP_ALL = 99;
	
	/**
	 * 上書き確認を行う。
	 * 上書きするならOVERWRITE、しないならNO_OVERWRITE、
	 * 動作を全て中止するならSTOP_ALL、
	 * 別ファイルに上書きするならNEW_DESTを返す。
	 * 
	 * @param srcFile	上書き元ファイル
	 * @param dest		上書き先ファイル
	 * @return
	 */ 
	public int getOverwritePolicy(VFile srcFile, VFile dest);

	/**
	 * 新しい上書き先ファイルを返す。
	 * @return
	 */
	public VFile getNewDestination();

	/**
	 * 上書き元、上書き先を設定する。
	 * 
	 * @param from	上書き元
	 * @param to	上書き先
	 */
	public void setFiles(VFile from, VFile to);
}
