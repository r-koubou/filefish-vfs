/*
 * 作成日: 2003/10/27
 *
 */
package com.nullfish.lib.vfs.impl.ftp;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSSystemException;
import com.nullfish.lib.vfs.impl.DefaultFileAttribute;

/**
 * @author shunji
 * 
 */
public class FTPFile extends VFile {

	org.apache.commons.net.ftp.FTPFile oro;

	private static final FTPFileManipulationFactory provider = new FTPFileManipulationFactory();

	/**
	 * コンストラクタ
	 * 
	 * @param fileSystem
	 * @param fileName
	 */
	public FTPFile(FileSystem fileSystem, FileName fileName) {
		super(fileSystem, fileName);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param fileSystem
	 * @param fileName
	 */
	public FTPFile(FileSystem fileSystem, FileName fileName,
			FileAttribute attributes, Permission permission) {
		super(fileSystem, fileName);
		super.attributes = attributes;
		super.permission = permission;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param fileSystem
	 * @param fileName
	 * @param oro
	 */
	public FTPFile(FileSystem fileSystem, FileName fileName,
			org.apache.commons.net.ftp.FTPFile oro) {
		super(fileSystem, fileName);
		this.oro = oro;
		FTPPermission p = new FTPPermission();
		p.converFromOroFTPFile(oro);
		this.permission = p;
		try {
			this.attributes = oro2FileAttribute(oro);
		} catch (VFSException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.VFile#getManipulationFactory()
	 */
	public ManipulationFactory getManipulationFactory() {
		return provider;
	}

	public static FileAttribute oro2FileAttribute(
			org.apache.commons.net.ftp.FTPFile oro) throws VFSException {
		String name = oro.getName();
		long length = oro.getSize();
		FileType type = null;
		switch (oro.getType()) {
		case org.apache.commons.net.ftp.FTPFile.FILE_TYPE:
			type = FileType.FILE;
			break;
		case org.apache.commons.net.ftp.FTPFile.DIRECTORY_TYPE:
			type = FileType.DIRECTORY;
			break;
		case org.apache.commons.net.ftp.FTPFile.SYMBOLIC_LINK_TYPE:
			type = FileType.LINK;
			break;
		default:
			throw new VFSSystemException("Unknown file type");
		}

		Date date = oro.getTimestamp().getTime();

		String group = oro.getGroup();
		String user = oro.getUser();

		return new DefaultFileAttribute(true, length, date, type);

	}

	/**
	 * FTPの転送モードを設定する。
	 * 
	 * @param ftp
	 * @throws IOException
	 */
	public void configFtpClient(FTPClient ftp) throws IOException {
		Boolean passive = (Boolean) getFileSystem().getVFS().getConfiguration()
				.getDefaultConfig(FTPFileSystem.CONFIG_PASSIVE);
		if (passive != null && passive.booleanValue()) {
			ftp.enterLocalPassiveMode();
		} else {
			ftp.enterLocalActiveMode();
		}

		String transferMode = (String) getFileSystem().getVFS()
				.getConfiguration().getDefaultConfig(FTPFileSystem.TRANSFER_MODE);

		if (FTPFileSystem.TRANSFER_MODE_EXTENSION.equals(transferMode)) {
			String extension = getFileName().getLowerExtension();
			List extensions = (List) getFileSystem().getVFS()
					.getConfiguration().getDefaultConfig(
							FTPFileSystem.CONFIG_ASCII_MODE_EXTENSION);
			if (extensions != null && extensions.contains(extension)) {
				ftp.setFileTransferMode(FTPClient.ASCII_FILE_TYPE);
			} else {
				ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
			}
		} else if (transferMode.equals(FTPFileSystem.TRANSFER_MODE_ASCII)) {
			ftp.setFileTransferMode(FTPClient.ASCII_FILE_TYPE);
		} else {
			ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
		}
	}
}
