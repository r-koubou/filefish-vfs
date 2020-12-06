package com.nullfish.lib.vfs.impl;


import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.manipulation.OverwritePolicy;


/**
 * OverwriteConfirmerのデフォルト実装。
 * confirmOverwriteで常に同一の値を返す。
 * 
 * @author shunji
 */
public class DefaultOverwritePolicy implements OverwritePolicy {
	/*
	 * confirmOverWriteで返される戻り値
	 */
	private int answer;
	
	/**
	 * 常に上書きする。
	 */
	public static final OverwritePolicy OVERWRITE = new DefaultOverwritePolicy(OverwritePolicy.OVERWRITE);
	
	/**
	 * 常に上書きしない。
	 */
	public static final OverwritePolicy NO_OVERWRITE = new DefaultOverwritePolicy(OverwritePolicy.NO_OVERWRITE);
	
	/**
	 * 常に中止する。
	 */
	public static final OverwritePolicy STOP_ALL = new DefaultOverwritePolicy(OverwritePolicy.STOP_ALL);
	
	/**
	 * コンストラクタ。
	 * 
	 * @param answer	常に返す結果。
	 */
	private DefaultOverwritePolicy(int answer) {
		this.answer = answer;
	}
	
	/* (非 Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.OverwriteConfirmer#confirmOverwrite(com.sexyprogrammer.lib.vfs.VFile, com.sexyprogrammer.lib.vfs.VFile)
	 */
	public int getOverwritePolicy(VFile srcFile, VFile dest) {
		return answer;
	}

	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.OverwritePolicy#getNewDestination()
	 */
	public VFile getNewDestination() {
		return null;
	}

	public void setFiles(VFile from, VFile to) {
		// 何もしない
	}

}
