package com.nullfish.lib.vfs.tag_db.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.nullfish.lib.vfs.VFile;

public class FileMovedCommand extends AbstractTagDataBaseCommand {
	private static final String QUERY = 
		"UPDATE file_tag SET "
			+ "file = ?, "
			+ "directory = ? "
		+ "WHERE file = ? ";

	private VFile from;
	private VFile to;

	public FileMovedCommand(VFile from, VFile to) {
		this.from = from;
		this.to = to;
	}

	protected PreparedStatement getStatement(Connection conn)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(QUERY);
		stmt.setString(1, to.getSecurePath());
		stmt.setString(2, to.getParent() != null ? to.getParent().getSecurePath() : null);
		stmt.setString(3, from.getSecurePath());

		return stmt;
	}

	protected PreparedStatement getPreStatement(Connection conn)
			throws SQLException {
		return null;
	}
}
