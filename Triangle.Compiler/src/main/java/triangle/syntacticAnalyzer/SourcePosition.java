/*
 * @(#)SourcePosition.java                       
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

public class SourcePosition {

	public int start, finish;

	public SourcePosition() {
		start = 0;
		finish = 0;
	}

	public SourcePosition(int s, int f) {
		start = s;
		finish = f;
	}

	@Override
	public String toString() {
		return "(" + start + ", " + finish + ")";
	}
}
