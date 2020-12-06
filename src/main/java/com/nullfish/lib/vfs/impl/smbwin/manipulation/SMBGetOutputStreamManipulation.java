package com.nullfish.lib.vfs.impl.smbwin.manipulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.WrongPermissionException;
import com.nullfish.lib.vfs.impl.smbwin.SMBFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetOutputStreamManipulation;


/**
 * 
 * @author Shunji Yamaura
 */
public class SMBGetOutputStreamManipulation
	extends AbstractGetOutputStreamManipulation {

	public SMBGetOutputStreamManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetOutputStreamManipulation#initOutputStream(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public OutputStream doGetOutputStream(VFile file) throws VFSException {
		return getOutputStreamImpl(file, false);
	}

	/**
	 * 追記書き込みストリームを取得する。
	 * 
	 * @see com.nullfish.lib.vfs.manipulation.abst.AbstractGetOutputStreamManipulation#doGetAppendOutputStream(com.nullfish.lib.vfs.VFile)
	 */
	public OutputStream doGetAppendOutputStream(VFile file) throws VFSException {
		return getOutputStreamImpl(file, true);
	}
	
	private OutputStream getOutputStreamImpl(VFile file, boolean append) throws VFSException {
		try {
			if(!file.exists(this)) {
				file.createFile(this);
			}

			return new FileOutputStream(((SMBFile)file).getFile(), append);
		} catch (FileNotFoundException e) {
			File javaFile = ((SMBFile)file).getFile();
			if(javaFile.exists()) {
				//	java.io.Fileは書き込み権限が無い場合もFileNotFoundExceptionを投げる。
				throw new WrongPermissionException(file);
			} else {
				throw new FileNotExistsException(file);
			}
		} finally {
			setFinished(true);
		}
		
	}
}
