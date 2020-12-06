/*
 * 作成日: 2003/11/13
 *
 */
package com.nullfish.lib.vfs.manipulation.abst;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.nullfish.lib.vfs.UpdateManager;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.manipulation.GetOutputStreamManipulation;


/**
 * @author shunji
 *
 */
public abstract class AbstractGetOutputStreamManipulation
	extends AbstractManipulation
	implements GetOutputStreamManipulation {

	private OutputStream rtn;
	
	private boolean append = false;
	
	public AbstractGetOutputStreamManipulation(VFile file) {
		super(file);
	}

	/**
	 * 出力ストリームを取得する。
	 * 
	 * @see com.nullfish.lib.vfs.manipulation.GetOutputStreamManipulation#getOutputStream()
	 */
	public OutputStream getOutputStream() {
		return rtn;
	}

	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#execute(com.sexyprogrammer.lib.vfs.VFSUser)
	 */
	public void doExecute() throws VFSException {
		if(append) {
			rtn = new UpdateManagingOutputStream(doGetAppendOutputStream(file), file);
		} else {
			rtn = new UpdateManagingOutputStream(doGetOutputStream(file), file);
		}
	}
	
	public abstract OutputStream doGetOutputStream(VFile file) throws VFSException;
	
	public abstract OutputStream doGetAppendOutputStream(VFile file) throws VFSException;
	
	/**
	 * クローズ時にファイル更新通知を行うアウトプットストリームクラス
	 */
	private static class UpdateManagingOutputStream extends BufferedOutputStream {
		VFile file;
		
		/**
		 * @param out
		 */
		public UpdateManagingOutputStream(OutputStream out, VFile file) {
			super(out);
			this.file = file;
		}
		
		/**
		 * クローズメソッド。
		 * ファイル更新を通知する。
		 */
		public void close() throws IOException {
			UpdateManager.getInstance().fileChanged(file);
			super.close();
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

	/**
	 * 追記書き込みを行なうか設定する。
	 * 
	 * @param append trueなら追記
	 */
	public void setAppend(boolean append) {
		this.append = append;
	}
}
