/*
 * @(#)IfExpression.java                       
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

import triangle.abstractSyntaxTrees.visitors.ExpressionVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class IfExpression extends Expression {

	public IfExpression(Expression e1AST, Expression e2AST, Expression e3AST, SourcePosition position) {
		super(position);
		E1 = e1AST;
		E2 = e2AST;
		E3 = e3AST;
	}

	public <TArg, TResult> TResult visit(ExpressionVisitor<TArg, TResult> v, TArg arg) {
		return v.visitIfExpression(this, arg);
	}

	public Expression E1;
	public Expression E2;
	public Expression E3;
}
