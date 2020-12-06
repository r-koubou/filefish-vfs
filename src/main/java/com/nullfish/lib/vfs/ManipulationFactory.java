package com.nullfish.lib.vfs;


import com.nullfish.lib.vfs.exception.ManipulationNotAvailableException;
import com.nullfish.lib.vfs.manipulation.CopyFileManipulation;
import com.nullfish.lib.vfs.manipulation.CreateFileManipulation;
import com.nullfish.lib.vfs.manipulation.CreateLinkManipulation;
import com.nullfish.lib.vfs.manipulation.DeleteManipulation;
import com.nullfish.lib.vfs.manipulation.GetAttributesManipulation;
import com.nullfish.lib.vfs.manipulation.GetChildrenManipulation;
import com.nullfish.lib.vfs.manipulation.GetInputStreamManipulation;
import com.nullfish.lib.vfs.manipulation.GetOutputStreamManipulation;
import com.nullfish.lib.vfs.manipulation.GetPermissionManipulation;
import com.nullfish.lib.vfs.manipulation.MoveManipulation;
import com.nullfish.lib.vfs.manipulation.SetAttributeManipulation;
import com.nullfish.lib.vfs.manipulation.SetPermissionManipulation;
import com.nullfish.lib.vfs.manipulation.SetTimestampManipulation;


/**
 * ファイル操作クラスのファクトリークラス。
 * 
 * @author shunji
 *
 */
public interface ManipulationFactory {
	/**
	 * ファイル属性初期化取得クラスを取得する。
	 * @param file
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public GetAttributesManipulation getGetAttributeManipulation(VFile file)
		throws ManipulationNotAvailableException;

	/**
	 * ファイル属性セットクラスを取得する。
	 * @param file
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public SetAttributeManipulation getSetAttributeManipulation(VFile file)
		throws ManipulationNotAvailableException;

	/**
	 * ファイルパーミッションクラス取得クラスを返す。
	 * @return
	 */
	public GetPermissionManipulation getGetPermissionManipulation(VFile file)
		throws ManipulationNotAvailableException;

	/**
	 * ファイルパーミッションセット操作クラスを返す。
	 * @param file
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public SetPermissionManipulation getSetPermissionManipulation(VFile file)
		throws ManipulationNotAvailableException;

	/**
	 * 入力ストリーム取得クラスを返す。
	 * @return
	 */
	public GetInputStreamManipulation getGetInputStreamManipulation(VFile file)
		throws ManipulationNotAvailableException;

	/**
	 * 出力ストリーム取得クラスを返す。
	 * @return
	 */
	public GetOutputStreamManipulation getGetOutputStreamManipulation(VFile file)
		throws ManipulationNotAvailableException;

	/**
	 * 子ファイル取得クラスを返す。
	 * @return
	 */
	public GetChildrenManipulation getGetChildrenManipulation(VFile file)
		throws ManipulationNotAvailableException;

	/**
	 * ファイル生成クラスを返す。
	 * @return
	 */
	public CreateFileManipulation getCreateFileManipulation(VFile file)
		throws ManipulationNotAvailableException;

	/**
	 * リンク生成クラスを返す。
	 * @return
	 */
	public CreateLinkManipulation getCreateLinkManipulation(VFile file)
		throws ManipulationNotAvailableException;

	/**
	 * ファイル削除クラスを返す。
	 * @return
	 */
	public DeleteManipulation getDeleteManipulation(VFile file)
		throws ManipulationNotAvailableException;

	/**
	 * ファイル名称変更クラスを返す。
	 * @return
	 */
	public MoveManipulation getMoveManipulation(VFile file)
		throws ManipulationNotAvailableException;

	/**
	 * ファイルコピー操作クラスを返す。
	 * @return
	 */
	public CopyFileManipulation getCopyFileManipulation(VFile file)
		throws ManipulationNotAvailableException;

	/**
	 * 最終更新日設定操作クラスを返す。
	 * @return
	 */
	public SetTimestampManipulation getSetTimestampManipulation(VFile file)
		throws ManipulationNotAvailableException;
}
