/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.geography.coordinates;

import javax.quantities.Length;
import javax.quantities.Quantity;
import javax.units.SI;
import javax.units.Unit;
import javax.units.converters.UnitConverter;

import org.jscience.geography.coordinates.crs.VerticalCRS;
import org.opengis.referencing.cs.CoordinateSystem;

/**
 * This class represents the Mean-Sea-Level {@link VerticalCRS vertical} 
 * altitude (MSL).
 *  
 * <p> Note: The current implementation approximates the MSL altitude to 
 *           the WGS-86 Ellipsoid Height. Future implementations will use 
 *           lookup tables in order to correct for regional discrepencies.</p> 
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 26, 2006
 */
public class Altitude extends Coordinates<VerticalCRS> implements
        Quantity<Length> {

    /**
     * Holds the coordinate reference system for all instances of this class. 
     */
    public static final VerticalCRS<Altitude> CRS = new VerticalCRS<Altitude>() {

        @Override
        protected Altitude coordinatesOf(AbsolutePosition position) {
            return Altitude.valueOf(position.heightWGS84.doubleValue(SI.METER),
                    SI.METER);
        }

        @Override
        protected AbsolutePosition positionOf(Altitude coordinates,
                AbsolutePosition position) {
            position.heightWGS84 = coordinates;
            return position;
        }

        @Override
        public CoordinateSystem getCoordinateSystem() {
            return VerticalCRS.HEIGHT_CS;
        }
    };

    /**
     * Holds the altitude in meters. 
     */
    private double _altitudeInMeter;

    /**
     * Returns the vertical position corresponding to the specified coordinates.
     * 
     * @param value the mean sea level altitude stated in the specified unit.
     * @param unit the length unit in which the altitude is stated.
     * @return the corresponding vertical position.
     */
    public static Altitude valueOf(double value, Unit<Length> unit) {
        return new Altitude(value, unit);
    }

    /**
     * Creates a vertical position corresponding to the specified coordinates.
     * 
     * @param value the mean sea level altitude stated in the specified unit.
     * @param unit the length unit in which the altitude is stated.
     */
    public Altitude(double value, Unit<Length> unit) {
        if (unit == SI.METER) {
            _altitudeInMeter = value;
        } else {
            UnitConverter toMeter = unit.getConverterTo(SI.METER);
            _altitudeInMeter = toMeter.convert(value);
        }
    }

    @Override
    public VerticalCRS getCoordinateReferenceSystem() {
        return Altitude.CRS;
    }

    // OpenGIS Interface.
    public int getDimension() {
        return 1;
    }

    // OpenGIS Interface.
    public double getOrdinate(int dimension) throws IndexOutOfBoundsException {
        if (dimension == 0) {
            Unit u = VerticalCRS.HEIGHT_CS.getAxis(0).getUnit();
            return SI.METER.getConverterTo(u).convert(_altitudeInMeter);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    // Implements Quantity<Length>
    public final double doubleValue(Unit<Length> unit) {
        return unit.equals(SI.METER) ? _altitudeInMeter : SI.METER
                .getConverterTo(unit).convert(_altitudeInMeter);
    }

    // Implements Quantity<Length>
    public final long longValue(Unit<Length> unit) {
        return Math.round(doubleValue(unit));
    }

    // Implements Quantity<Length>
    public int compareTo(Quantity<Length> arg0) {
        double arg0InMeter = arg0.doubleValue(SI.METER);
        return (_altitudeInMeter > arg0InMeter) ? 1
                : (_altitudeInMeter < arg0InMeter) ? -1 : 0;
    }

}