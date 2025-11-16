package triangle.codeGenerator;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import triangle.ErrorReporter;
import triangle.abstractMachine.Instruction;
import triangle.abstractMachine.Machine;
import triangle.abstractMachine.OpCode;
import triangle.abstractMachine.Primitive;
import triangle.abstractMachine.Register;

public class Emitter {

	// OBJECT CODE

	// Implementation notes:
	// Object code is generated directly into the TAM Code Store, starting at
	// CB.
	// The address of the next instruction is held in nextInstrAddr.

	ErrorReporter errorReporter;

	int nextInstrAddr;

	public Emitter(ErrorReporter errorReporter) {
		this.errorReporter = errorReporter;
		nextInstrAddr = Machine.CB;
	}

	public int getNextInstrAddr() {
		return nextInstrAddr;
	}

	public int emit(OpCode op) {
		return emit(op, 0, Register.CB, 0);
	}

	public int emit(OpCode op, int operand) {
		return emit(op, 0, Register.CB, operand);
	}

	public int emit(OpCode op, int length, int operand) {
		return emit(op, length, Register.CB, operand);
	}

	public int emit(OpCode op, Register staticRegister, Register register, int operand) {
		return emit(op, staticRegister.ordinal(), register, operand);
	}

	public int emit(OpCode op, Register register, int operand) {
		return emit(op, 0, register, operand);
	}

	public int emit(OpCode op, Register register) {
		return emit(op, 0, register, 0);
	}

	public int emit(OpCode op, int length, Register register) {
		return emit(op, length, register, 0);
	}

	public int emit(OpCode op, Register register, Primitive primitive) {
		return emit(op, 0, register, primitive.ordinal());
	}

	/**
	 * Appends an instruction, with the given fields, to the object code.
	 *
	 * @param op       the opcode
	 * @param length   the length field
	 * @param register the register field
	 * @param operand  the operand field
	 * @return the code address of the new instruction
	 **/
	public int emit(OpCode op, int length, Register register, int operand) {

		if (length > 255) {
			errorReporter.reportRestriction("length of operand can't exceed 255 words");
			length = 255; // to allow code generation to continue
		}

		var nextInstr = new Instruction(op, register, length, operand);

		var currentInstrAddr = nextInstrAddr;
		if (nextInstrAddr == Machine.PB) {
			errorReporter.reportRestriction("too many instructions for code segment");
		} else {
			Machine.code[nextInstrAddr++] = nextInstr;
		}
		return currentInstrAddr;

	}

	// Patches the d-field of the instruction at address addr with the next
	// instruction address.
	public void patch(int addr) {
		Machine.code[addr].setOperand(nextInstrAddr);
	}

	/**
	 * Saves the object program in the given object file.
	 * 
	 * @param objectFile the object file
	 */
	public void saveObjectProgram(String objectFileName) {
		try (var objectFile = new FileOutputStream(objectFileName)) {
			var objectStream = new DataOutputStream(objectFile);
			for (var addr = Machine.CB; addr < nextInstrAddr; addr++) {
				Machine.code[addr].write(objectStream);
			}
		} catch (FileNotFoundException fnfe) {
			System.err.println("Error opening object file: " + fnfe);
		} catch (IOException ioe) {
			System.err.println("Error writing object file: " + ioe);
		}
	}
}
