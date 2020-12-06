package com.nullfish.lib.vfs;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.WrongPathException;

/**
 * ファイル名を表す抽象クラス。不変オブジェクトとなる。
 * 
 * @author shunji
 */

public abstract class FileName {
	/**
	 * 区切り文字
	 */
	public static final String SEPARATOR = "/";

	/**
	 * 親パス
	 */
	public static final String PARENT = "..";

	/**
	 * カレントパス
	 */
	public static final String CURRENT = ".";

	/**
	 * スキーマ
	 */
	protected String scheme;

	/**
	 * パスを表すファイル名の配列
	 */
	protected String[] path;

	/**
	 * パスの文字列
	 */
	protected String pathString;

	/**
	 * 基準になるファイル名（アーカイブファイルなど）
	 */
	protected FileName baseFileName;

	/**
	 * ユーザー情報
	 */
	protected UserInfo userInfo;

	/**
	 * ホスト
	 */
	protected String host;

	/**
	 * ポート番号
	 */
	protected int port;

	/**
	 * クエリ
	 */
	protected String query;

	/**
	 * フラグメント
	 */
	protected String fragment;

	/**
	 * 拡張子
	 */
	private String extension;

	/**
	 * 拡張子以外
	 */
	private String exceptExtension;
	
	/**
	 * 拡張子の小文字表現
	 */
	private String lowerExtension;

	/**
	 * ファイル名の小文字表記
	 */
	private String lowerName;

	/**
	 * longの配列に直した小文字のファイル名。 数値はまとまりで一つの数字として捉える。
	 * それ以外の文字は数値に直し、そこにlongの最小値を足した数字に直す。
	 */
	private long[] longArrayName;

	/**
	 * ハッシュ値
	 */
	protected int hash = -1;

	/**
	 * 絶対パス
	 */
	private String absolutePath;

	/**
	 * コンストラクタ
	 * 
	 * @param scheme
	 *            スキーマ
	 * @param baseFileName
	 *            基準となるファイル名（アーカイブファイルなど)
	 * @param fileNames
	 *            ファイル名の配列で表されたパス
	 * @param userInfo
	 *            ユーザー情報
	 * @param host
	 *            ホスト名
	 * @param port
	 *            ポート番号
	 * @param query
	 *            クエリ
	 * @param fragment
	 *            フラグメント
	 */
	public FileName(String scheme, FileName baseFileName, String[] fileNames,
			UserInfo userInfo, String host, int port, String query,
			String fragment) {

		this.scheme = scheme;
		this.baseFileName = baseFileName;
		this.path = fileNames;
		this.userInfo = userInfo;
		this.host = host;
		this.port = port;
		this.query = query;
		this.fragment = fragment;
	}

	/**
	 * 絶対パスを求める。
	 * 
	 * @return パス
	 */
	public final String getAbsolutePath() {
		if (absolutePath == null) {
			absolutePath = doGetAbsolutePath();
		}

		return absolutePath;
	}

	/**
	 * 実際に相対パスを求める処理
	 * 
	 * @return
	 */
	public abstract String doGetAbsolutePath();

	/**
	 * セキュリティ上安全なパス（ユーザー情報の無いパス）を返す。
	 * 
	 * @return ユーザー情報抜きのパス
	 */
	public abstract String getSecurePath();

	/**
	 * セキュリティ上安全な名前オブジェクト（ユーザー情報の無いパス）を返す。
	 * 
	 * @return ユーザー情報抜きのパス
	 */
	public FileName getSecureName() {
		return createFileName(scheme, baseFileName, path, null, host, port, query, fragment);
	}

	/**
	 * URIを求める。
	 * 
	 * @return URI
	 */
	public abstract URI getURI() throws URISyntaxException;

	/**
	 * パスのセパレータ文字を返す。
	 */
	public abstract String getSeparator();

	/**
	 * ファイル名を生成する。
	 * 
	 * @param baseFileName
	 * @param fileNames
	 * @param userInfo
	 * @param host
	 * @param port
	 * @param query
	 * @param fragment
	 * @return 指定された条件に一致するファイル名
	 */
	public final FileName createFileName(String scheme,
			FileName baseFileName, String[] path, UserInfo userInfo,
			String host, int port, String query, String fragment) {
		if(VFS.IS_MAC && VFS.JAVA6_OR_LATER) {
			for(int i=0; i<path.length && path != null; i++) {
				if(!Normalizer.isNormalized(path[i], Normalizer.Form.NFC)) {
					path[i] = Normalizer.normalize(path[i], Normalizer.Form.NFC);
				}
			}
		}
		
		return doCreateFileName(scheme, baseFileName, path, userInfo, host, port, query, fragment);
	}

