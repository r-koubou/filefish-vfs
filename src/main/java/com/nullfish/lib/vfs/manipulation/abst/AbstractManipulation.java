/*
 * 作成日: 2003/11/13
 *
 */
package com.nullfish.lib.vfs.manipulation.abst;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.ManipulationEvent;
import com.nullfish.lib.vfs.ManipulationListener;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.ManipulationStoppedException;
import com.nullfish.lib.vfs.exception.VFSException;


/**
 * ファイル操作クラスの抽象実装。
 * 継承クラスではdoExecuteを実装して、処理を記述する。
 * 前処理が必要な場合はdoPrepareをオーバーライドする。
 * 操作の経過が2値より多く存在する場合、getProgressで始まるメソッドをオーバーライドする。
 * もしも操作が子操作を持つ場合、getCurrentManipulationをオーバーライドする。
 * 
 * @author shunji
 */
public abstract class AbstractManipulation implements Manipulation {
	/**
	 * 操作ID
	 */
	private int manipulationId = manipulationIdSeed++;

	/**
	 * 操作IDの基準値
	 */
	private static int manipulationIdSeed = 0;

	/**
	 * 経過メッセージのリソースバンドル
	 */
	protected final static ResourceBundle progressMessages =
		ResourceBundle.getBundle("progress_message");

	protected VFile file;

	/**
	 * 作業中止フラグ
	 */
	private boolean stopped = false;

	/**
	 * ステータスのmutex
	 */
	private Object statusObj = new Object();
	
	/**
	 * 親操作
	 */
	private Manipulation parentManipulation;

	/**
	 * 捜査終了フラグ
	 */
	private boolean finished = false;

	/**
	 * 前処理の終了フラグ
	 */
	private boolean prepared = false;


	/**
	 * ファイル操作リスナのリスト
	 */
	private List listeners;

	/**
	 * 現在実行中の作業
	 */
	private Manipulation currentManipulation;
	
	/**
	 * 実行スレッド
	 */
	private Thread workThread;
	
	public AbstractManipulation(VFile file) {
		this.file = file;
	}

	private static final int STARTED = 0;
	private static final int PREPARE_STARTED = 1;
	private static final int PREPARE_FINISHED = 2;
	private static final int MANIPULATION_STARTED = 3;
	private static final int MANIPULATION_FINISHED = 4;
	private static final int FINISHED = 5;
	private static final int MANIPULATION_STOPPED = 6;
	private static final int MANIPULATION_STOPPING = 7;
	
	/**
	 * 操作を非同期実行する。
	 * 
	 * @throws VFSException
	 */
	public void start() throws VFSException {
		try {
			callListeners(STARTED);
			prepare();
			execute();
		} finally {
			callListeners(FINISHED);
		}
	}
	
	/**
	 * 操作を非同期実行する。
	 * 
	 * @throws VFSException
	 */
	public void startAsync() throws VFSException {
		new ExecuterThread(this).start();
	}
	
	/**
	 * 操作の前処理を行う。
	 */
	public final void prepare() throws VFSException {
		workThread = Thread.currentThread();

		try {
			if(parentManipulation != null) {
				parentManipulation.setCurrentManipulation(this);
			}
			
			callListeners(PREPARE_STARTED);
			
			if(isStopped()) {
				throw new ManipulationStoppedException(this);
			}
			
//			if(file != null && !file.getFileSystem().isOpened()) {
			if(file != null) {
				file.getFileSystem().open(this);
			}
			
			doPrepare();
		} catch (VFSException e) {
			callListeners(MANIPULATION_STOPPED, e);
			throw e;
		} finally {
			setPrepared(true);
			callListeners(PREPARE_FINISHED);

			if(parentManipulation != null) {
				parentManipulation.setCurrentManipulation(null);
			}
		}
	}
	
	/**
	 * 操作の前処理を行う。
	 * 各操作クラスで前処理が必要な場合、このメソッドをオーバーライドする。
	 */
	public void doPrepare() throws VFSException {
	}
	
	/**
	 * 操作を実行する。
	 */
	public final void execute() throws VFSException {
		try {
			if(parentManipulation != null) {
				parentManipulation.setCurrentManipulation(this);
			}
			
			callListeners(MANIPULATION_STARTED);
			
			if (isStopped()) {
				throw new ManipulationStoppedException(this);
			}

			doExecute();
		} catch (VFSException e) {
			callListeners(MANIPULATION_STOPPED, e);
			throw e;
		} finally {
			setFinished(true);
			callListeners(MANIPULATION_FINISHED);
			
			if(parentManipulation != null) {
				parentManipulation.setCurrentManipulation(null);
			}
		}
	}
	
	/**
	 * 操作を実行する。
	 * 各操作クラスでこのメソッドを実装すること。
	 */
	public abstract void doExecute() throws VFSException;
	
	/**
	 * リスナにイベントを通知する。
	 * @param reason
	 */
	protected void callListeners(int reason) {
		if(listeners == null || listeners.size() == 0) {
			return;
		}
		callListeners(reason, new ManipulationEvent(this));
	}
	
	/**
	 * リスナにイベントを通知する。
	 * @param reason
	 */
	protected void callListeners(int reason, VFSException ex) {
		ManipulationEvent event = new ManipulationEvent(this);
		event.setException(ex);
		callListeners(reason, event);
	}
	
