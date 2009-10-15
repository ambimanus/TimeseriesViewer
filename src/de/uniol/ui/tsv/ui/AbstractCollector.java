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

/**
 * This abstract class defines base methods which must be implemented by all
 * statistical collectors in this software.
 * 
 * @author <a href=
 *         "mailto:Christian%20Hinrichs%20%3Cchristian.hinrichs@uni-oldenburg.de%3E"
 *         >Christian Hinrichs, christian.hinrichs@uni-oldenburg.de</a>
 */
public abstract class AbstractCollector {
	
	/** Name of this collector, for displaying purposes only */
	protected String name;
	
	/**
	 * Creates a new collector with the given name.
	 * 
	 * @param name
	 */
	public AbstractCollector(String name) {
		this.name = name;
	}
	
	/**
	 * @return the name of this collector
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Adds a new data point defined by the given time (x) and value
	 * (y) params.
	 * 
	 * @param time
	 * @param value
	 */
	public abstract void addObservation(double time, double value);
	
	/**
	 * @return the collected data as twodimensional array
	 */
	public abstract double[][] getResults();
	
	/**
	 * @return amount of collected data points
	 */
	public abstract int getSize();
	
	/**
	 * @param index
	 * @return data point at the specified index of the internal array (not
	 *         time point!)
	 */
	public abstract double[] getObservation(int index);
	
	/** Clears the contained data */
	public abstract void clear();
}