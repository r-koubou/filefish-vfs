package com.nullfish.lib.vfs.impl.filelist.condition;

import org.jdom.Document;
import org.jdom.Element;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;

public class LengthCondition implements Condition {
	private long length;
	
	private boolean above = true;
	
	public static final String NAME = "length";
	
	public LengthCondition() {}
	
	public LengthCondition(long length, boolean above) {
		this.length = length;
		this.above = above;
	}
	
	public void init(Element node) {
		length = Long.parseLong(node.getText());
		above = node.getAttributeValue("operation", "above").equals("above");
	}

	public String getName() {
		return NAME;
	}

	public boolean matches(VFile file, Manipulation manipulation) throws VFSException {
		long l = file.getLength(manipulation); 
		return above ? l >= length : l <= length;
	}

	public Element toNode(Document doc) {
		Element rtn = new Element(NAME);
		rtn.setText(Long.toString(length));
		rtn.setAttribute("operation", above ? "above" : "less");
		
		return rtn;
	}

	public long getLength() {
		return length;
	}

	public boolean isAbove() {
		return above;
	}
}
