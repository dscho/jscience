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

import javolution.util.Text;
import javolution.util.TextBuilder;
import javolution.util.TypeFormat;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This class represents a 32 bits floating point number.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class Float32 extends RealtimeNumber implements Comparable {

    /**
     * Holds the default XML representation for 32 bits floating point numbers.
     * This representation consists of a simple <code>value</code> attribute.
     */
    protected static final XmlFormat FLOAT32_XML = new XmlFormat(Float32.class) {
        public void format(Object obj, XmlElement xml) {
            xml.setAttribute("value", ((Float32) obj)._value);
        }

        public Object parse(XmlElement xml) {
            return Float32.valueOf(xml.getAttribute("value", 0.0f));
        }
    };

    /**
     * Holds the factory used to produce {@link Float32} instances.
     */
    private static final Factory FACTORY = new Factory() {

        public Object create() {
            return new Float32();
        }
    };

    /**
     * The 32 bits floating point representing zero.
     */
    public static final Float32 ZERO = (Float32) valueOf(0.0f).moveHeap();

    /**
     * The 32 bits float one.
     */
    public static final Float32 ONE = (Float32) valueOf(1.0f).moveHeap();

    /**
     * The associated double value.
     */
    private float _value;

    /**
     * Default constructor.
     */
    private Float32() {
    }

    /**
     * Returns the 32 bits float of specified <code>float</code> value.
     *
     * @param  floatValue the <code>float</code> value for this number.
     * @return the corresponding number.
     * @see    #doubleValue()
     */
    public static Float32 valueOf(float floatValue) {
        Float32 r = (Float32) FACTORY.object();
        r._value = floatValue;
        return r;
    }

    /**
     * Returns the 32 bits float for the specified character sequence.
     *
     * @param  chars the character sequence.
     * @return the corresponding number.
     */
    public static Float32 valueOf(CharSequence chars) {
        Float32 r = (Float32) FACTORY.object();
        r._value = TypeFormat.parseFloat(chars);
        return r;
    }

    /**
     * Indicates if this 32 bits float is infinite.
     *
     * @return <code>true</code> if this float is infinite;
     *         <code>false</code> otherwise.
     */
    public boolean isInfinite() {
        return Float.isInfinite(_value);
    }

    /**
     * Indicates if this 32 bits Float is not a number.
     *
     * @return <code>true</code> if this float is NaN;
     *         <code>false</code> otherwise.
     */
    public boolean isNaN() {
        return Float.isNaN(_value);
    }

    /**
     * Returns the negation of this number.
     *
     * @return <code>-this</code>.
     */
    public Float32 negate() {
        Float32 r = (Float32) FACTORY.object();
        r._value = -this._value;
        return r;
    }

    /**
     * Returns the sum of this number with the one specified.
     *
     * @param  that the number to be added.
     * @return <code>this + that</code>.
     */
    public Float32 add(Float32 that) {
        Float32 r = (Float32) FACTORY.object();
        r._value = this._value + that._value;
        return r;
    }

    /**
     * Returns the difference between this number and the one specified.
     *
     * @param  that the number to be subtracted.
     * @return <code>this - that</code>.
     */
    public Float32 subtract(Float32 that) {
        Float32 r = (Float32) FACTORY.object();
        r._value = this._value - that._value;
        return r;
    }

    /**
     * Returns the product of this number with the one specified.
     *
     * @param  that the multiplier.
     * @return <code>this * that</code>.
     */
    public Float32 multiply(Float32 that) {
        Float32 r = (Float32) FACTORY.object();
        r._value = this._value * that._value;
        return r;
    }

    /**
     * Returns the inverse of this number.
     *
     * @return <code>1 / this</code>.
     */
    public Float32 inverse() {
        Float32 r = (Float32) FACTORY.object();
        r._value = 1.0f / this._value;
        return r;
    }

    /**
     * Returns this number divided by the one specified.
     *
     * @param  that the divisor.
     * @return <code>this / that</code>.
     */
    public Float32 divide(Float32 that) {
        Float32 r = (Float32) FACTORY.object();
        r._value = this._value / that._value;
        return r;
    }

    /**
     * Returns the absolute value of this number.
     *
     * @return <code>abs(this)</code>.
     */
    public Float32 abs() {
        Float32 r = (Float32) FACTORY.object();
        r._value = Math.abs(this._value);
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
     * Indicates if two 32 bits float are "sufficiently" alike to be 
     * considered equal.
     *
     * @param  that the number to compare with.
     * @param  tolerance the maximum difference between them before
     *         they are considered <i>not</i> equal.
     * @return <code>true</code> if they are considered equal;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Float32 that, float tolerance) {
        return Math.abs(this._value - that._value) <= tolerance;
    }

    /**
     * Compares this 32 bits float  against the specified object.
     *
     * @param  that the object to compare with.
     * @return <code>true</code> if the objects are the same;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Object that) {
        return (that instanceof Float32)
                && (this._value == ((Float32) that)._value);
    }

    /**
     * Returns the hash code for this number.
     *
     * @return the hash code value.
     */
    public int hashCode() {
        return Float.floatToIntBits(_value);
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
        return _value;
    }

    /**
     * Returns the value of this number as a <code>double</code>.
     *
     * @return the <code>double</code> value
     */
    public double doubleValue() {
        return _value;
    }

    /**
     * Compares two 32 bits float numerically.
     *
     * @param  that the number to compare with.
     * @return -1, 0 or 1 as this number is numerically less than, equal
     *         to, or greater than <code>that</code>.
     */
    public int compareTo(Float32 that) {
        if (this._value < that._value) {
            return -1;
        } else if (this._value > that._value) {
            return 1;
        } else {
            int i1 = Float.floatToIntBits(this._value);
            int i2 = Float.floatToIntBits(that._value);
            return (i1 == i2 ? 0 : (i1 < i2 ? -1 : 1));
        }
    }

    // Implements Comparable
    public int compareTo(Object that) {
        return this.compareTo((Float32) that);
    }

    // Implements Operable.
    public Operable plus(Operable that) {
        return this.add((Float32) that);
    }

    // Implements Operable.
    public Operable opposite() {
        return this.negate();
    }

    // Implements Operable.
    public Operable times(Operable that) {
        return this.multiply((Float32) that);
    }

    // Implements Operable.
    public Operable reciprocal() {
        return this.inverse();
    }

}