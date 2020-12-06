package com.nullfish.lib.vfs.impl.filelist.condition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.tag_db.TagDataBase;

public class TagCondition implements Condition {
	private String pattern;

	public static final String NAME = "tag";
	
	private Set files;
	
	public TagCondition() {}
	
	public TagCondition(String pattern) {
		this.pattern = pattern;
	}
	
	public void init(Element node) {
		pattern = node.getText();
	}

	public String getName() {
		return NAME;
	}
	
	private void initFileSet(VFS vfs) {
		if(files != null) {
			return;
		}
		
		TagDataBase tagDataBase = vfs.getTagDataBase();
		if(tagDataBase == null) {
			files = new HashSet();
			return;
		}
		
		try {
			files = new HashSet();
			List filesList = tagDataBase.findFile(pattern, vfs);
			for(int i=0; i<filesList.size(); i++) {
				files.add(((VFile)filesList.get(i)).getSecurePath());
			}
		} catch (Exception e) {
			files = new HashSet();
			e.printStackTrace();
		}
	}

	public boolean matches(VFile file, Manipulation manipulation) throws VFSException {
		initFileSet(file.getFileSystem().getVFS());
		return (files.contains(file.getSecurePath()));
	}

	public Element toNode(Document doc) {
		Element rtn = new Element(NAME);
		rtn.setText(pattern);
		
		return rtn;
	}

	public String getPattern() {
		return pattern;
	}
}
