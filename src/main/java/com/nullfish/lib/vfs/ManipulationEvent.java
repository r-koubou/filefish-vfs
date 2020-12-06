/*
 * Created on 2004/01/03
 *
 */
package com.nullfish.lib.vfs;

import java.util.EventObject;

import com.nullfish.lib.vfs.exception.VFSException;


/**
 * @author shunji
 *
 */
public class ManipulationEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1892232283466909094L;
	VFSException exception;

	
	public ManipulationEvent(Manipulation source) {
		super(source);
	}

	/**
	 * @return Returns the exception.
	 */
	public VFSException getException() {
		return exception;
	}

	/**
	 * @param exception The exception to set.
	 */
	public void setException(VFSException exception) {
		this.exception = exception;
	}

}
