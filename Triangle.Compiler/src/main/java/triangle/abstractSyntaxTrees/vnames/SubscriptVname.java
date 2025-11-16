/*
 * @(#)SubscriptVname.java                       
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

package triangle.abstractSyntaxTrees.vnames;

import triangle.abstractSyntaxTrees.expressions.Expression;
import triangle.abstractSyntaxTrees.visitors.VnameVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

/**
 * a value-or-variable name for an array, with an index, e.g. a[4]  (a is the vAST, 4 is the eAST) 
 */
public class SubscriptVname extends Vname {

	public SubscriptVname(Vname vAST, Expression eAST, SourcePosition position) {
		super(position);
		V = vAST;
		E = eAST;
	}

	public <TArg, TResult> TResult visit(VnameVisitor<TArg, TResult> v, TArg arg) {
		return v.visitSubscriptVname(this, arg);
	}

	public Expression E;
	public final Vname V;
}
