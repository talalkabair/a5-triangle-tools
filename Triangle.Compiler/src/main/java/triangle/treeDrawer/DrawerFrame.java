/*
 * @(#)DrawerFrame.java                       
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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

class DrawerFrame extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = -3650404598416929282L;

	public DrawerFrame(JPanel panel) {
		setSize(300, 200);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		setTitle("Triangle Compiler Abstract Syntax Tree");
		setSize(screenWidth / 2, screenHeight / 2);
		setLocation(screenWidth / 4, screenHeight / 4);
		// Image img = tk.getImage("icon.gif");
		// setIconImage(img);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		Container contentPane = getContentPane();
		final JScrollPane jScrollPane = new JScrollPane(panel);
		
		contentPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				jScrollPane.getHorizontalScrollBar().setValue(jScrollPane.getHorizontalScrollBar().getMaximum());
				jScrollPane.getHorizontalScrollBar().setValue(jScrollPane.getHorizontalScrollBar().getValue() / 2);
			}
		});

		contentPane.add(jScrollPane);
		
	}

}