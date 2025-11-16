/*
 * @(#)IdentificationTable.java               
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

package triangle.contextualAnalyzer;

import triangle.abstractSyntaxTrees.declarations.Declaration;

public final class IdentificationTable {

	private int level;
	private IdEntry latest;

	public IdentificationTable() {
		level = 0;
		latest = null;
	}

	// Opens a new level in the identification table, 1 higher than the
	// current topmost level.

	public void openScope() {
		level++;
	}

	// Closes the topmost level in the identification table, discarding
	// all entries belonging to that level.

	public void closeScope() {
		// Presumably, idTable.level > 0.
		var entry = this.latest;
		while (entry.level == this.level) {
			entry = entry.previous;
		}

		this.level--;
		this.latest = entry;
	}

	// Makes a new entry in the identification table for the given identifier
	// and attribute. The new entry belongs to the current level.
	// duplicated is set to to true iff there is already an entry for the
	// same identifier at the current level.

	public void enter(String id, Declaration attr) {
		attr.duplicated = retrieve(id, true) != null;
		this.latest = new IdEntry(id, attr, this.level, this.latest);
	}

	// Finds an entry for the given identifier in the identification table,
	// if any. If there are several entries for that identifier, finds the
	// entry at the highest level, in accordance with the scope rules.
	// Returns null iff no entry is found.
	// otherwise returns the attribute field of the entry found.

	public Declaration retrieve(String id) {
		return retrieve(id, false);
	}

	// thisLevelOnly limits the search to only the current level
	
	public Declaration retrieve(String id, boolean thisLevelOnly) {
		var entry = this.latest;
		while (true) {
			if (entry == null || (thisLevelOnly && entry.level < this.level)) {
				break;
			} else if (entry.id.equals(id)) {
				return entry.attr;
			} else {
				entry = entry.previous;
			}
		}

		return null;
	}

}
