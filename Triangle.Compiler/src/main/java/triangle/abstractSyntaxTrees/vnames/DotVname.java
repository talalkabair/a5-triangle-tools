/*
 * @(#)DotVname.java                       
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

import triangle.abstractSyntaxTrees.terminals.Identifier;
import triangle.abstractSyntaxTrees.visitors.VnameVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

/**
 * Value (constant) or variable names including a sub-parts separated by dots. 
 * Example: fruitbox.applecount (fruitbox is a record, containing a variable applecount)
 */
public class DotVname extends Vname {

	public DotVname(Vname vAST, Identifier iAST, SourcePosition position) {
		super(position);
		V = vAST;
		I = iAST;
	}

	public <TArg, TResult> TResult visit(VnameVisitor<TArg, TResult> v, TArg arg) {
		return v.visitDotVname(this, arg);
	}

	public final Identifier I;
	public final Vname V;
}
