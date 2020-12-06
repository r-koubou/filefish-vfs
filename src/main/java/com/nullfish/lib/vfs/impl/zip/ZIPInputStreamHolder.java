/*
 * Created on 2003/12/08
 * 
 */
package com.nullfish.lib.vfs.impl.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;

import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;


/**
 * @author shunji
 * 
 */
public class ZIPInputStreamHolder {

	/**
	 * ファイルシステム
	 */
	ZIPFileSystem fileSystem;

	/**
	 * 入力ストリーム
	 */
	UnclosableZipInputStream zis;

	/**
	 * 現在のエントリ
	 */
	ZipEntry currentEntry;

	/**
	 * コンストラクタ
	 * 
	 * @param fileSystem ファイルシステム
	 */
	public ZIPInputStreamHolder(ZIPFileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}

	public void close() {
		if (zis != null) {
			try {
				zis.closeReally();
			} catch (Exception e) {
			} finally {
				zis = null;
				currentEntry = null;
			}
		}
	}

	public InputStream getInputStream(ZIPFile file) throws VFSException {
		try {
			ZipEntry entry = file.getZipEntry();
			int index = getEntryIndex(entry);
			if (index < 0) {
				throw new IOException(entry.getName());
			}

			int currentIndex =
				currentEntry != null ? getEntryIndex(currentEntry) : Integer.MAX_VALUE;

			if (currentIndex > index) {
				if(zis != null) {
					zis.closeReally();
					zis = null;
				}
			}

			if (zis == null) {
				zis =
					new UnclosableZipInputStream(
						fileSystem.getBaseFile().getInputStream());
			}

			while(true) {
				currentEntry = zis.getNextEntry();
				if(currentEntry == null) {
					break;
				}
				
				if(currentEntry.getName().equals(entry.getName())) {
					return zis;
				}
			}
			throw new FileNotExistsException(file);
		} catch (IOException e) {
			throw new VFSIOException(e);
		}
	}

	private int getEntryIndex(ZipEntry entry) {
		String entryName = entry.getName();
		List entries = fileSystem.getEntries();
		for (int i = 0; i < entries.size(); i++) {
			if (entryName.equals(((ZipEntry) entries.get(i)).getName())) {
				return i;
			}
		}

		return -1;
	}
}
