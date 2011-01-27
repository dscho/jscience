/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.number;

import java.io.IOException;
import java.math.BigDecimal;
import javolution.context.ObjectFactory;
import javolution.lang.MathLib;
import javolution.text.Cursor;
import javolution.text.TextFormat;
import javolution.text.TypeFormat;

/**
 * <p> This class represents a 64 bits floating point number.</p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, November 20, 2009
 */
public final class Float64 extends FieldNumber<Float64> {

    /**
     * Holds the default text format for 64 bits floating points numbers.
     *
     * @see TextFormat#getDefault
     */
    protected static final TextFormat<Float64> TEXT_FORMAT = new TextFormat<Float64>(Float64.class) {

        @Override
        public Appendable format(Float64 float64, Appendable out) throws IOException {
            return TypeFormat.format(float64._value, out);
        }

        @Override
        public Float64 parse(CharSequence csq, Cursor cursor) throws IllegalArgumentException {
            return Float64.valueOf(TypeFormat.parseDouble(csq, cursor));
        }
    };

    /**
     * Holds the factory used to produce 64 bits float instances.
     */
    private static final ObjectFactory<Float64> FACTORY = new ObjectFactory<Float64>() {

        protected Float64 create() {
            return new Float64();
        }
    };

    /**
     * The 64 bits floating point representing zero.
     */
    public static final Float64 ZERO = new Float64(0.0);

    /**
     * The 64 bits floating point representing one.
     */
    public static final Float64 ONE = new Float64(1.0);

    /**
     * The associated double value.
     */
    private double _value;

    /**
     * Default constructor.
     */
    private Float64() {
    }

    /**
     * Creates a 64 bits float number always on the heap independently from the
     * current {@link javolution.context.AllocatorContext allocator context}.
     * To allow for custom object allocation policies, static factory methods
     * <code>valueOf(...)</code> are recommended.
     *
     * @param doubleValue the <code>double</code> value for this number.
     */
    public Float64(double doubleValue) {
        _value = doubleValue;
    }

    /**
     * Returns the 64 bits float from the specified <code>double</code> value.
     *
     * @param  doubleValue the <code>double</code> value for this number.
     * @return the corresponding number.
     * @see    #doubleValue()
     */
    public static Float64 valueOf(double doubleValue) {
        Float64 r = FACTORY.object();
        r._value = doubleValue;
        return r;
    }

    /**
     * Returns the 64 bits floating point number for the specified character
     * sequence.
     *
     * @param  csq the character sequence.
     * @return <code>TEXT_FORMAT.parse(csq)</code>.
     * @throws IllegalArgumentException if the character sequence does not
     *         contain a parsable number.
     * @see #TEXT_FORMAT
     */
    public static Float64 valueOf(CharSequence csq) {
        return TEXT_FORMAT.parse(csq);
    }

    /**
     * Indicates if this number is infinite.
     *
     * @return <code>true</code> if this number is infinite;
     *         <code>false</code> otherwise.
     */
    public boolean isInfinite() {
        return Double.isInfinite(_value);
    }

    /**
     * Indicates if this number is not a number.
     *
     * @return <code>true</code> if this number is NaN;
     *         <code>false</code> otherwise.
     */
    public boolean isNaN() {
        return Double.isNaN(_value);
    }

    /**
     * Returns the sum of this number with the specified value.
     *
     * @param  value the value to be added.
     * @return <code>this + value</code>.
     */
    public Float64 plus(double value) {
        Float64 r = FACTORY.object();
        r._value = this._value + value;
        return r;
    }

    /**
     * Returns the difference between this number and the specified value.
     *
     * @param  value the value to be subtracted.
     * @return <code>this - value</code>.
     */
    public Float64 minus(double value) {
        Float64 r = FACTORY.object();
        r._value = this._value - value;
        return r;
    }

    /**
     * Returns the product of this number with the specified value.
     *
     * @param  value the value multiplier.
     * @return <code>this · value</code>.
     */
    public Float64 times(double value) {
        Float64 r = FACTORY.object();
        r._value = this._value * value;
        return r;
    }

    /**
     * Returns this number divided by the specified value.
     *
     * @param  value the value divisor.
     * @return <code>this / value</code>.
     */
    public Float64 divide(double value) {
        Float64 r = FACTORY.object();
        r._value = this._value / value;
        return r;
    }

