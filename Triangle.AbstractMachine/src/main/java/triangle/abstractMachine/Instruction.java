/*
 * @(#)Instruction.java                        
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

public class Instruction {

	// Java has no type synonyms, so the following representations are
	// assumed:
	//
	// type
	// OpCode = 0..15; {4 bits unsigned}
	// Length = 0..255; {8 bits unsigned}
	// Operand = -32767..+32767; {16 bits signed}

	// Represents TAM instructions.
	final OpCode opCode;
	final Register register;
	final int length;
	int operand; // Not final to allow for patching jump address

	public Instruction(OpCode opcode, Register register, int length, int operand) {
		this.opCode = opcode;
		this.register = register;
		this.length = length;
		this.operand = operand;
	}

	public void setOperand(int operand) {
		this.operand = operand;
	}
	
	public void write(DataOutputStream output) throws IOException {
		output.writeInt(opCode.ordinal());
		output.writeInt(register.ordinal());
		output.writeInt(length);
		output.writeInt(operand);
	}

	public static Instruction read(DataInputStream input) throws IOException {
		try {
			var opCode = OpCode.values()[input.readInt()];
			var register = Register.values()[input.readInt()];
			var length = input.readInt();
			var operand = input.readInt();
			return new Instruction(opCode, register, length, operand);
		} catch (EOFException s) {
			return null;
		}
	}
}
