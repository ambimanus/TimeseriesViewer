package de.uniol.ui.tsv.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.csvreader.CsvReader;

import de.uniol.ui.tsv.ui.LineChartDialog;
import de.uniol.ui.tsv.ui.TimeseriesCollector;

/**
 * CRD5110 Measurebox parser & viewer
 * 
 * @author <a href=
 *         "mailto:Christian%20Hinrichs%20%3Cchristian.hinrichs@uni-oldenburg.de%3E"
 *         >Christian Hinrichs, christian.hinrichs@uni-oldenburg.de</a>
 */
public class CRD5110 {

	/** Print parse errors? */
	public final static boolean DEBUG = false;
	/** Time format relative or absolute? */
	public final static boolean TIMEFORMAT_RELATIVE = true;
	/** Average all X lines */
	public final static int COMBINE = 1;
	
	/** Voltage scale, will be read from data file */
	private static double scale_v = 0.0;
	/** Current scale, will be read from data file */
	private static double scale_c = 0.0;
	/** Time of first data point */
	private static double time_start = -1.0;
	
	/**
	 * @param args - path to data file (optional)
	 */
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
			// Prepare data collectors
			TimeseriesCollector col_v = new TimeseriesCollector("Voltage");
			TimeseriesCollector col_c = new TimeseriesCollector("Current");
			TimeseriesCollector col_p = new TimeseriesCollector("Power");
			TimeseriesCollector col_vars = new TimeseriesCollector("VARS");
			TimeseriesCollector col_pf = new TimeseriesCollector("Power Factor");
			TimeseriesCollector col_f = new TimeseriesCollector("Frequency");
			try {
				// Configure parser
				CsvReader reader = new CsvReader(file);
				reader.setDelimiter(';');
				reader.setTextQualifier('\0');
				// Get scales from header line
				if (reader.readRecord()) {
					scale_v = Double.parseDouble(reader.get(4));
					scale_c = Double.parseDouble(reader.get(5));
				}
				// Helper variables
				int counter = 1;
				double current_v = 0.0;
				double current_c = 0.0;
				double current_p = 0.0;
				double current_vars = 0.0;
				double current_pf = 0.0;
				double current_f = 0.0;
				double time = 0.0; // milliseconds
				double mean_v = 0.0;
				double mean_c = 0.0;
				double mean_p = 0.0;
				double mean_vars = 0.0;
				double mean_pf = 0.0;
				double mean_f = 0.0;
				// Parse file
				while (reader.readRecord()) {
					// Read current values
					try {
						time = Double.parseDouble(reader.get(2)) * 1000.0;
						current_v = v(Double.parseDouble(reader.get(3)));
						current_c = c(Double.parseDouble(reader.get(4)));
						current_p = p(Double.parseDouble(reader.get(5)));
						current_vars = vars(Double.parseDouble(reader.get(6)));
						current_pf = pf(Double.parseDouble(reader.get(7)));
						current_f = f(Double.parseDouble(reader.get(8)));
					} catch (NumberFormatException nfe) {
						// Garbage data, skip this record
						if (DEBUG) {
							// Print error
							System.err.println("Skipping record "
									+ reader.getCurrentRecord() + " (" + nfe
									+ ").");
						}
						continue;
					}
					// Set starting time
					if (time_start < 0.0) {
						time_start = time;
					}
					// Process read values
					if (TIMEFORMAT_RELATIVE) {
						time -= time_start;
					}
					mean_v += current_v;
					mean_c += current_c;
					mean_p += current_p;
					mean_vars += current_vars;
					mean_pf += current_pf;
					mean_f += current_f;
					if (counter < COMBINE) {
						counter++;
					} else {
						col_v.addObservation(time, mean_v / ((double) counter));
						col_c.addObservation(time, mean_c / ((double) counter));
						col_p.addObservation(time, mean_p / ((double) counter));
						col_vars.addObservation(time, mean_vars / ((double) counter));
						col_pf.addObservation(time, mean_pf / ((double) counter));
						col_f.addObservation(time, mean_f / ((double) counter));
						
						counter = 1;
						mean_v = 0.0;
						mean_c = 0.0;
						mean_p = 0.0;
						mean_vars = 0.0;
						mean_pf = 0.0;
						mean_f = 0.0;
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Prepare chart
			LineChartDialog lcd = new LineChartDialog(shell, f.getName(),
					"Time", "Values", "min", "units", true);
			// Fill chart
			lcd.addSeries(col_v);
			lcd.addSeries(col_c);
			lcd.addSeries(col_p);
			lcd.addSeries(col_vars);
			lcd.addSeries(col_pf);
			lcd.addSeries(col_f);
			// Finish chart
			lcd.create();
			// Open window
			shell.setText("Timeseries Viewer - " + f.getName());
			shell.setSize(900, 500);
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		}
		display.dispose();
	}
	
	/*
	 * Value calculations according to CRD5110.pdf
	 */
	
	private static double v(double value) {
		return value * scale_v;
	}
	
	private static double c(double value) {
		return value * scale_c;
	}
	
	private static double p(double value) {
		return value * scale_v * scale_c;
	}
	
	private static double vars(double value) {
		return value * scale_v * scale_c;
	}
	
	private static double pf(double value) {
		return value * 100.0;
	}
	
	private static double f(double value) {
		return value;
	}
}