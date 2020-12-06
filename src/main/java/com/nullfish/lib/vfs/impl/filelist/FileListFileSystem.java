package com.nullfish.lib.vfs.impl.filelist;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.UpdateManager;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;

public class FileListFileSystem extends AbstractFileListFileSystem {
	protected List files = new ArrayList();

	private boolean initialized = false;
	
	public FileListFileSystem(VFS vfs, VFile baseFile) {
		super(vfs, baseFile);
	}
	
	public void init() throws VFSException {
	}

	public void initFileTree(Manipulation manipulation) throws VFSException {
		VFile baseFile = getBaseFile();
		
		BufferedReader reader = null;
		files.clear();
		try {
			initialized = true;
			
			reader = new BufferedReader(new InputStreamReader(baseFile.getInputStream(manipulation)));
			String line = null;
			
			while(true) {
				line = reader.readLine();
				if(line == null) {
					return;
				}
				
				line = line.trim();
				
				if(line.length() > 0) {
					files.add( getVFS().getFile(line) );
				}
			}
			
		} catch (IOException e) {
			throw new VFSIOException(e);
		} finally {
			try {
				reader.close();
			} catch (Exception e) {}
		}
	}

	public void closeFileList() throws VFSException {
		files.clear();
	}
	
	public void save() throws VFSException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(getBaseFile().getOutputStream()));
			for(int i=0; i<files.size(); i++) {
				writer.write(((VFile) files.get(i)).getSecurePath());
				writer.write("\n");
			}
		} catch (IOException e) {
			throw new VFSIOException(e);
		} finally {
			try {
				writer.flush();
			} catch (Exception e) {}
			try {
				writer.close();
			} catch (Exception e) {}
		}
	}
	
	public List doGetFiles(Manipulation manipulation) throws VFSException {
		initFileTree(manipulation);
		
		return files;
	}

	public void addFile(VFile file) {
		if(!files.contains(file)) {
			files.add(file);
		}
		
		UpdateManager.getInstance().fileChanged(getFile(getRootName()));
	}

	public void removeFile(VFile file) {
		files.remove(file);
		UpdateManager.getInstance().fileChanged(getFile(getRootName()));
	}
}
