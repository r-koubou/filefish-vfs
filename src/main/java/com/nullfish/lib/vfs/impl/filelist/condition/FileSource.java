package com.nullfish.lib.vfs.impl.filelist.condition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;

public class FileSource implements Iterator {
	private VFS vfs;
	
	private VFile directory;
	
	private int generation = 0;
	
	private boolean inited = false;
	private List iteratorsList = new ArrayList();
	int index = 0;
	
	public static final String NAME = "source";
	public static final String ATTR_GENERATION = "generation";
	
	public FileSource(VFS vfs) {
		this.vfs = vfs;
	}
	
	public FileSource(VFS vfs, VFile directory, int generation) {
		this.vfs = vfs;
		this.directory = directory;
		this.generation = generation;
	}
	
	public boolean hasNext() {
		init();
		
		if(index >= iteratorsList.size()) {
			return false;
		}
		
		if(((Iterator)iteratorsList.get(index)).hasNext()) {
			return true;
		}
		
		for(int i=index; i<iteratorsList.size(); i++) {
			if(((Iterator)iteratorsList.get(i)).hasNext()) {
				return true;
			}
		}
		
		return false;
	}
	
	public Object next() {
		Iterator current = (Iterator)iteratorsList.get(index);
		if(current.hasNext()) {
			return current.next();
		}
		
		for(;index < iteratorsList.size() && !((Iterator)iteratorsList.get(index)).hasNext(); index++) {
		}
		
		if(index >= iteratorsList.size()) {
			return null;
		}
		
		return ((Iterator)iteratorsList.get(index)).next();
	}
	
	private void init() {
		if(inited) {
			return;
		}

		inited = true;
		
		VFile[] files = null;
		try {
			files = directory.getChildren();
		} catch (VFSException e) {
			return;
		}
		
		List filesList = new ArrayList();
		for(int i=0; files != null && i<files.length; i++) {
			filesList.add(files[i]);
		}
		
		iteratorsList.add(filesList.iterator());
		for(int i=0; generation != 1 && files != null && i<files.length; i++) {
			iteratorsList.add(new FileSource(vfs, files[i], generation - 1));
		}
	}
	
	public void init(Element node) throws VFSException {
		directory = vfs.getFile(node.getTextTrim());
		generation = Integer.parseInt(node.getAttributeValue(ATTR_GENERATION, "0"));
	}

	public Element toElement() throws VFSException {
		Element rtn = new Element(NAME);
		rtn.setAttribute(ATTR_GENERATION, Integer.toString(getGeneration()));
		rtn.setText(directory.getAbsolutePath());
		
		return rtn;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public VFile getDirectory() {
		return directory;
	}

	public int getGeneration() {
		return generation;
	}
}
