/*
 * @(#)Machine.java                        
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

package triangle.abstractMachine;

public final class Machine {

	public final static int maxRoutineLevel = 7;

	// WORDS AND ADDRESSES

	// Java has no type synonyms, so the following representations are
	// assumed:
	//
	// type
	// Word = -32767..+32767; {16 bits signed}
	// DoubleWord = -2147483648..+2147483647; {32 bits signed}
	// CodeAddress = 0..+32767; {15 bits unsigned}
	// DataAddress = 0..+32767; {15 bits unsigned}

	// INSTRUCTIONS

	// CODE STORE

	public static Instruction[] code = new Instruction[1024];

	// CODE STORE REGISTERS

	public final static int CB = 0, PB = 1024, // = upper bound of code array + 1
			PT = 1052; // = PB + 28

	// REGISTER NUMBERS

	// DATA REPRESENTATION

	public final static int booleanSize = 1, characterSize = 1, integerSize = 1, addressSize = 1,
			closureSize = 2 * addressSize,

			linkDataSize = 3 * addressSize,

			falseRep = 0, trueRep = 1, maxintRep = 32767;

}
