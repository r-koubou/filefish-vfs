package com.nullfish.lib.vfs.impl.filelist.condition;

import org.jdom.Document;
import org.jdom.Element;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;

public class NameCondition implements Condition {
	private String pattern;

	private boolean caseSensitive = false;
	
	/**
	 * 大文字小文字を区別する
	 */
	public static final String ATTR_CASE_SENSITIVE = "casesensitive";
	
	public static final String NAME = "name";
	
	public NameCondition() {}
	
	public NameCondition(String pattern, boolean caseSensitive) {
		this.pattern = pattern;
		this.caseSensitive = caseSensitive;
	}
	
	public void init(Element node) {
		pattern = node.getText();
		
		caseSensitive = Boolean.parseBoolean(node.getAttributeValue(
				ATTR_CASE_SENSITIVE, "false"));
	}

	public String getName() {
		return NAME;
	}

	public boolean matches(VFile file, Manipulation manipulation) throws VFSException {
		if(caseSensitive) {
			return file.getName().contains(pattern);
		} else {
			return file.getName().toLowerCase().contains(pattern.toLowerCase());
		}
	}

	public Element toNode(Document doc) {
		Element rtn = new Element(NAME);
		rtn.setText(pattern);
		rtn.setAttribute("casesensitive", Boolean.toString(caseSensitive));
		
		return rtn;
	}

	public String getPattern() {
		return pattern;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}
}
