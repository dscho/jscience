/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.numbers;

import javolution.lang.MathLib;
import javolution.lang.Text;
import javolution.lang.TypeFormat;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

/**
 * <p> This class represents a 64 bits floating point number.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 6, 2004
 */
public final class Float64 extends Number<Float64> {

    /**
     * Holds the default XML representation for 64 bits floating point numbers.
     * This representation consists of a simple <code>value</code> attribute
     * holding the {@link #toText() textual} representation.
     */
    protected static final XmlFormat<Float64> XML = new XmlFormat<Float64>(
            Float64.class) {
        public void format(Float64 obj, XmlElement xml) {
            xml.setAttribute("value", obj._value);
        }

        public Float64 parse(XmlElement xml) {
            return Float64.valueOf(xml.getAttribute("value", 0.0));
        }
    };

    /**
     * Holds the factory used to produce 64 bits float instances.
     */
    private static final Factory<Float64> FACTORY = new Factory<Float64>() {

        public Float64 create() {
            return new Float64();
        }
    };

    /**
     * The 64 bits floating point representing zero.
     */
    public static final Float64 ZERO = valueOf(0.0).moveHeap();

    /**
     * The 64 bits floating point representing one.
     */
    public static final Float64 ONE = valueOf(1.0).moveHeap();

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
     * Returns the number for the specified character sequence.
     *
     * @param  chars the character sequence.
     * @return the corresponding number.
     */
    public static Float64 valueOf(CharSequence chars) {
        Float64 r = FACTORY.object();
        r._value = TypeFormat.parseDouble(chars);
        return r;
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
     * Returns the opposite of this number.
     *
     * @return <code>-this</code>.
     */
    public Float64 opposite() {
        Float64 r = FACTORY.object();
        r._value = -this._value;
        return r;
    }

    /**
     * Returns the sum of this number with the one specified.
     *
     * @param  that the number to be added.
     * @return <code>this + that</code>.
     */
    public Float64 plus(Float64 that) {
        Float64 r = FACTORY.object();
        r._value = this._value + that._value;
        return r;
    }

    /**
     * Returns the difference between this number and the one specified.
     *
     * @param  that the number to be subtracted.
     * @return <code>this - that</code>.
     */
    public Float64 minus(Float64 that) {
        Float64 r = FACTORY.object();
        r._value = this._value - that._value;
        return r;
    }

    /**
     * Returns the product of this number with the one specified.
     *
     * @param  that the number multiplier.
     * @return <code>this * that</code>.
     */
    public Float64 times(Float64 that) {
        Float64 r = FACTORY.object();
        r._value = this._value * that._value;
        return r;
    }

    /**
     * Returns the reciprocal of this number.
     *
     * @return <code>1 / this</code>.
     */
    public Float64 reciprocal() {
        Float64 r = FACTORY.object();
        r._value = 1.0 / this._value;
        return r;
    }

    /**
     * Returns this number divided by the one specified.
     *
     * @param  that the number divisor.
     * @return <code>this / that</code>.
     */
    public Float64 divide(Float64 that) {
        Float64 r = FACTORY.object();
        r._value = this._value / that._value;
        return r;
    }

    /**
     * Compares the {@link #norm norm} of this number with that number.
     *
     * @return <code>|this| > |that|</code>
     */
    public boolean isLargerThan(Float64 that) {
        return MathLib.abs(this._value) > MathLib.abs(that._value);
    }

    /**
     * Returns the norm of this number.
     *
     * @return <code>abs(this)</code>.
     */
    public Float64 norm() {
        Float64 r = FACTORY.object();
        r._value = MathLib.abs(this._value);
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
     * Returns the decimal text representation of this number.
     *
     * @return the text representation of this number.
     */
    public Text toText() {
        return Text.valueOf(_value);
    }

    /**
     * Compares this number against the specified object.
     *
     * @param  that the object to compare with.
     * @return <code>true</code> if the objects are the same;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Object that) {
        return (that instanceof Float64)
                && (this._value == ((Float64) that)._value);
    }

    /**
     * Returns the hash code for this number.
     * 
     * @return the hash code value.
     */
    public int hashCode() {
        int h = Float.floatToIntBits((float) _value);
        h += ~(h << 9);
        h ^= (h >>> 14);
        h += (h << 4);
        return h ^ (h >>> 10);
    }

    // Implements abstract method.
    public long longValue() {
        return (long) _value;
    }

    // Implements abstract method.
    public double doubleValue() {
        return _value;
    }

    // Implements Comparable.
    public int compareTo(Float64 that) {
        if (this._value < that._value) {
            return -1;
        } else if (this._value > that._value) {
            return 1;
        } else {
            long l1 = Double.doubleToLongBits(this._value);
            long l2 = Double.doubleToLongBits(that._value);
            return (l1 == l2 ? 0 : (l1 < l2 ? -1 : 1));
        }
    }

    private static final long serialVersionUID = 1L;
}