	/**
	 * ファイル名を生成する。
	 * 
	 * @param baseFileName
	 * @param fileNames
	 * @param userInfo
	 * @param host
	 * @param port
	 * @param query
	 * @param fragment
	 * @return 指定された条件に一致するファイル名
	 */
	public abstract FileName doCreateFileName(String scheme,
			FileName baseFileName, String[] path, UserInfo userInfo,
			String host, int port, String query, String fragment);

	/**
	 * このファイル名を元にユーザー情報を指定して、ファイル名を生成する。
	 * 
	 * @param userInfo
	 *            ユーザー情報
	 * @return 指定されたユーザー情報を持つ、このオブジェクトと同等のファイル名
	 */
	public FileName createFileName(UserInfo userInfo) {
		return createFileName(getScheme(), getBaseFileName(), getPath(),
				userInfo, getHost(), getPort(), getQuery(), getFragment());
	}

	/**
	 * 子ファイル名を生成する。
	 * 
	 * @param fileName
	 *            子ファイルのファイル名文字列
	 * @return 子ファイル名
	 */
	public FileName createChild(String fileName) {
		String[] newPath = new String[path.length + 1];
		System.arraycopy(path, 0, newPath, 0, path.length);
		newPath[newPath.length - 1] = fileName;

		return createFileName(scheme, baseFileName, newPath, userInfo, host,
				port, query, fragment);
	}

	/**
	 * 親ファイル名を取得する。
	 * 
	 * @return 親ファイル名
	 * @throws VFSException
	 */
	public FileName getParent() throws VFSException {
		if (path.length == 0) {
			throw new WrongPathException("The file is root.");
		}

		String[] newPath = new String[path.length - 1];
		System.arraycopy(path, 0, newPath, 0, newPath.length);

		return createFileName(scheme, baseFileName, newPath, userInfo, host,
				port, query, fragment);
	}

	/**
	 * ルートを求める。
	 * 
	 * @return 同一ファイルシステムのルートファイル名
	 */
	public FileName getRoot() {
		return createFileName(scheme, baseFileName, new String[0], userInfo,
				host, port, null, null);
	}

	/**
	 * ファイル名文字列を求める。
	 * 
	 * @return ファイル名文字列
	 */
	public String getName() {
		if (path != null && path.length > 0) {
			return path[path.length - 1];
		} else {
			return "";
		}
	}

	/**
	 * ファイル名文字列の小文字表現を求める。
	 * 
	 * @return ファイル名文字列の小文字表現
	 */
	public String getLowerName() {
		if (lowerName == null) {
			lowerName = getName().toLowerCase();
		}

		return lowerName;
	}

	/**
	 * long配列に直した小文字表記のファイル名を返す。
	 * ファイル名中のアルファベットはcharに直し、そこからlongの最小値を
	 * 引いた数に直してある。
	 * 主にソート時に使用する（ファイル名の数値もソートするケースで)。
	 * 
	 * @return
	 */
	public long[] getLowerLongArrayName() {
		if (path.length == 0) {
			return new long[0];
		}

		if (longArrayName == null) {
			longArrayName = string2longArray(getLowerName());
		}

		return longArrayName;
	}

	/**
	 * パスをファイル名の配列で返す。
	 * 
	 * @return ファイル名の配列形式のパス
	 */
	public String[] getPath() {
		return path;
	}

	/**
	 * パス文字列を返す。
	 * 
	 * @return パスの文字列
	 */
	public String getPathString() {
		if (pathString == null) {
			StringBuffer buffer = new StringBuffer("/");

			for (int i = 0; path != null && i < path.length; i++) {
				if (i > 0) {
					buffer.append("/");
				}

				buffer.append(path[i]);
			}

			if (query != null && query.length() > 0) {
				buffer.append("?");
				buffer.append(query);
			}

			if (fragment != null && fragment.length() > 0) {
				buffer.append("#");
				buffer.append(fragment);
			}

			pathString = buffer.toString();
		}

		return pathString;
	}

