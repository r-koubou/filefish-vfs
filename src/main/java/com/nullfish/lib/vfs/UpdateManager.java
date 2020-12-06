/*
 * Created on 2004/04/27
 *
 */
package com.nullfish.lib.vfs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * ファイル更新管理クラス。 シングルトンになっている。
 * 
 * @author shunji
 */
public class UpdateManager {

	/**
	 * ファイルとファイル更新リスナのセットのマップ
	 */
	private Map fileListenerMap = new HashMap();

	/**
	 * ファイルと子ファイル更新リスナのセットのマップ
	 */
	private Map childListenerMap = new HashMap();

	//	ファイル更新
	private static final int FILE_CHANGED = 0;

	//	ファイル生成
	private static final int FILE_CREATED = 1;

	//	ファイル削除
	private static final int FILE_DELETED = 2;

	/**
	 * シングルトンインスタンス
	 */
	private static UpdateManager instance = new UpdateManager();

	/**
	 * コンストラクタ
	 *  
	 */
	private UpdateManager() {
	}

	/**
	 * シングルトンインスタンスを取得する。
	 */
	public static UpdateManager getInstance() {
		return instance;
	}

	/**
	 * ファイルリスナを追加する。
	 * 
	 * @param file
	 *            対象ファイル
	 * @param listener
	 *            追加するリスナ
	 */
	public void addFileListener(VFile file, FileListener listener) {
		synchronized (fileListenerMap) {
			Set listenersSet = (Set) fileListenerMap.get(file);
			if (listenersSet == null) {
				listenersSet = new HashSet();
				fileListenerMap.put(file, listenersSet);
			}

			listenersSet.add(listener);
		}
	}

	/**
	 * ファイルリスナを削除する
	 * 
	 * @param file
	 *            対象ファイル
	 * @param listener
	 *            削除するファイルリスナ
	 */
	public void removeFileListener(VFile file, FileListener listener) {
		synchronized (fileListenerMap) {
			Set listenersSet = (Set) fileListenerMap.get(file);
			if (listenersSet == null) {
				return;
			}

			listenersSet.remove(listener);
			if (listenersSet.size() == 0) {
				fileListenerMap.remove(file);
			}
		}
	}

	/**
	 * 子ファイルのファイルリスナを追加する。
	 * 
	 * @param file
	 *            対象ファイル
	 * @param listener
	 *            追加するリスナ
	 */
	public void addChildFileListener(VFile file, FileListener listener) {
		synchronized (childListenerMap) {
			Set listenersSet = (Set) childListenerMap.get(file);
			if (listenersSet == null) {
				listenersSet = new HashSet();
				childListenerMap.put(file, listenersSet);
			}

			listenersSet.add(listener);
		}
	}

	/**
	 * 子ファイルのファイルリスナを外す。
	 * 
	 * @param file
	 *            対象ファイル
	 * @param listener
	 *            削除するファイルリスナ
	 */
	public void removeChildFileListener(VFile file, FileListener listener) {
		synchronized (childListenerMap) {
			Set listenersSet = (Set) childListenerMap.get(file);
			if (listenersSet == null) {
				return;
			}

			listenersSet.remove(listener);
			if (listenersSet.size() == 0) {
				childListenerMap.remove(file);
			}
		}
	}

	/**
	 * ファイルが更新された際に呼び出される
	 * 
	 * @param file
	 *            対象ファイル
	 */
	public void fileChanged(VFile file) {
		callListeners(file, FILE_CHANGED);
	}

	/**
	 * ファイルが削除された際に呼び出される。
	 * 
	 * @param file
	 *            対象ファイル
	 */
	public void fileDeleted(VFile file) {
		callListeners(file, FILE_DELETED);
	}

	/**
	 * ファイルが生成された際に呼び出される。
	 * 
	 * @param file
	 *            対象ファイル
	 */
	public void fileCreated(VFile file) {
//		do {
			callListeners(file, FILE_CREATED);
//			file = file.getParent();
//		} while(file != null);
	}

	/**
	 * ファイルリスナを削除する
	 * 
	 * @param file
	 *            対象ファイル
	 * @param listener
	 *            削除するファイルリスナ
	 */
	public void removeListener(FileListener listener) {
		removeListener(listener, fileListenerMap);
		removeListener(listener, childListenerMap);
	}

	private void removeListener(FileListener listener, Map listenerMap) {
		synchronized (listenerMap) {
			VFile[] files = new VFile[listenerMap.size()];
			files = (VFile[])listenerMap.keySet().toArray(files);
			for(int i=0; i<files.length; i++) {
				VFile file = files[i];
				Set listenersSet = (Set) listenerMap.get(file);
				if (listenersSet != null) {
					listenersSet.remove(listener);
					if (listenersSet.size() == 0) {
						listenerMap.remove(file);
					}
				}
			}
		}
	}

	/**
	 * リスナを呼ぶ
	 * 
	 * @param file
	 * @param updateType
	 */
	private synchronized void callListeners(VFile file, int updateType) {
		synchronized (fileListenerMap) {
			Set listenersSet = (Set) fileListenerMap.get(file);
			if (listenersSet != null) {
				Iterator ite = listenersSet.iterator();
				while (ite.hasNext()) {
					FileListener listener = (FileListener) ite.next();
					switch (updateType) {
					case FILE_CHANGED:
						listener.fileChanged(file);
						break;
					case FILE_CREATED:
						listener.fileCreated(file);
						break;
					case FILE_DELETED:
						listener.fileDeleted(file);
						break;
					}
				}
			}
		}

		if (!file.isRoot()) {
			VFile parent = file.getParent();
			synchronized (childListenerMap) {
				Set childListenerSet = (Set) childListenerMap.get(parent);
				if (childListenerSet != null) {
					Iterator ite = childListenerSet.iterator();
					while (ite.hasNext()) {
						FileListener listener = (FileListener) ite.next();
						switch (updateType) {
						case FILE_CHANGED:
							listener.fileChanged(file);
							break;
						case FILE_CREATED:
							listener.fileCreated(file);
							break;
						case FILE_DELETED:
							listener.fileDeleted(file);
							break;
						}
					}
				}
			}
		}
	}
}
