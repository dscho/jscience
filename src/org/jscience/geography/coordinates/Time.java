/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.geography.coordinates;

import java.util.Date;

import javax.measure.converters.UnitConverter;
import javax.measure.quantities.Duration;
import javax.measure.quantities.Quantity;
import javax.measure.units.SI;
import javax.measure.units.Unit;

import org.jscience.geography.coordinates.crs.TemporalCRS;
import org.opengis.referencing.cs.CoordinateSystem;

/**
 * This class represents the {@link TemporalCRS temporal} UTC time coordinates.
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 6, 2006
 */
public class Time extends Coordinates<TemporalCRS> implements Quantity<Duration> {

    /**
     * Holds the coordinate reference system for all instances of this class. 
     */
    public static final TemporalCRS<Time> CRS = new TemporalCRS<Time>() {

        @Override
        protected Time coordinatesOf(AbsolutePosition position) {
            if (position.timeUTC instanceof Time)
                return (Time) position.timeUTC;
            return Time.valueOf(position.timeUTC.doubleValue(SI.SECOND),
                    SI.SECOND);
        }

        @Override
        protected AbsolutePosition positionOf(Time coordinates, AbsolutePosition position) {
            position.timeUTC = coordinates;
            return position;
        }

        @Override
        public CoordinateSystem getCoordinateSystem() {
            return TemporalCRS.TIME_CS;
        }
        
    };

    /**
     * Holds the time in second since midnight, January 1, 1970 UTC. 
     */
    private double _timeInSecond;

    /**
     * Returns the temporal position corresponding to the specified coordinates.
     * 
     * @param value the time since midnight, January 1, 1970 UTC stated in the  
     *        specified unit.
     * @param unit the duration unit in which the time value is stated.
     * @return the corresponding temporal position.
     */
    public static Time valueOf(double value, Unit<Duration> unit) {
        return new Time(value, unit);
    }

    /**
     * Returns the temporal position corresponding to the specified date.
     * 
     * @param date the date.
     * @return the corresponding temporal position.
     */
    public static Time valueOf(Date date) {
        return new Time(date.getTime(), SI.MILLI(SI.SECOND));
    }

    /**
     * Creates the temporal position corresponding to the specified coordinates.
     * 
     * @param value the time since midnight, January 1, 1970 UTC stated in the  
     *        specified unit.
     * @param unit the duration unit in which the time value is stated.
     */
    public Time(double value, Unit<Duration> unit) {
        if (unit == SI.SECOND) {
            _timeInSecond = value;
        } else {
            UnitConverter toSecond = unit.getConverterTo(SI.SECOND);
            _timeInSecond = toSecond.convert(value);
        }
    }

    @Override
    public TemporalCRS getCoordinateReferenceSystem() {
        return CRS;
    }

    // OpenGIS Interface.
    public int getDimension() {
        return 1;
    }

    // OpenGIS Interface.
    public double getOrdinate(int dimension) throws IndexOutOfBoundsException {
        if (dimension == 0) {
            Unit u = TemporalCRS.TIME_CS.getAxis(0).getUnit();
            return SI.SECOND.getConverterTo(u).convert(_timeInSecond);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    // Implements Quantity<Duration>
    public final double doubleValue(Unit<Duration> unit) {
        return unit.equals(SI.SECOND) ? _timeInSecond : SI.SECOND
                .getConverterTo(unit).convert(_timeInSecond);
    }

    // Implements Quantity<Duration>
    public final long longValue(Unit<Duration> unit) {
        return Math.round(doubleValue(unit));
    }

    // Implements Quantity<Duration>
    public int compareTo(Quantity<Duration> arg0) {
        double arg0InSecond = arg0.doubleValue(SI.SECOND);
        return (_timeInSecond > arg0InSecond) ? 1
                : (_timeInSecond < arg0InSecond) ? -1 : 0;
    }

}