	/**
	 * このファイルがファイルシステムのルート要素か判定する。
	 * 
	 * @return このファイルがファイルシステムのルート要素ならtrueを返す。
	 */
	public boolean isRoot() {
		return (path.length == 0);
	}

	/**
	 * 拡張子を返す。 もしも拡張子が無い場合、空文字を返す。
	 * 
	 * @return 拡張子
	 */
	public String getExtension() {
		if (extension == null) {
			String fileName = getName();
			if (fileName == null) {
				extension = "";
			} else {
				int lastPeriod = fileName.lastIndexOf('.');
				if (lastPeriod == -1) {
					extension = "";
				} else {
					extension = fileName.substring(lastPeriod + 1);
				}
			}
		}

		return extension;
	}

	/**
	 * 拡張子以外の部分を取得する。
	 * @return
	 */
	public String getExceptExtension() { 
		if (exceptExtension == null) {
			String fileName = getName();
			if (fileName == null) {
				exceptExtension = "";
			} else {
				int lastPeriod = fileName.lastIndexOf('.');
				if (lastPeriod == -1) {
					exceptExtension = fileName;
				} else {
					exceptExtension = fileName.substring(0, lastPeriod);
				}
			}
		}

		return exceptExtension;
	}
	
	/**
	 * 小文字に直した拡張子を返す。
	 * 
	 * @return 小文字に直した拡張子
	 */
	public String getLowerExtension() {
		if (lowerExtension == null) {
			lowerExtension = getExtension().toLowerCase();
		}

		return lowerExtension;
	}

	/**
	 * ユーザー情報を取得する。
	 * 
	 * @return ユーザー情報
	 */
	public UserInfo getUserInfo() {
		return userInfo;
	}

	/**
	 * スキームを取得する。
	 * 
	 * @return スキーム
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * ホストを取得する。
	 * 
	 * @return ホスト
	 */
	public String getHost() {
		return host;
	}

	/**
	 * クエリを取得する
	 * 
	 * @return クエリ
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * フラグメントを取得する。
	 * 
	 * @return フラグメント
	 */
	public String getFragment() {
		return fragment;
	}

	/**
	 * 基準になるファイル名を返す。
	 * 
	 * @return 基準ファイル名
	 */
	public FileName getBaseFileName() {
		return baseFileName;
	}

	/**
	 * ポート番号を取得する
	 * 
	 * @return ポート番号
	 */
	public int getPort() {
		return port;
	}

	/**
	 * このファイル名を基準にした相対パスを取得する。 もしもファイルシステムが異なる場合、絶対パスを返す。
	 * 
	 * @param otherFileName
	 *            相対ファイル
	 * @return 相対パス
	 */
	public String resolveRelation(FileName otherFileName) {
		if (!isBaseFileSame(otherFileName)) {
			return otherFileName.getAbsolutePath();
		}

		String otherScheme = otherFileName.getScheme();
		if (!otherScheme.equals(getScheme())) {
			return otherFileName.getAbsolutePath();
		}

		if (!compareStrings(this.getHost(), otherFileName.getHost())) {
			return otherFileName.getAbsolutePath();
		}

		String[] thisPath = getPath();
		String[] otherPath = otherFileName.getPath();

		//	パスが同一の階層数を求める。
		int sameIndex = 0;
		for (sameIndex = 0; sameIndex < thisPath.length
				&& sameIndex < otherPath.length
				&& thisPath[sameIndex].equals(otherPath[sameIndex]); sameIndex++) {
		}

		if (sameIndex == thisPath.length && sameIndex == otherPath.length) {
			return CURRENT;
		}

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < thisPath.length - sameIndex; i++) {
			if (buffer.length() > 0) {
				buffer.append(SEPARATOR);
			}
			buffer.append(PARENT);
		}

		for (int i = sameIndex; i < otherPath.length; i++) {
			if (buffer.length() > 0) {
				buffer.append(SEPARATOR);
			}

			buffer.append(otherPath[i]);
		}

