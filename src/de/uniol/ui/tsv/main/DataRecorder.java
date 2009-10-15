package de.uniol.ui.tsv.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.csvreader.CsvReader;

import de.uniol.ui.tsv.ui.LineChartDialog;
import de.uniol.ui.tsv.ui.TimeseriesCollector;

public class DataRecorder {

	public final static int COMBINE = 3;
	
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		FileDialog fd = new FileDialog(shell, SWT.OPEN);
		String file = fd.open();
		if (file != null) {
			TimeseriesCollector col_t = new TimeseriesCollector("Temperature T");
			TimeseriesCollector col_to = new TimeseriesCollector("Temperature T^O");
			try {
				CsvReader reader = new CsvReader(file);
				reader.setDelimiter(';');
				reader.readHeaders();
				int counter = 1;
				double meanval_to = 0.0;
				double meanval_t = 0.0;
				while (reader.readRecord()) {
					try {
						meanval_to += Double.parseDouble(reader
								.get("Indoor Temperature"));
						meanval_t += Double.parseDouble(reader
								.get("Outdoor Temperature 1"));
						if (counter < COMBINE) {
							counter++;
						} else {
							String sdate = reader.get("Date");
							DateFormat format = DateFormat.getDateTimeInstance(
									DateFormat.SHORT, DateFormat.SHORT,
									Locale.GERMANY);
							Date date = format.parse(sdate);
							col_to.addObservation((double) date.getTime(),
									meanval_to / (double) COMBINE);
							col_t.addObservation((double) date.getTime(),
									meanval_t / (double) COMBINE);
							counter = 1;
							meanval_to = 0.0;
							meanval_t = 0.0;
						}
					} catch (ParseException pe) {
						System.err.println(pe.getMessage());
					} catch (NumberFormatException nfe) {
						System.err.println(nfe.getMessage());
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Prepare charts
			LineChartDialog lcd = new LineChartDialog(shell,
					"Temperature progress", "Time (h)", "Temperature (°C)",
					"min", "°C", 5.0, 25.0, false);
//			StepChartDialog scd = new StepChartDialog(shell, "Load progress",
//					"Time (h)", "Load (W)", "min", "W", 0.0, 70.0);
			// Fill charts
			lcd.addSeries(col_t);
			lcd.addSeries(col_to);
			// Finish charts
			lcd.create();
//			scd.create();
			// Open window
			shell.setText("Timeseries Viewer - " + new File(file).getName());
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