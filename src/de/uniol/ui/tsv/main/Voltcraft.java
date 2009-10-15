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

public class Voltcraft {

	public final static int COMBINE = 1;
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		FileDialog fd = new FileDialog(shell, SWT.OPEN);
		String file = fd.open();
		if (file != null) {
			TimeseriesCollector col_t = new TimeseriesCollector("Temperature T");
			try {
				CsvReader reader = new CsvReader(file);
				reader.setDelimiter('\t');
				// Skip first eight lines
				reader.readRecord();
				reader.readRecord();
				reader.readRecord();
				reader.readRecord();
				reader.readRecord();
				reader.readRecord();
				reader.readRecord();
				reader.readRecord();
				int counter = 1;
				double meanval_t = 0.0;
				while (reader.readRecord()) {
					try {
						meanval_t += Double.parseDouble(reader.get(3));
						if (counter < COMBINE) {
							counter++;
						} else {
							String sdate = reader.get(1);
							DateFormat format = DateFormat.getDateInstance(
									DateFormat.SHORT, Locale.GERMANY);
							Date date = format.parse(sdate);
							String tdate = reader.get(2);
							format = DateFormat.getTimeInstance(
									DateFormat.SHORT, Locale.GERMANY);
							Date time = format.parse(tdate);
							date.setHours(time.getHours());
							date.setMinutes(time.getMinutes());
							date.setSeconds(time.getSeconds());
							col_t.addObservation((double) date.getTime(),
									meanval_t / (double) COMBINE);
							counter = 1;
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
					"min", "°C", 5.0, 25.0, true);
//			StepChartDialog scd = new StepChartDialog(shell, "Load progress",
//					"Time (h)", "Load (W)", "min", "W", 0.0, 70.0);
			// Fill charts
			lcd.addSeries(col_t);
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