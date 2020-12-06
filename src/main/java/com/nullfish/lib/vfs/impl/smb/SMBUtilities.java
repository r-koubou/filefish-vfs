/*
 * Created on 2003/12/30
 * 
 */
package com.nullfish.lib.vfs.impl.smb;

import jcifs.smb.NtStatus;
import jcifs.smb.SmbException;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileCreationException;
import com.nullfish.lib.vfs.exception.FileDeletionException;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.FileSystemNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.exception.VFSSystemException;
import com.nullfish.lib.vfs.exception.WrongPathException;
import com.nullfish.lib.vfs.exception.WrongPermissionException;
import com.nullfish.lib.vfs.exception.WrongUserException;

/**
 * @author shunji
 * 
 */
public class SMBUtilities {
	/**
	 * SmbExceptionからVFSExceptionに変換するメソッド。 
	 * まだ分類中。
	 * 
	 * @param e
	 * @param file
	 * @return
	 */
	public static VFSException getVFSException(SmbException e, VFile file) {
		int errorCode = e.getNtStatus();
		
		switch (errorCode) {
			case NtStatus.NT_STATUS_OK:
				return null;
			case NtStatus.NT_STATUS_NO_SUCH_USER:
			case NtStatus.NT_STATUS_WRONG_PASSWORD:
			case NtStatus.NT_STATUS_LOGON_FAILURE:
			case NtStatus.NT_STATUS_ACCOUNT_RESTRICTION:
			case NtStatus.NT_STATUS_INVALID_LOGON_HOURS:
			case NtStatus.NT_STATUS_INVALID_WORKSTATION:
			case NtStatus.NT_STATUS_PASSWORD_EXPIRED:
			case NtStatus.NT_STATUS_ACCOUNT_DISABLED:
			case NtStatus.NT_STATUS_LOGON_TYPE_NOT_GRANTED:
			case NtStatus.NT_STATUS_ACCOUNT_LOCKED_OUT:
				return new WrongUserException(file.getFileName().getUserInfo(), e);
	
			case NtStatus.NT_STATUS_OBJECT_NAME_INVALID:
			case NtStatus.NT_STATUS_NO_SUCH_FILE:
			case NtStatus.NT_STATUS_OBJECT_PATH_SYNTAX_BAD:
				return new WrongPathException(file.getAbsolutePath(), e);
	
			case NtStatus.NT_STATUS_OBJECT_PATH_NOT_FOUND:
			case NtStatus.NT_STATUS_OBJECT_NAME_NOT_FOUND:
				return new FileNotExistsException(e, file);
	
			case NtStatus.NT_STATUS_OBJECT_NAME_COLLISION:
				return new FileCreationException(e, file);
	
			case NtStatus.NT_STATUS_CANNOT_DELETE:
				return new FileDeletionException(e, file);
	
			case NtStatus.NT_STATUS_ACCESS_VIOLATION:
			case NtStatus.NT_STATUS_ACCESS_DENIED:
				return new WrongPermissionException(e, file);
	
			case NtStatus.NT_STATUS_PORT_DISCONNECTED:
			case NtStatus.NT_STATUS_PIPE_NOT_AVAILABLE:
			case NtStatus.NT_STATUS_INVALID_PIPE_STATE:
			case NtStatus.NT_STATUS_PIPE_BUSY:
			case NtStatus.NT_STATUS_PIPE_DISCONNECTED:
			case NtStatus.NT_STATUS_PIPE_CLOSING:
			case NtStatus.NT_STATUS_PIPE_LISTENING:
			case NtStatus.NT_STATUS_PIPE_BROKEN:
			case NtStatus.NT_STATUS_DELETE_PENDING:
			case NtStatus.NT_STATUS_FILE_IS_A_DIRECTORY:
			case NtStatus.NT_STATUS_INSTANCE_NOT_AVAILABLE:
			case NtStatus.NT_STATUS_INVALID_INFO_CLASS:
			case NtStatus.NT_STATUS_NOT_A_DIRECTORY:
				return new VFSIOException(e);
	
			case NtStatus.NT_STATUS_UNSUCCESSFUL:
			case NtStatus.NT_STATUS_INVALID_HANDLE:
			case NtStatus.NT_STATUS_SHARING_VIOLATION:
			case NtStatus.NT_STATUS_BAD_NETWORK_NAME:
			case NtStatus.NT_STATUS_PATH_NOT_COVERED:
				return new FileSystemNotExistsException(e, file.getFileSystem());
	
			case NtStatus.NT_STATUS_NOT_IMPLEMENTED:
				return new VFSSystemException(e);
			default:
				return new VFSIOException(e);
		}
	}
}
