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
 * This class represents a dimensionless quantity. It contains methods for
 * performing basic numeric operations such as the elementary exponential,
 * logarithm and trigonometric functions.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Scalar extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = Unit.ONE;

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new Scalar();
        }
    };

    /**
     * Represents a {@link Scalar} amounting to nothing.
     */
    public final static Scalar ZERO = (Scalar) valueOf(0, SYSTEM_UNIT);

    /**
     * Represents a {@link Scalar} amounting to <code>1</code>.
     */
    public final static Scalar ONE = (Scalar) valueOf(1, SYSTEM_UNIT);

    /**
     * Holds the natural logarithmic base.
     */
    public static final Scalar e = (Scalar) valueOf(Math.E, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Scalar() {}

    /**
     * Returns the {@link Scalar} corresponding to the specified quantity.
     *
     * @param  quantity a quantity compatible with {@link Scalar}.
     * @return the specified quantity or a new {@link Scalar} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link Scalar}.
     */
    public static Scalar scalarOf(Quantity quantity) {
        return (Scalar) FACTORY.quantity(quantity);
    }

    /**
     * Returns the {@link Scalar} of specified amount.
     *
     * @param  amount the estimated amount (± 1/2 LSB).
     * @return the corresponding {@link Scalar}.
     */
    public static Scalar valueOf(double amount) {
        return (Scalar) FACTORY.rangeExact(amount, amount);
    }

    /**
     * Returns the {@link Scalar} of specified amount and measurement error.
     *
     * @param  amount the estimated amount (± error).
     * @param  error the measurement error (absolute).
     * @return the corresponding {@link Scalar}.
     */
    public static Scalar valueOf(double amount, double error) {
        return (Scalar) FACTORY.rangeExact(amount - error, amount + error);
    }

    /**
     * Shows {@link Scalar} instances in the specified unit.
     *
     * @param  unit the output unit for {@link Scalar} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    /////////////////////
    // SCALAR SPECIFIC //
    /////////////////////

    /**
     * Returns the exponential number <i>e</i> raised to the power of this
     * {@link Scalar}.
     *
     * @return <code>exp(this)</code>
     */
    public Scalar exp() {
        return (Scalar) FACTORY.rangeApprox(
            Math.exp(getMinimum()), Math.exp(getMaximum()));
    }

    /**
     * Returns the natural logarithm (base e) of this {@link Scalar}.
     *
     * @return <code>log(this)</code>
     */
    public Scalar log() {
        return (Scalar) FACTORY.rangeApprox(
            Math.log(getMinimum()), Math.log(getMaximum()));
    }

    /**
     * Returns this {@link Scalar} raised to the power of the specified
     * {@link Scalar} exponent.
     *
     * @param  exp the exponent.
     * @return <code>this**exp</code>
     */
    public Scalar pow(Scalar exp) {
        return (Scalar) FACTORY.rangeApprox(
            Math.pow(getMinimum(), exp.getMinimum()),
            Math.pow(getMaximum(), exp.getMaximum()));
    }

    /**
     * Returns an {@link Angle} such as its sine is this {@link Scalar}.
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
        return (Angle) Factory.getInstance(SI.RADIAN).rangeApprox(
            Math.asin(min), Math.asin(max));
    }

    /**
     * Returns an {@link Angle} such as its cosine is this {@link Scalar}.
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
        return (Angle) Factory.getInstance(SI.RADIAN).rangeApprox(
            Math.acos(max), Math.acos(min));
    }

    /**
     * Returns an {@link Angle} such as its tangent is this {@link Scalar}.
     *
     * @return the arc tangent of this angle
     */
    public Angle atan() {
        return Angle.atan2(this, ONE);
    }

    private static final long serialVersionUID = -7499422907469986142L;
}
