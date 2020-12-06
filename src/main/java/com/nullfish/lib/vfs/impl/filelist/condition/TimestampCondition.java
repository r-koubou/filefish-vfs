package com.nullfish.lib.vfs.impl.filelist.condition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jdom.Document;
import org.jdom.Element;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;

public class TimestampCondition implements Condition {
	private String operation;
	private String time;
	private Date date;

	public static final String NAME = "timestamp";
	
	public TimestampCondition() {
	}
	
	public TimestampCondition(String operation, String time) {
		this.operation = operation;
		this.time = time;
	}
	
	public void init(Element node) throws VFSException {
		time = node.getText();
		operation = node.getAttributeValue("operation", "after");
		
		if("recent".equals(operation)) {
			date = parsePast(time);
		} else {
			try {
				date = parseDate(time);
			} catch (ParseException e) {
				throw new VFSIOException(e);
			}
		}
	}

	public String getName() {
		return NAME;
	}

	public boolean matches(VFile file, Manipulation manipulation) throws VFSException {
		if("recent".equals(operation) || "after".equals(operation)) {
			return date.before(file.getTimestamp(manipulation));
		} else if("before".equals(operation)) {
			return date.after(file.getTimestamp(manipulation));
		}
		
		return false;
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
	
	private Date parseDate(String str) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		return format.parse(str);
	}
	
	public Element toNode(Document doc) {
		Element rtn = new Element(NAME);
		rtn.setText(time);
		rtn.setAttribute("operation", operation);
		
		return rtn;
	}

	public String getOperation() {
		return operation;
	}

	public String getTime() {
		return time;
	}
}
