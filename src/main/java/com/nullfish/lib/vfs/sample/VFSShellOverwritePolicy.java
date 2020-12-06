/*
 * Created on 2004/04/02
 *
 */
package com.nullfish.lib.vfs.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.manipulation.OverwritePolicy;


/**
 * @author shunji
 *
 */
public class VFSShellOverwritePolicy implements OverwritePolicy {
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	VFile newDest;

	
	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.OverwritePolicy#isDoOverwrite(com.sexyprogrammer.lib.vfs.VFile, com.sexyprogrammer.lib.vfs.VFile)
	 */
	public int getOverwritePolicy(VFile srcFile, VFile dest) {
		System.out.println(dest.getAbsolutePath() + " exists.");
		System.out.println("Overwrite?(Yes/No/Cancel/Other file)");
		
		try {
			while(true) {
				String answer = reader.readLine().toLowerCase();
				if(answer.equals("y")) {
					return OVERWRITE;
				} else if(answer.equals("n")) {
					return NO_OVERWRITE;
				} else if(answer.equals("c")) {
					return STOP_ALL;
				} else if(answer.equals("o")) {
					while(true) {
						System.out.println("Input new file name.");
						String newFileName = reader.readLine();
						if(newFileName.length() > 0) {
							try {
								newDest = dest.getParent().getChild(newFileName);
								return NEW_DEST;
							} catch (VFSException e) {
								System.out.println("Wrong file name : " + newFileName);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return NO_OVERWRITE;
	}
	
	/* (non-Javadoc)
	 * @see com.sexyprogrammer.lib.vfs.manipulation.OverwritePolicy#getNewDestination()
	 */
	public VFile getNewDestination() {
		return newDest;
	}

	public void setFiles(VFile from, VFile to) {
		// TODO Auto-generated method stub
		
	}
}