    /**
     * Returns the positive square root of this number.
     *
     * @return <code>sqrt(this)</code>.
     */
    public Float64 sqrt() {
        Float64 r = FACTORY.object();
        r._value = MathLib.sqrt(this._value);
        return r;
    }

    /**
     * Returns the exponential number <i>e</i> raised to the power of this
     * number.
     *
     * @return <code>exp(this)</code>.
     */
    public Float64 exp() {
        Float64 r = FACTORY.object();
        r._value = MathLib.exp(this._value);
        return r;
    }

    /**
     * Returns the natural logarithm (base e) of this number.
     *
     * @return <code>log(this)</code>.
     */
    public Float64 log() {
        Float64 r = FACTORY.object();
        r._value = MathLib.log(this._value);
        return r;
    }

    /**
     * Returns this number raised to the power of the specified exponent.
     *
     * @param  that the exponent.
     * @return <code>this**that</code>.
     */
    public Float64 pow(Float64 that) {
        Float64 r = FACTORY.object();
        r._value = MathLib.pow(this._value, that._value);
        return r;
    }

    /**
     * Returns this number raised to the specified power.
     *
     * @param  e the exponent.
     * @return <code>this**e</code>.
     */
    public Float64 pow(double e) {
        Float64 r = FACTORY.object();
        r._value = MathLib.pow(this._value, e);
        return r;
    }

    /**
     * Indicates if this number is equal to the specified value.
     *
     * @param value the value to compare with.
     * @return <code>this.doubleValue() == value</code>.
     */
    public boolean equals(double value) {
        return this._value == value;
    }

    /**
     * Compares this number with the specified value for order.
     *
     * @param value the value to be compared with.
     * @return a negative integer, zero, or a positive integer as this number
     *        is less than, equal to, or greater than the specified value.
     */
    public int compareTo(double value) {
        if (this._value < value) {
            return -1;
        } else if (this._value > value) {
            return 1;
        } else {
            long l1 = Double.doubleToLongBits(this._value);
            long l2 = Double.doubleToLongBits(value);
            return (l1 == l2 ? 0 : (l1 < l2 ? -1 : 1));
        }
    }

    /**
     * Returns the closest integer value to this 64 bits floating point number.
     * 
     * @return this number rounded to the nearest integer.
     */
    public long round() {
        return MathLib.round(_value);
    }

    // Implements GroupAdditive.
    public Float64 opposite() {
        Float64 r = FACTORY.object();
        r._value = -this._value;
        return r;
    }

    // Implements GroupAdditive.
    public Float64 plus(Float64 that) {
        Float64 r = FACTORY.object();
        r._value = this._value + that._value;
        return r;
    }

    @Override
    public Float64 minus(Float64 that) {
        Float64 r = FACTORY.object();
        r._value = this._value - that._value;
        return r;
    }

    @Override
    public Float64 times(long multiplier) {
        return this.times((double) multiplier);
    }

    // Implements GroupMultiplicative.
    public Float64 times(Float64 that) {
        Float64 r = FACTORY.object();
        r._value = this._value * that._value;
        return r;
    }

    // Implements GroupMultiplicative.
    public Float64 inverse() {
        Float64 r = FACTORY.object();
        r._value = 1.0 / this._value;
        return r;
    }

    @Override
    public Float64 divide(long n) {
        return this.divide((double) n);
    }

    @Override
    public Float64 divide(Float64 that) {
        Float64 r = FACTORY.object();
        r._value = this._value / that._value;
        return r;
    }

    @Override
    public Float64 pow(int exp) {
        return this.pow((double) exp);
    }

    // Implements abstract class Number.
    public Float64 abs() {
        Float64 r = FACTORY.object();
        r._value = MathLib.abs(this._value);
        return r;
    }

    // Implements abstract class Number.
    public long longValue() {
        return (long) _value;
    }

    // Implements abstract class Number.
    public double doubleValue() {
        return _value;
    }

    // Implements abstract class Number.
    public BigDecimal decimalValue() {
        return BigDecimal.valueOf(_value);
    }

    // Implements abstract class Number.
    public int compareTo(Float64 that) {
        return compareTo(that._value);
    }

    // Implements abstract class Number.
    public Float64 copy() {
        return Float64.valueOf(_value);
    }
    private static final long serialVersionUID = 1L;

}
