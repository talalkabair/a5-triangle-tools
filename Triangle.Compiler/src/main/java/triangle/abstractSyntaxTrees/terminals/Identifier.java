/*
 * @(#)Identifier.java                       
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

package triangle.abstractSyntaxTrees.terminals;

import triangle.abstractSyntaxTrees.AbstractSyntaxTree;
import triangle.abstractSyntaxTrees.types.TypeDenoter;
import triangle.abstractSyntaxTrees.visitors.IdentifierVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class Identifier extends Terminal {

	public Identifier(String spelling, SourcePosition position) {
		super(spelling, position);
		type = null;
		decl = null;
	}

	public TypeDenoter type;
	public AbstractSyntaxTree decl; // Either a Declaration or a FieldTypeDenoter

	public <TArg, TResult> TResult visit(IdentifierVisitor<TArg, TResult> visitor, TArg arg) {
		return visitor.visitIdentifier(this, arg);
	}

	public <TArg, TResult> TResult visit(IdentifierVisitor<TArg, TResult> visitor) {
		return visit(visitor, null);
	}
}
