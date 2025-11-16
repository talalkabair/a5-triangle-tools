/*
 * @(#)SingleFieldTypeDenoter.java               
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

package triangle.abstractSyntaxTrees.types;

import triangle.abstractSyntaxTrees.terminals.Identifier;
import triangle.abstractSyntaxTrees.visitors.TypeDenoterVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class SingleFieldTypeDenoter extends FieldTypeDenoter {

	public SingleFieldTypeDenoter(Identifier iAST, TypeDenoter tAST, SourcePosition position) {
		super(position);
		I = iAST;
		T = tAST;
	}

	public <TArg, TResult> TResult visit(TypeDenoterVisitor<TArg, TResult> v, TArg arg) {
		return v.visitSingleFieldTypeDenoter(this, arg);
	}
	
	@Override
	public int getSize() {
		return T.getSize();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof SingleFieldTypeDenoter) {
			SingleFieldTypeDenoter ft = (SingleFieldTypeDenoter) obj;
			return (this.I.spelling.compareTo(ft.I.spelling) == 0) && this.T.equals(ft.T);
		} else {
			return false;
		}
	}

	public final Identifier I;
	public TypeDenoter T;
}
