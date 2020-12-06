package com.nullfish.lib.vfs.impl;


import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.manipulation.DeleteFailurePolicy;


/**
 * DeletePolicyのデフォルト実装。
 * confirmDeleteで常に同一の値を返す。
 * 
 * @author shunji
 */
public class DefaultDeletePolicy implements DeleteFailurePolicy {
	/*
	 * confirmDeleteで返される戻り値
	 */
	private int answer;
	
	/**
	 * 常にリトライする。
	 */
	public static final DeleteFailurePolicy RETRY = new DefaultDeletePolicy(DeleteFailurePolicy.RETRY);
	
	/**
	 * 常に無視する。
	 */
	public static final DeleteFailurePolicy IGNORE = new DefaultDeletePolicy(DeleteFailurePolicy.IGNORE);
	
	/**
	 * 常に中止する。
	 */
	public static final DeleteFailurePolicy FAIL = new DefaultDeletePolicy(DeleteFailurePolicy.FAIL);
	
	/**
	 * コンストラクタ。
	 * 
	 * @param answer	常に返す結果。
	 */
	private DefaultDeletePolicy(int answer) {
		this.answer = answer;
	}
	
	public int getDeleteFailurePolicy(VFile file) {
		return answer;
	}
}
