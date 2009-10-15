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

import org.eclipse.swt.widgets.Shell;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYStepRendererFast;
import org.jfree.data.Range;
import org.jfree.ui.RectangleInsets;

/**
 * This class shows a step chart. That is a chart which connects data points
 * only with horizontal and vertical lines.
 * 
 * @author <a href=
 *         "mailto:Christian%20Hinrichs%20%3Cchristian.hinrichs@uni-oldenburg.de%3E"
 *         >Christian Hinrichs, christian.hinrichs@uni-oldenburg.de</a>
 */
public class StepChartDialog extends LineChartDialog {

	/**
	 * Creates a new step chart with the given values.
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
	public StepChartDialog(Shell parent, String title, String xTitle,
			String yTitle, String tooltipRangeUnits, String tooltipValueUnits,
			double minRange, double maxRange, boolean useRelativeHourFormat) {
		super(parent, title, xTitle, yTitle, tooltipRangeUnits,
				tooltipValueUnits, minRange, maxRange, useRelativeHourFormat);
	}

	protected JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createXYStepChartFast(title, xTitle,
				yTitle, xy, PlotOrientation.VERTICAL, true, false, false);

		chart.setBackgroundPaint(Color.white);
		chart.getLegend().setBackgroundPaint(new Color(224, 224, 224));
		
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(new Color(224, 224, 224));
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setRenderer(new XYStepRendererFast(null, null));
		
		XYItemRenderer xyr = plot.getRenderer();
//		xyr.setBaseToolTipGenerator(new XYToolTipGenerator() {
//			public String generateToolTip(XYDataset dataset, int series,
//					int item) {
//				return nf.format(dataset.getXValue(series, item))
//						+ tooltipRangeUnits + ", "
//						+ nf2.format(dataset.getYValue(series, item))
//						+ tooltipValueUnits;
//			}
//		});
		
		for (int i : seriesStrokes.keySet()) {
			xyr.setSeriesStroke(i, seriesStrokes.get(i));
		}
		for (int i : seriesColors.keySet()) {
			xyr.setSeriesPaint(i, seriesColors.get(i));
		}

		DateAxis da = new DateAxis(xTitle);
		if (useRelativeHourFormat) {
			da.setDateFormatOverride(new RelativeHourFormat());
		}
		da.setLowerMargin(0.03);
		plot.setDomainAxis(da);

		ValueAxis yaxis = plot.getRangeAxis();
		yaxis.setRange(new Range(minRange, maxRange));

		return chart;
	}
}