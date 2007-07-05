/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2007 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.measure;

import java.io.Serializable;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

/**
 * <p> This class represents the result of a measurement stated in a 
 *     known unit.</p>
 * 
 * <p> There is no constraint upon the measurement value itself: scalars, 
 *     vectors, or even data sets are valid values as long as 
 *     an aggregate magnitude can be determined (see {@link Measurable}).</p>
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 4.1, June 8, 2007
 */
public abstract class Measure<V, Q extends Quantity> implements Measurable<Q>,
        Serializable {

    /**
     * Default constructor.
     */
    protected Measure() {
    }

    /**
     * Returns the scalar measure for the specified number stated in the 
     * specified unit.
     * 
     * @param number the measurement value.
     * @param unit the measurement unit.
     */
    public static <N extends java.lang.Number, Q extends Quantity> Measure<N, Q> valueOf(
            N number, Unit<Q> unit) {
        return new Number<N, Q>(number, unit);
    }

    /**
     * Returns the scalar measure for the specified <code>double</code>
     * stated in the specified unit (this method is equivalent to 
     * {@link #valueOf(Number, Unit)} but avoids autoboxing).
     * 
     * @param doubleValue the measurement value.
     * @param unit the measurement unit.
     */
    public static <Q extends Quantity> Measure<java.lang.Double, Q> valueOf(
            double doubleValue, Unit<Q> unit) {
        return new Double<Q>(doubleValue, unit);
    }

    /**
     * Returns the scalar measure for the specified <code>double</code>
     * stated in the specified unit (this method is equivalent to 
     * {@link #valueOf(Number, Unit)} but avoids autoboxing).
     * 
     * @param longValue the measurement value.
     * @param unit the measurement unit.
     */
    public static <Q extends Quantity> Measure<java.lang.Long, Q> valueOf(
            long longValue, Unit<Q> unit) {
        return new Long<Q>(longValue, unit);
    }

    /**
     * Returns the scalar measure for the specified <code>float</code>
     * stated in the specified unit (this method is equivalent to 
     * {@link #valueOf(Number, Unit)} but avoids autoboxing).
     * 
     * @param floatValue the measurement value.
     * @param unit the measurement unit.
     */
    public static <Q extends Quantity> Measure<java.lang.Float, Q> valueOf(
            float floatValue, Unit<Q> unit) {
        return new Float<Q>(floatValue, unit);
    }

    /**
     * Returns the scalar measure for the specified <code>int</code>
     * stated in the specified unit (this method is equivalent to 
     * {@link #valueOf(Number, Unit)} but avoids autoboxing).
     * 
     * @param intValue the measurement value.
     * @param unit the measurement unit.
     */
    public static <Q extends Quantity> Measure<java.lang.Integer, Q> valueOf(
            int intValue, Unit<Q> unit) {
        return new Integer<Q>(intValue, unit);
    }

    /**
     * Returns the measurement value of this measure.
     *    
     * @return the measurement value.
     */
    public abstract V getValue();

    /**
     * Returns the measurement unit of this measure.
     * 
     * @return the measurement unit.
     */
    public abstract Unit<Q> getUnit();

    /**
     * Compares this measure against the specified object for 
     * strict equality (same unit and amount).
     * To compare measures stated using different units the  
     * {@link #compareTo} method should be used. 
     *
     * @param  obj the object to compare with.
     * @return <code>true</code> if both objects are identical (same 
     *         unit and same amount); <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Measure))
            return false;
        Measure that = (Measure) obj;
        return this.getUnit().equals(that.getUnit())
                && this.getValue().equals(that.getValue());
    }

    /**
     * Returns the hash code for this scalar.
     * 
     * @return the hash code value.
     */
    public int hashCode() {
        return getUnit().hashCode() + getValue().hashCode();
    }

    /**
     * Returns the <code>String</code> representation of this measure.
     * 
     * @return <code>this.getValue() + " " + this.getUnit()</code>
     */
    public String toString() {
        return this.getValue() + " " + this.getUnit();
    }

    /**
     * Holds scalar implementation for any number value.
     */
    private static final class Number<N extends java.lang.Number, Q extends Quantity>
            extends Measure<N, Q> {

        private final N _value;

        private final Unit<Q> _unit;

        public Number(N value, Unit<Q> unit) {
            _value = value;
            _unit = unit;
        }

        @Override
        public Unit<Q> getUnit() {
            return _unit;
        }

        @Override
        public N getValue() {
            return _value;
        }

        public double doubleValue(Unit<Q> unit) {
            if ((unit == _unit) || (unit.equals(_unit)))
                return _value.doubleValue();
            return _unit.getConverterTo(unit).convert(_value.doubleValue());
        }

        public long longValue(Unit<Q> unit) throws ArithmeticException {
            double doubleValue = doubleValue(unit);
            if ((doubleValue < java.lang.Long.MIN_VALUE)
                    || (doubleValue > java.lang.Long.MAX_VALUE))
                throw new ArithmeticException(doubleValue + " " + unit
                        + " cannot be represented as long");
            return Math.round(doubleValue);
        }

        public int compareTo(Measurable<Q> that) {
            return java.lang.Double.compare(_value.doubleValue(), that
                    .doubleValue(_unit));
        }

        private static final long serialVersionUID = 1L;

    }

    /**
     * Holds scalar implementation for <code>double</code> values.
     */
    private static final class Double<Q extends Quantity> extends
            Measure<java.lang.Double, Q> {

        private final double _value;

        private final Unit<Q> _unit;

        public Double(double value, Unit<Q> unit) {
            _value = value;
            _unit = unit;
        }

        @Override
        public Unit<Q> getUnit() {
            return _unit;
        }

        @Override
        public java.lang.Double getValue() {
            return _value;
        }

        public double doubleValue(Unit<Q> unit) {
            if ((unit == _unit) || (unit.equals(_unit)))
                return _value;
            return _unit.getConverterTo(unit).convert(_value);
        }

        public long longValue(Unit<Q> unit) throws ArithmeticException {
            double doubleValue = doubleValue(unit);
            if ((doubleValue < java.lang.Long.MIN_VALUE)
                    || (doubleValue > java.lang.Long.MAX_VALUE))
                throw new ArithmeticException(doubleValue + " " + unit
                        + " cannot be represented as long");
            return Math.round(doubleValue);
        }

        public int compareTo(Measurable<Q> that) {
            return java.lang.Double.compare(_value, that.doubleValue(_unit));
        }

        private static final long serialVersionUID = 1L;

    }

    /**
     * Holds scalar implementation for <code>long</code> values.
     */
    private static final class Long<Q extends Quantity> extends
            Measure<java.lang.Long, Q> {

        private final long _value;

        private final Unit<Q> _unit;

        public Long(long value, Unit<Q> unit) {
            _value = value;
            _unit = unit;
        }

        @Override
        public Unit<Q> getUnit() {
            return _unit;
        }

        @Override
        public java.lang.Long getValue() {
            return _value;
        }

        public double doubleValue(Unit<Q> unit) {
            if (unit == _unit)
                return _value;
            return _unit.getConverterTo(unit).convert(_value);
        }

        public long longValue(Unit<Q> unit) throws ArithmeticException {
            if ((unit == _unit) || (unit.equals(_unit)))
                return _value; // No conversion, returns value directly.
            double doubleValue = doubleValue(unit);
            if ((doubleValue < java.lang.Long.MIN_VALUE)
                    || (doubleValue > java.lang.Long.MAX_VALUE))
                throw new ArithmeticException(doubleValue + " " + unit
                        + " cannot be represented as long");
            return Math.round(doubleValue);
        }

        public int compareTo(Measurable<Q> that) {
            return java.lang.Double.compare(_value, that.doubleValue(_unit));
        }

        private static final long serialVersionUID = 1L;

    }

    /**
     * Holds scalar implementation for <code>float</code> values.
     */
    private static final class Float<Q extends Quantity> extends
            Measure<java.lang.Float, Q> {

        private final float _value;

        private final Unit<Q> _unit;

        public Float(float value, Unit<Q> unit) {
            _value = value;
            _unit = unit;
        }

        @Override
        public Unit<Q> getUnit() {
            return _unit;
        }

        @Override
        public java.lang.Float getValue() {
            return _value;
        }

        public double doubleValue(Unit<Q> unit) {
            if ((unit == _unit) || (unit.equals(_unit)))
                return _value;
            return _unit.getConverterTo(unit).convert(_value);
        }

        public long longValue(Unit<Q> unit) throws ArithmeticException {
            double doubleValue = doubleValue(unit);
            if ((doubleValue < java.lang.Long.MIN_VALUE)
                    || (doubleValue > java.lang.Long.MAX_VALUE))
                throw new ArithmeticException(doubleValue + " " + unit
                        + " cannot be represented as long");
            return Math.round(doubleValue);
        }

        public int compareTo(Measurable<Q> that) {
            return java.lang.Double.compare(_value, that.doubleValue(_unit));
        }

        private static final long serialVersionUID = 1L;

    }

    /**
     * Holds scalar implementation for <code>long</code> values.
     */
    private static final class Integer<Q extends Quantity> extends
            Measure<java.lang.Integer, Q> {

        private final int _value;

        private final Unit<Q> _unit;

        public Integer(int value, Unit<Q> unit) {
            _value = value;
            _unit = unit;
        }

        @Override
        public Unit<Q> getUnit() {
            return _unit;
        }

        @Override
        public java.lang.Integer getValue() {
            return _value;
        }

        public double doubleValue(Unit<Q> unit) {
            if (unit == _unit)
                return _value;
            return _unit.getConverterTo(unit).convert(_value);
        }

        public long longValue(Unit<Q> unit) throws ArithmeticException {
            if ((unit == _unit) || (unit.equals(_unit)))
                return _value; // No conversion, returns value directly.
            double doubleValue = doubleValue(unit);
            if ((doubleValue < java.lang.Long.MIN_VALUE)
                    || (doubleValue > java.lang.Long.MAX_VALUE))
                throw new ArithmeticException(doubleValue + " " + unit
                        + " cannot be represented as long");
            return Math.round(doubleValue);
        }

        public int compareTo(Measurable<Q> that) {
            return java.lang.Double.compare(_value, that.doubleValue(_unit));
        }

        private static final long serialVersionUID = 1L;

    }
}