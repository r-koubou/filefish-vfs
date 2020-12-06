package com.nullfish.lib.vfs.impl.filelist.condition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.exception.VFSSystemException;

public class SearchCondition {
	private VFS vfs;
	
	private List sources = new ArrayList();
	
	private List conditions = new ArrayList();
	
	private int maxNumber = -1;
	
	public SearchCondition(VFS vfs) {
		this.vfs = vfs;
	}
	
	private static final ConditionFactory[] factories = {
		new ConditionFactory("name", NameCondition.class),
		new ConditionFactory("length", LengthCondition.class),
		new ConditionFactory("timestamp", TimestampCondition.class),
		new ConditionFactory("grep", GrepCondition.class),
		new ConditionFactory("tag", TagCondition.class),
	};
	
	private static Map nameFactoryMap = new HashMap();
	
	{
		for(int i=0; i<factories.length; i++) {
			nameFactoryMap.put(factories[i].getNodeName(), factories[i]);
		}
	}
	
	public void init(VFile file) throws VFSException {
		try {
			Document doc = new SAXBuilder().build(file.getInputStream());
			init(doc);
		} catch (JDOMException e) {
			throw new VFSSystemException(e);
		} catch (IOException e) {
			throw new VFSIOException(e);
		} catch (InstantiationException e) {
			throw new VFSSystemException(e);
		} catch (IllegalAccessException e) {
			throw new VFSSystemException(e);
		}
	}
	
	private void init(Document doc) throws JDOMException, VFSException, InstantiationException, IllegalAccessException {
		Element root = doc.getRootElement();
		
		List sourceNodes = XPath.selectNodes(doc, "/smartlist/sources/source");
		for(int i=0; i<sourceNodes.size(); i++) {
			FileSource source = new FileSource(vfs);
			source.init((Element)sourceNodes.get(i));
			
			sources.add(source);
		}
		
		maxNumber = Integer.parseInt(root.getAttributeValue("maxcount", "-1"));
		
		Element conditionsNode = (Element) XPath.selectSingleNode(doc, "/smartlist/conditions");
		List conditionNodes = conditionsNode.getChildren();
		for(int i=0; i<conditionNodes.size(); i++) {
			Element conditionNode = (Element)conditionNodes.get(i);
			
			Condition condition = ((ConditionFactory)nameFactoryMap.get(conditionNode.getName())).create(conditionNode);
			conditions.add(condition);
		}
	}
	
	public void save(VFile file) throws VFSException {
		try {
			Document doc = new Document();
			
			Element rootElement = new Element("smartlist");
			doc.setRootElement(rootElement);
			
			Element sourcesElement = new Element("sources");
			rootElement.addContent(sourcesElement);
			for(int i=0; i<sources.size(); i++) {
				FileSource source = (FileSource)sources.get(i);
				sourcesElement.addContent(source.toElement());
			}
	
			rootElement.setAttribute("maxcount", Integer.toString(maxNumber));
	
			Element conditionsElement = new Element("conditions");
			rootElement.addContent(conditionsElement);
			for(int i=0; i<conditions.size(); i++) {
				Condition condition = (Condition)conditions.get(i);
				conditionsElement.addContent(condition.toNode(doc));
			}
	
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			outputter.output(doc, file.getOutputStream());
		} catch (IOException e) {
			throw new VFSIOException(e);
		}
	}
	
	public List find(Manipulation manipulation) throws VFSException {
		List found = new ArrayList();
		
		for(int i=0; i<sources.size() && (maxNumber < 0  || found.size() < maxNumber); i++) {
			FileSource source = (FileSource)sources.get(i);
			
			while(source.hasNext() && (maxNumber < 0  || found.size() < maxNumber)) {
				VFile file = (VFile)source.next();
				if(matches(file, manipulation)) {
					found.add(file);
				}
			}
		}
		
		return found;
	}
	
	private boolean matches(VFile file, Manipulation manipulation) throws VFSException {
		for(int i=0; i<conditions.size(); i++) {
			if(!((Condition)conditions.get(i)).matches(file, manipulation)) {
				return false;
			}
		}
		
		return true;
	}

	public List getConditions() {
		return conditions;
	}

	public List getSources() {
		return sources;
	}

	public int getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}
}
