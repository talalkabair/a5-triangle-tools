/*
 * @(#)KnownValue.java                       
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

package triangle.codeGenerator.entities;

import triangle.abstractMachine.OpCode;
import triangle.abstractSyntaxTrees.vnames.Vname;
import triangle.codeGenerator.Emitter;
import triangle.codeGenerator.Frame;

public class KnownValue extends RuntimeEntity implements FetchableEntity {

	private final int value;

	public KnownValue(int size, int value) {
		super(size);
		this.value = value;
	}

	public final int getValue() {
		return value;
	}

	public void encodeFetch(Emitter emitter, Frame frame, int size, Vname vname) {
		// presumably offset = 0 and indexed = false
		emitter.emit(OpCode.LOADL, 0, value);
	}
}