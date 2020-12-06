package com.nullfish.lib.vfs.tag_db.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.nullfish.lib.vfs.VFile;

public class FileRemovedCommand extends AbstractTagDataBaseCommand {
	private static final String QUERY = 
		"DELETE FROM file_tag "
		+ "WHERE file = ?";

	private VFile file;

	public FileRemovedCommand(VFile file) {
		this.file = file;
	}

	protected PreparedStatement getStatement(Connection conn)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(QUERY);
		stmt.setString(1, file.getSecurePath());

		return stmt;
	}

	protected PreparedStatement getPreStatement(Connection conn)
			throws SQLException {
		return null;
	}
}
