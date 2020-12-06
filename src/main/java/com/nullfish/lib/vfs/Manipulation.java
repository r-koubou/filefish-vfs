package com.nullfish.lib.vfs;

import com.nullfish.lib.vfs.exception.VFSException;

/**
 * ファイルに対する操作のインターフェイス。
 * 
 * @author shunji
 */

public interface Manipulation {
	/**
	 * 不確定プログレス値の定数
	 */
	public static final long PROGRESS_INDETERMINED = Long.MIN_VALUE;
	
	/**
	 * 操作を同期実行する。
	 * 
	 * @throws VFSException
	 */
	public abstract void start() throws VFSException;
	
	/**
	 * 操作を非同期実行する。
	 * 
	 * @throws VFSException
	 */
	public abstract void startAsync() throws VFSException;
	
	/**
	 * 操作の前処理（最大プログレス計算など）を実行する。
	 * 
	 * @throws VFSException
	 */
	public abstract void prepare() throws VFSException;
	
	/**
	 * 操作を実行する。
	 * 
	 * @throws VFSException
	 */
	public abstract void execute() throws VFSException;
	
	/**
	 * 作業状況を取得する。
	 */
	public abstract long getProgress();
	
	/**
	 * 作業状況の最大を取得する。
	 * @return
	 */
	public abstract long getProgressMin();
	
	/**
	 * 作業状況の最小を取得する。
	 * @return
	 */
	public abstract long getProgressMax();
	
	/**
	 * 作業経過メッセージを取得する。
	 * @return
	 */
	public String getProgressMessage();

	/**
	 * 今実際に実行されている作業を取得する。
	 * @return
	 */ 
	public abstract Manipulation getCurrentManipulation();

	/**
	 * 今実際に実行されている作業を設定する。
	 * @return
	 */ 
	public abstract void setCurrentManipulation(Manipulation currentManipulation);

	/**
	 * 作業を中止する。
	 *
	 */
	public void stop();
	
	/**
	 * 作業が中止されているならtrueを返す。
	 * @return
	 */
	public boolean isStopped();
	
	/**
	 * 終了
	 *
	 */
	public void setFinished(boolean finished);
	
	/**
	 * 作業が終了しているならtrueを返す。
	 * @return
	 */
	public boolean isFinished();

	/**
	 * 親となる操作をセットする。
	 * @param parent
	 */
	public void setParentManipulation(Manipulation parent);

	/**
	 * 親となる操作を取得する。
	 * @return
	 */
	public Manipulation getParentManipulation();

	/**
	 * ルートとなる操作を取得する。
	 * @return
	 */
	public Manipulation getRootManipulation();

	/**
	 * 操作にリスナを追加する。
	 */
	public void addManipulationListener(ManipulationListener listener);

	/**
	 * 操作からリスナを削除する。
	 */
	public void removeManipulationListener(ManipulationListener listener);
}
