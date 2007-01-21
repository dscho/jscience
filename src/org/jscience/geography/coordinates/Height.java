/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.geography.coordinates;

import javax.measure.converters.UnitConverter;
import javax.measure.quantities.Length;
import javax.measure.quantities.Quantity;
import javax.measure.units.SI;
import javax.measure.units.Unit;

import org.jscience.geography.coordinates.crs.VerticalCRS;
import org.opengis.referencing.cs.CoordinateSystem;

/**
 * This class represents the {@link VerticalCRS vertical} height above the 
 * WGS84 ellipsoid.
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 6, 2006
 */
public class Height extends Coordinates<VerticalCRS> implements
        Quantity<Length> {

    /**
     * Holds the coordinate reference system for all instances of this class. 
     */
    public static final VerticalCRS<Height> CRS = new VerticalCRS<Height>() {

        @Override
        protected Height coordinatesOf(AbsolutePosition position) {
            if (position.heightWGS84 instanceof Height)
                return (Height) position.heightWGS84;
            return Height.valueOf(position.heightWGS84.doubleValue(SI.METER),
                    SI.METER);
        }

        @Override
        protected AbsolutePosition positionOf(Height coordinates,
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
     * Holds the height in meters. 
     */
    private double _heightInMeter;

    /**
     * Returns the vertical position corresponding to the specified coordinates.
     * 
     * @param value the height above the WGS84 ellipsoid stated in the 
     *        specified unit.
     * @param unit the length unit in which the height is stated.
     * @return the corresponding vertical position.
     */
    public static Height valueOf(double value, Unit<Length> unit) {
        return new Height(value, unit);
    }

    /**
     * Creates a vertical position corresponding to the specified coordinates.
     * 
     * @param value the height above the WGS84 ellipsoid stated in the 
     *        specified unit.
     * @param unit the length unit in which the height is stated.
     */
    public Height(double value, Unit<Length> unit) {
        if (unit == SI.METER) {
            _heightInMeter = value;
        } else {
            UnitConverter toMeter = unit.getConverterTo(SI.METER);
            _heightInMeter = toMeter.convert(value);
        }
    }

    @Override
    public VerticalCRS getCoordinateReferenceSystem() {
        return Height.CRS;
    }

    // OpenGIS Interface.
    public int getDimension() {
        return 1;
    }

    // OpenGIS Interface.
    public double getOrdinate(int dimension) throws IndexOutOfBoundsException {
        if (dimension == 0) {
            Unit u = VerticalCRS.HEIGHT_CS.getAxis(0).getUnit();
            return SI.METER.getConverterTo(u).convert(_heightInMeter);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    // Implements Quantity<Length>
    public final double doubleValue(Unit<Length> unit) {
        return unit.equals(SI.METER) ? _heightInMeter : SI.METER
                .getConverterTo(unit).convert(_heightInMeter);
    }

    // Implements Quantity<Length>
    public final long longValue(Unit<Length> unit) {
        return Math.round(doubleValue(unit));
    }

    // Implements Quantity<Length>
    public int compareTo(Quantity<Length> arg0) {
        double arg0InMeter = arg0.doubleValue(SI.METER);
        return (_heightInMeter > arg0InMeter) ? 1
                : (_heightInMeter < arg0InMeter) ? -1 : 0;
    }

}