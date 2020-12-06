package com.nullfish.lib.vfs.impl.filelist.condition;

import java.util.Calendar;
import java.util.Date;

import org.jdom.Document;
import org.jdom.Element;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;

public class RecentTimestampCondition implements Condition {
	private String time;
	private Date date;
	
	public static final String NAME = "recent";
	
	public void init(Element node) throws VFSException {
		time = node.getText();
		
		date = parsePast(time);
	}

	public boolean matches(VFile file, Manipulation manipulation) throws VFSException {
		return date.before(file.getTimestamp(manipulation));
	}

	private Date parsePast(String str) {
		Calendar calendar = Calendar.getInstance();
		
		int number = Integer.parseInt(str.substring(0, str.length() - 1));
		if(str.endsWith("h")) {
			calendar.add(Calendar.HOUR, number * -1);
		} else if(str.endsWith("d")) {
			calendar.add(Calendar.DATE, number * -1);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else if(str.endsWith("w")) {
			calendar.add(Calendar.DATE, number * 7 * -1);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else if(str.endsWith("m")) {
			calendar.add(Calendar.MONTH, number * -1);
		} else if(str.endsWith("y")) {
			calendar.add(Calendar.YEAR, number * -1);
		}
		
		return calendar.getTime();
	}
	
	public Element toNode(Document doc) {
		Element rtn = new Element(NAME);
		rtn.setText(time);
		
		return rtn;
	}

	public String getName() {
		return NAME;
	}
}
