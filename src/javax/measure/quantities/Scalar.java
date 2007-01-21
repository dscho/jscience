/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.measure.quantities;

import java.io.Serializable;

import javax.measure.converters.UnitConverter;
import javax.measure.units.Unit;

/**
 * This class represents a simple quantity implementation, that is completely 
 * specified by its magnitude and has no direction.
 * 
 * <p>Instances of this class are immutable</p>
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 22, 2006
 */
public class Scalar<Q extends Quantity> extends Number implements Quantity<Q>,
        Comparable<Quantity<Q>>, Serializable {

    /**
     * Holds the amount value.
     */
    private double _amount;

    /**
     * Holds the unit.
     */
    private Unit<Q> _unit;

    /**
     * Creates a scalar quantity of specified amount stated in
     * the specified unit.
     * 
     * @param amount the quantity amount stated in the specified unit.
     * @param unit the unit used to specified the amount
     */
    public Scalar(double amount, Unit<Q> unit) {
        _amount = amount;
        _unit = unit;
    }

    /**
     * Returns the amount stated in this scalar quantity {@link #getUnit unit}.
     * 
     * @return the amount value.
     */
    public final double getAmount() {
        return _amount;
    }

    /**
     * Returns the unit used for this scalar quantity.
     * 
     * @return the amount unit.
     */
    public final Unit<Q> getUnit() {
        return _unit;
    }

    // Implements Quantity<Q>
    public final double doubleValue(Unit<Q> unit) {
        if (unit == _unit)
            return _amount;
        UnitConverter cvtr = _unit.getConverterTo(unit);
        return cvtr.convert(_amount);
    }

    // Implements Quantity<Q>
    public final long longValue(Unit<Q> unit) throws ArithmeticException {
        double doubleValue = doubleValue(unit);
        if ((doubleValue >= Long.MIN_VALUE) && (doubleValue <= Long.MAX_VALUE))
            return Math.round(doubleValue);
        throw new ArithmeticException(doubleValue + " " + unit
                + " cannot be represented as long");
    }

    /**
     * Compares the value of this quantity to the specified quantity value
     * both stated using the same unit.
     * 
     * @param that the quantity to compare with.
     * @return negative integer, zero, or a positive integer as this quantity
     *         amount is less than, equal to, or greater than the specified 
     *         quantity.
     */
    public int compareTo(Quantity<Q> that) {
        double thatValue = that.doubleValue(_unit);
        if (this._amount < thatValue) {
            return -1;
        } else if (this._amount > thatValue) {
            return 1;
        } else {
            long l1 = java.lang.Double.doubleToLongBits(this._amount);
            long l2 = java.lang.Double.doubleToLongBits(thatValue);
            return (l1 == l2 ? 0 : (l1 < l2 ? -1 : 1));
        }
    }
    
    /**
     * Returns the amount as an <code>int</code>.
     * This may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>int</code>.
     */
    public final int intValue() {
        return (int) getAmount();
    }

    /**
     * Returns the amount as a <code>long</code>.
     * This may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>long</code>.
     */
    public final long longValue() {
        return (long) getAmount();
    }

    /**
     * Returns the amount as a <code>float</code>.
     * This may involve rounding.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>float</code>.
     */
    public final float floatValue() {
        return (float) getAmount();
    }

    /**
     * Returns the amount as a <code>double</code>.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>double</code>.
     */
    public final double doubleValue() {
        return getAmount();
    }

    /**
     * Compares this scalar  against the specified object.
     *
     * @param  that the object to compare with.
     * @return <code>true</code> if the objects are the same;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Object that) {
        return (that instanceof Scalar)
                && (this._amount == ((Scalar) that)._amount);
    }

    /**
     * Returns the hash code for this scalar.
     * 
     * @return the hash code value.
     */
    public int hashCode() {
        int h = Float.floatToIntBits((float) _amount);
        h += ~(h << 9);
        h ^= (h >>> 14);
        h += (h << 4);
        return h ^ (h >>> 10);
    }

    private static final long serialVersionUID = 1L;
}