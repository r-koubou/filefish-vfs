/*
 * Created on 2004/04/21
 *
 */
package com.nullfish.lib.vfs.manipulation.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.ManipulationStoppedException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.manipulation.abst.AbstractManipulation;

/**
 * @author shunji
 *
 */
public class GetContentManipulation extends AbstractManipulation {

	/**
	 * 中身
	 */
	private byte[] content;

	/**
	 * メッセージのパラメータ
	 */
	protected VFile[] messageParam;
	
	/**
	 * 経過フォーマット
	 */
	private MessageFormat progressFormat;
	
	private long read = 0;
	
	private long max = 0;
	
	/**
	 * コンストラクタ
	 * @param file	操作対象ファイル
	 */
	public GetContentManipulation(VFile file) {
		super(file);
		messageParam = new VFile[1];
		messageParam[0] = file;
	}
	
	/* (non-Javadoc)
	 * @see org.sexyprogrammer.lib.vfs.manipulation.abst.AbstractManipulation#doExecute()
	 */
	public void doExecute() throws VFSException {
		ByteArrayOutputStream os = null;
		
		BufferedInputStream is = null;
		try {
			max = file.getLength(this);
			
			os = new ByteArrayOutputStream();
			is = new BufferedInputStream(file.getInputStream(this));

			byte[] buffer = new byte[4096];
			int l = 0;
			while((l = is.read(buffer)) > 0) {
				if (isStopped()) {
					throw new ManipulationStoppedException(this);
				}
				
				os.write(buffer, 0, l);
				read += l;
			}
			
			os.flush();
			content = os.toByteArray();
		} catch (IOException e) {
			throw new VFSIOException(e);
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
			try {
				os.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ハッシュ値を取得する。
	 * @return	ハッシュ値
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * 作業経過メッセージを取得する。
	 * 
	 * @return
	 */
	public String getProgressMessage() {
		if (progressFormat == null) {
			progressFormat = new MessageFormat(progressMessages
					.getString("get_content"));
		}
		return progressFormat.format(messageParam);
	}
	
	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#getProgress()
	 */
	public long getProgress() {
		if (!isPrepared()) {
			return PROGRESS_INDETERMINED;
		}

		if (isFinished()) {
			return max;
		}

		return read;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.sexyprogrammer.lib.vfs.Manipulation#getProgressMax()
	 */
	public long getProgressMax() {
		return max;
	}
}