	/**
	 * リスナにイベントを通知する。
	 * @param reason
	 */
	private void callListeners(int reason, ManipulationEvent e) {
		if(listeners != null) {
			ManipulationListener listener;
			for(int i=0; i<listeners.size(); i++) {
				listener = (ManipulationListener)listeners.get(i);
				switch(reason) {
					case STARTED : listener.started(e);break;
					case PREPARE_STARTED : listener.preparationStarted(e);break;
					case PREPARE_FINISHED : listener.preparationFinished(e);break;
					case MANIPULATION_STARTED : listener.manipulationStarted(e);break;
					case MANIPULATION_FINISHED : listener.manipulationFinished(e);break;
					case MANIPULATION_STOPPED : listener.manipulationStopped(e);break;
					case MANIPULATION_STOPPING : listener.manipulationStopping(e);break;
					case FINISHED : listener.finished(e);break;
				}
			}
		}
	}
	
	/**
	 * 作業を中止する。
	 *
	 */
	public void stop() {
		synchronized (statusObj) {
			stopped = true;

			doStop();
			
			Manipulation man = getCurrentManipulation();
			if(man != null && man != this) {
				man.stop();
			}
			callListeners(MANIPULATION_STOPPING);
		}
	}
	
	/**
	 * 終了操作の実装。
	 * もしも操作固有の終了操作がある場合、これを継承して実装する。
	 *
	 */
	protected void doStop() {}

	/**
	 * 作業が中止されているならtrueを返す。
	 * @return
	 */
	public boolean isStopped() {
		synchronized (statusObj) {
			if (stopped) {
				return true;
			}
	
			if (parentManipulation != null) {
				return parentManipulation.isStopped();
			}
	
			return false;
		}
	}

	/**
	 * 終了
	 */
	public void setFinished(boolean finished) {
		synchronized (statusObj) {
			this.finished = finished;
		}
	}

	/**
	 * 作業が終了しているならtrueを返す。
	 * @return
	 */
	public boolean isFinished() {
		synchronized (statusObj) {
			return finished;
		}
	}


	/**
	 * 親となる操作をセットする。
	 * @param parent
	 */
	public void setParentManipulation(Manipulation parent) {
		parentManipulation = parent;
	}

	/**
	 * 親となる操作を取得する。
	 * @return
	 */
	public Manipulation getParentManipulation() {
		return parentManipulation;
	}


	/**
	 * ルートとなる操作を取得する。
	 * @return
	 */
	public Manipulation getRootManipulation() {
		Manipulation m = this;
		while(true) {
			if(m.getParentManipulation() == null) {
				return m;	
			}
			
			m = m.getParentManipulation();
		}
	}

	/**
	 * 前処理完了をセットする。
	 * @param prepared
	 */
	public void setPrepared(boolean prepared) {
		this.prepared = prepared;
	}

	/**
	 * 前処理が完了しているならtrueを返す。
	 * @return
	 */
	public boolean isPrepared() {
		return prepared;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#getProgress()
	 */
	public long getProgress() {
		if(!isPrepared()) {
			return PROGRESS_INDETERMINED;
		}
		
		return isFinished() ? 0 : 1;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#getProgressMin()
	 */
	public long getProgressMin() {
		return 0;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#getProgressMax()
	 */
	public long getProgressMax() {
		return 1;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#getCurrentManipulation()
	 */
	public synchronized final Manipulation getCurrentManipulation() {
		if(currentManipulation == null
				|| currentManipulation == this) {
			return this;
		} else {
			return currentManipulation.getCurrentManipulation();
		}
	}

	/**
	 * 実行中の操作をセットする。
	 * @param currentManipulation
	 */
	public synchronized final void setCurrentManipulation(Manipulation currentManipulation) {
		this.currentManipulation = currentManipulation;
	}
	
	class ExecuterThread extends Thread {

		AbstractManipulation manipulation;

		
		public ExecuterThread(AbstractManipulation manipulation) {
			this.manipulation = manipulation;
		}
		
		public void run() {
			try {
				manipulation.start();
			} catch (ManipulationStoppedException e) {
				// 何もしない
			} catch (VFSException e) {
				e.printStackTrace();
			}
		}

		public Manipulation getManipulation() {
			return manipulation;
		}

	}

	/**
	 * 操作にリスナを追加する。
	 */
	public void addManipulationListener(ManipulationListener listener) {
		if(listeners == null) {
			listeners = new ArrayList();
		}
		
		listeners.add(listener);
	}

	/**
	 * 操作からリスナを削除する。
	 */
	public void removeManipulationListener(ManipulationListener listener) {
		if(listeners == null) {
			return;
		}
		
		listeners.remove(listener);
	}
	
	/**
	 * ハッシュ値を取得する。
	 */
	public int hashCode() { 
		return manipulationId;
	}

	/**
	 * equalsの実装。
	 * 同一オブジェクトの場合true。
	 */
	public boolean equals(Object o) {
		return this == o;
	}
	
	/**
	 * 実行スレッドを取得する。
	 * 
	 * @return
	 */
	protected Thread getWorkThread() {
		return workThread;
	}
}
