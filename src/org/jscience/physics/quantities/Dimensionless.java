/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.quantities;
import javolution.lang.MathLib;

import org.jscience.physics.units.SI;
import org.jscience.physics.units.Unit;

/**
 * This class represents a dimensionless quantity. It contains methods for
 * performing basic numeric operations such as the elementary exponential,
 * logarithm and trigonometric functions.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Dimensionless extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<Dimensionless> UNIT = Unit.ONE;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Dimensionless> FACTORY = new Factory<Dimensionless>(
            UNIT) {
        protected Dimensionless create() {
            return new Dimensionless();
        }
    };

    /**
     * Represents a {@link Dimensionless} amounting to nothing.
     */
    public final static Dimensionless ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Represents a {@link Dimensionless} amounting to <code>1</code>.
     */
    public final static Dimensionless ONE = Quantity.valueOf(1, UNIT);

    /**
     * Holds the natural logarithmic base.
     */
    public static final Dimensionless e = Quantity.valueOf(MathLib.E, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Dimensionless() {
    }

    /**
     * Returns the dimensionless corresponding to the specified 
     * <code>doubleValue</code>
     * 
     * @param doubleValue the double value.
     */
    public static Dimensionless valueOf(double doubleValue) {
        return Quantity.valueOf(doubleValue, Unit.ONE);
    }

    /**
     * Shows {@link Dimensionless} instances in the specified unit.
     *
     * @param unit the display unit for {@link Dimensionless} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Dimensionless.class, unit);
    }


    /////////////////////
    // SCALAR SPECIFIC //
    /////////////////////

    /**
     * Returns the exponential number <i>e</i> raised to the power of this
     * {@link Dimensionless}.
     *
     * @return <code>exp(this)</code>
     */
    public Dimensionless exp() {
        return Quantity.valueOf(
            MathLib.exp(getMinimum()), MathLib.exp(getMaximum()), UNIT);
    }

    /**
     * Returns the natural logarithm (base e) of this {@link Dimensionless}.
     *
     * @return <code>log(this)</code>
     */
    public Dimensionless log() {
        return Quantity.valueOf(
            MathLib.log(getMinimum()), MathLib.log(getMaximum()), UNIT);
    }

    /**
     * Returns this {@link Dimensionless} raised to the power of the specified
     * {@link Dimensionless} exponent.
     *
     * @param  exp the exponent.
     * @return <code>this**exp</code>
     */
    public Dimensionless pow(Dimensionless exp) {
        Dimensionless system = to(Unit.ONE);
        double min = system.getMinimum();
        double max = system.getMaximum();
        return Quantity.valueOf(
            MathLib.pow(getMinimum(), exp.getMinimum()),
            MathLib.pow(getMaximum(), exp.getMaximum()), UNIT);
    }

    /**
     * Returns an {@link Angle} such as its sine is this {@link Dimensionless}.
     *
     * @return the arc sine of this angle.
     */
    public Angle asin() {
        double min = getMinimum();
        double max = getMaximum();
        if (min < -1.0) {
            min = -1;
        }
        if (max > 1.0) {
            max = 1;
        }
        return Quantity.valueOf(
            MathLib.asin(min), MathLib.asin(max), SI.RADIAN);
    }

    /**
     * Returns an {@link Angle} such as its cosine is this {@link Dimensionless}.
     *
     * @return the arc cosine of this angle.
     */
    public Angle acos() {
        double min = getMinimum();
        double max = getMaximum();
        if (min < -1.0) {
            min = -1;
        }
        if (max > 1.0) {
            max = 1;
        }
        return Quantity.valueOf(
            MathLib.acos(max), MathLib.acos(min), SI.RADIAN);
    }

    /**
     * Returns an {@link Angle} such as its tangent is this {@link Dimensionless}.
     *
     * @return the arc tangent of this angle
     */
    public Angle atan() {
        return Angle.atan2(this, ONE);
    }

    private static final long serialVersionUID = 1L;
}
