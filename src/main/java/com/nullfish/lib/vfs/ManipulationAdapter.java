/*
 * Created on 2004/04/05
 *
 */
package com.nullfish.lib.vfs;


/**
 * ManipulationListenerのアダプタクラス。
 * 
 * @author shunji
 */
public class ManipulationAdapter implements ManipulationListener {
	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationListener#started(com.sexyprogrammer.lib.vfs.ManipulationEvent)
	 */
	public void started(ManipulationEvent e) {
	}
	
	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationListener#preparationStarted(com.sexyprogrammer.lib.vfs.ManipulationEvent)
	 */
	public void preparationStarted(ManipulationEvent e) {
	}
	
	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationListener#preparationFinished(com.sexyprogrammer.lib.vfs.ManipulationEvent)
	 */
	public void preparationFinished(ManipulationEvent e) {
	}
	
	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationListener#manipulationStarted(com.sexyprogrammer.lib.vfs.ManipulationEvent)
	 */
	public void manipulationStarted(ManipulationEvent e) {
	}
	
	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationListener#manipulationFinished(com.sexyprogrammer.lib.vfs.ManipulationEvent)
	 */
	public void manipulationFinished(ManipulationEvent e) {
	}
	
	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationListener#finished(com.sexyprogrammer.lib.vfs.ManipulationEvent)
	 */
	public void finished(ManipulationEvent e) {
	}
	
	/**
	 * 操作中止が呼び出された際に呼び出される。
	 * @param e
	 */
	public void manipulationStopping(ManipulationEvent e) {
	}
	
	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.ManipulationListener#manipulationStopped(com.sexyprogrammer.lib.vfs.ManipulationEvent)
	 */
	public void manipulationStopped(ManipulationEvent e) {
	}
}
