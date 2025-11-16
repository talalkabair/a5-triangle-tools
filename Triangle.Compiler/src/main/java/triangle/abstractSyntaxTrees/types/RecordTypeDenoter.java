/*
 * @(#)RecordTypeDenoter.java               
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

import triangle.abstractSyntaxTrees.visitors.TypeDenoterVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class RecordTypeDenoter extends TypeDenoter {

	public RecordTypeDenoter(FieldTypeDenoter ftAST, SourcePosition position) {
		super(position);
		FT = ftAST;
	}

	public <TArg, TResult> TResult visit(TypeDenoterVisitor<TArg, TResult> v, TArg arg) {
		return v.visitRecordTypeDenoter(this, arg);
	}
	
	@Override
	public int getSize() {
		return FT.getSize();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof ErrorTypeDenoter) {
			return true;
		} else if (obj != null && obj instanceof RecordTypeDenoter) {
			return this.FT.equals(((RecordTypeDenoter) obj).FT);
		} else {
			return false;
		}
	}

	public FieldTypeDenoter FT;
}
