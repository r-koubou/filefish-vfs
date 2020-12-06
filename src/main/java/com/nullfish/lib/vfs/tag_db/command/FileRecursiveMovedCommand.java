package com.nullfish.lib.vfs.tag_db.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

import com.nullfish.lib.vfs.VFile;

public class FileRecursiveMovedCommand extends AbstractTagDataBaseCommand {
	private static final String QUERY = 
		"UPDATE file_tag SET "
			+ "file = REGEXP_REPLACE(file, ?, ?), "
			+ "directory = REGEXP_REPLACE(directory, ?, ?) "
		+ "WHERE directory LIKE ? ";

	private VFile from;
	private VFile to;

	public FileRecursiveMovedCommand(VFile from, VFile to) {
		this.from = from;
		this.to = to;
	}

	protected PreparedStatement getStatement(Connection conn)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(QUERY);
		int i=1;
		String fromQuote = "^" + Pattern.quote(from.getSecurePath());
		
		stmt.setString(i++, fromQuote);
		stmt.setString(i++, to.getSecurePath());
		stmt.setString(i++, fromQuote);
		stmt.setString(i++, to.getSecurePath());
		stmt.setString(i++, from.getSecurePath() + "%");
		
		return stmt;
	}

	protected PreparedStatement getPreStatement(Connection conn)
			throws SQLException {
		return null;
	}
}
