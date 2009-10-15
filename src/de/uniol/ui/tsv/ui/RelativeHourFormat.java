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
 * 
 * NOTICE:
 * The following disclaimer refers to this file and the JFreeChart library only.
 * 
 * ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2007, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * JFreeChart is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 *
 * JFreeChart is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, 
 * USA.  
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * -----------------------
 * RelativeHourFormat.java
 * -----------------------
 * (C) Copyright 2006, 2007, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor:      Christian Hinrichs
 *
 * Changes:
 * --------
 * 23-Jun-2008 : Improved number formatting (Christian Hinrichs)
 *
 */
package de.uniol.ui.tsv.ui;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jfree.chart.util.RelativeDateFormat;

/**
 * This class is a modification of {@link RelativeDateFormat} with optimized
 * number formatting.
 * 
 * @author <a href=
 *         "mailto:Christian%20Hinrichs%20%3Cchristian.hinrichs@uni-oldenburg.de%3E"
 *         >Christian Hinrichs, christian.hinrichs@uni-oldenburg.de</a>
 */
public class RelativeHourFormat extends DateFormat {

	/**
	 * default
	 */
	private static final long serialVersionUID = 1L;

	/** The base milliseconds for the elapsed time calculation. */
    private long baseMillis;
    
    /**
     * A string appended after the hours.
     */
    private String hourSuffix;
    
    /**
     * A string appended after the minutes.
     */
    private String minuteSuffix;
    
    /**
     * A formatter for the seconds (and milliseconds).
     */
    private NumberFormat secondFormatter;
    
    /**
     * A string appended after the seconds.
     */
    private String secondSuffix;
    
    /**
     * A string appended after the milliseconds.
     */
    private String milliSuffix;

    /**
     * A constant for the number of milliseconds in one hour.
     */
    private static long MILLISECONDS_IN_ONE_HOUR = 60 * 60 * 1000L;
    
    /**
     * Creates a new instance.
     */
    public RelativeHourFormat() {
        this(0L);  
    }
    
    /**
     * Creates a new instance.
     * 
     * @param time  the date/time (<code>null</code> not permitted).
     */
    public RelativeHourFormat(Date time) {
        this(time.getTime());
    }
    
    /**
     * Creates a new instance.
     * 
     * @param baseMillis  the time zone (<code>null</code> not permitted).
     */
    public RelativeHourFormat(long baseMillis) {
        super();        
        this.baseMillis = baseMillis;
        this.hourSuffix = "h";
        this.minuteSuffix = "m";
        this.secondFormatter = NumberFormat.getNumberInstance();
        this.secondFormatter.setMaximumFractionDigits(3);
        this.secondFormatter.setMinimumFractionDigits(3);
        this.secondSuffix = "s";
        this.milliSuffix = "ms";

        // we don't use the calendar or numberFormat fields, but equals(Object) 
        // is failing without them being non-null
        this.calendar = new GregorianCalendar();
        this.numberFormat = new DecimalFormat("0");    
    }
    
    /**
     * Returns the base date/time used to calculate the elapsed time for 
     * display.
     * 
     * @return The base date/time in milliseconds since 1-Jan-1970.
     * 
     * @see #setBaseMillis(long)
     */
    public long getBaseMillis() {
        return this.baseMillis;
    }
    
    /**
     * Sets the base date/time used to calculate the elapsed time for display.  
     * This should be specified in milliseconds using the same encoding as
     * <code>java.util.Date</code>.
     * 
     * @param baseMillis  the base date/time in milliseconds.
     * 
     * @see #getBaseMillis()
     */
    public void setBaseMillis(long baseMillis) {
        this.baseMillis = baseMillis;
    }

    /**
     * Returns the string that is appended to the hour count.
     * 
     * @return The string.
     * 
     * @see #setHourSuffix(String)
     */
    public String getHourSuffix() {
        return this.hourSuffix;
    }
    
    /**
     * Sets the string that is appended to the hour count.
     * 
     * @param suffix  the suffix (<code>null</code> not permitted).
     * 
     * @see #getHourSuffix()
     */
    public void setHourSuffix(String suffix) {
        if (suffix == null) {
            throw new IllegalArgumentException("Null 'suffix' argument.");
        }
        this.hourSuffix = suffix;
    }

    /**
     * Returns the string that is appended to the minute count.
     * 
     * @return The string.
     * 
     * @see #setMinuteSuffix(String)
     */
    public String getMinuteSuffix() {
        return this.minuteSuffix;
    }
    
    /**
     * Sets the string that is appended to the minute count.
     * 
     * @param suffix  the suffix (<code>null</code> not permitted).
     * 
     * @see #getMinuteSuffix()
     */
    public void setMinuteSuffix(String suffix) {
        if (suffix == null) {
            throw new IllegalArgumentException("Null 'suffix' argument.");
        }
        this.minuteSuffix = suffix;
    }

