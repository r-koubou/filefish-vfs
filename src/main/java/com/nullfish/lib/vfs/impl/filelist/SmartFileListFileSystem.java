package com.nullfish.lib.vfs.impl.filelist;

import java.util.List;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.filelist.condition.SearchCondition;

public class SmartFileListFileSystem extends AbstractFileListFileSystem {
	private SearchCondition condition;

	public static final String EXTENSION = "slist";
	
	public SmartFileListFileSystem(VFS vfs, VFile baseFile) {
		super(vfs, baseFile);
		
	}

	public void closeFileList() throws VFSException {
		
	}

	public void init() throws VFSException {
		condition = new SearchCondition(getVFS());
		condition.init(getBaseFile());
	}

	public List doGetFiles(Manipulation manipulation) throws VFSException {
		return condition.find(manipulation);
	}

	public boolean isLocal() {
		return false;
	}

	
}
