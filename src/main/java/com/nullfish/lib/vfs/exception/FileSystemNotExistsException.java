package com.nullfish.lib.vfs.exception;

import com.nullfish.lib.vfs.FileSystem;

/**
 * 
 * @author Shunji Yamaura
 */
public class FileSystemNotExistsException extends VFSException {
	public static final String NAME = "file_system_not_exists";

	FileSystem fileSystem;

	
	public FileSystemNotExistsException(FileSystem fileSystem) {
		super();
		this.fileSystem = fileSystem;
	}
	
	public FileSystemNotExistsException(String message, FileSystem fileSystem) {
		super(message);
		this.fileSystem = fileSystem;
	}
	
	public FileSystemNotExistsException(String message, Throwable cause, FileSystem fileSystem) {
		super(message, cause);
		this.fileSystem = fileSystem;
	}
	
	public FileSystemNotExistsException(Throwable cause, FileSystem fileSystem) {
		super(cause);
		this.fileSystem = fileSystem;
	}

	public FileSystem getFileSystem() {
		return fileSystem;
	}

	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.exception.VFSException#getName()
	 */
	public String getName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.exception.VFSException#getErrorValues()
	 */
	public Object[] getErrorValues() {
		Object[] rtn = {
				fileSystem.getRootName().getSecurePath()
		};
		return rtn;
	}
}
