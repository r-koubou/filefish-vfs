package com.nullfish.lib.vfs.impl.rar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.UserInfo;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.impl.AbstractFileSystem;
import com.nullfish.lib.vfs.impl.DefaultFileAttribute;
import com.nullfish.lib.vfs.impl.antzip.TemporaryFileUtil;
import com.nullfish.lib.vfs.impl.local.LocalFile;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;

public class RARFileSystem extends AbstractFileSystem {

	private List headers = new ArrayList();

	private Map nameFileMap = new HashMap();

	private UserInfo userInfo;

	boolean opened = false;

	private long totalSpace = 0;
	
	/**
	 * ローカルのテンポラリファイル
	 */
	private File tempFile;

	/**
	 * ファイルのローカルコピーを使用するかどうかのフラグ
	 */
	private boolean usesLocalCopy = false;

	private Archive rarFile;
	
	public RARFileSystem(VFS vfs, VFile baseFile, UserInfo userInfo) {
		super(vfs);
		parentFileSystem = baseFile.getFileSystem();
		rootName = new RARFileName(new String[0], baseFile.getFileName(),
				userInfo);
	}

	public VFile getBaseFile() {
		return parentFileSystem.getFile(rootName.getBaseFileName());
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#open()
	 */
	public void doOpen(Manipulation manipulation) throws VFSException {
		try {
			VFile baseFile = getBaseFile();
			baseFile.getFileSystem().open(manipulation);
	
			if (!opened) {
				if (baseFile instanceof LocalFile) {
					usesLocalCopy = false;
					rarFile = new Archive(((LocalFile) baseFile).getFile());
				} else {
					usesLocalCopy = true;
					tempFile = TemporaryFileUtil.getInstance().getTemporaryFile(
							baseFile, manipulation).getFile();
					rarFile = new Archive(tempFile);
				}
	
				initFileTree(rarFile);
			}
			opened = true;
		} catch (IOException e) {
			try {
				if(rarFile != null) {
					rarFile.close();
				}
				if(usesLocalCopy) {
					tempFile.delete();
				}
			} catch (Exception ex) {}
			throw new VFSIOException(e);
		} catch (RarException e) {
			throw new VFSIOException(e);
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#close()
	 */
	public void doClose(Manipulation manipulation) throws VFSException {
		opened = false;
		headers.clear();
		nameFileMap.clear();
		try {
			if(rarFile != null) {
				rarFile.close();
			}
	
			if(usesLocalCopy && tempFile != null) {
				tempFile.delete();
			}
		} catch (IOException e) {e.printStackTrace();}
	}

	public boolean isOpened() {
		return opened;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#createFileSystem()
	 */
	public void createFileSystem() throws VFSException {
		VFile baseFile = getBaseFile();
		if (!baseFile.exists()) {
			baseFile.createFile();
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#getFile(com.sexyprogrammer.lib.vfs.FileName)
	 */
	public VFile getFile(FileName fileName) {
		return RARFile.getInstance(this, (RARFileName) fileName);
	}

	/*
	 * ファイル名に対応するファイルインスタンスが既に存在するなら返す。
	 */
	RARFile getFileCache(RARFileName fileName) {
		return (RARFile) nameFileMap.get(fileName);
	}

	/*
	 * ファイル名に対応するファイルのキャッシュを登録する。
	 */
	void addFileCache(RARFileName name, RARFile file) {
		nameFileMap.put(name, file);
	}

	public InputStream getInputStream(final RARFile file) throws VFSException {
		try {
			final PipedInputStream in = new PipedInputStream();
			final PipedOutputStream out = new PipedOutputStream(in);

			new Thread(new Runnable() {
				public void run() {
					try {
						rarFile.extractFile(file.getFileHeader(), out);
					} catch (RarException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						try {out.close();} catch (Exception e) {}
					}
			    }
			}).start();
			
			return in;
		} catch (IOException e) {
			throw new VFSIOException(e);
		}
	}

	/**
	 * アーカイブ内のエントリ情報からファイルツリーを初期化する。
	 * 
	 * @param archive
	 */
	private void initFileTree(Archive archive) throws VFSException {
		List headersList = archive.getFileHeaders();
		for (int i=0; i<headersList.size(); i++) {
			FileHeader fileHeader = (FileHeader) headersList.get(i);
			headers.add(fileHeader);
			if(!fileHeader.isDirectory()) {
				totalSpace += fileHeader.getFullUnpackSize();
			}
		}
		
		for (int i=0; i<headers.size(); i++) {
			FileHeader entry = (FileHeader)headers.get(i);
			
			String path = entry.isUnicode() ? entry.getFileNameW() : entry.getFileNameString();
			path = path.replace('\\', '/');
			FileName name = rootName.resolveFileName(path,
					FileType.DIRECTORY);
			RARFile file = RARFile.getInstance(this, (RARFileName) name);
			file.setFileHeader(entry);

			FileType fileType;
			if (entry.isDirectory()) {
				fileType = FileType.DIRECTORY;
			} else {
				fileType = FileType.FILE;
			}
			
			FileAttribute attr = new DefaultFileAttribute(true, entry
					.getFullUnpackSize(), entry.getMTime(), fileType, totalSpace, 0);
			file.setInitialAttribute(attr);
			nameFileMap.put(name, file);

			initFileParent(file);
		}
	}

	private void initFileParent(RARFile file) throws VFSException {
		while (!file.isRoot()) {
			FileName name = file.getFileName().getParent();
			RARFile parentFile = (RARFile) nameFileMap.get(name);
			if (parentFile == null) {
				parentFile = RARFile.getInstance(this, (RARFileName) name);
			}

			if (parentFile.getInitialAttribute() == null) {
				FileAttribute attr = new DefaultFileAttribute(true, -1,
						new Date(), FileType.DIRECTORY, totalSpace, 0);
				parentFile.setInitialAttribute(attr);
				nameFileMap.put(name, parentFile);
			}

			parentFile.addChildFile(file);

			file = parentFile;
		}
	}

	public List getEntries() {
		return headers;
	}

	public boolean isLocal() {
		return getBaseFile().getFileSystem().isLocal();
	}

	/**
	 * このファイルシステムがシェルで解釈可能ならtrueを返す。
	 * @return
	 */
	public boolean isShellCompatible() {
		return false;
	}
	
	public long getTotalSpace() {
		return totalSpace;
	}

}
