/*
 * @(#)Compiler.java
 *
 * Revisions and updates (c) 2022–2025 Sandy Brownlee. alexander.brownlee@stir.ac.uk
 *
 * Modified by Student B (2025) – Added Constant Folding Optimiser integration
 * for Task 2.
 *
 * Original release:
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * University of Glasgow & Robert Gordon University.
 * Educational use only.
 */

package triangle;

import triangle.abstractSyntaxTrees.Program;
import triangle.codeGenerator.Emitter;
import triangle.codeGenerator.Encoder;
import triangle.contextualAnalyzer.Checker;
import triangle.optimiser.ConstantFolder;
import triangle.syntacticAnalyzer.Parser;
import triangle.syntacticAnalyzer.Scanner;
import triangle.syntacticAnalyzer.SourceFile;
import triangle.treeDrawer.Drawer;

/**
 * Main driver class for the Triangle compiler.
 */
public class Compiler {

	/** Default output file for TAM object code. */
	private static String objectName = "obj.tam";

	/** Flags for visualisation and optimisation. */
	private static boolean showTree = false;
	private static boolean folding = false;

	/** Compiler components. */
	private static Scanner scanner;
	private static Parser parser;
	private static Checker checker;
	private static Encoder encoder;
	private static Emitter emitter;
	private static ErrorReporter reporter;
	private static Drawer drawer;

	/** Root AST node for the entire source program. */
	private static Program theAST;

	/**
	 * Compile the source program to TAM code.
	 *
	 * @param sourceName   path to the source file (.tri)
	 * @param objectName   name of the output TAM file
	 * @param showingAST   display AST graphically after contextual analysis
	 * @param showingTable display object details during code generation (unused)
	 * @return true if compilation succeeds, false otherwise
	 */
	private static boolean compileProgram(String sourceName, String objectName,
										  boolean showingAST, boolean showingTable) {

		System.out.println("********** Triangle Compiler (Java Version 2.1) **********");

		System.out.println("Syntactic Analysis ...");
		SourceFile source = SourceFile.ofPath(sourceName);

		if (source == null) {
			System.out.println("Can't access source file " + sourceName);
			System.exit(1);
		}

		scanner = new Scanner(source);
		reporter = new ErrorReporter(false);
		parser = new Parser(scanner, reporter);
		checker = new Checker(reporter);
		emitter = new Emitter(reporter);
		encoder = new Encoder(emitter, reporter);
		drawer = new Drawer();

		// === 1. Parse ===
		theAST = parser.parseProgram();
		if (reporter.getNumErrors() > 0) return false;

		// === 2. Contextual Analysis ===
		System.out.println("Contextual Analysis ...");
		checker.check(theAST);
		if (showingAST) drawer.draw(theAST);

		// === 3. Optional Optimisation: Constant Folding ===
		if (folding && reporter.getNumErrors() == 0) {
			System.out.println("Optimising (Constant Folding) ...");
			ConstantFolder folder = new ConstantFolder();
			theAST.visit(folder); // run optimiser visitor
		}

		// === 4. Code Generation ===
		if (reporter.getNumErrors() == 0) {
			System.out.println("Code Generation ...");
			encoder.encodeRun(theAST, showingTable);
		}

		// === 5. Save Output ===
		boolean success = (reporter.getNumErrors() == 0);
		if (success) {
			emitter.saveObjectProgram(objectName);
			System.out.println("Compilation was successful.");
		} else {
			System.out.println("Compilation was unsuccessful.");
		}
		return success;
	}

	/**
	 * Entry point for command-line execution.
	 * Usage:  tc <sourcefile.tri> [-o=output.tam] [tree] [folding]
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: tc filename [-o=outputfilename] [tree] [folding]");
			System.exit(1);
		}

		parseArgs(args);
		String sourceName = args[0];

		boolean compiledOK = compileProgram(sourceName, objectName, showTree, false);

		if (!showTree) {
			System.exit(compiledOK ? 0 : 1);
		}
	}

	/**
	 * Parse command-line flags.
	 */
	private static void parseArgs(String[] args) {
		for (String s : args) {
			String lower = s.toLowerCase();
			if (lower.equals("tree")) {
				showTree = true;
			} else if (lower.startsWith("-o=")) {
				objectName = s.substring(3);
			} else if (lower.equals("folding")) {
				folding = true;
			}
		}
	}
}
