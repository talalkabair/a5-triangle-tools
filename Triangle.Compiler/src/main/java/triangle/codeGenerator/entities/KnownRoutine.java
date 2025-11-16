/*
 * @(#)KnownRoutine.java                       
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
import triangle.abstractMachine.Register;
import triangle.codeGenerator.Emitter;
import triangle.codeGenerator.Frame;

public class KnownRoutine extends RuntimeEntity implements RoutineEntity {

	private final ObjectAddress address;

	public KnownRoutine(int size, int level, int displacement) {
		super(size);
		address = new ObjectAddress(level, displacement);
	}

	public final ObjectAddress getAddress() {
		return address;
	}

	public void encodeCall(Emitter emitter, Frame frame) {
		emitter.emit(OpCode.CALL, frame.getDisplayRegister(address), Register.CB, address.getDisplacement());
	}

	public void encodeFetch(Emitter emitter, Frame frame) {
		emitter.emit(OpCode.LOADA, frame.getDisplayRegister(address), 0);
		emitter.emit(OpCode.LOADA, Register.CB, address.getDisplacement());
	}

}
