package com.nullfish.lib.vfs.impl.local.native_lib;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.Permission;

public class LocalFileInfo {
	private String parentPath;
	
	private String fileName;
	
	private Permission permission;
	
	private FileAttribute attribute;

	public FileAttribute getAttribute() {
		return attribute;
	}

	public String getFileName() {
		return fileName;
	}

	public String getParentPath() {
		return parentPath;
	}

	public Permission getPermission() {
		return permission;
	}
}
