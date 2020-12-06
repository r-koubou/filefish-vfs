/*
 * 作成日: 2003/10/06
 *
 */
package com.nullfish.lib.vfs.manipulation;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFile;

/**
 * ファイルの移動操作クラス
 * @author Shunji Yamaura
 */
public interface MoveManipulation extends Manipulation {
	/**
	 * 操作名
	 */
	public static final String NAME = "move_file";
	
	/**
	 * 変更先ファイル名称をセットする。
	 * @param dest
	 */
	public abstract void setDest(VFile dest);

	/**
	 * 上書き方針をセットする。
	 * 
	 * @param policy
	 */
	public abstract void setOverwritePolicy(OverwritePolicy policy);
	
	/**
	 * パーミッションのコピーを行うか設定する。
	 * デフォルトではtrue
	 * @param bool
	 */
	public abstract void setCopiesPermission(boolean bool);
	
	/**
	 * パーティション内移動に失敗した際にコピー→削除での移動を行わないかを指定する。
	 * @param noCopy	trueなら行わない。
	 */
	public abstract void setNoCopy(boolean noCopy);
}