		return buffer.toString();
	}

	/*
	 * 基準ファイル名が同一か判定する。
	 * 
	 * @param otherFileName 比較対象 @return 同一ならtrueを返す。
	 */
	private boolean isBaseFileSame(FileName otherFileName) {
		FileName baseFileName = getBaseFileName();
		FileName otherBaseFileName = otherFileName.getBaseFileName();

		if (baseFileName == null && otherBaseFileName == null) {
			return true;
		} else if (baseFileName != null && otherBaseFileName == null) {
			return false;
		} else if (otherBaseFileName != null && baseFileName == null) {
			return false;
		}

		return baseFileName.equals(otherBaseFileName);
	}

	/**
	 * nullの可能性のある二つのStringを比較し、同一ならtrueを返す。
	 * 
	 * @param s1
	 *            文字列1
	 * @param s2
	 *            文字列2
	 * @return 同一ならtrueを返す。
	 */
	private boolean compareStrings(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return true;
		} else if (s1 != null && s2 == null) {
			return false;
		} else if (s2 != null && s1 == null) {
			return false;
		}

		return s1.equals(s2);
	}

	/**
	 * 相対パスを解釈し、相対ファイルを返す。
	 * 
	 * @param relation
	 *            相対パス
	 * @param type
	 *            TODO
	 * 
	 * @return このクラスを基準にし、relationで表される相対ファイル
	 * @throws VFSException
	 */
	public FileName resolveFileName(String relation, FileType type)
			throws VFSException {
		StringTokenizer tokenizer = new StringTokenizer(relation, SEPARATOR);
		FileName base;
		if (relation.startsWith(SEPARATOR)) {
			base = createFileName(scheme, getBaseFileName(), new String[0],
					getUserInfo(), getHost(), getPort(), getQuery(),
					getFragment());
		} else {
			if (type == FileType.FILE) {
				base = this.getParent();
			} else {
				base = this;
			}
		}

		String[] fileNames = base.getPath();
		List fileNamesList = new LinkedList();
		for (int i = 0; i < fileNames.length; i++) {
			fileNamesList.add(fileNames[i]);
		}

		while (tokenizer.hasMoreTokens()) {
			String file = tokenizer.nextToken();

			if (file.equals(PARENT)) {
				if (fileNamesList.size() == 0) {
					throw new WrongPathException();
				}

				fileNamesList.remove(fileNamesList.size() - 1);
			} else if (file.equals(CURRENT)) {
				//	カレントディレクトリなので何もしない
			} else {
				fileNamesList.add(file);
			}
		}

		String[] newPath = new String[fileNamesList.size()];
		newPath = (String[]) fileNamesList.toArray(newPath);

		return createFileName(scheme, getBaseFileName(), newPath,
				getUserInfo(), getHost(), getPort(), getQuery(), getFragment());
	}

	/**
	 * Stringをlongの配列に変換するメソッド。
	 * 基本的に一文字を1要素に変換するが、
	 * String内の数字はまとめて一つの数字にし、
	 * それ以外の文字は数値に直してlongの最小値を足している。
	 * 
	 */
	protected long[] string2longArray(String str) {
		long[] newArray = new long[str.length()];

		int arrayLength = 0;
		long number = 0;
		boolean numberFlag = false;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c <= '9' && c >= '0') {
				numberFlag = true;
				number = (number * 10) + (c - '0');
			} else {
				if (numberFlag) {
					newArray[arrayLength] = number;
					arrayLength++;
					number = 0;
					numberFlag = false;
				}
				newArray[arrayLength] = ((long) c) + Long.MIN_VALUE;
				arrayLength++;
			}
		}

		if (number != 0) {
			newArray[arrayLength] = number;
			arrayLength++;
			number = 0;
		}

		if (arrayLength == 0) {
			return (new long[0]);
		}

		long[] rtn = new long[arrayLength];
		System.arraycopy(newArray, 0, rtn, 0, arrayLength);

		return rtn;
	}

	/**
	 * ハッシュ値を取得する。
	 * 
	 * @return ハッシュ値
	 */
	public int hashCode() {
		if (hash == -1) {
			hash = FileFactory.interpretPath(this).hashCode();
		}

		return hash;
	}

	/**
	 * オブジェクトが等しいか判定する。
	 * 
	 * @return 等しければtrueを返す。
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null) {
			return false;
		}

		if (!getClass().equals(o.getClass())) {
			return false;
		}
		
		FileName other = (FileName) o;
		return getAbsolutePath().equals(other.getAbsolutePath());
	}

}