    /**
     * Returns the string that is appended to the second count.
     * 
     * @return The string.
     * 
     * @see #setSecondSuffix(String)
     */
    public String getSecondSuffix() {
        return this.secondSuffix;
    }
    
    /**
     * Sets the string that is appended to the second count.
     * 
     * @param suffix  the suffix (<code>null</code> not permitted).
     * 
     * @see #getSecondSuffix()
     */
    public void setSecondSuffix(String suffix) {
        if (suffix == null) {
            throw new IllegalArgumentException("Null 'suffix' argument.");
        }
        this.secondSuffix = suffix;
    }
    
    /**
     * Returns the string that is appended to the millisecond count.
     * 
     * @return The string.
     * 
     * @see #setMilliSuffix(String)
     */
    public String getMilliSuffix() {
        return this.milliSuffix;
    }
    
    /**
     * Sets the string that is appended to the second count.
     * 
     * @param suffix  the suffix (<code>null</code> not permitted).
     * 
     * @see #getMilliSuffix()
     */
    public void setMilliSuffix(String suffix) {
        if (suffix == null) {
            throw new IllegalArgumentException("Null 'suffix' argument.");
        }
        this.milliSuffix = suffix;
    }
    
    /**
     * Sets the formatter for the seconds and milliseconds.
     * 
     * @param formatter  the formatter (<code>null</code> not permitted).
     */
    public void setSecondFormatter(NumberFormat formatter) {
        if (formatter == null) {
            throw new IllegalArgumentException("Null 'formatter' argument.");
        }
        this.secondFormatter = formatter;
    }

    /**
     * Formats the given date as the amount of elapsed time (relative to the
     * base date specified in the constructor).
     * 
     * @param date  the date.
     * @param toAppendTo  the string buffer.
     * @param fieldPosition  the field position.
     * 
     * @return The formatted date.
     */
    public StringBuffer format(Date date, StringBuffer toAppendTo,
                               FieldPosition fieldPosition) {
        long currentMillis = date.getTime();
        long elapsed = currentMillis - this.baseMillis;
        boolean zero = elapsed == 0L;
        
        long hours = elapsed / MILLISECONDS_IN_ONE_HOUR;
        elapsed = elapsed - (hours * MILLISECONDS_IN_ONE_HOUR);
        long minutes = elapsed / 60000L;
        elapsed = elapsed - (minutes * 60000L);
        long seconds = elapsed / 1000L;
        elapsed = elapsed - (seconds * 1000L);
        if (zero || hours != 0) {
        	toAppendTo.append(String.valueOf(hours) + getHourSuffix());
        }
        // Show minutes if not zero or we're on second level
        if (minutes != 0 || seconds != 0 || elapsed != 0) {
        	toAppendTo.append(String.valueOf(minutes) + getMinuteSuffix());
        }
        // Show seconds if not zero
        if (seconds != 0 || elapsed != 0) {
        	toAppendTo.append(String.valueOf(seconds) + getSecondSuffix());
        }
        // Show millis of not zero
        if (elapsed != 0) {
        	toAppendTo.append(String.valueOf(elapsed) + getMilliSuffix());
        }
        return toAppendTo;
    }

    /**
     * Parses the given string (not implemented).
     * 
     * @param source  the date string.
     * @param pos  the parse position.
     * 
     * @return <code>null</code>, as this method has not been implemented.
     */
    public Date parse(String source, ParsePosition pos) {
        return null;   
    }

    /**
     * Tests this formatter for equality with an arbitrary object.
     * 
     * @param obj  the object (<code>null</code> permitted).
     * 
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RelativeHourFormat)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        RelativeHourFormat that = (RelativeHourFormat) obj;
        if (this.baseMillis != that.baseMillis) {
            return false;
        }
        if (!this.hourSuffix.equals(that.hourSuffix)) {
            return false;
        }
        if (!this.minuteSuffix.equals(that.minuteSuffix)) {
            return false;
        }
        if (!this.secondSuffix.equals(that.secondSuffix)) {
            return false;
        }
        if (!this.secondFormatter.equals(that.secondFormatter)) {
            return false;
        }
        if (!this.milliSuffix.equals(that.milliSuffix)) {
            return false;
        }
        return true;
    }
    
    /**
     * Returns a hash code for this instance.
     * 
     * @return A hash code.
     */
    public int hashCode() {
        int result = 193;
        result = 37 * result 
                + (int) (this.baseMillis ^ (this.baseMillis >>> 32));
        result = 37 * result + this.hourSuffix.hashCode();
        result = 37 * result + this.minuteSuffix.hashCode();
        result = 37 * result + this.secondSuffix.hashCode();
        result = 37 * result + this.secondFormatter.hashCode();
        result = 37 * result + this.milliSuffix.hashCode();
        return result;
    }

    /**
     * Returns a clone of this instance.
     * 
     * @return A clone.
     */
    public Object clone() {
    	RelativeHourFormat clone = (RelativeHourFormat) super.clone();
        clone.secondFormatter = (NumberFormat) this.secondFormatter.clone();
        return clone;
    }
}