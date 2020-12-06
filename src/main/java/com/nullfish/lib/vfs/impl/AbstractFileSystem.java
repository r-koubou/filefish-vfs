/*
 * Created on 2003/12/08
 *
 */
package com.nullfish.lib.vfs.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;

/**
 * @author shunji
 * 
 */
public abstract class AbstractFileSystem implements FileSystem {

	/**
	 * 親ファイルシステム
	 */
	protected FileSystem parentFileSystem;

	/**
	 * 子ファイルシステム
	 */
	protected Set childrenFileSystems = new HashSet();

	/**
	 * ルートのファイル名
	 */
	protected FileName rootName;

	/**
	 * 大元となる仮想ファイルシステム
	 */
	private VFS vfs;

	/**
	 * ファイルシステムの使用者のセット
	 */
	private Set userSet;

	/**
	 * ファイルシステムのオープン状態フラグ
	 * 
	 */
	private boolean opened = false;
	
	/**
	 * コンストラクタ
	 * 
	 * @param vfs
	 */
	public AbstractFileSystem(VFS vfs) {
		this.vfs = vfs;
	}

	/**
	 * 仮想ファイルシステムを取得する。
	 */
	public VFS getVFS() {
		return vfs;
	}

	/**
	 * ファイルシステムを開く。
	 * 
	 * @see com.nullfish.lib.vfs.FileSystem#open()
	 */
	public final void open(Manipulation manipulation) throws VFSException {
		opened = true;
		
		if (parentFileSystem != null) {
			parentFileSystem.open(manipulation);

			// 親にこのファイルシステムを登録する。
			((AbstractFileSystem) parentFileSystem).addChild(this);
		}

		doOpen(manipulation);
	}

	/**
	 * 実際にファイルシステムを開く処理を記述する。
	 * 
	 * @throws VFSException
	 */
	public abstract void doOpen(Manipulation manipulation) throws VFSException;

	/**
	 * ファイルシステムを閉じる。
	 * 
	 * @see com.nullfish.lib.vfs.FileSystem#close()
	 */
	public final void close(Manipulation manipulation) throws VFSException {
		if (parentFileSystem != null) {
			// 親からこのファイルシステムを削除する。
			((AbstractFileSystem) parentFileSystem).removeChild(this);
		}

		// 子を閉じる。
		List childList = new ArrayList(childrenFileSystems);
		for(int i=0; i<childList.size(); i++) {
			FileSystem child = (FileSystem) childList.get(i);
			child.close(manipulation);
		}

		doClose(manipulation);

		opened = false;

		getVFS().removeFileSystem(this);
	}

	/**
	 * 実際にファイルシステムを閉じる処理を記述する。
	 * 
	 * @throws VFSException
	 */
	public abstract void doClose(Manipulation manipulation) throws VFSException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#isOpened()
	 */
	public boolean isOpened() {
		return opened;
	}

	/**
	 * 子ファイルシステムを追加する。
	 * 
	 * @param child	子ファイルシステム
	 */
	private synchronized void addChild(FileSystem child) {
		childrenFileSystems.add(child);
	}
	
	/**
	 * 子ファイルシステムを追加する。
	 * 
	 * @param child	子ファイルシステム
	 */
	private synchronized void removeChild(FileSystem child) {
		childrenFileSystems.remove(child);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.FileSystem#createFileSystem()
	 */
	public void createFileSystem() throws VFSException {
	}

	/**
	 * ファイルシステムのマウントポイントを取得する。 もしも親の存在しないファイルシステムならnullを返す。
	 * 
	 * @see com.nullfish.lib.vfs.FileSystem#getMountPoint()
	 */
	public VFile getMountPoint() {
		if (parentFileSystem != null && rootName.getBaseFileName() != null) {
			return parentFileSystem.getFile(rootName.getBaseFileName());
		}

		return null;
	}

	/**
	 * ルートファイル名を取得する。
	 * 
	 * @see com.nullfish.lib.vfs.FileSystem#getRootName()
	 */
	public FileName getRootName() {
		return rootName;
	}

	/**
	 * 親ファイルシステムを取得する。
	 * 
	 * @return
	 */
	public FileSystem getParentFileSystem() {
		return parentFileSystem;
	}

	/**
	 * このファイルシステムがパラメータのファイルシステムの祖先なら trueを返す。
	 */
	public boolean isAncestor(FileSystem otherFileSystem) {
		FileSystem fileSystem = null;
		while (true) {
			VFile mountPoint = otherFileSystem.getMountPoint();
			if (mountPoint == null) {
				return false;
			}

			fileSystem = mountPoint.getFileSystem();

			if (fileSystem.equals(this)) {
				return true;
			}
		}
	}

	/**
	 * このファイルシステムに使用者を登録する。
	 * 
	 * @param user
	 */
	public synchronized void registerUser(Object user) {
		if (userSet == null) {
			userSet = new HashSet();
		}

		userSet.add(user);
	}

	/**
	 * このファイルシステムの使用者を削除する。
	 * 
	 * @param user
	 */
	public synchronized void removeUser(Object user) {
		if (userSet != null) {
			userSet.remove(user);
		}
	}

	/**
	 * 使用中ならtrueを返す。
	 * 
	 * @return
	 */
	public synchronized boolean isInUse() {
		if (userSet != null && userSet.size() > 0) {
			return true;
		}

		Iterator ite = childrenFileSystems.iterator();
		while (ite.hasNext()) {
			FileSystem child = (FileSystem) ite.next();
			if (child.isInUse()) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Object#equalsのオーバーライド。
	 * ルート名が等しいかで判断する。
	 */
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(obj.getClass() != getClass()) {
			return false;
		}
		
		AbstractFileSystem other = (AbstractFileSystem)obj;
		return rootName.equals(other.rootName);
	}
}
