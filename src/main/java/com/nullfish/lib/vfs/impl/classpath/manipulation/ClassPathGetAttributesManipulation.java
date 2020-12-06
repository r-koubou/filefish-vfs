/*
 * 作成日: 2003/11/18
 *
 */
package com.nullfish.lib.vfs.impl.classpath.manipulation;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.WrongPathException;
import com.nullfish.lib.vfs.impl.DefaultFileAttribute;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetAttributesManipulation;

/**
 * @author shunji
 * 
 */
public class ClassPathGetAttributesManipulation
		extends
			AbstractGetAttributesManipulation {

	public ClassPathGetAttributesManipulation(VFile file) {
		super(file);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.manipulation.abst.AbstractInitAttributesManipulation#doGetAttribute(com.sexyprogrammer.lib.vfs.VFile)
	 */
	public FileAttribute doGetAttribute(VFile file) throws VFSException {
		try {
			URL url = getClass().getResource(file.getURI().getPath());
			return new DefaultFileAttribute(url != null, -1, new Date(), FileType.FILE);
		} catch (URISyntaxException e) {
			throw new WrongPathException(file.getAbsolutePath());
		}
	}

}
