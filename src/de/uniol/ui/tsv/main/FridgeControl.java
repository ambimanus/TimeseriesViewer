package de.uniol.ui.tsv.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import de.uniol.ui.tsv.ui.LineChartDialog;
import de.uniol.ui.tsv.ui.TimeseriesCollector;

public class FridgeControl {
	
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		String file = null;
		File f = null;
		if (args.length > 0 && args[0].length() > 0) {
			f = new File(args[0]);
			if (f.exists()) {
				file = args[0];
			}
		}
		if (file == null) {
			// Query data file
			FileDialog fd = new FileDialog(shell, SWT.OPEN);
			file = fd.open();
			if (file != null) {
				f = new File(file);
			}
		}
		if (f != null && f.exists()) {
			TimeseriesCollector col_t = new TimeseriesCollector("Temperature T");
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = null;
				while((line = br.readLine()) != null) {
					if (line.startsWith("#")) {
						String[] tokens = line.split("\t");
						long time = Long.parseLong(tokens[1]);
						float temp = Float.parseFloat(tokens[2]);
						col_t.addObservation(time * 1000L, temp);
					}
				}
			} catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			// Prepare charts
			LineChartDialog lcd = new LineChartDialog(shell, f.getName(),
					"Time (h)", "Temperature (°C)", "min", "°C", 5.5, 10.0,
					true);
//			StepChartDialog scd = new StepChartDialog(shell, "Load progress",
//					"Time (h)", "Load (W)", "min", "W", 0.0, 70.0);
			// Fill charts
			lcd.addSeries(col_t);
			// Finish charts
			lcd.create();
//			scd.create();
			// Open window
			shell.setText("Timeseries Viewer - " + f.getName());
//			shell.setSize(900, 1000);
			shell.setSize(900, 500);
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		}
		display.dispose();
	}
}