package com.nullfish.lib.vfs;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

public class ConnectionCache {
	private static Map dirConnectionMap = new HashMap();
	
	public static Connection getConnection(String dir) throws Exception {
		Connection rtn = (Connection) dirConnectionMap.get(dir);
		if(rtn == null) {
			VFile dbDir = VFS.getInstance().getFile(dir);
			if (!dbDir.exists()) {
				dbDir.createDirectory();
			}

			Class.forName("org.h2.Driver");
			String dirUrl = dbDir.getURI().toString() + "filefish_db";
			dirUrl = URLDecoder.decode(dirUrl, Charset.defaultCharset().name());

			rtn = DriverManager.getConnection("jdbc:h2:" + dirUrl, "sa", "");
			dirConnectionMap.put(dir, rtn);
		}
		
		return rtn;
	}

}
