package com.nullfish.lib.vfs.tag_db.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.nullfish.lib.vfs.VFile;

public class TagRemovedCommand extends AbstractTagDataBaseCommand {
	private static final String QUERY = 
		"DELETE FROM file_tag "
		+ "WHERE file = ? AND tag = ?";

	private VFile file;
	private String tag;

	public TagRemovedCommand(VFile file, String tag) {
		this.file = file;
		this.tag = tag;
	}

	protected PreparedStatement getStatement(Connection conn)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(QUERY);
		stmt.setString(1, file.getSecurePath());
		stmt.setString(2, tag);

		return stmt;
	}

	protected PreparedStatement getPreStatement(Connection conn)
			throws SQLException {
		return null;
	}
}
