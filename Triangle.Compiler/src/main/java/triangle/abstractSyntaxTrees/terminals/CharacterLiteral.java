/*
 * @(#)CharacterLiteral.java                       
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

import triangle.abstractSyntaxTrees.visitors.LiteralVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class CharacterLiteral extends Terminal {

	public CharacterLiteral(String spelling, SourcePosition position) {
		super(spelling, position);
	}

	public <TArg, TResult> TResult visit(LiteralVisitor<TArg, TResult> v, TArg arg) {
		return v.visitCharacterLiteral(this, arg);
	}

	public <TArg, TResult> TResult visit(LiteralVisitor<TArg, TResult> visitor) {
		return visit(visitor, null);
	}

	public int getValue() {
		return spelling.charAt(1);
	}
}
