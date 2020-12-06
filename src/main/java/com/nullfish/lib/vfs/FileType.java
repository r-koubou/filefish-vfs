package com.nullfish.lib.vfs;

/**
 * ファイル種類を表すクラス。
 * インスタンス生成は出来ず、FILE、DIRECTORY、LINKのみ存在する。
 * 
 * @author shunji
 */
public class FileType {
	/**
	 * ファイル
	 */
	public static final FileType FILE = new FileType("file", false, true, true);

	/**
	 * ディレクトリ
	 */
	public static FileType DIRECTORY =
		new FileType("direcrory", true, false, true);

	/**
	 * リンク
	 */
	public static FileType LINK = new FileType("LINK", false, true, true);

	/**
	 * ファイル不在
	 */
	public static FileType NOT_EXISTS = new FileType("not_exists", false, false, false);

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 子ファイルを持つかのフラグ
	 */
	boolean hasChildren;

	/**
	 * 内容をもつかのフラグ
	 */
	boolean hasContent;

	/**
	 * 属性を持つかのフラグ
	 */
	boolean hasAttributes;

	/**
	 * ハッシュ値
	 */
	int hashCode = -1;
	
	private FileType(
		String name,
		boolean hasChildren,
		boolean hasContent,
		boolean hasAttributes) {
		this.name = name;
		this.hasChildren = hasChildren;
		this.hasContent = hasContent;
		this.hasAttributes = hasAttributes;
	}

	/**
	 * @return
	 */
	public boolean isHasAttributes() {
		return hasAttributes;
	}

	/**
	 * @return
	 */
	public boolean isHasChildren() {
		return hasChildren;
	}

	/**
	 * @return
	 */
	public boolean hasContent() {
		return hasContent;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * toString()の実装
	 */
	public String toString() {
		return "com.sexyprogrammer.lib.vfs.FileType:name="
			+ name
			+ ":hasChildren=" + hasChildren
			+ ":hasContent=" + hasContent
			+ ":hasAttribute=" + hasAttributes;
	}
	
	/**
	 * ハッシュ値を求める。
	 */
	public int hashCode() {
		if(hashCode == -1) {
			hashCode = toString().hashCode();
		}
		
		return hashCode;
	}
}
