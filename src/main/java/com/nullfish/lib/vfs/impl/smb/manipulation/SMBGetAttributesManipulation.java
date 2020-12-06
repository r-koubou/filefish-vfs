package com.nullfish.lib.vfs.impl.smb.manipulation;

import java.net.UnknownHostException;
import java.util.Date;

import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.WrongUserException;
import com.nullfish.lib.vfs.impl.DefaultFileAttribute;
import com.nullfish.lib.vfs.impl.smb.SMBFile;
import com.nullfish.lib.vfs.impl.smb.SMBFileSystem;
import com.nullfish.lib.vfs.impl.smb.SMBUtilities;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetAttributesManipulation;


/**
 * SMBファイルの属性初期化、取得操作クラス
 * @author Shunji Yamaura
 */
public class SMBGetAttributesManipulation
	extends AbstractGetAttributesManipulation {
		
	public SMBGetAttributesManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractInitAttributesManipulation#initFileAttribute(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public FileAttribute doGetAttribute(VFile file) throws VFSException {
		try {
			SmbFile f = ((SMBFile)file).getFileAsFile();
			if (!f.exists()) {
				return new DefaultFileAttribute(false, 0, null, FileType.NOT_EXISTS);
			}
	
			long length = f.length();
			Date date = new Date(f.lastModified());
			FileType fileType = null;
			if (f.isFile()) {
				fileType = FileType.FILE;
			} else {
				fileType = FileType.DIRECTORY;
			}
	
((SMBFileSystem)file.getFileSystem()).cacheAuthentication(f);
			return new DefaultFileAttribute(true, length, date, fileType);
		} catch (UnknownHostException e) {
			throw new FileNotExistsException(file);
		} catch (SmbAuthException e) {
			throw new WrongUserException(file.getFileName().getUserInfo(), e);
		} catch (SmbException e) {
			throw SMBUtilities.getVFSException(e, file);
		}
	}
}
