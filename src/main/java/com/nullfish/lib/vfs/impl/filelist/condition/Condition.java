package com.nullfish.lib.vfs.impl.filelist.condition;

import org.jdom.Document;
import org.jdom.Element;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;

public interface Condition {
	public String getName();
	
	public boolean matches(VFile file, Manipulation manipulation) throws VFSException;
	
	public void init(Element node) throws VFSException;
	
	public Element toNode(Document doc);
}
