/*
 * @(#)CharacterExpression.java                       
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

package triangle.abstractSyntaxTrees.expressions;

import triangle.abstractSyntaxTrees.terminals.CharacterLiteral;
import triangle.abstractSyntaxTrees.visitors.ExpressionVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class CharacterExpression extends Expression {

	public CharacterExpression(CharacterLiteral clAST, SourcePosition position) {
		super(position);
		CL = clAST;
	}

	public <TArg, TResult> TResult visit(ExpressionVisitor<TArg, TResult> v, TArg arg) {
		return v.visitCharacterExpression(this, arg);
	}

	public final CharacterLiteral CL;
	
	@Override
	public boolean isLiteral() {
		return true;
	}
	
	@Override
	public int getValue() {
		return CL.getValue();
	}
}
