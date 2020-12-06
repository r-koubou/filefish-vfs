package com.nullfish.lib.vfs;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.DefaultDeletePolicy;
import com.nullfish.lib.vfs.impl.DefaultOverwritePolicy;
import com.nullfish.lib.vfs.manipulation.CopyFileManipulation;
import com.nullfish.lib.vfs.manipulation.CreateFileManipulation;
import com.nullfish.lib.vfs.manipulation.CreateLinkManipulation;
import com.nullfish.lib.vfs.manipulation.DeleteFailurePolicy;
import com.nullfish.lib.vfs.manipulation.DeleteManipulation;
import com.nullfish.lib.vfs.manipulation.GetAttributesManipulation;
import com.nullfish.lib.vfs.manipulation.GetChildrenManipulation;
import com.nullfish.lib.vfs.manipulation.GetInputStreamManipulation;
import com.nullfish.lib.vfs.manipulation.GetOutputStreamManipulation;
import com.nullfish.lib.vfs.manipulation.GetPermissionManipulation;
import com.nullfish.lib.vfs.manipulation.MoveManipulation;
import com.nullfish.lib.vfs.manipulation.OverwritePolicy;
import com.nullfish.lib.vfs.manipulation.SetPermissionManipulation;
import com.nullfish.lib.vfs.manipulation.SetTimestampManipulation;
import com.nullfish.lib.vfs.manipulation.common.GetContentManipulation;
import com.nullfish.lib.vfs.manipulation.common.MD5HashManipulation;
import com.nullfish.lib.vfs.permission.FileAccess;
import com.nullfish.lib.vfs.permission.PermissionType;
import com.nullfish.lib.vfs.tag_db.TagDataBase;
import com.nullfish.lib.vfs.tag_db.command.FileTaggedCommand;
import com.nullfish.lib.vfs.tag_db.command.TagRemovedCommand;

/**
 * ファイルを表す抽象クラス
 */

public abstract class VFile {

	/**
	 * ファイル名称クラス
	 */
	protected FileName fileName;

	/**
	 * ファイルシステム
	 */
	protected FileSystem fileSystem;

	/**
	 * ファイル属性
	 */
	protected FileAttribute attributes;

	/**
	 * ファイルパーミッション
	 */
	protected Permission permission;

	/**
	 * タグ
	 */
	private List tags;
	
	/**
	 * ハッシュ値
	 */
	protected int hashCode = -1;

	/**
	 * 相対パスのキャッシュ
	 */
	private String relationCache;

	/**
	 * 相対パスのキャッシュの基準ファイル
	 */
	private VFile relationFileCache;

	/**
	 * 親ファイルのキャッシュ
	 */
	private VFile parentCache;

