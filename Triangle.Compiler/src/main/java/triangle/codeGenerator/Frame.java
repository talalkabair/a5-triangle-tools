/*
 * @(#)Frame.java                       
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

package triangle.codeGenerator;

import triangle.abstractMachine.Register;
import triangle.codeGenerator.entities.ObjectAddress;

public class Frame {

	public static final Frame Initial = new Frame(0, 0);

	private final int level;

	private final int size;

	private Frame(int level, int size) {
		this.level = level;
		this.size = size;
	}

	public final int getLevel() {
		return level;
	}

	public final int getSize() {
		return size;
	}

	public Frame expand(int increment) {
		return new Frame(level, size + increment);
	}

	public Frame replace(int size) {
		return new Frame(level, size);
	}

	public Frame push(int size) {
		return new Frame(level + 1, size);
	}

	/**
	 * Returns the display register appropriate for object code at the current
	 * static level to access a data object at the static level of the given
	 * address.
	 * 
	 * @param address the address of the data object
	 * @return the display register required for static addressing
	 */
	public Register getDisplayRegister(ObjectAddress address) {
		if (address.getLevel() == 0) {
			return Register.SB;
		}

		if (level - address.getLevel() <= 6) {
			return Register.values()[Register.LB.ordinal() + level - address.getLevel()]; // LB|L1|...|L6
		}

		// _errorReporter.ReportRestriction("can't access data more than 6 levels out");
		return Register.L6; // to allow code generation to continue
	}
}
