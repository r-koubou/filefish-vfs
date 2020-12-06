package com.nullfish.lib.vfs.impl.local.native_lib;

public class NativeFileUtil {
	/** JNI を使うかどうかを示します。 */
	private static boolean useJNI = true;
	
	static {
		try {
			System.loadLibrary("com_nullfish_lib_vfs_impl_local_native_lib_NativeFileUtil");
		} catch (UnsatisfiedLinkError e) {
			useJNI = false;
		}
	}
	
	public static boolean usesJNI() {
		return useJNI;
	}

	public static native LocalFileInfo listFile(String path);
}
