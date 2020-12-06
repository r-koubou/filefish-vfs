package com.nullfish.lib.vfs.impl.filelist.condition;

import org.jdom.Element;

import com.nullfish.lib.vfs.exception.VFSException;

public class ConditionFactory {
	private  String nodeName;
	
	private Class clazz;
	
	public ConditionFactory(String nodeName, Class clazz) {
		this.nodeName = nodeName;
		this.clazz = clazz;
	}
	
	public Condition create(Element node) throws InstantiationException, IllegalAccessException, VFSException {
		Condition rtn = (Condition)clazz.newInstance();
		rtn.init(node);
		
		return rtn;
	}
	
	public String getNodeName() {
		return nodeName;
	}
}
