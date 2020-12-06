package com.nullfish.lib.vfs;

import java.util.Date;

import com.nullfish.lib.vfs.exception.ManipulationNotAvailableException;
import com.nullfish.lib.vfs.manipulation.CopyFileManipulation;
import com.nullfish.lib.vfs.manipulation.CreateFileManipulation;
import com.nullfish.lib.vfs.manipulation.CreateLinkManipulation;
import com.nullfish.lib.vfs.manipulation.DeleteFailurePolicy;
import com.nullfish.lib.vfs.manipulation.DeleteManipulation;
import com.nullfish.lib.vfs.manipulation.GetAttributesManipulation;
import com.nullfish.lib.vfs.manipulation.GetChildrenManipulation;
import com.nullfish.lib.vfs.manipulation.GetInputStreamManipulation;
import com.nullfish.lib.vfs.manipulation.GetOutputStreamManipulation;
import com.nullfish.lib.vfs.manipulation.GetPermissionManipulation;
import com.nullfish.lib.vfs.manipulation.MoveManipulation;
import com.nullfish.lib.vfs.manipulation.OverwritePolicy;
import com.nullfish.lib.vfs.manipulation.SetPermissionManipulation;
import com.nullfish.lib.vfs.manipulation.SetTimestampManipulation;
import com.nullfish.lib.vfs.manipulation.common.GetContentManipulation;
import com.nullfish.lib.vfs.manipulation.common.MD5HashManipulation;
import com.nullfish.lib.vfs.permission.FileAccess;
import com.nullfish.lib.vfs.permission.PermissionType;

/**
 * FileFishの操作クラス生成用ユーティリティクラス
 * @author shunji
 */
