/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.mathematics.numbers;


import java.io.IOException;

import javolution.util.MathLib;
import javolution.lang.Text;
import javolution.lang.TextBuilder;
import javolution.lang.TypeFormat;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This class represents a 64 bits floating point number.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class Float64 extends RealtimeNumber implements Comparable {

    /**
     * Holds the default XML representation for 64 bits floating point numbers.
     * This representation consists of a simple <code>value</code> attribute.
     */
    protected static final XmlFormat FLOAT64_XML = new XmlFormat(Float64.class) {
        public void format(Object obj, XmlElement xml) {
            xml.setAttribute("value", ((Float64) obj)._value);
        }

        public Object parse(XmlElement xml) {
            return Float64.valueOf(xml.getAttribute("value", 0.0));
        }
    };

    /**
     * Holds the factory used to produce 64 bits float instances.
     */
    private static final Factory FACTORY = new Factory() {

        public Object create() {
            return new Float64();
        }
    };

    /**
     * The 64 bits floating point representing zero.
     */
    public static final Float64 ZERO = (Float64) valueOf(0.0).moveHeap();

    /**
     * The 64 bits float one.
     */
    public static final Float64 ONE = (Float64) valueOf(1.0).moveHeap();

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
     * Returns the 64 bits float of specified <code>double</code> value.
     *
     * @param  doubleValue the <code>double</code> value for this number.
     * @return the corresponding number.
     * @see    #doubleValue()
     */
    public static Float64 valueOf(double doubleValue) {
        Float64 r = (Float64) FACTORY.object();
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
        Float64 r = (Float64) FACTORY.object();
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
     * Returns the negation of this number.
     *
     * @return <code>-this</code>.
     */
    public Float64 negate() {
        Float64 r = (Float64) FACTORY.object();
        r._value = -this._value;
        return r;
    }

    /**
     * Returns the sum of this number with the one specified.
     *
     * @param  that the number to be added.
     * @return <code>this + that</code>.
     */
    public Float64 add(Float64 that) {
        Float64 r = (Float64) FACTORY.object();
        r._value = this._value + that._value;
        return r;
    }

    /**
     * Returns the difference between this number and the one specified.
     *
     * @param  that the number to be subtracted.
     * @return <code>this - that</code>.
     */
    public Float64 subtract(Float64 that) {
        Float64 r = (Float64) FACTORY.object();
        r._value = this._value - that._value;
        return r;
    }

    /**
     * Returns the product of this number with the one specified.
     *
     * @param  that the number multiplier.
     * @return <code>this * that</code>.
     */
    public Float64 multiply(Float64 that) {
        Float64 r = (Float64) FACTORY.object();
        r._value = this._value * that._value;
        return r;
    }

    /**
     * Returns the inverse of this number.
     *
     * @return <code>1 / this</code>.
     */
    public Float64 inverse() {
        Float64 r = (Float64) FACTORY.object();
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
        Float64 r = (Float64) FACTORY.object();
        r._value = this._value / that._value;
        return r;
    }

    /**
     * Returns the absolute value of this number.
     *
     * @return <code>abs(this)</code>.
     */
    public Float64 abs() {
        Float64 r = (Float64) FACTORY.object();
        r._value = MathLib.abs(this._value);
        return r;
    }

    /**
     * Returns the positive square root of this number.
     *
     * @return <code>sqrt(this)</code>.
     */
    public Float64 sqrt() {
        Float64 r = (Float64) FACTORY.object();
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
        Float64 r = (Float64) FACTORY.object();
        r._value = MathLib.exp(this._value);
        return r;
    }

    /**
     * Returns the natural logarithm (base e) of this number.
     *
     * @return <code>log(this)</code>.
     */
    public Float64 log() {
        Float64 r = (Float64) FACTORY.object();
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
        Float64 r = (Float64) FACTORY.object();
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
        Float64 r = (Float64) FACTORY.object();
        r._value = MathLib.pow(this._value, that._value);
        return r;
    }

    /**
     * Returns the decimal text representation of this number.
     *
     * @return the text representation of this number.
     */
    public Text toText() {
        try {
            TextBuilder tb = TextBuilder.newInstance();
            TypeFormat.format(_value, tb);
            return tb.toText();
        } catch (IOException ioError) {
            throw new InternalError(); // Should never get there.
        }
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
        long bits = Double.doubleToLongBits(_value);
        return (int) (bits ^ (bits >>> 32));
    }

    /**
     * Returns the value of this number as an <code>int</code>.
     *
     * @return the <code>int</code> value.
     */
    public int intValue() {
        return (int) _value;
    }

    /**
     * Returns the value of this number as a <code>long</code>.
     *
     * @return the <code>long</code> value.
     */
    public long longValue() {
        return (long) _value;
    }

    /**
     * Returns the value of this number as a <code>float</code>.
     *
     * @return the <code>float</code> value.
     */
    public float floatValue() {
        return (float) _value;
    }

    /**
     * Returns the value of this number as a <code>double</code>.
     *
     * @return the <code>double</code> value.
     */
    public double doubleValue() {
        return _value;
    }

    /**
     * Compares two 64 bits float numerically.
     *
     * @param  that the number to compare with.
     * @return -1, 0 or 1 as this number is numerically less than, equal
     *         to, or greater than <code>that</code>.
     */
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

    // Implements Comparable
    public int compareTo(Object that) {
        return this.compareTo((Float64) that);
    }

    // Implements Operable.
    public Operable plus(Operable that) {
        return this.add((Float64) that);
    }

    // Implements Operable.
    public Operable opposite() {
        return this.negate();
    }

    // Implements Operable.
    public Operable times(Operable that) {
        return this.multiply((Float64) that);
    }

    // Implements Operable.
    public Operable reciprocal() {
        return this.inverse();
    }

    private static final long serialVersionUID = 3258125860459590456L;
}