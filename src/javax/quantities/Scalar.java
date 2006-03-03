/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.quantities;

import java.io.Serializable;

import javax.units.Unit;
import javax.units.converters.UnitConverter;

/**
 * This class represents a simple quantity implementation, that is completely 
 * specified by its magnitude and has no direction.
 * It should be noted that instances of this class are mutable and 
 * can be used by functions to return compound quantities. For example:[code]
 *    public void getCoefficients(Scalar<Length> x, Scalar<Velocity> v, Scalar<Acceleration> a) {
 *        ...
 *        x.setValue(coef0, METER);
 *        v.setValue(coef1, METER_PER_SECOND);
 *        a.setValue(coef3, METER_PER_SQUARE_SECOND);
 *   }[/code]
 *  For performance reasons and to avoid unnecessary conversions, it is 
 *  recommended to use the same units (e.g. non-scaled {@link javax.units.SI SI}
 *  units) when setting and reading scalar values.
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, January 14, 2006
 */
public class Scalar<Q extends Quantity> implements Quantity<Q>,
        Comparable<Quantity<Q>>, Serializable {

    /**
     * Holds the amount value.
     */
    private double _value;

    /**
     * Holds the unit.
     */
    private Unit<Q> _unit;

    /**
     * Creates a simple quantity of specified amount stated in
     * the specified unit.
     * 
     * @param value the quantity amount stated in the specified unit.
     * @param unit the value unit.
     */
    public Scalar(double value, Unit<Q> unit) {
        _value = value;
        _unit = unit;
    }

    /**
     * Sets this quantity amount.
     * 
     * @param value the new amount stated in the specified unit.
     * @param unit the unit in which the value is stated.
     */
    public final void setValue(double value, Unit<Q> unit) {
        _value = value;
        _unit = unit;
    }

    // Implements Quantity<Q>
    public final double doubleValue(Unit<Q> unit) {
        if (unit == _unit)
            return _value;
        UnitConverter cvtr = _unit.getConverterTo(unit);
        return cvtr.convert(_value);
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
        if (this._value < thatValue) {
            return -1;
        } else if (this._value > thatValue) {
            return 1;
        } else {
            long l1 = java.lang.Double.doubleToLongBits(this._value);
            long l2 = java.lang.Double.doubleToLongBits(thatValue);
            return (l1 == l2 ? 0 : (l1 < l2 ? -1 : 1));
        }
    }

    private static final long serialVersionUID = 4549321816567634463L;

}