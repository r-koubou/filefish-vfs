/*
 * 作成日: 2003/10/17
 *
 */
package com.nullfish.lib.vfs.impl.smbwin;


import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.impl.local.LocalFileSystem;
import com.nullfish.lib.vfs.permission.ClassFileAccess;
import com.nullfish.lib.vfs.permission.FileAccess;
import com.nullfish.lib.vfs.permission.PermissionType;


/**
 * 
 * @author Shunji Yamaura
 */
public class SMBPermission implements Permission {

	SMBFile file;

	
	public SMBPermission(SMBFile file) {
		this.file = file;
	}
	
	/*
	 * パーミッション属性名
	 */
	private static final PermissionType[] keys = {
		PermissionType.READABLE,
		PermissionType.WRITABLE
	};
	
	/*
	 * Java6以上でのパーミッション属性名
	 */
	private static final PermissionType[] java6Keys = {
		PermissionType.READABLE,
		PermissionType.WRITABLE,
		PermissionType.EXECUTABLE
	};
	
	/**
	 * アクセス種別
	 */
	private static final FileAccess[] access = {
		ClassFileAccess.ALL
	};
	
	/**
	 * 読み込み可能
	 */
	private boolean readable;
	
	private boolean readableInited = false;
	
	/**
	 * 書き込み可能
	 */
	private boolean writable;
	  
	private boolean writableInited = false;
	
	/**
	 * 実行可能
	 */
	private boolean executable;
	  
	private boolean executableInited = false;
	
	/**
	 * パーミッション属性名を取得する。
	 * @see com.nullfish.lib.vfs.Permission#getKeys()
	 */
	public PermissionType[] getTypes() {
		return LocalFileSystem.aboveJava6() ? java6Keys : keys;
	}

	/**
	 * 属性値を取得する。
	 * @see com.nullfish.lib.vfs.Permission#getValue()
	 */
	public boolean hasPermission(PermissionType name, FileAccess access) {
		if(PermissionType.READABLE.equals(name)) {
			return isReadable();
		} else if(PermissionType.WRITABLE.equals(name)) {
			return isWritable();
		} else if(PermissionType.EXECUTABLE.equals(name)) {
			return isExecutable(); 
		}
		
		return false;
	}

	/**
	 * 属性値をセットする。
	 * @see com.nullfish.lib.vfs.Permission#setValue(java.lang.String, java.lang.Object)
	 */
	public void setPermission(PermissionType name, FileAccess access, boolean value) {
		if(PermissionType.READABLE.equals(name)) {
			readable = value;
			readableInited = true;
		} else if(PermissionType.WRITABLE.equals(name)) {
			writable = value;
			writableInited = true;
		}
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Permission#getOwner()
	 */
	public String getOwner() {
		return null;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Permission#setOwner(java.lang.String)
	 */
	public void setOwner(String owner) {
	}

	/**
	 * グループを取得する
	 * @return
	 */
	public String getGroup() {
		return null;
	}
	
	/**
	 * グループをセットする。
	 * @param owner
	 */
	public void setGroup(String group) {
	}

	/**
	 * アクセス種別を取得する。
	 * @see com.nullfish.lib.vfs.Permission#getAccess()
	 */
	public FileAccess[] getAccess() {
		return access;
	}

	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Permission#importPermission(com.sexyprogrammer.lib.vfs.Permission)
	 */
	public void importPermission(Permission permission) {
		this.readable = permission.hasPermission(PermissionType.READABLE, ClassFileAccess.ALL);
		this.writable = permission.hasPermission(PermissionType.WRITABLE, ClassFileAccess.ALL);
		this.executable = permission.hasPermission(PermissionType.EXECUTABLE,ClassFileAccess.ALL);
	}
	
	/**
	 * パーミッションの文字列表現を取得する。
	 * @return
	 */
	public String getPermissionString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(readable ? "r" : "-");
		buffer.append(writable ? "w" : "-");
		if(LocalFileSystem.aboveJava6()) {
			buffer.append(isExecutable() ? "e" : "-");
		}
		
		return buffer.toString();
	}
	
	private boolean isWritable() {
		if(!writableInited) {
			writable = file.getFile().canWrite();
			writableInited = true;
		}
		
		return writable;
	}
	
	private boolean isReadable() {
		if(!readableInited) {
			readable = file.getFile().canRead();
			readableInited = true;
		}
		
		return readable;
	}

	private boolean isExecutable() {
		if(!executableInited) {
			executable = file.getFile().canExecute();
			executableInited = true;
		}
		
		return executable;
	}
	
	/**
	 * パーミッション文字列から初期化する。
	 *
	 */
	public void initFromString(String permissionStr) {
		if(LocalFileSystem.aboveJava6()) {
			readable = permissionStr.charAt(0) == 'r';
			writable = permissionStr.charAt(1) == 'w';
			executable = permissionStr.charAt(2) == 'e';
		}
	}
	
	public boolean isEditable(PermissionType name, FileAccess access) {
		return !LocalFileSystem.aboveJava6();
	}
}
