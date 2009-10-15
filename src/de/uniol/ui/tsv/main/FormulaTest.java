package de.uniol.ui.tsv.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.uniol.ui.tsv.ui.LineChartDialog;
import de.uniol.ui.tsv.ui.TimeseriesCollector;

/**
 * Graph viewer.
 * 
 * @author <a href=
 *         "mailto:Christian%20Hinrichs%20%3Cchristian.hinrichs@uni-oldenburg.de%3E"
 *         >Christian Hinrichs, christian.hinrichs@uni-oldenburg.de</a>
 */
public class FormulaTest {

	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout(SWT.VERTICAL));

		TimeseriesCollector col_1 = new TimeseriesCollector("(n²+9n)/2");
		TimeseriesCollector col_2 = new TimeseriesCollector("n²+10");
		for (int i = 0; i < 20; i++) {
			// (n²+9n)/2
			col_1.addObservation(i, (i * i + 9 * i) / 2);
			col_2.addObservation(i, i * i + 10);
		}
		// Prepare chart
		LineChartDialog lcd = new LineChartDialog(shell, "Graph viewer",
				"Time", "Values", "min", "units", true);
		// Fill chart
		lcd.addSeries(col_1);
		lcd.addSeries(col_2);
		// Finish chart
		lcd.create();
		// Open window
		shell.setText("Timeseries Viewer - " + "Graph viewer");
		shell.setSize(900, 500);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}
}