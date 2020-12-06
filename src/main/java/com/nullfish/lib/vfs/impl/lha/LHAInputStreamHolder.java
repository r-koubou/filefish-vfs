/*
 * Created on 2003/12/08
 * 
 */
package com.nullfish.lib.vfs.impl.lha;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jp.gr.java_conf.dangan.util.lha.LhaHeader;

import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;


/**
 * @author shunji
 * 
 */
public class LHAInputStreamHolder {

	/**
	 * ファイルシステム
	 */
	LHAFileSystem fileSystem;

	/**
	 * 入力ストリーム
	 */
	UnclosableLhaInputStream lis;

	/**
	 * 現在のエントリ
	 */
	LhaHeader currentEntry;

	/**
	 * コンストラクタ
	 * 
	 * @param file
	 */
	public LHAInputStreamHolder(LHAFileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}

	public void close() {
		if (lis != null) {
			try {
				lis.closeReally();
			} catch (Exception e) {
			} finally {
				lis = null;
				currentEntry = null;
			}
		}
	}

	public InputStream getInputStream(LHAFile file) throws VFSException {
		try {
			LhaHeader entry = file.getLhaHeader();
			int index = getEntryIndex(entry);
			if (index < 0) {
				throw new IOException(entry.getPath());
			}

			int currentIndex =
				currentEntry != null ? getEntryIndex(currentEntry) : Integer.MAX_VALUE;

			if (currentIndex > index) {
				if(lis != null) {
					lis.closeReally();
					lis = null;
				}
			}

			if (lis == null) {
				lis =
					new UnclosableLhaInputStream(
						fileSystem.getBaseFile().getInputStream());
			}

			while(true) {
				currentEntry = lis.getNextEntry();
				if(currentEntry == null) {
					break;
				}
				
				if(currentEntry.getPath().equals(entry.getPath())) {
					return lis;
				}
			}
			throw new FileNotExistsException(file);
		} catch (IOException e) {
			throw new VFSIOException(e);
		}
	}

	private int getEntryIndex(LhaHeader entry) {
		String entryName = entry.getPath();
		List entries = fileSystem.getEntries();
		for (int i = 0; i < entries.size(); i++) {
			if (entryName.equals(((LhaHeader) entries.get(i)).getPath())) {
				return i;
			}
		}

		return -1;
	}
}
