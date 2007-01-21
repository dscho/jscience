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
import javax.measure.quantities.*;
import javax.measure.units.SI;
import javax.measure.units.Unit;

import static org.jscience.geography.coordinates.crs.ReferenceEllipsoid.WGS84;

import org.jscience.geography.coordinates.crs.GeocentricCRS;
import org.opengis.referencing.cs.CoordinateSystem;

/**
 * This class represents the {@link GeocentricCRS geocentric} Earth-Centered, 
 * Earth-Fixed (ECEF) cartesian coordinates used in GPS/GLONASS.
 *
 * @author Paul D. Anderson
 * @version 3.0, February 18, 2006
 */
public class XYZ extends Coordinates<GeocentricCRS<XYZ>> {

    /**
     * Holds the coordinate reference system for all instances of this class.
     */
    public static final GeocentricCRS<XYZ> CRS = new GeocentricCRS<XYZ>() {

        @Override
        protected XYZ coordinatesOf(AbsolutePosition position) {
            double latitude = position.latitudeWGS84.doubleValue(SI.RADIAN);
            double longitude = position.longitudeWGS84.doubleValue(SI.RADIAN);
            double height = position.heightWGS84.doubleValue(SI.METER);

            double cosLat = Math.cos(latitude);
            double sinLat = Math.sin(latitude);
            double cosLon = Math.cos(longitude);
            double sinLon = Math.sin(longitude);

            double roc = WGS84.verticalRadiusOfCurvature(latitude);
            double x = (roc + height) * cosLat * cosLon;
            double y = (roc + height) * cosLat * sinLon;
            double z = ((1.0 - WGS84.getEccentricitySquared()) * roc + height)
                    * sinLat;

            return XYZ.valueOf(x, y, z, SI.METER);
        }

        @Override
        protected AbsolutePosition positionOf(XYZ coordinates,
                AbsolutePosition position) {
            final double x = coordinates._xInMeter;
            final double y = coordinates._yInMeter;
            final double z = coordinates._zInMeter;

            final double longitude = Math.atan2(y, x);

            final double latitude;
            final double xy = Math.hypot(x, y);
            // conventional result if xy == 0.0...
            if (xy == 0.0) {
                latitude = (z >= 0.0) ? Math.PI / 2.0 : -Math.PI / 2.0;
            } else {
                final double a = WGS84.getSemimajorAxis().doubleValue(SI.METER);
                final double b = WGS84.getsSemiminorAxis()
                        .doubleValue(SI.METER);
                final double ea2 = WGS84.getEccentricitySquared();
                final double eb2 = WGS84.getSecondEccentricitySquared();
                final double beta = Math.atan2(a * z, b * xy);
                double numerator = z + b * eb2 * cube(Math.sin(beta));
                double denominator = xy - a * ea2 * cube(Math.cos(beta));
                latitude = Math.atan2(numerator, denominator);
            }

            final double height = xy / Math.cos(latitude)
                    - WGS84.verticalRadiusOfCurvature(latitude);
            position.latitudeWGS84 = new Scalar<Angle>(latitude, SI.RADIAN);
            position.longitudeWGS84 = new Scalar<Angle>(longitude, SI.RADIAN);
            position.heightWGS84 = new Scalar<Length>(height, SI.METER);
            return position;
        }

        @Override
        public CoordinateSystem getCoordinateSystem() {
            return GeocentricCRS.XYZ_CS;
        }
    };

    private static double cube(final double x) {
        return x * x * x;
    }

    /**
     * Holds the x position in meters.
     */
    private double _xInMeter;

    /**
     * Holds the y position in meters.
     */
    private double _yInMeter;

    /**
     * Holds the z position in meters.
     */
    private double _zInMeter;

    /**
     * Returns the spatial position corresponding to the specified coordinates.
     *
     * @param x the x value stated in the specified unit.
     * @param y the y value stated in the specified unit.
     * @param z the z value stated in the specified unit.
     * @param unit the length unit in which the coordinates are stated.
     * @return the corresponding 3D position.
     */
    public static XYZ valueOf(double x, double y, double z, Unit<Length> unit) {
        return new XYZ(x, y, z, unit);
    }

    /**
     * Creates a spatial position corresponding to the specified coordinates.
     *
     * @param x the x value stated in the specified unit.
     * @param y the y value stated in the specified unit.
     * @param z the z value stated in the specified unit.
     * @param unit the length unit in which the coordinates are stated.
     */
    public XYZ(double x, double y, double z, Unit<Length> unit) {
        if (unit == SI.METER) {
            _xInMeter = x;
            _yInMeter = y;
            _zInMeter = z;
        } else {
            UnitConverter toMeter = unit.getConverterTo(SI.METER);
            _xInMeter = toMeter.convert(x);
            _yInMeter = toMeter.convert(y);
            _zInMeter = toMeter.convert(z);
        }
    }

    /**
     * Returns the x coordinate value as <code>double</code>
     *
     * @param unit the length unit of the x coordinate value to return.
     * @return the x coordinate stated in the specified unit.
     */
    public final double xValue(Unit<Length> unit) {
        return unit.equals(SI.METER) ? _xInMeter : SI.METER
                .getConverterTo(unit).convert(_xInMeter);
    }

    /**
     * Returns the y coordinate value as <code>double</code>
     *
     * @param unit the length unit of the x coordinate value to return.
     * @return the z coordinate stated in the specified unit.
     */
    public final double yValue(Unit<Length> unit) {
        return unit.equals(SI.METER) ? _yInMeter : SI.METER
                .getConverterTo(unit).convert(_yInMeter);
    }

    /**
     * Returns the z coordinate value as <code>double</code>
     *
     * @param unit the length unit of the x coordinate value to return.
     * @return the z coordinate stated in the specified unit.
     */
    public final double zValue(Unit<Length> unit) {
        return unit.equals(SI.METER) ? _zInMeter : SI.METER
                .getConverterTo(unit).convert(_zInMeter);
    }

    @Override
    public GeocentricCRS<XYZ> getCoordinateReferenceSystem() {
        return CRS;
    }

    // OpenGIS Interface.
    public int getDimension() {
        return 3;
    }

    // OpenGIS Interface.
    public double getOrdinate(int dimension) throws IndexOutOfBoundsException {
        if (dimension == 0) {
            Unit u = GeocentricCRS.XYZ_CS.getAxis(0).getUnit();
            return SI.METER.getConverterTo(u).convert(_xInMeter);
        } else if (dimension == 1) {
            Unit u = GeocentricCRS.XYZ_CS.getAxis(1).getUnit();
            return SI.METER.getConverterTo(u).convert(_yInMeter);
        } else if (dimension == 2) {
            Unit u = GeocentricCRS.XYZ_CS.getAxis(2).getUnit();
            return SI.METER.getConverterTo(u).convert(_zInMeter);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
}