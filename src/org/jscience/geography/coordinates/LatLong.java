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
import javax.measure.quantities.Angle;
import javax.measure.quantities.Scalar;
import javax.measure.units.SI;
import javax.measure.units.Unit;

import org.jscience.geography.coordinates.crs.GeographicCRS;
import org.opengis.referencing.cs.CoordinateSystem;

/**
 * This class represents the {@link GeographicCRS geographic} latitude/longitude
 * coordinates onto the WGS84 ellipsoid.
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
public class LatLong extends Coordinates<GeographicCRS> {

    /**
     * Holds the coordinate reference system for all instances of this class. 
     */
    public static final GeographicCRS<LatLong> CRS = new GeographicCRS<LatLong>() {

        @Override
        protected LatLong coordinatesOf(AbsolutePosition position) {
            return LatLong.valueOf(position.latitudeWGS84.doubleValue(SI.RADIAN),
                    position.longitudeWGS84.doubleValue(SI.RADIAN), SI.RADIAN);
        }

        @Override
        protected AbsolutePosition positionOf(LatLong coordinates, AbsolutePosition position) {
            position.latitudeWGS84 = new Scalar<Angle>(coordinates._latitudeInRadian, SI.RADIAN);
            position.longitudeWGS84 = new Scalar<Angle>(coordinates._longitudeInRadian, SI.RADIAN); 
            return position;
        }

        @Override
        public CoordinateSystem getCoordinateSystem() {
            return GeographicCRS.LATITUDE_LONGITUDE_CS;
        }

    };
    
    /**
     * Holds the latitude in radians. 
     */
    private double _latitudeInRadian;

    /**
     * Holds the longitude in radians. 
     */
    private double _longitudeInRadian;

    /**
     * Returns the surface position corresponding to the specified coordinates.
     * 
     * @param latitude the latitude value stated in the specified unit.
     * @param longitude the longitude value stated in the specified unit.
     * @param unit the angle unit in which the coordinates are stated.
     * @return the corresponding surface position.
     */
    public static LatLong valueOf(double latitude, double longitude,
            Unit<Angle> unit) {
        return new LatLong(latitude, longitude, unit);
    }

    /**
     * Creates the surface position corresponding to the specified coordinates.
     * 
     * @param latitude the latitude value stated in the specified unit.
     * @param longitude the longitude value stated in the specified unit.
     * @param unit the angle unit in which the coordinates are stated.
     */
    public LatLong(double latitude, double longitude, Unit<Angle> unit) {
        if (unit == SI.RADIAN) {
            _latitudeInRadian = latitude;
            _longitudeInRadian = longitude;
        } else {
            UnitConverter toRadian = unit.getConverterTo(SI.RADIAN);
            _latitudeInRadian = toRadian.convert(latitude);
            _longitudeInRadian = toRadian.convert(longitude);
        }
    }

    /**
     * Returns the latitude value as <code>double</code>
     * 
     * @param unit the angle unit of the latitude to return.
     * @return the latitude stated in the specified unit.
     */
    public final double latitudeValue(Unit<Angle> unit) {
        return unit.equals(SI.RADIAN) ? _latitudeInRadian : SI.RADIAN
                .getConverterTo(unit).convert(_latitudeInRadian);
    }

    /**
     * Returns the longitude value as <code>double</code>
     * 
     * @param unit the angle unit of the longitude to return.
     * @return the longitude stated in the specified unit.
     */
    public final double longitudeValue(Unit<Angle> unit) {
        return unit.equals(SI.RADIAN) ? _longitudeInRadian : SI.RADIAN
                .getConverterTo(unit).convert(_longitudeInRadian);
    }
    
    @Override
    public GeographicCRS<LatLong> getCoordinateReferenceSystem() {
        return CRS;
    }

    // OpenGIS Interface.
    public int getDimension() {
        return 2;
    }

    // OpenGIS Interface.
    public double getOrdinate(int dimension) throws IndexOutOfBoundsException {
        if (dimension == 0) {
            Unit u = GeographicCRS.LATITUDE_LONGITUDE_CS.getAxis(0).getUnit();
            return SI.RADIAN.getConverterTo(u).convert(_latitudeInRadian);
        } else if (dimension == 1) {
            Unit u = GeographicCRS.LATITUDE_LONGITUDE_CS.getAxis(1).getUnit();
            return SI.RADIAN.getConverterTo(u).convert(_longitudeInRadian);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

}