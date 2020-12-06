/*
 * Created on 2004/04/21
 *
 */
package com.nullfish.lib.vfs.search;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.ManipulationStoppedException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.manipulation.abst.AbstractManipulation;

/**
 * ファイル検索操作
 * 
 * @author shunji
 */
public class SearchManipulation extends AbstractManipulation {
	/**
	 * ファイル判定クラス
	 */
	private FileMatcher matcher;

	/**
	 * trueならサブディレクトリも検索する
	 */
	private boolean searchesSubDirectory = false;

	/**
	 * 検索対象ファイル
	 */
	private VFile[] files;

	/**
	 * 一致ファイルリスト
	 */
	private List matchedFiles = new ArrayList();
	
	/**
	 * メッセージのパラメータ
	 */
	protected VFile[] messageParam = new VFile[1];

	/**
	 * 経過フォーマット
	 */
	private MessageFormat progressFormat;

	/**
	 * コンストラクタ
	 * 
	 * @param file
	 *            操作対象ファイル
	 */
	public SearchManipulation(VFile[] files, boolean searchesSubDirectory,
			FileMatcher matcher) {
		super(null);
		this.files = files;
		this.matcher = matcher;
		this.searchesSubDirectory = searchesSubDirectory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sexyprogrammer.lib.vfs.manipulation.abst.AbstractManipulation#doExecute()
	 */
	public void doExecute() throws VFSException {
		if(searchesSubDirectory) {
			for(int i=0; i<files.length; i++) {
				tryFile(files[i]);
			}
		} else {
			for(int i=0; i<files.length; i++) {
				if(matcher.matches(files[i])) {
					matchedFiles.add(files[i]);
				}
			}
		}
	}

	private void tryFile(VFile file) throws VFSException {
		messageParam[0] = file;
		if(matcher.matches(file)) {
			matchedFiles.add(file);
		}
		
		if (file.isDirectory(this)) {
			try {
				VFile[] children = file.getChildren(this);
				for (int i = 0; i < children.length; i++) {
					tryFile(children[i]);
				}
			} catch (ManipulationStoppedException e) {
				// 何もしない
			} catch (VFSException e) {
				// 握りつぶす
				e.printStackTrace();
			}
		}
	}

	/**
	 * 作業経過メッセージを取得する。
	 * 
	 * @return
	 */
	public String getProgressMessage() {
		if (progressFormat == null) {
			progressFormat = new MessageFormat(progressMessages
					.getString("searching"));
		}
		return progressFormat.format(messageParam);
	}

	/**
	 * 一致したファイルを取得する。
	 * @return
	 */
	public VFile[] getMatchedFiles() {
		VFile[] rtn = new VFile[matchedFiles.size()];
		rtn = (VFile[])matchedFiles.toArray(rtn);
		return rtn;
	}
}
