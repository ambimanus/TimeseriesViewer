/*
 * FSM Copyright (C) 2008 Christian Hinrichs
 * 
 * FSM is copyright under the GNU General Public License.
 * 
 * This file is part of FSM.
 * 
 * FSM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * FSM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with FSM.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.uniol.ui.tsv.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
 * This composite represents a progress bar.
 * 
 * @author <a href=
 *         "mailto:Christian%20Hinrichs%20%3Cchristian.hinrichs@uni-oldenburg.de%3E"
 *         >Christian Hinrichs, christian.hinrichs@uni-oldenburg.de</a>
 */
public class ProgressComposite extends Composite {

	/** underlying progress bar widget */
	private ProgressBar bar;
	/** parent dislay */
	private static Display display;
	/** parent shell */
	private static Shell shell;
	/** title of the progress window */
	private String title = new String();
	
	/**
	 * Creates a shell with this composite as contents.
	 */
	public ProgressComposite() {
		this(createShell(), SWT.APPLICATION_MODAL);
	}
	
	/**
	 * Creates this composite in the given shell.
	 * 
	 * @param parent
	 * @param style
	 */
	public ProgressComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	/**
	 * Initializes the progress bar.
	 */
	private void initialize() {
		setLayout(new FillLayout());
		
		bar = new ProgressBar(this, SWT.NONE);
		bar.setMinimum(0);
		bar.setMaximum(100);
	}
	
	/**
	 * Sets the new progress. This method is thread-safe.
	 * 
	 * @param p
	 */
	public void setProgress(int p) {
		final int pr = p;
		if (!isDisposed()) {
			getDisplay().syncExec(new Runnable() {
				public void run() {
					if (pr < 100) {
						if (!bar.isDisposed()) {
							bar.setSelection(pr);
						}
					} else {
						shell.close();
					}
				}
			});
		}
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Creates a new shell for this composite.
	 * 
	 * @return
	 */
	public static Shell createShell() {
		display = Display.getDefault();
		shell = new Shell(display);
		shell.setSize(600, 64);
		shell.setLayout(new FillLayout());
		shell.setText("Simulation progress");
		return shell;
	}
	
	/**
	 * Opens the parent shell and shows the contents. Blocks until the shell is
	 * closed.
	 */
	public void open() {
		if (shell == null) {
			shell = getShell();
		}
		shell.setText(title);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}