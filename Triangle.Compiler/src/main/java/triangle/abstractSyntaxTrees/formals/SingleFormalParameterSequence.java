/*
 * @(#)SingleFormalParameterSequence.java               
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

package triangle.abstractSyntaxTrees.formals;

import triangle.abstractSyntaxTrees.visitors.FormalParameterSequenceVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class SingleFormalParameterSequence extends FormalParameterSequence {

	public SingleFormalParameterSequence(FormalParameter fpAST, SourcePosition position) {
		super(position);
		FP = fpAST;
	}

	public <TArg, TResult> TResult visit(FormalParameterSequenceVisitor<TArg, TResult> v, TArg arg) {
		return v.visitSingleFormalParameterSequence(this, arg);
	}

	@Override
	public boolean equals(Object fpsAST) {
		if (fpsAST instanceof SingleFormalParameterSequence) {
			SingleFormalParameterSequence sfpsAST = (SingleFormalParameterSequence) fpsAST;
			return FP.equals(sfpsAST.FP);
		} else {
			return false;
		}
	}

	public final FormalParameter FP;
}
