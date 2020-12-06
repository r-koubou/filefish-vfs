package com.nullfish.lib.vfs.impl.ftp.util;

/**
 * 一定時間後、自動でinterruptされるスレッド
 * @author shunji
 *
 */
public class AutoInterruptionThread extends Thread {
	private long waitingTime = 0;
	
	private Interrupter interrupter = new Interrupter();
	
    public AutoInterruptionThread(long waitingTime) {
    	this.waitingTime = waitingTime;
    }
    
    public AutoInterruptionThread(Runnable target, long waitingTime) {
    	super(target);
    	this.waitingTime = waitingTime;
    }
    
    public AutoInterruptionThread(ThreadGroup group, Runnable target, long waitingTime) {
    	super(group, target);
    	this.waitingTime = waitingTime;
    }

    public AutoInterruptionThread(String name, long waitingTime) {
    	super(name);
    	this.waitingTime = waitingTime;
    }

    public AutoInterruptionThread(ThreadGroup group, String name, long waitingTime) {
    	super(group,name);
    	this.waitingTime = waitingTime;
    }

    public AutoInterruptionThread(Runnable target, String name, long waitingTime) {
    	super(target, name);
    	this.waitingTime = waitingTime;
    }

    public AutoInterruptionThread(ThreadGroup group, Runnable target, String name, long waitingTime) {
    	super(group, target, name);
    	this.waitingTime = waitingTime;
    }

    public AutoInterruptionThread(ThreadGroup group, Runnable target, String name,
            long stackSize, long waitingTime) {
    	super(group, target, name, stackSize);
    	this.waitingTime = waitingTime;
    }
    
    
	public void start() {
		interrupter.start();
		super.start();
	}
	
	private class Interrupter extends Thread {
		public void run() {
			try {
				AutoInterruptionThread.this.join(waitingTime);
			} catch (InterruptedException e) {}
			
			if(AutoInterruptionThread.this.isAlive()) {
				AutoInterruptionThread.this.interrupt();
			}
		}
	}
}
