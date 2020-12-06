package com.nullfish.lib.vfs.tag_db.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.nullfish.lib.vfs.tag_db.TagDataBaseCommand;

public abstract class AbstractTagDataBaseCommand implements TagDataBaseCommand {
	public void execute(Connection conn) throws SQLException {		
		PreparedStatement stmt = null;
		
		try {
			stmt = getPreStatement(conn);
			if(stmt != null) {
				stmt.executeUpdate();
			}
		} finally {
			try {stmt.close();} catch (Exception e) {}
		}
		
		try {
			stmt = getStatement(conn);
			
			stmt.executeUpdate();
		} finally {
			stmt.close();
		}
	}
	
	protected abstract PreparedStatement getPreStatement(Connection conn) throws SQLException;
	
	protected abstract PreparedStatement getStatement(Connection conn) throws SQLException;
}
