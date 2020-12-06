/*
 * 作成日: 2003/10/26
 * 
 */
package com.nullfish.lib.vfs.sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.WrongPathException;
import com.nullfish.lib.vfs.manipulation.CopyFileManipulation;
import com.nullfish.lib.vfs.manipulation.MoveManipulation;


/**
 * @author shunji
 * 
 * 仮想ファイルシステムを利用した、簡単なコマンドシェルソフトウェア。
 * 
 */
public class VFSShell {

	/*
	 * カレントディレクトリ
	 */
	VFile currentDir;

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	//	対応コマンド群
	public static final String MKDIR = "mkdir";
	public static final String TOUCH = "touch";
	public static final String DELETE = "rm";
	public static final String COPY = "cp";
	public static final String MOVE = "mv";
	public static final String LS = "ls";
	public static final String CD = "cd";
	public static final String EXIT = "exit";
	public static final String HASH = "hash";

	/**
	 * コンストラクタ
	 *
	 */
	public VFSShell() {
		try {
			currentDir = path2VFile(System.getProperty("user.home"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * メインの処理メソッド。
	 * １行ごとに入力を読み込み、コマンドを実行する。
	 */
	public void run() {
		BufferedReader reader =
			new BufferedReader(new InputStreamReader(System.in));
		String line;
		while (true) {
			try {
				System.out.print(currentDir.getSecurePath() + " > ");
				line = reader.readLine();

				StringTokenizer tokenizer = new StringTokenizer(line, " ");
				if (tokenizer.hasMoreTokens()) {
					String command = tokenizer.nextToken();

					if (MKDIR.equals(command)) {
						VFile file = path2VFile(tokenizer.nextToken());
						mkdir(file);
					} else if (TOUCH.equals(command)) {
						VFile file = path2VFile(tokenizer.nextToken());
						touch(file);
					} else if (DELETE.equals(command)) {
						VFile file = path2VFile(tokenizer.nextToken());
						delete(file);
					} else if (COPY.equals(command)) {
						VFile file = path2VFile(tokenizer.nextToken());
						VFile dest = path2VFile(tokenizer.nextToken());
						copy(file, dest);
					} else if (MOVE.equals(command)) {
						VFile file = path2VFile(tokenizer.nextToken());
						VFile dest = path2VFile(tokenizer.nextToken());
						move(file, dest);
					} else if (LS.equals(command)) {
						if (tokenizer.hasMoreTokens()) {
							VFile file = path2VFile(tokenizer.nextToken());
							ls(file);
						} else {
							ls(currentDir);
						}
					} else if (CD.equals(command)) {
						VFile file = path2VFile(tokenizer.nextToken());
						if (file.exists()) {
							currentDir = file;
						} else {
							System.out.println(
								file.getAbsolutePath() + " does not exists.");
						}
					} else if (EXIT.equals(command)) {
						System.exit(1);
					} else if (HASH.equals(command)) {
						VFile file = path2VFile(tokenizer.nextToken());
						System.out.println(file.getContentHashStr());
					}
				}
			} catch (VFSException e) {
				System.out.println(e.getErrorMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void mkdir(VFile file) {
		try {
			file.createDirectory();
			System.out.println("\nfinished");
		} catch (VFSException e) {
			System.out.println( e.getErrorMessage() );
			//e.printStackTrace();
		}
	}

	private void touch(VFile file) {
		try {
			file.createFile();
			System.out.println("\nfinished");
		} catch (VFSException e) {
			System.out.println( e.getErrorMessage() );
			//e.printStackTrace();
		}
	}

	private void delete(VFile file) {
		try {
			file.delete();
			System.out.println("\nfinished");
		} catch (VFSException e) {
			System.out.println( e.getErrorMessage() );
			//e.printStackTrace();
		}
	}

	private void copy(VFile file, VFile dest) {
		long start = System.currentTimeMillis();
		try {
			if(dest.exists() && dest.isDirectory()) {
				dest = dest.getChild(file.getName());
			}
			
			CopyFileManipulation m =
				file.getManipulationFactory().getCopyFileManipulation(file);
			m.setDest(dest);
			m.setOverwritePolicy(new VFSShellOverwritePolicy());

			m.startAsync();

			while (!m.isFinished()) {
				System.out.println(m.getCurrentManipulation().getProgressMessage());
				System.out.println(m.getProgress() + "/" + m.getProgressMax());
				System.out.println(
					m.getCurrentManipulation().getProgress()
						+ "/"
						+ m.getCurrentManipulation().getProgressMax());

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

//			m.start();
			
			System.out.println("\nfinished");
		} catch (VFSException e) {
			System.out.println( e.getErrorMessage() );
			//e.printStackTrace();
		}
		System.out.println(
			(System.currentTimeMillis() - start) + "mili seconds");
	}

	private void move(VFile file, VFile dest) {
		try {
			if(dest.exists() && dest.isDirectory()) {
				dest = dest.getChild(file.getName());
			}
			
			MoveManipulation m = file.getManipulationFactory().getMoveManipulation(file);
			m.setDest(dest);
			m.setOverwritePolicy(new VFSShellOverwritePolicy());
/*
			m.executeAsync();

			while (!m.isFinished()) {
				System.out.println(m.getProgress() + "/" + m.getProgressMax());
				System.out.println(
					m.getCurrentManipulation().getProgress()
						+ "/"
						+ m.getCurrentManipulation().getProgressMax());

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
*/
			m.start();
			
			System.out.println("\nfinished");
		} catch (VFSException e) {
			System.out.println( e.getErrorMessage() );
			//e.printStackTrace();
		}
	}

	private void ls(VFile file) {
		try {
			VFile[] files = file.getChildren();
			for (int i = 0; i < files.length; i++) {
				System.out.println(
					dateFormat.format(files[i].getTimestamp()) + " " + 
					files[i].getPermission().getOwner() + "\t" + 
					files[i].getPermission().getGroup() + "\t" + 
					files[i].getPermission().getPermissionString() + "\t" +
					files[i].getLength() + "\t" +
					files[i].getName());
			}

			System.out.println("\nfinished");
		} catch (VFSException e) {
			System.out.println( e.getErrorMessage() );
			e.printStackTrace();
		}
	}

	private VFile path2VFile(String path) throws WrongPathException {
		try {
			VFile rtn = VFS.getInstance().getFile(path);
			//rtn.getFileSystem().open();
			return rtn;
		} catch (Exception e) {
			//e.printStackTrace();
		}

		try {
			VFile rtn =
				VFS.getInstance().getFile(
					currentDir.getFileName().resolveFileName(path, FileType.DIRECTORY));
			return rtn;
		} catch (Exception e) {
			//e.printStackTrace();
		}

		throw new WrongPathException(path);
	}

	public static void main(String[] args) {
		VFSShell shell = new VFSShell();
		shell.run();
	}
}
