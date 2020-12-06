/*
 * Created on 2004/04/09
 *
 */
package com.nullfish.lib.vfs.manipulation.abst;

import java.util.ArrayList;
import java.util.List;

import com.nullfish.lib.vfs.UpdateManager;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.manipulation.SetPermissionManipulation;
import com.nullfish.lib.vfs.permission.FileAccess;
import com.nullfish.lib.vfs.permission.PermissionType;


/**
 * @author shunji
 *
 */
public abstract class AbstractSetPermissionManipulation extends AbstractManipulation
		implements
			SetPermissionManipulation {

	List values = new ArrayList();

	
	/**
	 * コンストラクタ
	 * @param file
	 */
	public AbstractSetPermissionManipulation(VFile file) {
		super(file);
	}
	
	/**
	 * 実際の処理
	 * @see com.nullfish.lib.vfs.manipulation.abst.AbstractManipulation#doExecute()
	 */
	public void doExecute() throws VFSException {
		file.clearPermission();
		doSetPermission(file, values);
	}

	/**
	 * 実際にパーミッションをセットする処理を記述する。
	 * @param file
	 * @param values
	 * @throws VFSException
	 */
	public abstract void doSetPermission(VFile file, List values) throws VFSException;
	
	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.SetPermissionManipulation#addPermission(com.sexyprogrammer.lib.vfs.permission.PermissionType, com.sexyprogrammer.lib.vfs.permission.FileAccess, boolean)
	 */
	public void addPermission(PermissionType type, FileAccess access,
			boolean value) {
		values.add(new AccessTypeValueSet(access, type, value));
		UpdateManager.getInstance().fileChanged(file);
	}
	
	protected static class AccessTypeValueSet {
		FileAccess access;
		
		PermissionType type;
		
		boolean value;
		
		public AccessTypeValueSet(FileAccess access, PermissionType type, boolean value) {
			this.access = access;
			this.type = type;
			this.value = value;
		}
		
		/**
		 * @return Returns the access.
		 */
		public FileAccess getAccess() {
			return access;
		}
		
		/**
		 * @return Returns the type.
		 */
		public PermissionType getType() {
			return type;
		}
		
		/**
		 * @return Returns the value.
		 */
		public boolean getValue() {
			return value;
		}
	}

	/**
	 * 作業経過メッセージを取得する。
	 * 
	 * @return
	 */
	public String getProgressMessage() {
		return "";
	}
}
