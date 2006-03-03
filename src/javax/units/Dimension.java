/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.units;

import java.io.Serializable;

import javax.units.converters.UnitConverter;

/**
 * <p> This class represents the fundamental dimension of an unit.
 *     Two units <code>u1</code> and <code>u2</code> are 
 *     {@link Unit#isCompatible compatible} if and 
 *     only if <code>(u1.getDimension().equals(u2.getDimension()))</code></p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.1, January 22, 2006
 * @see Unit#getDimension
 */
public final class Dimension implements Serializable {

    /**
     * Holds dimensionless.
     */
    public static final Dimension NONE = new Dimension(Unit.ONE);

    /**
     * Holds length dimension (L).
     */
    public static final Dimension LENGTH = new Dimension('L');

    /**
     * Holds mass dimension (M).
     */
    public static final Dimension MASS = new Dimension('M');

    /**
     * Holds time dimension (T).
     */
    public static final Dimension TIME = new Dimension('T');

    /**
     * Holds electric current dimension (I).
     */
    public static final Dimension ELECTRIC_CURRENT = new Dimension('I');

    /**
     * Holds temperature dimension (θ).
     */
    public static final Dimension TEMPERATURE = new Dimension('θ');

    /**
     * Holds amount of substance dimension (N).
     */
    public static final Dimension AMOUNT_OF_SUBSTANCE = new Dimension('N');

    /**
     * Holds luminous intensity dimension (J).
     */
    public static final Dimension LUMINOUS_INTENSITY = new Dimension('J');

    /**
     * Holds the pseudo unit associated to this dimension.
     */
    private Unit _pseudoUnit;

    /**
     * Holds the intrinsic unit (only for fundamental dimensions).
     */
    private final Unit _intrinsicUnit;

    /**
     * Returns a new dimension associated to the specified symbol.
     * 
     * @param symbol the symbol for this dimension.
     */
    public Dimension(char symbol) {
        _intrinsicUnit = new BaseUnit("[" + symbol + "]", Dimension.NONE);
        _pseudoUnit = _intrinsicUnit;
    }

    /**
     * Creates a dimension having the specified pseudo-unit.
     * 
     * @param pseudoUnit the pseudo-unit identifying this dimension.
     */
    private Dimension(Unit pseudoUnit) {
        _intrinsicUnit = null;
        _pseudoUnit = pseudoUnit;
    }

    /**
     * Declares this dimension equivalent to the one specified 
     * (allows for conversion between units of those dimensions).
     * For example:[code]
     *     // Makes length and time equivalent (relativistic context).
     *     LENGTH.setEquivalentTo(TIME, new MultiplyConverter(1 / c));
     *     
     *     // Restores the length dimension to its default.
     *     LENGTH.setEquivalentTo(LENGTH, Converter.IDENTITY));
     *     
     *  [/code]
     * 
     * @param that the dimension equivalent to this dimension.
     * @param transform the transform to the specified dimension 
     *        (typically a multiply converter).
     * @throws IllegalArgumentException if the specified dimension is not 
     *         fundamental dimension (created with the 
     *         {@link Dimension#Dimension(char)} constructor).
     */
    public synchronized void setEquivalentTo(Dimension that,
            UnitConverter transform) {
        if (that._intrinsicUnit == null)
            throw new IllegalArgumentException("Dimension: " + that
                    + " is not fundamental");
        _pseudoUnit = (transform.equals(UnitConverter.IDENTITY)) ? that._intrinsicUnit
                : that._intrinsicUnit.transform(transform);
    }

    /**
     * Returns the product of this dimension with the one specified.
     *
     * @param  that the dimension multiplicand.
     * @return <code>this * that</code>
     */
    public final Dimension multiply(Dimension that) {
        return new Dimension(this._pseudoUnit.times(that._pseudoUnit));
    }

    /**
     * Returns the quotient of this dimension with the one specified.
     *
     * @param  that the dimension divisor.
     * @return <code>this / that</code>
     */
    public final Dimension divide(Dimension that) {
        return new Dimension(this._pseudoUnit.divide(that._pseudoUnit));
    }

    /**
     * Returns this dimension raised to an exponent.
     *
     * @param  n the exponent.
     * @return the result of raising this dimension to the exponent.
     */
    public final Dimension pow(int n) {
        return new Dimension(this._pseudoUnit.pow(n));
    }

    /**
     * Returns the given root of this dimension.
     *
     * @param  n the root's order.
     * @return the result of taking the given root of this dimension.
     * @throws ArithmeticException if <code>n == 0</code>.
     */
    public final Dimension root(int n) {
        return new Dimension(this._pseudoUnit.root(n));
    }

    /**
     * Returns the standard <code>String</code> representation of this
     * dimension.
     *
     * @return string representation of this dimension.
     */
    public String toString() {
        return _pseudoUnit.getBaseUnits().toString();
    }

    /**
     * Returns the intrinsic transform of this dimension.
     * @return the intrinsic transform to its equivalent form.
     */
    UnitConverter getTransform() {
        return this._pseudoUnit.toBaseUnits();
    }

    /**
     * Indicates if the specified dimension is considered equals to the one
     * specified.
     *
     * @param that the object to compare to.
     * @return <code>true</code> if this dimension is equals to that dimension;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Object that) {
        if (this == that)
            return true;
        return (that instanceof Dimension)
                && ((Dimension) that)._pseudoUnit.getBaseUnits().equals(
                        _pseudoUnit.getBaseUnits());
    }

    /**
     * Returns the hash code for this dimension.
     *
     * @return this dimension hashcode value.
     */
    public int hashCode() {
        return _pseudoUnit.hashCode();
    }

    private static final long serialVersionUID = -5206990834227516550L;
}