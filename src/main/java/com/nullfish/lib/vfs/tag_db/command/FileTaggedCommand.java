package com.nullfish.lib.vfs.tag_db.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.nullfish.lib.vfs.VFile;

public class FileTaggedCommand extends AbstractTagDataBaseCommand {
	static final String PRE_QUERY = 
		"DELETE FROM file_tag "
		+ "WHERE file = ? AND tag = ?";
	
	private static final String QUERY = 
		"INSERT INTO file_tag "
		+ "(file, directory, tag) "
		+ "VALUES (?, ?, ?) ";

	private VFile file;
	private String tag;

	public FileTaggedCommand(VFile file, String tag) {
		this.file = file;
		this.tag = tag;
	}

	protected PreparedStatement getStatement(Connection conn)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(QUERY);
		stmt.setString(1, file.getSecurePath());
		stmt.setString(2, file.getParent() != null ? file.getParent().getSecurePath() : null);
		stmt.setString(3, tag);

		return stmt;
	}

	protected PreparedStatement getPreStatement(Connection conn)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(PRE_QUERY);
		stmt.setString(1, file.getSecurePath());
		stmt.setString(2, tag);

		return stmt;
	}
}
