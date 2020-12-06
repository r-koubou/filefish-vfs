package com.nullfish.lib.vfs.impl.root;

import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.ManipulationNotAvailableException;
import com.nullfish.lib.vfs.impl.root.manipulation.RootFileGetAttributesManipulation;
import com.nullfish.lib.vfs.impl.root.manipulation.RootFileGetChildrenManipulation;
import com.nullfish.lib.vfs.impl.root.manipulation.RootFileGetPermissionManipulation;
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

public class RootManipulationFactory implements ManipulationFactory {

	public CopyFileManipulation getCopyFileManipulation(VFile file)
			throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, CopyFileManipulation.NAME);
	}

	public CreateFileManipulation getCreateFileManipulation(VFile file)
			throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, CreateFileManipulation.NAME);
	}

	public CreateLinkManipulation getCreateLinkManipulation(VFile file)
			throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, CreateLinkManipulation.NAME);
	}

	public DeleteManipulation getDeleteManipulation(VFile file)
			throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, DeleteManipulation.NAME);
	}

	public GetAttributesManipulation getGetAttributeManipulation(VFile file)
			throws ManipulationNotAvailableException {
		return new RootFileGetAttributesManipulation(file);
	}

	public GetChildrenManipulation getGetChildrenManipulation(VFile file)
			throws ManipulationNotAvailableException {
		return new RootFileGetChildrenManipulation(file);
	}

	public GetInputStreamManipulation getGetInputStreamManipulation(VFile file)
			throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, GetInputStreamManipulation.NAME);
	}

	public GetOutputStreamManipulation getGetOutputStreamManipulation(VFile file)
			throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, GetOutputStreamManipulation.NAME);
	}

	public GetPermissionManipulation getGetPermissionManipulation(VFile file)
			throws ManipulationNotAvailableException {
		return new RootFileGetPermissionManipulation(file);
	}

	public MoveManipulation getMoveManipulation(VFile file)
			throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, MoveManipulation.NAME);
	}

	public SetAttributeManipulation getSetAttributeManipulation(VFile file)
			throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, SetAttributeManipulation.NAME);
	}

	public SetPermissionManipulation getSetPermissionManipulation(VFile file)
			throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, SetPermissionManipulation.NAME);
	}

	public SetTimestampManipulation getSetTimestampManipulation(VFile file)
			throws ManipulationNotAvailableException {
		throw new ManipulationNotAvailableException(file, SetTimestampManipulation.NAME);
	}

}
