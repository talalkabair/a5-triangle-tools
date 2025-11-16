/*
 * @(#)FuncDeclaration.java                       
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

import triangle.abstractSyntaxTrees.expressions.Expression;
import triangle.abstractSyntaxTrees.formals.FormalParameterSequence;
import triangle.abstractSyntaxTrees.terminals.Identifier;
import triangle.abstractSyntaxTrees.types.TypeDenoter;
import triangle.abstractSyntaxTrees.visitors.DeclarationVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class FuncDeclaration extends Declaration implements FunctionDeclaration {

	public FuncDeclaration(Identifier iAST, FormalParameterSequence fpsAST, TypeDenoter tAST, Expression eAST,
			SourcePosition position) {
		super(position);
		I = iAST;
		FPS = fpsAST;
		T = tAST;
		E = eAST;
	}

	public <TArg, TResult> TResult visit(DeclarationVisitor<TArg, TResult> v, TArg arg) {
		return v.visitFuncDeclaration(this, arg);
	}

	@Override
	public FormalParameterSequence getFormals() {
		return FPS;
	}
	
	@Override
	public TypeDenoter getType() {
		return T;
	}
	
	public final Identifier I;
	public final FormalParameterSequence FPS;
	public TypeDenoter T;
	public Expression E;
}
