/*
 * @(#)Polyline.java                       
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

package triangle.treeDrawer;

/**
 * used to keep track of the position for components in the tree to be drawn
 */
class Polyline {
	int dx, dy;
	Polyline link;

	Polyline(int dx, int dy, Polyline link) {
		this.dx = dx;
		this.dy = dy;
		this.link = link;
	}
}
