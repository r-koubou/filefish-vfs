/*
 * Created on 2004/03/18
 *
 */
package com.nullfish.lib.vfs;

import java.io.File;
import java.net.URLClassLoader;

/**
 * @author shunji
 *
 */
public class FileSystemClassLoader extends ClassLoader {
	URLClassLoader loader;
	
	public static final String ZIP_EXTENSION = ".zip";
	
	public static final String JAR_EXTENSION = ".jar";
	
	public FileSystemClassLoader(File directory) {
		File[] files = directory.listFiles();
		
		for(int i=0; i<files.length; i++) {
			String fileName = files[i].getName().toLowerCase();
			if(files[i].isFile() 
					&& fileName.endsWith(ZIP_EXTENSION)|| fileName.endsWith(JAR_EXTENSION)) {
				
			}
		}
	}
}
