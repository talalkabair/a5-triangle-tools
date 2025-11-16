/*
 * @(#)SequentialDeclaration.java               
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

package triangle.abstractSyntaxTrees.declarations;

import triangle.abstractSyntaxTrees.visitors.DeclarationVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class SequentialDeclaration extends Declaration {

	public SequentialDeclaration(Declaration d1AST, Declaration d2AST, SourcePosition position) {
		super(position);
		D1 = d1AST;
		D2 = d2AST;
	}

	public <TArg, TResult> TResult visit(DeclarationVisitor<TArg, TResult> v, TArg arg) {
		return v.visitSequentialDeclaration(this, arg);
	}

	public final Declaration D1, D2;
}
