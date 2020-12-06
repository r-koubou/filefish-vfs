/*
 * Created on 2004/06/25
 *
 */
package com.nullfish.lib.vfs.impl.local;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * @author shunji
 *
 */
public class TestFile extends File {
	public TestFile(String path) {
		super(path);
	}
	
	public TestFile(File parent, String child) {
		super(parent, child);
	}
	
	public TestFile(String parent, String child) {
		super(parent, child);
	}
	
	public boolean canRead() {
		System.out.println("read : " + getAbsolutePath());
		return super.canRead();
	}
	
	public boolean canWrite() {
		System.out.println("write : " + getAbsolutePath());
		return super.canWrite();
	}
	
	public long length() {
		System.out.println("length : " + getAbsolutePath());
		return super.length();
	}

	
	public boolean isFile() {
		System.out.println("isFile : " + getAbsolutePath());
		return super.isFile();
	}
	
	public boolean isDirectory() {
		System.out.println("isDirectory : " + getAbsolutePath());
		return super.isDirectory();
	}
	
	public long lastModified() {
		System.out.println("lastModified : " + getAbsolutePath());
		return super.lastModified();
	}
	
	public String[] list() {
		System.out.println("list : " + getAbsolutePath());
		return super.list();
	}
	
	public File[] listFiles() {
		System.out.println("listFiles : " + getAbsolutePath());
		
		String[] ss = list();
		if (ss == null) return null;
		int n = ss.length;
		File[] fs = new File[n];
		for (int i = 0; i < n; i++) {
		    fs[i] = new TestFile(getPath(), ss[i]);
		}
		return fs;
	}
	
	public File[] listFiles(FileFilter filter) {
		System.out.println("listFiles filter : " + getAbsolutePath());

		String ss[] = list();
		if (ss == null) return null;
		ArrayList v = new ArrayList();
		for (int i = 0 ; i < ss.length ; i++) {
		    File f = new TestFile(getPath(), ss[i]);
		    if ((filter == null) || filter.accept(f)) {
			v.add(f);
		    }
		}
		return (TestFile[])(v.toArray(new TestFile[0]));
	}
	
	public File[] listFiles(FilenameFilter filter) {
		System.out.println("listFiles namefilter : " + getAbsolutePath());

		String ss[] = list();
		if (ss == null) return null;
		ArrayList v = new ArrayList();
		for (int i = 0 ; i < ss.length ; i++) {
		    if ((filter == null) || filter.accept(this, ss[i])) {
			v.add(new TestFile(getPath(), ss[i]));
		    }
		}
		return (TestFile[])(v.toArray(new TestFile[0]));	}
}
