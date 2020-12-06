/*
 * 作成日: 2003/10/27
 *
 */
package com.nullfish.lib.vfs.impl.ftp;


import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.ManipulationNotAvailableException;
import com.nullfish.lib.vfs.impl.ftp.manipulation.FTPCreateFileManipulation;
import com.nullfish.lib.vfs.impl.ftp.manipulation.FTPDeleteManipulation;
import com.nullfish.lib.vfs.impl.ftp.manipulation.FTPGetAttributeManipulation;
import com.nullfish.lib.vfs.impl.ftp.manipulation.FTPGetChildrenManipulation;
import com.nullfish.lib.vfs.impl.ftp.manipulation.FTPGetInputStreamManipulation;
import com.nullfish.lib.vfs.impl.ftp.manipulation.FTPGetOutputStreamManipulation;
import com.nullfish.lib.vfs.impl.ftp.manipulation.FTPGetPermissionManipulation;
import com.nullfish.lib.vfs.impl.ftp.manipulation.FTPMoveManipulation;
import com.nullfish.lib.vfs.impl.ftp.manipulation.FTPSetPermissionManipulation;
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
 * @author shunji
 *
 */
public class FTPFileManipulationFactory implements ManipulationFactory {

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationFactory#getInitAttributeManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public GetAttributesManipulation getGetAttributeManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new FTPGetAttributeManipulation(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationFactory#getSetAttributeManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public SetAttributeManipulation getSetAttributeManipulation(VFile file)
		throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, SetAttributeManipulation.NAME);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationFactory#getGetPermissionManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public GetPermissionManipulation getGetPermissionManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new FTPGetPermissionManipulation(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationFactory#getSetPermissionManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public SetPermissionManipulation getSetPermissionManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new FTPSetPermissionManipulation(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationFactory#getGetInputStreamManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public GetInputStreamManipulation getGetInputStreamManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new FTPGetInputStreamManipulation(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationFactory#getGetOutputStreamManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public GetOutputStreamManipulation getGetOutputStreamManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new FTPGetOutputStreamManipulation(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationFactory#getGetChildrenManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public GetChildrenManipulation getGetChildrenManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new FTPGetChildrenManipulation(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationFactory#getCreateFileManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public CreateFileManipulation getCreateFileManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new FTPCreateFileManipulation(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationFactory#getCreateLinkManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public CreateLinkManipulation getCreateLinkManipulation(VFile file)
		throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, CreateLinkManipulation.NAME);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationFactory#getDeleteManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public DeleteManipulation getDeleteManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new FTPDeleteManipulation(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationFactory#getMoveManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public MoveManipulation getMoveManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new FTPMoveManipulation(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationFactory#getCopyFileManipulation(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public CopyFileManipulation getCopyFileManipulation(VFile file)
		throws ManipulationNotAvailableException {
		return new DefaultCopyFileManipulation(file);
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
