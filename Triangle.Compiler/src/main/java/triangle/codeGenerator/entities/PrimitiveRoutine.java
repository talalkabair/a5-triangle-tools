/*
 * @(#)PrimitiveRoutine.java                       
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
import triangle.abstractMachine.Primitive;
import triangle.abstractMachine.Register;
import triangle.codeGenerator.Emitter;
import triangle.codeGenerator.Frame;

public class PrimitiveRoutine extends RuntimeEntity implements RoutineEntity {

	private final Primitive primitive;

	public PrimitiveRoutine(int size, Primitive primitive) {
		super(size);
		this.primitive = primitive;
	}

	public final Primitive getPrimitive() {
		return primitive;
	}

	public void encodeCall(Emitter emitter, Frame frame) {
		if (primitive != Primitive.ID) {
			emitter.emit(OpCode.CALL, Register.PB, primitive);
		}
	}

	public void encodeFetch(Emitter emitter, Frame frame) {
		emitter.emit(OpCode.LOADA, 0, Register.SB, 0);
		emitter.emit(OpCode.LOADA, Register.PB, primitive);
	}

}