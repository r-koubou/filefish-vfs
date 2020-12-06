package com.nullfish.lib.vfs.impl.root.manipulation;

import java.util.ArrayList;
import java.util.List;

import com.nullfish.lib.vfs.FileFactory;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetChildrenManipulation;


/**
 * @author shunji
 *
 */
public class RootFileGetChildrenManipulation
	extends AbstractGetChildrenManipulation {
	public RootFileGetChildrenManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractGetChildrenManipulation#doGetChildren(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public VFile[] doGetChildren(VFile file) throws VFSException {
		List roots = new ArrayList();
		VFS vfs = file.getFileSystem().getVFS();
		FileFactory[] factories = vfs.getFactories();
		
		for(int i=0; i<factories.length; i++) {
			VFile[] factoryRoots = factories[i].listRoots();
			for(int j=0; factoryRoots != null && j<factoryRoots.length; j++) {
				roots.add(factoryRoots[j]);
			}
		}
		
		VFile[] rtn = new VFile[roots.size()];
		return (VFile[])roots.toArray(rtn);
	}
}
