/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.physics.quantities;
import org.jscience.physics.units.ConversionException;
import org.jscience.physics.units.SI;
import org.jscience.physics.units.Unit;

/**
 * This class represents the figure formed by two lines diverging from a common
 * point. The system unit for this quantity is "rad" (Système International
 * d'Unités).
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Angle extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.RADIAN;

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new Angle();
        }
    };

    /**
     * Represents a {@link Angle} amounting to nothing.
     */
    public final static Angle ZERO = (Angle) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Angle() {}

    /**
     * Returns the {@link Angle} corresponding to the specified quantity.
     *
     * @param  quantity a quantity compatible with {@link Angle}.
     * @return the specified quantity or a new {@link Angle} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link Angle}.
     */
    public static Angle angleOf(Quantity quantity) {
        return (Angle) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link Angle} instances in the specified unit.
     *
     * @param  unit the output unit for {@link Angle} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    ////////////////////
    // ANGLE SPECIFIC //
    ////////////////////

    /**
     * Converts rectangular coordinates (<code>x</code>,&nbsp;<code>y</code>)
     * to polar (&rho;,&nbsp;<i>&theta;</i>).
     * This method computes the phase <i>&theta;</i> by computing an arc tangent
     * of <code>y/x</code>.
     *
     * @param  y   y coordinate
     * @param  x   x coordinate
     * @return the <i>&theta;</i> component of the point
     *         (<i>&rho;</i>,&nbsp;<i>&theta;</i>) in polar coordinates that
     *         corresponds to the point(<i>x</i>,&nbsp;<i>y</i>) in Cartesian
     *         coordinates.
     * @throws IllegalArgumentException x and y represent quantities of
     *         different nature (system units are different).
     */
    public static Angle atan2(Quantity y, Quantity x) {
        if (x.getSystemUnit() != y.getSystemUnit()) {
            throw new IllegalArgumentException(
                "x (" + x.getSystemUnit() + ") and y (" + y.getSystemUnit() +
                ") are of different nature");
        }
        if ((x.getMinimum() < 0.0) && (x.getMaximum() > 0.0) &&
            (y.getMinimum() < 0.0) && (y.getMaximum() > 0.0)) {
            // Encompasses (0,0), all angles are possible
            return (Angle) valueOf(0, Math.PI, SI.RADIAN);
        }
        double a1 = Math.atan2(y.getMinimum(), x.getMinimum());
        double a2 = Math.atan2(y.getMinimum(), x.getMaximum());
        double a3 = Math.atan2(y.getMaximum(), x.getMinimum());
        double a4 = Math.atan2(y.getMaximum(), x.getMaximum());
        // Discontinuity at -Pi
        if (x.getMaximum() <= 0.0) {
            if (a1 < 0) {
                a1 += 2 * Math.PI;
            }
            if (a2 < 0) {
                a2 += 2 * Math.PI;
            }
            if (a3 < 0) {
                a3 += 2 * Math.PI;
            }
            if (a4 < 0) {
                a4 += 2 * Math.PI;
            }
        }
        // Search for minimum
        double min = a1;
        if (a2 < min) {
            min = a2;
        }
        if (a3 < min) {
            min = a3;
        }
        if (a4 < min) {
            min = a4;
        }
        // Search for maximum
        double max = a1;
        if (a2 > max) {
            max = a2;
        }
        if (a3 > max) {
            max = a3;
        }
        if (a4 > max) {
            max = a4;
        }
        return (Angle) FACTORY.rangeApprox(min, max);
    }

    /**
     * Returns the trigonometric sine of this {@link Angle}.
     *
     * @return the sine of this angle.
     */
    public Scalar sine() {
        return sine(getMinimum(), getMaximum());
    }

    /**
     * Returns the trigonometric cosine of this {@link Angle}.
     *
     * @return the cosine of this angle.
     */
    public Scalar cos() {
        return sine(getMinimum() + SQUARE_ANGLE, getMaximum() + SQUARE_ANGLE);
    }
    private static final double SQUARE_ANGLE =  Math.PI / 2;

    /**
     * Returns the trigonometric tangent of this {@link Angle}.
     *
     * @return the tangent of this angle.
     */
    public Scalar tan() {
        // Bounds min to [-Pi/2, Pi/2]
        double minBounded = Math.IEEEremainder(this.getMinimum(), Math.PI);
        // Measures distance to the next discontinuity
        double nextDisc = Math.PI / 2 - minBounded;
        // Test if this quantity passes across a discontinuity
        if (getMinimum() + nextDisc < getMaximum()) {
            return (Scalar) Factory.getInstance(Unit.ONE).rangeExact(
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        return (Scalar) Factory.getInstance(Unit.ONE).rangeApprox(
            Math.tan(getMinimum()), Math.tan(getMaximum()));
    }

    /**
     * Returns an {@link Angle} in the range of -<i>&pi;</i> through
     * <i>&pi;</i> (system unit) by removing or adding a discrete number of
     * revolution to this angle.
     *
     * @return an angle in the the range of -<i>&pi;</i> through <i>&pi;</i>
     */
    public Angle bounded() {
        double error = getAbsoluteError();
        double value = doubleValue();
        double boundedValue = Math.IEEEremainder(value, 2.0 * Math.PI);
        return (Angle) valueOf(boundedValue, error, SI.RADIAN);
    }

    /**
     * Returns the trigonometric sine for the specified minimum and maximum
     * {@link Angle}.
     *
     * @param  min the angle minimumm.
     * @param  max the angle maximumm.
     * @return the sine quantity corresponding to the specified interval.
     */
    private Scalar sine(double min, double max) {
        // Bounds min to [-Pi, Pi]
        double minBounded = Math.IEEEremainder(min, 2.0 * Math.PI);
        double nextMax = Math.PI / 2 - minBounded;
        if (nextMax < 0) {
            nextMax += 2 * Math.PI;
        }
        double nextMin = -Math.PI / 2 - minBounded;
        if (nextMin < 0) {
            nextMin += 2 * Math.PI;
        }
        double sineMin = Math.sin(min);
        double sineMax = Math.sin(max);
        if (sineMin > sineMax) { // Swaps.
            double tmp = sineMin;
            sineMin = sineMax;
            sineMax = tmp;
        }
        // Test if angle passes across a minima or a maxima
        if (min + nextMax < max) {
            sineMax = 1.0;
        }
        if (min + nextMin < max) {
            sineMin = -1.0;
        }
        return (Scalar) Factory.getInstance(Unit.ONE).rangeApprox(
            sineMin, sineMax);
    }

    private static final long serialVersionUID = 8923036116116831307L;
}