	/**
	 * コンストラクタ
	 * 
	 * @param fileSystem
	 *            ファイルシステム
	 * @param fileName
	 *            ファイル名
	 */
	public VFile(FileSystem fileSystem, FileName fileName) {
		this(fileSystem, fileName, null);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param fileSystem
	 *            ファイルシステム
	 * @param fileName
	 *            ファイル名
	 * @param attributes
	 *            ファイル属性
	 */
	public VFile(FileSystem fileSystem, FileName fileName,
			FileAttribute attributes) {
		this.fileSystem = fileSystem;
		this.fileName = fileName;
		this.attributes = attributes;
	}

	/**
	 * ファイルの操作クラスを提供クラスを取得する。
	 * 
	 * @return ファイル操作クラス
	 */
	public abstract ManipulationFactory getManipulationFactory();

	/**
	 * ファイル名の文字列を取得する。
	 * 
	 * @return ファイル名文字列
	 */
	public String getName() {
		return fileName.getName();
	}

	/**
	 * ファイル名を取得する。
	 * 
	 * @return ファイル名
	 */
	public FileName getFileName() {
		return fileName;
	}

	/**
	 * ファイルシステムを取得する。
	 * 
	 * @return ファイルシステム
	 */
	public FileSystem getFileSystem() {
		return fileSystem;
	}

	/**
	 * ファイルリスナを追加する。
	 * 
	 * @param listener
	 *            追加するリスナ
	 */
	public void addFileListener(FileListener listener) {
		UpdateManager.getInstance().addFileListener(this, listener);
	}

	/**
	 * ファイルリスナを削除する
	 * 
	 * @param listener
	 *            削除するファイルリスナ
	 */
	public void removeFileListener(FileListener listener) {
		UpdateManager.getInstance().removeFileListener(this, listener);
	}

	/**
	 * 子ファイルのファイルリスナを追加する。
	 * 
	 * @param listener
	 *            追加するリスナ
	 */
	public void addChildFileListener(FileListener listener) {
		UpdateManager.getInstance().addChildFileListener(this, listener);
	}

	/**
	 * サブファイルのファイルリスナを外す。
	 * 
	 * @param listener
	 *            削除するファイルリスナ
	 */
	public void removeChildFileListener(FileListener listener) {
		UpdateManager.getInstance().removeChildFileListener(this, listener);
	}

	/**
	 * 絶対パスの文字列を取得する。
	 * 
	 * @return 絶対パス
	 */
	public String getAbsolutePath() {
		return FileFactory.interpretPath(fileName);
	}

	/**
	 * ユーザー情報抜きの絶対パスを取得する。
	 * 
	 * @return ユーザー情報を覗いた絶対パス
	 */
	public String getSecurePath() {
		return FileFactory.interpretSecurePath(fileName);
	}

	/**
	 * URIを取得する。
	 * 
	 * @return このファイルのURI
	 * @throws URISyntaxException
	 *             このファイルのパスがURIに一致しない場合投げられる。
	 */
	public URI getURI() throws URISyntaxException {
		return fileName.getURI();
	}

	/**
	 * キャッシュされたファイル属性をクリアする。
	 *  
	 */
	public synchronized void clearFileAttribute() {
		attributes = null;
	}

	/**
	 * パーミッションを取得する。
	 * 
	 * @return パーミッション
	 * @throws VFSException
	 */
	public Permission getPermission() throws VFSException {
		return getPermission(null);
	}

	/**
	 * パーミッションを取得する。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @return パーミッション
	 * @throws VFSException
	 */
	public Permission getPermission(Manipulation parentManipulation)
			throws VFSException {
		if (permission == null) {
			GetPermissionManipulation manipulation = FileUtil
					.prepareGetPermission(this, parentManipulation);
			manipulation.start();
			permission = manipulation.getPermission();
		}

		return permission;
	}

	/**
	 * パーミッションを設定する
	 * 
	 * @param access
	 * @param type
	 * @param value
	 * @throws VFSException
	 */
	public void setPermission(FileAccess access, PermissionType type,
			boolean value) throws VFSException {
		setPermission(access, type, value, null);
	}

	/**
	 * パーミッションを設定する
	 * 
	 * @param access
	 * @param type
	 * @param value
	 * @param parentManipulation
	 * @throws VFSException
	 */
	public void setPermission(FileAccess access, PermissionType type,
			boolean value, Manipulation parentManipulation) throws VFSException {
		SetPermissionManipulation manipulation = FileUtil.prepareSetPermission(
				this, access, type, value, parentManipulation);
		manipulation.start();
	}

	/**
	 * パーミッションを設定する。
	 * 
	 * @param permission
	 * @throws VFSException
	 */
	public void setPermission(Permission permission) throws VFSException {
		setPermission(permission, null);
	}

	/**
	 * パーミッションを設定する。
	 * 
	 * @param permission
	 * @param parentManipulation
	 * @throws VFSException
	 */
	public void setPermission(Permission permission,
			Manipulation parentManipulation) throws VFSException {
		SetPermissionManipulation manipulation = getManipulationFactory()
				.getSetPermissionManipulation(this);
		manipulation.setParentManipulation(parentManipulation);
		FileAccess[] accesses = permission.getAccess();
		PermissionType[] types = permission.getTypes();
		for (int i = 0; i < accesses.length; i++) {
			for (int j = 0; j < types.length; j++) {
				manipulation.addPermission(types[j], accesses[i], permission
						.hasPermission(types[j], accesses[i]));
			}
		}

		manipulation.start();
	}

	/**
	 * キャッシュされたパーミッションをクリアする。
	 *  
	 */
	public synchronized void clearPermission() {
		permission = null;
	}

	/**
	 * パーミッションのキャッシュを取得する。
	 * 
	 * @return パーミッションのキャッシュ
	 */
	public synchronized Permission getPermissionCache() {
		return permission;
	}

	/**
	 * パーミッションのキャッシュをセットする。
	 * 
	 * @param permission
	 *            パーミッション
	 */
	public synchronized void setPermissionCache(Permission permission) {
		this.permission = permission;
	}

	/**
	 * 入力ストリームを取得する。
	 * 
	 * @return ファイルの入力ストリーム
	 * @throws VFSException
	 */
	public InputStream getInputStream() throws VFSException {
		return getInputStream(null);
	}

	/**
	 * 入力ストリームを取得する。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @return ファイルの入力ストリーム
	 * @throws VFSException
	 */
	public InputStream getInputStream(Manipulation parentManipulation)
			throws VFSException {
		GetInputStreamManipulation manipulation = FileUtil
				.prepareGetInputStream(this, parentManipulation);
		manipulation.start();
		return manipulation.getInputStream();
	}

	/**
	 * 出力ストリームを取得する。
	 * 
	 * @return ファイルの出力ストリーム
	 * @throws VFSException
	 */
	public OutputStream getOutputStream() throws VFSException {
		return getOutputStream(null);
	}

	/**
	 * 出力ストリームを取得する。
	 * 
	 * @param append trueなら追記書き込みをする。
	 * @return ファイルの出力ストリーム
	 * @throws VFSException
	 */
	public OutputStream getOutputStream(boolean append) throws VFSException {
		return getOutputStream(append, null);
	}

	/**
	 * 出力ストリームを取得する。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @return ファイルの出力ストリーム
	 * @throws VFSException
	 */
	public OutputStream getOutputStream(Manipulation parentManipulation)
			throws VFSException {
		return getOutputStream(false, parentManipulation);
	}

	/**
	 * 出力ストリームを取得する。
	 * 
	 * @param append trueなら追記書き込みをする。
	 * @param parentManipulation
	 *            親操作
	 * @return ファイルの出力ストリーム
	 * @throws VFSException
	 */
	public OutputStream getOutputStream(boolean append, Manipulation parentManipulation)
			throws VFSException {
		GetOutputStreamManipulation manipulation = FileUtil
				.prepareGetOutputStream(this, append, parentManipulation);
		manipulation.start();
		return manipulation.getOutputStream();
	}

	/**
	 * ファイル属性を取得する。
	 * 
	 * @return ファイルの属性
	 * @throws VFSException
	 */
	public FileAttribute getAttribute() throws VFSException {
		return getAttribute(null);
	}

	/**
	 * ファイル属性を取得する。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @return ファイル属性
	 * @throws VFSException
	 */
	public FileAttribute getAttribute(Manipulation parentManipulation)
			throws VFSException {
		if (attributes == null) {
			GetAttributesManipulation manipulation = FileUtil
					.prepareGetAttribute(this, parentManipulation);
			manipulation.start();
			attributes = manipulation.getAttribute();
		}

		return attributes;
	}

	/**
	 * ファイル属性をキャッシュする。
	 * 
	 * @param attr
	 *            ファイル属性
	 */
	public synchronized void setAttributeCache(FileAttribute attr) {
		attributes = attr;
	}

	/**
	 * ファイル属性のキャッシュを取得する。
	 * 
	 * @return ファイル属性キャッシュ
	 */
	public synchronized FileAttribute getAttributeCache() {
		return attributes;
	}

	/**
	 * ファイル長を取得する。
	 * 
	 * @return
	 */
	public long getLength() throws VFSException {
		return getLength(null);
	}

	/**
	 * ファイル長を取得する。
	 * 
	 * @return
	 */
	public long getLength(Manipulation parentManipulation) throws VFSException {
		return getAttribute(parentManipulation).getLength();
	}

	/**
	 * タイムスタンプを取得する。
	 * 
	 * @return @throws
	 *         VFSException
	 */
	public Date getTimestamp() throws VFSException {
		return getTimestamp(null);
	}

	/**
	 * タイムスタンプを取得する。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @return タイムスタンプ
	 * @throws VFSException
	 */
	public Date getTimestamp(Manipulation parentManipulation)
			throws VFSException {
		return getAttribute(parentManipulation).getDate();
	}

	/**
	 * タイムスタンプを設定する。
	 * 
	 * @param date
	 * @throws VFSException
	 */
	public void setTimestamp(Date date) throws VFSException {
		setTimestamp(date, null);
	}

	/**
	 * タイムスタンプを設定する。
	 * 
	 * @param date
	 * @param parentManipulation
	 * @throws VFSException
	 */
	public void setTimestamp(Date date, Manipulation parentManipulation)
			throws VFSException {
		SetTimestampManipulation manipulation = FileUtil.prepareSetTimestamp(
				this, date, parentManipulation);
		manipulation.start();
	}

	/**
	 * ファイルが存在するならtrueを返す。
	 * 
	 * @return @throws
	 *         VFSException
	 */
	public boolean exists() throws VFSException {
		return exists(null);
	}

	/**
	 * ファイルが存在するならtrueを返す。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @return @throws
	 *         VFSException
	 */
	public boolean exists(Manipulation parentManipulation)
			throws VFSException {
		return getAttribute(parentManipulation).isExists();
	}

	/**
	 * ファイルの種類を返す。
	 * 
	 * @return @throws
	 *         VFSException
	 */
	public FileType getType() throws VFSException {
		return getType(null);
	}

	/**
	 * ファイルの種類を返す。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @return @throws
	 *         VFSException
	 */
	public FileType getType(Manipulation parentManipulation)
			throws VFSException {
		return getAttribute(parentManipulation).getFileType();
	}

	/**
	 * ファイルがファイルならtrueを返す。
	 * 
	 * @return @throws
	 *         VFSException
	 */
	public boolean isFile() throws VFSException {
		return isFile(null);
	}

	/**
	 * ファイルがファイルならtrueを返す。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @return @throws
	 *         VFSException
	 */
	public boolean isFile(Manipulation parentManipulation) throws VFSException {
		FileType type = getType(parentManipulation);
		return FileType.FILE.equals(type);
	}

	/**
	 * ファイルがディレクトリならtrueを返す。
	 * 
	 * @return @throws
	 *         VFSException
	 */
	public boolean isDirectory() throws VFSException {
		return isDirectory(null);
	}

	/**
	 * ファイルがディレクトリならtrueを返す。
	 * 
	 * @return @throws
	 *         VFSException
	 */
	public boolean isDirectory(Manipulation parentManipulation)
			throws VFSException {
		FileType type = getType(parentManipulation);
		return FileType.DIRECTORY.equals(type);
	}

	/**
	 * ファイルがリンクならtrueを返す。
	 * 
	 * @return @throws
	 *         VFSException
	 */
	public boolean isLink() throws VFSException {
		return isLink(null);
	}

	/**
	 * ファイルがリンクならtrueを返す。
	 * 
	 * @return @throws
	 *         VFSException
	 */
	public boolean isLink(Manipulation parentManipulation) throws VFSException {
		FileType type = getType(parentManipulation);
		return FileType.LINK.equals(type);
	}

	/**
	 * 親ファイルを取得する。 もしもルートファイルの場合、nullを返す。
	 * 
	 * @return 親ファイル
	 * @throws FileNameException
	 *             このファイルがルートなら投げられる。
	 * @throws VFSException
	 */
	public VFile getParent() {
		if (isRoot()) {
			return null;
		}

		if (parentCache != null) {
			return parentCache;
		}

		try {
			FileName parentFileName = fileName.getParent();
			parentCache = fileSystem.getFile(parentFileName);
			return parentCache;
		} catch (VFSException e) {
			return null;
		}
	}

	/**
	 * このファイルがファイルシステムのルートなら、trueを返す。
	 * 
	 * @return
	 */
	public boolean isRoot() {
		return fileName.isRoot();
	}

	/**
	 * 子ファイルを取得する。
	 * 
	 * @return 子ファイル
	 * @throws VFSException
	 */
	public VFile[] getChildren() throws VFSException {
		return getChildren(null);
	}

	/**
	 * 子ファイルを取得する。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @return 子ファイル
	 * @throws VFSException
	 */
	public VFile[] getChildren(Manipulation parentManipulation)
			throws VFSException {
		GetChildrenManipulation manipulation = FileUtil.prepareGetChildren(
				this, parentManipulation);
		manipulation.start();
		VFile[] children = manipulation.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].parentCache = this;
		}

