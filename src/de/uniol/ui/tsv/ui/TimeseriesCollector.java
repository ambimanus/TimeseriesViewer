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

import java.util.ArrayList;

/**
 * This collector just collects simple data points with their occurence
 * timestamps.
 * 
 * @author <a href=
 *         "mailto:Christian%20Hinrichs%20%3Cchristian.hinrichs@uni-oldenburg.de%3E"
 *         >Christian Hinrichs, christian.hinrichs@uni-oldenburg.de</a>
 */
public class TimeseriesCollector extends AbstractCollector {

	/** holds the timestamps (x-values) */
	protected ArrayList<Double> times = new ArrayList<Double>();
	/** holds the collected values (y-values) */
	protected ArrayList<Double> values = new ArrayList<Double>();
	
	/**
	 * Creates a new {@link TimeseriesCollector}.
	 * 
	 * @param name
	 */
	public TimeseriesCollector(String name) {
		super(name);
	}

	public void addObservation(double time, double value) {
		times.add(time);
		values.add(value);
	}
	
	public double[][] getResults() {
		double[] t = new double[times.size()];
		double[] v = new double[values.size()];
		if (t.length != v.length) {
			throw new RuntimeException("Different length of x/y arrays!");
		}
		for (int i = 0; i < t.length; i++) {
//			t[i] = times.get(i) * 60000d;
			t[i] = times.get(i);
			v[i] = values.get(i);
		}
		return new double[][] { t, v };
	}

	public double[] getObservation(int index) {
		return new double[] { times.get(index), values.get(index) };
	}

	public int getSize() {
		return times.size();
	}

	public void clear() {
		times.clear();
		values.clear();
	}
}