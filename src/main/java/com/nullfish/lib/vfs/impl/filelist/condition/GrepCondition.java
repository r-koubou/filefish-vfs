package com.nullfish.lib.vfs.impl.filelist.condition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jdom.Document;
import org.jdom.Element;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;

public class GrepCondition implements Condition {
	private String pattern;
	private String lowerPattern;
	private String encode;
	private boolean caseSensitive;
	
	public static final String NAME = "grep";
	
	public GrepCondition() {}
	
	public GrepCondition(String pattern, String encode, boolean caseSensitive) {
		this.pattern = pattern;
		lowerPattern = pattern.toLowerCase();
		this.encode = encode;
		this.caseSensitive = caseSensitive;
	}
	
	public void init(Element node) {
		pattern = node.getText();
		lowerPattern = pattern.toLowerCase();
		encode = node.getAttributeValue("encode", System.getProperty("file.encoding"));
		caseSensitive = Boolean.parseBoolean(node.getAttributeValue("casesensitive", "false"));
	}
	
	public String getName() {
		return NAME;
	}

	public final boolean matches(VFile file, Manipulation manipulation) throws VFSException {
		BufferedReader reader = null;
		try {
			if (file.isDirectory(manipulation)) {
				return false;
			}

			reader = new BufferedReader(new InputStreamReader(file
					.getInputStream(manipulation), encode));

			String line = null;
			while ((line = reader.readLine()) != null) {
				if (!caseSensitive) {
					line = line.toLowerCase();
				}

				if (check(line)) {
					return true;
				}
			}

			return false;
		} catch (IOException e) {
			return false;
		} catch (VFSException e) {
			return false;
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
	}

	public boolean check(String line) {
		return line.indexOf(caseSensitive ? pattern : lowerPattern) != -1;
	}
	
	public Element toNode(Document doc) {
		Element rtn = new Element(NAME);
		rtn.setText(pattern);
		if(encode != null) {
			rtn.setAttribute("encode", encode);
		}
		rtn.setAttribute("casesensitive", Boolean.toString(caseSensitive));
		
		return rtn;
	}

	public String getPattern() {
		return pattern;
	}

	public String getEncode() {
		return encode;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}
}
