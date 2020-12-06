/*
 * 作成日: 2003/11/13
 *
 */
package com.nullfish.lib.vfs.manipulation.abst;


import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.manipulation.GetChildrenManipulation;
import com.nullfish.lib.vfs.tag_db.TagDataBase;


/**
 * @author shunji
 *
 */
public abstract class AbstractGetChildrenManipulation
	extends AbstractManipulation
	implements GetChildrenManipulation {

	VFile[] children;

	/**
	 * メッセージのパラメータ
	 */
	protected Object[] messageParam;
	
	/**
	 * 経過フォーマット
	 */
	private MessageFormat progressFormat;

	public AbstractGetChildrenManipulation(VFile file) {
		super(file);
		messageParam = new String[1];
		messageParam[0] = file.getSecurePath();
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.GetChildrenManipulation#getChildren()
	 */
	public VFile[] getChildren() {
		return children;
	}

	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#execute(com.sexyprogrammer.lib.vfs.VFSUser)
	 */
	public void doExecute() throws VFSException {
		children = doGetChildren(file);
		
		TagDataBase tagDataBase = file.getFileSystem().getVFS().getTagDataBase();
		if(tagDataBase != null) {
			try {
				Map fileTagMap = tagDataBase.findTagsInDirectory(file);
				for(int i=0; i<children.length; i++) {
					List tags = (List) fileTagMap.get(children[i].getSecurePath());
					tags = tags != null ? tags : new ArrayList();
					children[i].setTagCache(tags);
				}
			} catch (SQLException e) {
				throw new VFSIOException(e);
			}
			
		}
	}
	
	public abstract VFile[] doGetChildren(VFile file) throws VFSException;

	/**
	 * 作業経過メッセージを取得する。
	 * 
	 * @return
	 */
	public String getProgressMessage() {
		if(progressFormat == null) {
			progressFormat = new MessageFormat(progressMessages.getString(GetChildrenManipulation.NAME));
		}
		return progressFormat.format(messageParam);
	}
}
