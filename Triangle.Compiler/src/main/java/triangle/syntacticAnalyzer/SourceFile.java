/*
 * @(#)SourceFile.java                       
 * 
 * Revisions and updates (c) 2022-2025 Sandy Brownlee. alexander.brownlee@stir.ac.uk
 * 
 * Original release:
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */

package triangle.syntacticAnalyzer;

import java.net.URL;

public class SourceFile {

	public static final char EOL = '\n';
	public static final char EOT = '\u0000';

	java.io.File sourceFile;
	java.io.InputStream source;
	int currentLine;

	public static SourceFile ofPath(String pathname) {
		try {
			SourceFile sf = new SourceFile();
			sf.sourceFile = new java.io.File(pathname);
			sf.source = new java.io.FileInputStream(sf.sourceFile);
			return sf;
		} catch (java.io.IOException s) {
			return null;
		}
	}
	
	public static SourceFile fromResource(String handle) {
		SourceFile sf = new SourceFile();
		//sf.sourceFile = new java.io.File(pathname);
		sf.source = sf.getClass().getResourceAsStream(handle);
		return sf;
	}

	private SourceFile() {
		currentLine = 1;
	}

	char getSource() {
		try {
			int c = source.read();

			if (c == -1) {
				c = EOT;
			} else if (c == EOL) {
				currentLine++;
			}
			return (char) c;
		} catch (java.io.IOException s) {
			return EOT;
		}
	}

	int getCurrentLine() {
		return currentLine;
	}
}
