package com.nullfish.lib.vfs.tag_db;

import java.sql.Connection;
import java.sql.SQLException;

public interface TagDataBaseCommand {
	public void execute(Connection conn) throws SQLException;
}
