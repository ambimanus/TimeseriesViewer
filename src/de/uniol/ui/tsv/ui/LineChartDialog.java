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

import java.awt.Color;
import java.awt.Stroke;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.FastXYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRendererFast;
import org.jfree.data.Range;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.FastXYDataset;
import org.jfree.data.xy.FastXYSeries;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.RectangleInsets;

/**
 * This class shows a line chart. That is a chart which connects all given data
 * points with straight lines.
 * 
 * @author <a href=
 *         "mailto:Christian%20Hinrichs%20%3Cchristian.hinrichs@uni-oldenburg.de%3E"
 *         >Christian Hinrichs, christian.hinrichs@uni-oldenburg.de</a>
 */
public class LineChartDialog extends Dialog {
	
	/** NumberFormat used in tooltips for x-values */
	protected static NumberFormat nf = NumberFormat.getNumberInstance();
	static {
		nf.setMaximumFractionDigits(0);
		nf.setMinimumFractionDigits(0);
	}
	
	/** NumberFormat used in tooltips for y-values */
	protected static NumberFormat nf2 = NumberFormat.getNumberInstance();
	static {
		nf2.setMaximumFractionDigits(3);
		nf2.setMinimumFractionDigits(3);
	}
	
	/** Underlying dataset */
	protected AbstractXYDataset xy;
	/** title of this chart */
	protected String title;
	/** title of the x axis */
	protected String xTitle;
	/** title of the y axis */
	protected String yTitle;
	/** unit string for x values */
	protected String tooltipRangeUnits;
	/** unit string for y values */
	protected String tooltipValueUnits;
	/** lower border of y axis range */
	protected double minRange;
	/** upper border of y axis range */
	protected double maxRange;
	/** use RelativeHourFormat? */
	protected boolean useRelativeHourFormat = true;
	
	/** Defines width values for individual series */
	protected HashMap<Integer, Stroke> seriesStrokes = new HashMap<Integer, Stroke>();
	/** Defines color values for individual series */
	protected HashMap<Integer, Color> seriesColors = new HashMap<Integer, Color>();

	/**
	 * Creates a new line chart with the given values.
	 * 
	 * @param parent
	 * @param title
	 * @param xTitle
	 * @param yTitle
	 * @param tooltipRangeUnits
	 * @param tooltipValueUnits
	 * @param minRange
	 * @param maxRange
	 */
	public LineChartDialog(Shell parent, String title, String xTitle,
			String yTitle, String tooltipRangeUnits, String tooltipValueUnits,
			double minRange, double maxRange, boolean useRelativeHourFormat) {
		super(parent, SWT.APPLICATION_MODAL);
		this.title = title;
		this.xTitle = xTitle;
		this.yTitle = yTitle;
		this.tooltipRangeUnits = tooltipRangeUnits;
		this.tooltipValueUnits = tooltipValueUnits;
		this.maxRange = maxRange;
		this.minRange = minRange;
		this.useRelativeHourFormat = useRelativeHourFormat;
//		xy = new DefaultXYDataset();
		xy = new FastXYDataset();
	}
	
	/**
	 * Creates a new line chart with the given values.
	 * 
	 * @param parent
	 * @param title
	 * @param xTitle
	 * @param yTitle
	 * @param tooltipRangeUnits
	 * @param tooltipValueUnits
	 */
	public LineChartDialog(Shell parent, String title, String xTitle,
			String yTitle, String tooltipRangeUnits, String tooltipValueUnits,
			boolean useRelativeHourFormat) {
		this(parent, title, xTitle, yTitle, tooltipRangeUnits,
				tooltipValueUnits, 0.0, 0.0, useRelativeHourFormat);
	}

	/**
	 * Adds a series represented in the given Collector.
	 * 
	 * @param name
	 * @param data
	 */
	public void addSeries(String name, double[][] data) {
		if (xy instanceof FastXYDataset) {
			FastXYSeries series = new FastXYSeries(name);
			for (int i = 0; i < data[0].length; i++) {
				boolean notify = i == data[0].length - 1;
				series.add(data[0][i], data[1][i], notify);
			}
			((FastXYDataset) xy).addSeries(series);
		} else if (xy instanceof DefaultXYDataset) {
			((DefaultXYDataset) xy).addSeries(name, data);
		}
	}
	
	public void addSeries(AbstractCollector col) {
		this.addSeries(col.getName(), col.getResults());
	}
	
	/**
	 * Adds all series from the given list of Collectors.
	 * 
	 * @param list
	 */
	public void addAllSeries(List<? extends AbstractCollector> list) {
		Iterator<? extends AbstractCollector> it = list.iterator();
		while(it.hasNext()) {
			addSeries(it.next());
		}
	}

	/**
	 * Defines the stroke of the specified series.
	 * 
	 * @param series
	 * @param stroke
	 */
	public void setSeriesStroke(int series, Stroke stroke) {
		seriesStrokes.put(series, stroke);
	}
	
	/**
	 * Defines the color of the specified series.
	 * 
	 * @param series
	 * @param c
	 */
	public void setSeriesColor(int series, Color c) {
		seriesColors.put(series, c);
	}

	/**
	 * @return the resulting chart object
	 */
	protected JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createTimeSeriesChartFast(title,
				xTitle, yTitle, xy, true, false, false);

		chart.setBackgroundPaint(Color.white);
		chart.getLegend().setBackgroundPaint(new Color(224, 224, 224));

		FastXYPlot plot = (FastXYPlot) chart.getPlot();
//		plot.setBackgroundPaint(new Color(224, 224, 224));
		plot.setBackgroundPaint(new Color(77, 77, 77));
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		StandardXYItemRendererFast r = new StandardXYItemRendererFast(
				StandardXYItemRendererFast.LINES, null, null);
		plot.setRenderer(r);
		
		for (int i : seriesStrokes.keySet()) {
			r.setSeriesStroke(i, seriesStrokes.get(i));
		}
		for (int i : seriesColors.keySet()) {
			r.setSeriesPaint(i, seriesColors.get(i));
		}

		DateAxis da = new DateAxis(xTitle);
		if (useRelativeHourFormat) {
			da.setDateFormatOverride(new RelativeHourFormat());
		}
		da.setLowerMargin(0.03);
		plot.setDomainAxis(da);

		if (minRange != 0.0 && maxRange != 0.0) {
			ValueAxis yaxis = plot.getRangeAxis();
			yaxis.setRange(new Range(minRange, maxRange));
		}
		
		return chart;
	}
	
	/**
	 * Creates the chart object in the current parent shell.
	 */
	public void create() {
		Shell shell = getParent();
		ChartComposite cc = new ChartComposite(shell, SWT.NONE, createChart(),
				true);
		cc.setDisplayToolTips(true);
		cc.setHorizontalAxisTrace(false);
		cc.setVerticalAxisTrace(false);
	}

	/**
	 * Creates the chart object in the current parent shell and shows this
	 * shell.
	 * 
	 * @param blocking
	 *            defines whether this methods blocks until the shell is closed
	 */
	public void open(boolean blocking) {
		create();
		Shell shell = getParent();
		shell.open();
		Display display = shell.getDisplay();
		while (blocking && !shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}