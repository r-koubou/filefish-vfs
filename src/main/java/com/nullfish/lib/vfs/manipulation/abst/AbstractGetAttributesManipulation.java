/*
 * 作成日: 2003/11/13
 *
 */
package com.nullfish.lib.vfs.manipulation.abst;



import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.ManipulationStoppedException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.manipulation.GetAttributesManipulation;


/**
 * @author shunji
 *
 */
public abstract class AbstractGetAttributesManipulation
	extends AbstractManipulation
	implements GetAttributesManipulation {

	/**
	 * 取得結果属性
	 */
	FileAttribute rtn;

	public AbstractGetAttributesManipulation(VFile file) {
		super(file);
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.InitAttributesManipulation#getAttribute()
	 */
	public FileAttribute getAttribute() {
		return rtn;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#execute(com.sexyprogrammer.lib.vfs.VFSUser)
	 */
	public void doExecute() throws VFSException {
		if (isStopped()) {
			throw new ManipulationStoppedException(this);
		}
		
		rtn = file.getAttributeCache();
		if(rtn != null) {
			return;
		}

		rtn = doGetAttribute(file);
		
		file.setAttributeCache(rtn);
	}
	
	public abstract FileAttribute doGetAttribute(VFile file) throws VFSException;

	/**
	 * 作業経過メッセージを取得する。
	 * 
	 * @return
	 */
	public String getProgressMessage() {
		return "";
	}
}