public class FileUtil {
	/**
	 * パーミッション取得操作作成メソッド
	 * @param file
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static GetPermissionManipulation prepareGetPermission(VFile file,
			Manipulation parent) throws ManipulationNotAvailableException {
		GetPermissionManipulation manipulation = file.getManipulationFactory()
				.getGetPermissionManipulation(file);
		manipulation.setParentManipulation(parent);
		return manipulation;
	}

	/**
	 * パーミッション設定操作作成メソッド
	 * @param file
	 * @param access
	 * @param type
	 * @param value
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static SetPermissionManipulation prepareSetPermission(VFile file,
			FileAccess access, PermissionType type, boolean value,
			Manipulation parent) throws ManipulationNotAvailableException {
		SetPermissionManipulation manipulation = file.getManipulationFactory()
				.getSetPermissionManipulation(file);
		manipulation.setParentManipulation(parent);
		manipulation.addPermission(type, access, value);
		return manipulation;
	}

	/**
	 * 入力ストリーム取得操作作成メソッド
	 * @param file
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static GetInputStreamManipulation prepareGetInputStream(VFile file,
			Manipulation parent) throws ManipulationNotAvailableException {
		GetInputStreamManipulation manipulation = file.getManipulationFactory()
			.getGetInputStreamManipulation(file);
		manipulation.setParentManipulation(parent);
		return manipulation;
	}

	/**
	 * 出力ストリーム取得操作作成メソッド
	 * @param file
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static GetOutputStreamManipulation prepareGetOutputStream(VFile file,
			Manipulation parent) throws ManipulationNotAvailableException {
		return prepareGetOutputStream(file, false, parent);
	}
	
	/**
	 * 出力ストリーム取得操作作成メソッド
	 * @param file
	 * @param append 
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static GetOutputStreamManipulation prepareGetOutputStream(VFile file,
			boolean append, Manipulation parent) throws ManipulationNotAvailableException {
		GetOutputStreamManipulation manipulation = file.getManipulationFactory()
			.getGetOutputStreamManipulation(file);
		manipulation.setParentManipulation(parent);
		manipulation.setAppend(append);
		return manipulation;
	}

	/**
	 * 属性取得操作作成メソッド
	 * @param file
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static GetAttributesManipulation prepareGetAttribute(VFile file,
			Manipulation parent) throws ManipulationNotAvailableException {
		GetAttributesManipulation manipulation = file.getManipulationFactory()
			.getGetAttributeManipulation(file);
		manipulation.setParentManipulation(parent);
		return manipulation;
	}

	/**
	 * タイムスタンプ設定操作作成メソッド
	 * @param file
	 * @param date
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static SetTimestampManipulation prepareSetTimestamp(VFile file, Date date,
			Manipulation parent) throws ManipulationNotAvailableException {
		SetTimestampManipulation manipulation = file.getManipulationFactory()
			.getSetTimestampManipulation(file);
		manipulation.setParentManipulation(parent);
		manipulation.setDate(date);
		return manipulation;
	}
	
	/**
	 * 子ファイル取得操作作成メソッド
	 * @param file
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static GetChildrenManipulation prepareGetChildren(VFile file,
			Manipulation parent) throws ManipulationNotAvailableException {
		GetChildrenManipulation manipulation = file.getManipulationFactory()
			.getGetChildrenManipulation(file);
		manipulation.setParentManipulation(parent);
		return manipulation;
	}
	
	/**
	 * ファイル作成操作生成メソッド
	 * @param file
	 * @param type
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static CreateFileManipulation prepareCreateFile(VFile file,
			FileType type, Manipulation parent) throws ManipulationNotAvailableException {
		CreateFileManipulation manipulation = file.getManipulationFactory()
			.getCreateFileManipulation(file);
		manipulation.setParentManipulation(parent);
		manipulation.setType(type);
		return manipulation;
	}

	/**
	 * リンク作成操作生成メソッド
	 * @param file
	 * @param dest
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static CreateLinkManipulation prepareCreateLink(VFile file,
			VFile dest, Manipulation parent) throws ManipulationNotAvailableException {
		CreateLinkManipulation manipulation = file.getManipulationFactory()
			.getCreateLinkManipulation(file);
		manipulation.setParentManipulation(parent);
		manipulation.setDest(dest);
		return manipulation;
	}

	/**
	 * ファイル削除操作作成メソッド
	 * @param file
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static DeleteManipulation prepareDelete(VFile file,
			DeleteFailurePolicy policy, Manipulation parent) throws ManipulationNotAvailableException {
		DeleteManipulation manipulation = file.getManipulationFactory()
			.getDeleteManipulation(file);
		manipulation.setDeleteFailurePolicy(policy);
		manipulation.setParentManipulation(parent);
		return manipulation;
	}

	/**
	 * 移動操作作成メソッド
	 * @param file
	 * @param dest
	 * @param policy
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static MoveManipulation prepareMoveTo(VFile file,
			VFile dest, OverwritePolicy policy, Manipulation parent) throws ManipulationNotAvailableException {
		MoveManipulation manipulation = file.getManipulationFactory()
			.getMoveManipulation(file);
		manipulation.setParentManipulation(parent);
		manipulation.setDest(dest);
		manipulation.setOverwritePolicy(policy);
		return manipulation;
	}
	
	/**
	 * コピー操作作成メソッド
	 * @param file
	 * @param dest
	 * @param policy
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static CopyFileManipulation prepareCopyTo(VFile file,
			VFile dest, OverwritePolicy policy, Manipulation parent) throws ManipulationNotAvailableException {
		return prepareCopyTo(file, dest, policy, true, parent);
	}
	
	/**
	 * コピー操作作成メソッド
	 * @param file
	 * @param dest
	 * @param policy
	 * @param copiesPermission
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static CopyFileManipulation prepareCopyTo(VFile file,
			VFile dest, OverwritePolicy policy, boolean copiesPermission, Manipulation parent) throws ManipulationNotAvailableException {
		CopyFileManipulation manipulation = file.getManipulationFactory()
			.getCopyFileManipulation(file);
		manipulation.setParentManipulation(parent);
		manipulation.setDest(dest);
		manipulation.setOverwritePolicy(policy);
		manipulation.setCopiesPermission(copiesPermission);
		return manipulation;
	}
	
	/**
	 * バイナリ取得操作作成メソッド
	 * @param file
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static MD5HashManipulation prepareGetContentHash(VFile file, Manipulation parent) throws ManipulationNotAvailableException {
		MD5HashManipulation manipulation = new MD5HashManipulation(file);
		manipulation.setParentManipulation(parent);
		return manipulation;
	}
	
	/**
	 * バイナリ取得操作作成メソッド
	 * @param file
	 * @param parent
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public static GetContentManipulation prepareGetContent(VFile file, Manipulation parent) throws ManipulationNotAvailableException {
		GetContentManipulation manipulation = new GetContentManipulation(file);
		manipulation.setParentManipulation(parent);
		return manipulation;
	}
}
