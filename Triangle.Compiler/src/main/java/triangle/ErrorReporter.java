/*
 * @(#)ErrorReporter.java                       
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

package triangle;

import triangle.syntacticAnalyzer.SourcePosition;

public class ErrorReporter {

	private int numErrors;
	
	private boolean throwExceptions;

	/**
	 * @param throwExceptions if true, throw exceptions (good for unit tests) otherwise write to stdout
	 */
	public ErrorReporter(boolean throwExceptions) {
		numErrors = 0;
		this.throwExceptions = throwExceptions;
	}

	public void reportError(String message, String tokenName, SourcePosition pos) {

		numErrors++;
		
		String s = ("ERROR: ");

		for (int p = 0; p < message.length(); p++)
			if (message.charAt(p) == '%')
				s += tokenName;
			else
				s += message.charAt(p);
		s += (" " + pos.start + ".." + pos.finish);
		
		if (throwExceptions) {
			throw new RuntimeException(s);
		} else {
			System.out.println(s);
		}
		
	}

	public void reportRestriction(String message) {
		System.out.println("RESTRICTION: " + message);
	}
	
	public int getNumErrors() {
		return numErrors;
	}
}
