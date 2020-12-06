package com.nullfish.lib.vfs.impl.smb;


import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.ManipulationNotAvailableException;
import com.nullfish.lib.vfs.impl.smb.manipulation.SMBCreateFileManipulation;
import com.nullfish.lib.vfs.impl.smb.manipulation.SMBDeleteManipulation;
import com.nullfish.lib.vfs.impl.smb.manipulation.SMBGetAttributesManipulation;
import com.nullfish.lib.vfs.impl.smb.manipulation.SMBGetChildrenManipulation;
import com.nullfish.lib.vfs.impl.smb.manipulation.SMBGetInputStreamManipulation;
import com.nullfish.lib.vfs.impl.smb.manipulation.SMBGetOutputStreamManipulation;
import com.nullfish.lib.vfs.impl.smb.manipulation.SMBGetPermissionManipulation;
import com.nullfish.lib.vfs.impl.smb.manipulation.SMBMoveManipulation;
import com.nullfish.lib.vfs.impl.smb.manipulation.SMBSetAttributeManipulation;
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
import com.nullfish.lib.vfs.manipulation.common.DefaultCopyFileManipulation;


/**
 * ローカルのファイル操作を提供するクラス。
 * 
 * @author Shunji Yamaura
 */
public class SMBFileManipulationFactory implements ManipulationFactory {

	/**
	 * ファイルパーミッション取得クラスを取得する。
	 * @see com.nullfish.lib.vfs.ManipulationFactory#getGetPermissionManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public GetPermissionManipulation getGetPermissionManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new SMBGetPermissionManipulation(file);
	}

	/**
	 * ファイルパーミッションセット操作クラスを返す。
	 * @param file
	 * @return
	 * @throws ManipulationNotAvailableException
	 */
	public SetPermissionManipulation getSetPermissionManipulation(VFile file)
		throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, SetAttributeManipulation.NAME);
	}

	/**
	 * 入力ストリーム取得クラスを取得する。
	 * @see com.nullfish.lib.vfs.ManipulationFactory#getGetInputStreamManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public GetInputStreamManipulation getGetInputStreamManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new SMBGetInputStreamManipulation(file);
	}

	/**
	 * 出力ストリーム取得クラスを取得する。
	 * @see com.nullfish.lib.vfs.ManipulationFactory#getGetOutputStreamManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public GetOutputStreamManipulation getGetOutputStreamManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new SMBGetOutputStreamManipulation(file);
	}

	/**
	 * 子ファイル取得クラスを取得する。
	 * @see com.nullfish.lib.vfs.ManipulationFactory#getGetChildrenManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public GetChildrenManipulation getGetChildrenManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new SMBGetChildrenManipulation(file);
	}

	/**
	 * ファイル生成クラスを取得する。
	 * @see com.nullfish.lib.vfs.ManipulationFactory#getCreateFileManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public CreateFileManipulation getCreateFileManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new SMBCreateFileManipulation(file);
	}

	/**
	 * リンク作成クラスを取得する。
	 * サポートしない。
	 * @see com.nullfish.lib.vfs.ManipulationFactory#getCreateLinkManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public CreateLinkManipulation getCreateLinkManipulation(VFile file)
		throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, CreateLinkManipulation.NAME);
	}

	/**
	 * ファイル削除操作クラスを取得する。
	 * @see com.nullfish.lib.vfs.ManipulationFactory#getDeleteManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public DeleteManipulation getDeleteManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new SMBDeleteManipulation(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationFactory#getRenameManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public MoveManipulation getMoveManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new SMBMoveManipulation(file);
	}

	/**
	 * ファイルコピー操作クラスを返す。
	 * @return
	 */
	public CopyFileManipulation getCopyFileManipulation(VFile file)
		throws ManipulationNotAvailableException {

		return new DefaultCopyFileManipulation(file);
	}

	/**
	 * ファイル属性初期化、取得操作クラスを返す。
	 * @see com.nullfish.lib.vfs.ManipulationFactory#getGetAttributeManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public GetAttributesManipulation getGetAttributeManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new SMBGetAttributesManipulation(file);
	}

	/**
	 * ファイル属性セットクラスを取得する。
	 * @see com.nullfish.lib.vfs.ManipulationFactory#getSetAttributeManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public SetAttributeManipulation getSetAttributeManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new SMBSetAttributeManipulation(file);
	}
	
	/**
	 * タイムスタンプ設定操作クラスを返す。
	 * @return
	 */
	public SetTimestampManipulation getSetTimestampManipulation(VFile file)
		throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, SetTimestampManipulation.NAME);
	}
}
