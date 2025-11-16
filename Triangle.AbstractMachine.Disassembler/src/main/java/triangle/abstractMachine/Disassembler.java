/*
 * @(#)Disassembler.java                        
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Disassembles the TAM code in the given file, and displays the instructions on
 * standard output.
 *
 * For example:
 *
 * <pre>
 *   java TAM.Disassembler obj.tam
 * </pre>
 *
 * <p>
 * Copyright 1991 David A. Watt, University of Glasgow<br>
 * Copyright 1998 Deryck F. Brown, The Robert Gordon University<br>
 * </p>
 *
 */

public class Disassembler {

	static String objectName;

	static int CT;

	/**
	 * Writes the r-field of an instruction in the form "l<I>reg</I>r", where l and
	 * r are the bracket characters to use.
	 *
	 * @param leftbracket  the character to print before the register.
	 * @param r            the number of the register.
	 * @param rightbracket the character to print after the register.
	 */
	private static void writeR(char leftbracket, Register r, char rightbracket) {

		System.out.print(leftbracket);
		System.out.print(r.toString());
		System.out.print(rightbracket);
	}

	private static void writeR(char leftBracket, int r, char rightBracket) {
		var register = Register.values()[r];
		writeR(leftBracket, register, rightBracket);
	}

	/**
	 * Writes a void n-field of an instruction.
	 */
	private static void blankN() {
		System.out.print("      ");
	}

	// Writes the n-field of an instruction.
	/**
	 * Writes the n-field of an instruction in the form "(n)".
	 *
	 * @param n the integer to write.
	 */
	private static void writeN(int n) {
		System.out.print("(" + n + ") ");
		if (n < 10) {
			System.out.print("  ");
		} else if (n < 100) {
			System.out.print(" ");
		}
	}

	/**
	 * Writes the d-field of an instruction.
	 *
	 * @param d the integer to write.
	 */
	private static void writeD(int d) {
		System.out.print(d);
	}

	/**
	 * Writes the name of primitive routine with relative address d.
	 *
	 * @param d the displacement of the primitive routine.
	 */
	private static void writePrimitive(int d) {
		var primitive = Primitive.values()[d];
		switch (primitive) {
		case ID:
			System.out.print("id      ");
			break;
		case NOT:
			System.out.print("not     ");
			break;
		case AND:
			System.out.print("and     ");
			break;
		case OR:
			System.out.print("or      ");
			break;
		case SUCC:
			System.out.print("succ    ");
			break;
		case PRED:
			System.out.print("pred    ");
			break;
		case NEG:
			System.out.print("neg     ");
			break;
		case ADD:
			System.out.print("add     ");
			break;
		case SUB:
			System.out.print("sub     ");
			break;
		case MULT:
			System.out.print("mult    ");
			break;
		case DIV:
			System.out.print("div     ");
			break;
		case MOD:
			System.out.print("mod     ");
			break;
		case LT:
			System.out.print("lt      ");
			break;
		case LE:
			System.out.print("le      ");
			break;
		case GE:
			System.out.print("ge      ");
			break;
		case GT:
			System.out.print("gt      ");
			break;
		case EQ:
			System.out.print("eq      ");
			break;
		case NE:
			System.out.print("ne      ");
			break;
		case EOL:
			System.out.print("eol     ");
			break;
		case EOF:
			System.out.print("eof     ");
			break;
		case GET:
			System.out.print("get     ");
			break;
		case PUT:
			System.out.print("put     ");
			break;
		case GETEOL:
			System.out.print("geteol  ");
			break;
		case PUTEOL:
			System.out.print("puteol  ");
			break;
		case GETINT:
			System.out.print("getint  ");
			break;
		case PUTINT:
			System.out.print("putint  ");
			break;
		case NEW:
			System.out.print("new     ");
			break;
		case DISPOSE:
			System.out.print("dispose ");
			break;
		}
	}

	/**
	 * Writes the given instruction in assembly-code format.
	 *
	 * @param instr the instruction to display.
	 */
	private static void writeInstruction(Instruction instr) {

		switch (instr.opCode) {
		case LOAD:
			System.out.print("LOAD  ");
			writeN(instr.length);
			writeD(instr.operand);
			writeR('[', instr.register, ']');
			break;

		case LOADA:
			System.out.print("LOADA ");
			blankN();
			writeD(instr.operand);
			writeR('[', instr.register, ']');
			break;

		case LOADI:
			System.out.print("LOADI ");
			writeN(instr.length);
			break;

		case LOADL:
			System.out.print("LOADL ");
			blankN();
			writeD(instr.operand);
			break;

		case STORE:
			System.out.print("STORE ");
			writeN(instr.length);
			writeD(instr.operand);
			writeR('[', instr.register, ']');
			break;

		case STOREI:
			System.out.print("STOREI");
			writeN(instr.length);
			break;

		case CALL:
			System.out.print("CALL  ");
			if (instr.register == Register.PB) {
				blankN();
				writePrimitive(instr.operand);
			} else {
				writeR('(', instr.length, ')');
				System.out.print("  ");
				writeD(instr.operand);
				writeR('[', instr.register, ']');
			}
			break;

		case CALLI:
			System.out.print("CALLI ");
			break;

		case RETURN:
			System.out.print("RETURN");
			writeN(instr.length);
			writeD(instr.operand);
			break;

		case PUSH:
			System.out.print("PUSH  ");
			blankN();
			writeD(instr.operand);
			break;

		case POP:
			System.out.print("POP   ");
			writeN(instr.length);
			writeD(instr.operand);
			break;

		case JUMP:
			System.out.print("JUMP  ");
			blankN();
			writeD(instr.operand);
			writeR('[', instr.register, ']');
			break;

		case JUMPI:
			System.out.print("JUMPI ");
			break;

		case JUMPIF:
			System.out.print("JUMPIF");
			writeN(instr.length);
			writeD(instr.operand);
			writeR('[', instr.register, ']');
			break;

		case HALT:
			System.out.print("HALT  ");
		}
	}

	/**
	 * Writes all instructions of the program in code store.
	 */
	private static void disassembleProgram() {
		for (int addr = Machine.CB; addr < CT; addr++) {
			System.out.print(addr + ":  ");
			writeInstruction(Machine.code[addr]);
			System.out.println();
		}
	}

	// LOADING

	/**
	 * Loads the TAM object program into code store from the named file.
	 *
	 * @param objectName the name of the file containing the program.
	 */
	static void loadObjectProgram(String objectName) {

		var finished = false;

		try (var objectFile = new FileInputStream(objectName)) {
			var objectStream = new DataInputStream(objectFile);
			var addr = Machine.CB;
			while (!finished) {
				Machine.code[addr] = Instruction.read(objectStream);
				if (Machine.code[addr] == null) {
					finished = true;
				} else {
					addr = addr + 1;
				}
			}
			CT = addr;
		} catch (FileNotFoundException s) {
			CT = Machine.CB;
			System.err.println("Error opening object file: " + s);
		} catch (IOException s) {
			CT = Machine.CB;
			System.err.println("Error reading object file: " + s);
		}
	}

	// DISASSEMBLE

	public static void main(String[] args) {
		System.out.println("********** TAM Disassembler (Sun Version 2.1) **********");

		if (args.length == 1) {
			objectName = args[0];
		} else {
			objectName = "obj.tam";
		}

		loadObjectProgram(objectName);
		disassembleProgram();
	}
}
