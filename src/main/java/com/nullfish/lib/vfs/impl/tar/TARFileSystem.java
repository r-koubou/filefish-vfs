/*
 * 作成日: 2003/11/07
 *
 */
package com.nullfish.lib.vfs.impl.tar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

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
import com.nullfish.lib.vfs.impl.UNIXFilePermission;
import com.nullfish.lib.vfs.impl.antzip.TemporaryFileUtil;
import com.nullfish.lib.vfs.impl.local.LocalFile;

/**
 */
public class TARFileSystem extends AbstractFileSystem {

	private List entries = new ArrayList();

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

	private TarInputStream tis;
	
	/**
	 * ZIPファイルのアーカイブ内のエンコーディング
	 */
//	public static final String CONFIG_FILE_ENCODING = "zip_encoding";
	
	public TARFileSystem(VFS vfs, VFile baseFile) {
		super(vfs);
		parentFileSystem = baseFile.getFileSystem();
		rootName = new TARFileName(new String[0], baseFile.getFileName());
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
			VFile baseFile = getBaseFile();
			baseFile.getFileSystem().open(manipulation);

			if (!opened) {
				if (baseFile instanceof LocalFile) {
					usesLocalCopy = false;
				} else {
					usesLocalCopy = true;
					tempFile = TemporaryFileUtil.getInstance().getTemporaryFile(
							baseFile, manipulation).getFile();
				}

				recreateTarFile(manipulation);
				initFileTree(tis);
				opened = true;
			}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#close()
	 */
	public void doClose(Manipulation manipulation) throws VFSException {
		opened = false;
		entries.clear();
		nameFileMap.clear();
		try {
			if(tis != null) {
				tis.close();
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
		return TARFile.getInstance(this, (TARFileName) fileName);
	}

	/*
	 * ファイル名に対応するファイルインスタンスが既に存在するなら返す。
	 */
	TARFile getFileCache(TARFileName fileName) {
		return (TARFile) nameFileMap.get(fileName);
	}

	/*
	 * ファイル名に対応するファイルのキャッシュを登録する。
	 */
	void addFileCache(TARFileName name, TARFile file) {
		nameFileMap.put(name, file);
	}

	public InputStream getInputStream(TARFile file, Manipulation manipulation) throws VFSException {
		try {
			recreateTarFile(manipulation);
			TarEntry entry = file.getTarEntry();
			while(!entry.equals(tis.getNextEntry())) {
			}
			return tis;
		} catch (IOException e) {
			throw new VFSIOException(e);
		}
	}

    private void recreateTarFile(Manipulation manipultion) throws VFSException
    {
        if (tis != null)
        {
            try
            {
                tis.close();
            }
            catch (IOException e)
            {
                throw new VFSIOException(e);
            }
        }
        
        File baseFile = usesLocalCopy ? tempFile : ((LocalFile)getBaseFile()).getFile();
        if(baseFile.getName().endsWith(".tgz") || baseFile.getName().endsWith("tar.gz") ) {
            try {
				tis = new TarInputStream(new GZIPInputStream(new FileInputStream(baseFile)) );
			} catch (IOException e) {
				throw new VFSIOException(e);
			}
        } else if(baseFile.getName().endsWith(".bz2") || baseFile.getName().endsWith("tar.bz2") || baseFile.getName().endsWith("tbz2") ) {
            try {
            	InputStream is = new FileInputStream(baseFile);
                final int b1 = is.read();
                final int b2 = is.read();
                if (b1 != 'B' || b2 != 'Z')
                {
                    throw new VFSIOException();
                }
                tis = new TarInputStream(new CBZip2InputStream(is) );
			} catch (IOException e) {
				throw new VFSIOException(e);
			}
        }
    }
    
	/**
	 * アーカイブ内のエントリ情報からファイルツリーを初期化する。
	 * 
	 * @param zipFile
	 */
	private void initFileTree(TarInputStream tis) throws VFSException {
		TarEntry entry;
		
		try {
			while ((entry = tis.getNextEntry()) != null) {
				entries.add(entry);
				if(!entry.isDirectory()) {
					totalSpace += entry.getSize();
				}
				
				FileName name = rootName.resolveFileName(entry.getName(),
						FileType.DIRECTORY);
				TARFile file = TARFile.getInstance(this, (TARFileName) name);
				file.setTarEntry(entry);

				FileType fileType;
				if (entry.isDirectory()) {
					fileType = FileType.DIRECTORY;
				} else {
					fileType = FileType.FILE;
				}

				FileAttribute attr = new DefaultFileAttribute(true, entry
						.getSize(), entry.getModTime(), fileType, totalSpace, 0);
				file.setInitialAttribute(attr);
				nameFileMap.put(name, file);
				
				UNIXFilePermission permission = new UNIXFilePermission();
				permission.setGroup(entry.getGroupName());
				permission.setOwner(entry.getUserName());
				String permissionStr = Integer.toString(entry.getMode(), 8);
				permission.initFromString(permissionStr.substring(permissionStr.length() - 3));
				file.setPermissionCache(permission);
				
				initFileParent(file);
			}
		} catch (IOException e) {
			throw new VFSIOException(e);
		}
	}

	private void initFileParent(TARFile file) throws VFSException {
		while (!file.isRoot()) {
			FileName name = file.getFileName().getParent();
			TARFile parentFile = (TARFile) nameFileMap.get(name);
			if (parentFile == null) {
				parentFile = TARFile.getInstance(this, (TARFileName) name);
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
		return entries;
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
