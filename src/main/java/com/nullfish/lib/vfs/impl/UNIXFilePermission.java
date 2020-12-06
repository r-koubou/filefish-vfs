package com.nullfish.lib.vfs.impl;

import org.apache.commons.net.ftp.FTPFile;

import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.permission.ClassFileAccess;
import com.nullfish.lib.vfs.permission.FileAccess;
import com.nullfish.lib.vfs.permission.PermissionType;

public class UNIXFilePermission  implements Permission {
	String owner;
	
	String group;
	
	boolean[][] values = new boolean[3][3];
	
	/*
	 * パーミッション属性名
	 */
	private static final PermissionType[] TYPES = {
		PermissionType.READABLE,
		PermissionType.WRITABLE,
		PermissionType.EXECUTABLE
	};
	
	/**
	 * アクセス種別
	 */
	private static final FileAccess[] ACCESS = {
		ClassFileAccess.OWNER,
		ClassFileAccess.GROUP,
		ClassFileAccess.ALL
	};
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Permission#getKeys()
	 */
	public PermissionType[] getTypes() {
		return TYPES;
	}

	public FileAccess[] getAccess() {
		return ACCESS;
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Permission#getValue(java.lang.String)
	 */
	public boolean hasPermission(PermissionType name, FileAccess access) {
		int accessIndex = getIndex(access, ACCESS);
		int typeIndex = getIndex(name, TYPES);
		
		if(accessIndex != -1 && typeIndex != -1) {
			return values[accessIndex][typeIndex];
		} else {
			return false;
		}
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Permission#setValue(java.lang.String, java.lang.Object)
	 */
	public void setPermission(PermissionType name, FileAccess access, boolean value) {
		int accessIndex = getIndex(access, ACCESS);
		int typeIndex = getIndex(name, TYPES);
		
		if(accessIndex != -1 && typeIndex != -1) {
			values[accessIndex][typeIndex] = value;
		}
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Permission#getOwner()
	 */
	public String getOwner() {
		return owner;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Permission#setOwner(java.lang.String)
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * グループを取得する
	 * @return
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * グループをセットする。
	 * @param owner
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	
	private int getIndex(Object value, Object[] array) {
		for(int i=0; i<array.length; i++) {
			if(value.equals(array[i])) {
				return i;
			}
		}
		
		return -1;
	}
	
	
	private static final int[] ORO_PERMISSIONS = {
		FTPFile.READ_PERMISSION,
		FTPFile.WRITE_PERMISSION,
		FTPFile.EXECUTE_PERMISSION
	};
	
	private static final int[] ORO_ACCESSES = {
		FTPFile.USER_ACCESS,
		FTPFile.GROUP_ACCESS,
		FTPFile.WORLD_ACCESS
	};
	
	/**
	 * CommensNETのFTPFileクラスから、パーミッションをコピーする
	 * @param oro
	 */
	public void converFromOroFTPFile(FTPFile oro) {
		owner = oro.getUser();
		group = oro.getGroup();
		for(int i=0; i<ACCESS.length; i++) {
			for(int j=0; j<TYPES.length; j++) {
				values[i][j] = oro.hasPermission(ORO_ACCESSES[i], ORO_ACCESSES[j]);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Permission#importPermission(com.sexyprogrammer.lib.vfs.Permission)
	 */
	public void importPermission(Permission permission) {
		for(int i=0; i<ACCESS.length; i++) {
			for(int j=0; j<TYPES.length; j++) {
				values[i][j] = permission.hasPermission(TYPES[j], ACCESS[i]);
			}
		}
	}
	
	/**
	 * パーミッションの文字列表現を取得する。
	 * @return
	 */
	public String getPermissionString() {
		return Integer.toString(getPermissionSum());
/*
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<ACCESS.length; i++) {
			buffer.append(values[i][0] ? "r" : "-");
			buffer.append(values[i][1] ? "w" : "-");
			buffer.append(values[i][2] ? "x" : "-");
		}
		
		return buffer.toString();
*/
	}
	
	/**
	 * 3桁の値（755とか644とか）に変換する。
	 * @return
	 */
	public int getPermissionSum() {
		return 
			(hasPermission(PermissionType.READABLE, ClassFileAccess.OWNER) ? 400 : 0) +
			(hasPermission(PermissionType.WRITABLE, ClassFileAccess.OWNER) ? 200 : 0) +
			(hasPermission(PermissionType.EXECUTABLE, ClassFileAccess.OWNER) ? 100 : 0) +
			(hasPermission(PermissionType.READABLE, ClassFileAccess.GROUP) ? 40 : 0) +
			(hasPermission(PermissionType.WRITABLE, ClassFileAccess.GROUP) ? 20 : 0) +
			(hasPermission(PermissionType.EXECUTABLE, ClassFileAccess.GROUP) ? 10 : 0) +
			(hasPermission(PermissionType.READABLE, ClassFileAccess.ALL) ? 4 : 0) +
			(hasPermission(PermissionType.WRITABLE, ClassFileAccess.ALL) ? 2 : 0) +
			(hasPermission(PermissionType.EXECUTABLE, ClassFileAccess.ALL) ? 1 : 0);
	}
	
	/**
	 * パーミッション文字列から初期化する。
	 *
	 */
	public void initFromString(String permissionStr) {
		int permission = Integer.parseInt(permissionStr, 16);
		setPermission(PermissionType.READABLE, ClassFileAccess.OWNER, (permission & 0x400) != 0);
		setPermission(PermissionType.WRITABLE, ClassFileAccess.OWNER, (permission & 0x200) != 0);
		setPermission(PermissionType.EXECUTABLE, ClassFileAccess.OWNER, (permission & 0x100) != 0);
		setPermission(PermissionType.READABLE, ClassFileAccess.GROUP, (permission & 0x40) != 0);
		setPermission(PermissionType.WRITABLE, ClassFileAccess.GROUP, (permission & 0x20) != 0);
		setPermission(PermissionType.EXECUTABLE, ClassFileAccess.GROUP, (permission & 0x10) != 0);
		setPermission(PermissionType.READABLE, ClassFileAccess.ALL, (permission & 0x4) != 0);
		setPermission(PermissionType.WRITABLE, ClassFileAccess.ALL, (permission & 0x2) != 0);
		setPermission(PermissionType.EXECUTABLE, ClassFileAccess.ALL, (permission & 0x1) != 0);
	}

	public boolean isEditable(PermissionType name, FileAccess access) {
		return true;
	}
}