		return manipulation.getChildren();
	}

	/**
	 * ファイルを生成する。
	 * 
	 * @throws VFSException
	 */
	public void createFile() throws VFSException {
		createFile(null);
	}

	/**
	 * ファイルを生成する。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @throws VFSException
	 */
	public void createFile(Manipulation parentManipulation) throws VFSException {
		CreateFileManipulation manipulation = FileUtil.prepareCreateFile(this,
				FileType.FILE, parentManipulation);
		manipulation.start();
	}

	/**
	 * ディレクトリを生成する。
	 * 
	 * @throws VFSException
	 */
	public void createDirectory() throws VFSException {
		createDirectory(null);
	}

	/**
	 * ディレクトリを生成する。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @throws VFSException
	 */
	public void createDirectory(Manipulation parentManipulation)
			throws VFSException {
		CreateFileManipulation manipulation = FileUtil.prepareCreateFile(this,
				FileType.DIRECTORY, parentManipulation);
		manipulation.start();
	}

	/**
	 * リンクを生成する。
	 * 
	 * @param dest
	 *            リンク先
	 * @throws VFSException
	 */
	public void createLink(VFile dest) throws VFSException {
		createLink(dest, null);
	}

	/**
	 * リンクを生成する。
	 * 
	 * @param dest
	 *            リンク先
	 * @param parentManipulation
	 *            親操作
	 * @throws VFSException
	 */
	public void createLink(VFile dest, Manipulation parentManipulation)
			throws VFSException {
		CreateLinkManipulation manipulation = FileUtil.prepareCreateLink(this,
				dest, parentManipulation);
		manipulation.start();
	}

	/**
	 * ファイルを削除する。
	 * 
	 * @throws VFSException
	 */
	public void delete() throws VFSException {
		delete(null);
	}

	/**
	 * ファイルを削除する。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @throws VFSException
	 */
	public void delete(Manipulation parentManipulation) throws VFSException {
		delete(DefaultDeletePolicy.FAIL, parentManipulation);
	}

	/**
	 * ファイルを削除する。
	 * 
	 * @param policy 削除失敗時ポリシー
	 * @param parentManipulation
	 *            親操作
	 * @throws VFSException
	 */
	public void delete(DeleteFailurePolicy policy, Manipulation parentManipulation) throws VFSException {
		DeleteManipulation manipulation = FileUtil.prepareDelete(this,
				policy, parentManipulation);
		manipulation.start();
	}

	/**
	 * ファイルを移動する。
	 * 
	 * @param dest
	 *            移動先ファイル
	 * @throws VFSException
	 */
	public void moveTo(VFile dest) throws VFSException {
		moveTo(dest, (Manipulation)null);
	}

	/**
	 * ファイルを移動する。
	 * 
	 * @param dest
	 *            移動先ファイル
	 * @param parentManipulation
	 *            親操作
	 * @throws VFSException
	 */
	public void moveTo(VFile dest, Manipulation parentManipulation)
			throws VFSException {
		moveTo(dest, DefaultOverwritePolicy.OVERWRITE, null);
	}

	/**
	 * ファイルを移動する。
	 * 
	 * @param dest
	 *            移動先
	 * @param policy
	 *            上書きポリシー
	 * @throws VFSException
	 */
	public void moveTo(VFile dest, OverwritePolicy policy) throws VFSException {
		moveTo(dest, policy, null);
	}
	
	/**
	 * ファイルを移動する。
	 * 
	 * @param dest
	 *            移動先
	 * @param policy
	 *            上書きポリシー
	 * @param parentManipulation
	 *            親操作
	 * @throws VFSException
	 */
	public void moveTo(VFile dest, OverwritePolicy policy,
			Manipulation parentManipulation) throws VFSException {
		moveTo(dest, policy, parentManipulation, false);
	}

	/**
	 * ファイルを移動する。
	 * 
	 * @param dest
	 *            移動先
	 * @param policy
	 *            上書きポリシー
	 * @param parentManipulation
	 *            親操作
	 *  @param noCopy
	 *            trueならコピーから削除の移動を行わない。
	 *  @throws VFSException
	 */
	public void moveTo(VFile dest, OverwritePolicy policy,
			Manipulation parentManipulation, boolean noCopy) throws VFSException {
		MoveManipulation manipulation = FileUtil.prepareMoveTo(this, dest,
				policy, parentManipulation);
		manipulation.setNoCopy(noCopy);
		manipulation.start();
	}

	/**
	 * ファイルをコピーする。
	 * 
	 * @param dest
	 *            コピー先
	 * @throws VFSException
	 */
	public void copyTo(VFile dest) throws VFSException {
		copyTo(dest, (Manipulation)null);
	}

	/**
	 * ファイルをコピーする。
	 * 
	 * @param dest
	 *            コピー先
	 * @param parentManipulation
	 *            親操作
	 * @throws VFSException
	 */
	public void copyTo(VFile dest, Manipulation parentManipulation)
			throws VFSException {
		copyTo(dest, DefaultOverwritePolicy.OVERWRITE, parentManipulation);
	}

	/**
	 * ファイルをコピーする。
	 * 
	 * @param dest
	 *            コピー先
	 * @param policy
	 *            上書きポリシー
	 * @throws VFSException
	 */
	public void copyTo(VFile dest, OverwritePolicy policy)
			throws VFSException {
		copyTo(dest, policy, null);
	}

	/**
	 * ファイルをコピーする。
	 * 
	 * @param dest
	 *            コピー先
	 * @param policy
	 *            上書きポリシー
	 * @param parentManipulation
	 *            親操作
	 * @throws VFSException
	 */
	public void copyTo(VFile dest, OverwritePolicy policy,
			Manipulation parentManipulation) throws VFSException {
		CopyFileManipulation manipulation = FileUtil.prepareCopyTo(this, dest,
				policy, parentManipulation);
		manipulation.start();
	}

	/**
	 * ファイルをコピーする。
	 * 
	 * @param dest
	 *            コピー先
	 * @param policy
	 *            上書きポリシー
	 * @param copiesPermission　trueならパーミッションのコピーを行う
	 * @param parentManipulation
	 *            親操作
	 * @throws VFSException
	 */
	public void copyTo(VFile dest, OverwritePolicy policy, boolean copiesPermission,
			Manipulation parentManipulation) throws VFSException {
		CopyFileManipulation manipulation = FileUtil.prepareCopyTo(this, dest,
				policy, copiesPermission, parentManipulation);
		manipulation.start();
	}

	/**
	 * 指定された名称の子ファイルオブジェクトを生成する。
	 * 
	 * @param name
	 *            子ファイルのファイル名称
	 * @return 引数で指定された子ファイル
	 * @throws VFSException
	 */
	public VFile getChild(String name) throws VFSException {
		FileName childFileName = fileName.createChild(name);
		return fileSystem.getFile(childFileName);
	}

	/**
	 * 相対パスで指定されるファイルを取得する。
	 * 
	 * @param relation
	 *            相対パス
	 * @return 相対ファイル
	 * @throws VFSException
	 */
	public VFile getRelativeFile(String relation) throws VFSException {
		FileName relativeFileName = fileName.resolveFileName(relation, getType());
		return fileSystem.getVFS().getFile(relativeFileName);
	}

	/**
	 * このファイルを基準にした他のファイルの相対パスを返す。 もしもファイルシステムが異なる場合は絶対パスを返す。
	 * 
	 * @param file
	 *            比較対照のファイル
	 * @return 相対パス
	 */
	public String getRelation(VFile file) {
		//	キャッシュの利用
		if (file.relationCache != null && this.equals(file.relationFileCache)) {
			return file.relationCache;
		}

		String rtn = getFileName().resolveRelation(file.getFileName());
		file.relationFileCache = this;
		file.relationCache = rtn;

		return rtn;
	}

	/**
	 * 文字列に変換する。
	 */
	public String toString() {
		return getAbsolutePath();
	}

	/**
	 * ハッシュ値を取得する。
	 */
	public int hashCode() {
		if (hashCode == -1) {
			hashCode = ("VFile:" + getAbsolutePath()).hashCode();
		}

		return hashCode;
	}

	/**
	 * ファイルの中身のMD5ハッシュ値の文字列を返す。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @return MD5ハッシュ文字列
	 * @throws VFSException
	 */
	public String getContentHashStr() throws VFSException {
		return getContentHashStr(null);
	}

	/**
	 * ファイルの中身のMD5ハッシュ値の文字列を返す。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @return MD5ハッシュ文字列
	 * @throws VFSException
	 */
	public String getContentHashStr(Manipulation parentManipulation)
			throws VFSException {
		byte[] hashByte = getContentHash(parentManipulation);
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < hashByte.length; i++) {
			int n = hashByte[i] & 0xff;

			if (n <= 16) {
				buffer.append("0");
			}
			buffer.append(Integer.toHexString(n).toUpperCase());
		}

		return buffer.toString();
	}

	/**
	 * ファイルの中身のMD5ハッシュ値を返す。
	 * 
	 * @return MD5ハッシュ
	 * @throws VFSException
	 */
	public byte[] getContentHash() throws VFSException {
		return getContentHash(null);
	}

	/**
	 * ファイルの中身のMD5ハッシュ値を返す。
	 * 
	 * @param parentManipulation
	 *            親操作
	 * @return MD5ハッシュ
	 * @throws VFSException
	 */
	public byte[] getContentHash(Manipulation parentManipulation)
			throws VFSException {
		if (!isFile(parentManipulation)) {
			return new byte[0];
		}

		MD5HashManipulation manipulation = FileUtil.prepareGetContentHash(this,
				parentManipulation);
		manipulation.start();
		return manipulation.getHash();
	}

	/**
	 * ファイルの中身を比較する。
	 * @param otherFile
	 * @param parentManipulation
	 * @return	同一ならtrueを返す。
	 * @throws VFSException
	 */
	public boolean contentEquals(VFile otherFile, Manipulation parentManipulation) throws VFSException {
		if(exists(parentManipulation) != otherFile.exists(parentManipulation)) {
			return false;
		}
		
		if(!exists(parentManipulation)) {
			return true;
		}
		
		String thisHash = getContentHashStr();
		String otherHash = otherFile.getContentHashStr();
		
		return thisHash.equals(otherHash);
	}
	
	/**
	 * ファイルが等しいか判定する。 クラスが同じでパスが等しいかで判定している。
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

		VFile otherFile = (VFile) o;
		return this.getFileName().equals(otherFile.getFileName());
	}

	/**
	 * このファイルの内部のファイルシステムのルートディレクトリを取得する。 もしも内部にルートが存在しない場合、nullを返す。
	 * 
	 * @return
	 */
	public VFile getInnerRoot() {
		return getFileSystem().getVFS().getInnerRoot(this);
	}

	/**
	 * ファイルの中身をbyte配列として返す。
	 * 
	 * @return @throws
	 *         VFSException
	 */
	public byte[] getContent() throws VFSException {
		return getContent(null);
	}

	/**
	 * ファイルの中身をbyte配列として返す。
	 * 
	 * @param parentManipulation
	 * @return @throws
	 *         VFSException
	 */
	public byte[] getContent(Manipulation parentManipulation)
			throws VFSException {
		if (!isFile(parentManipulation)) {
			return new byte[0];
		}

		GetContentManipulation manipulation = FileUtil
				.prepareGetContent(this, parentManipulation);
		manipulation.start();
		return manipulation.getContent();
	}
	
	
	/**
	 * タグ付けをする
	 * @param tag
	 */
	public void addTag(String tag) {
		TagDataBase tagDataBase = getFileSystem().getVFS().getTagDataBase();
		if(tagDataBase != null) {
			tagDataBase.addCommand(new FileTaggedCommand(this, tag));
		}
		tags = null;
	}
	
	/**
	 * タグを削除する
	 * @param tag
	 */
	public void removeTag(String tag) {
		TagDataBase tagDataBase = getFileSystem().getVFS().getTagDataBase();
		if(tagDataBase != null) {
			tagDataBase.addCommand(new TagRemovedCommand(this, tag));
		}
		tags = null;
	}
	
	/**
	 * タグを取得する。
	 */
	public List getTag() {
		if(tags == null) {
			TagDataBase tagDataBase = getFileSystem().getVFS().getTagDataBase();
			try {
				tags = tagDataBase != null ? tagDataBase.findTags(this) : null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return tags;
	}
	
	public void setTagCache(List tag) {
		this.tags = tag;
	}
}
