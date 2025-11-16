/*
 * @(#)DrawerPanel.java                       
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

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

class DrawerPanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 565914745506889669L;
	private Drawer drawer;

	public DrawerPanel(Drawer drawer) {
		setPreferredSize(new Dimension(4096, 4096));
		this.drawer = drawer;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawer.paintAST(g);
	}
}