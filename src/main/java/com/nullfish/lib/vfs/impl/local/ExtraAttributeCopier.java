package com.nullfish.lib.vfs.impl.local;

import com.nullfish.lib.vfs.exception.VFSException;

public interface ExtraAttributeCopier {
	public void copyAttribute(LocalFile from, LocalFile to) throws VFSException;
}
