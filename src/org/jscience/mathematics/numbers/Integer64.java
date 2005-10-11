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
 * <p> This class represents a 64 bits integer number.</p>
 * 
 * <p><i> Note: Arithmetic operation are non-modular (e.g. {@link #reciprocal}
 *        throws {@link ArithmeticException}), for modular arithmetic the 
 *        {@link LargeInteger} class should be used.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 6, 2004
 */
public final class Integer64 extends Number<Integer64> {

    /**
     * Holds the default XML representation for 64 bits integer numbers.
     * This representation consists of a simple <code>value</code> attribute
     * holding the {@link #toText() textual} representation.
     */
    protected static final XmlFormat<Integer64> XML = new XmlFormat<Integer64>(
            Integer64.class) {
        public void format(Integer64 obj, XmlElement xml) {
            xml.setAttribute("value", obj._value);
        }

        public Integer64 parse(XmlElement xml) {
            return Integer64.valueOf(xml.getAttribute("value", 0L));
        }
    };

    /**
     * Holds the factory used to produce 64 bits integer instances.
     */
    private static final Factory<Integer64> FACTORY = new Factory<Integer64>() {

        public Integer64 create() {
            return new Integer64();
        }
    };

    /**
     * The 64 bits integer representing zero.
     */
    public static final Integer64 ZERO = valueOf(0).moveHeap();

    /**
     * The 64 bits integer representing one.
     */
    public static final Integer64 ONE = valueOf(1).moveHeap();

    /**
     * The associated long value.
     */
    private long _value;

    /**
     * Default constructor.
     */
    private Integer64() {
    }

    /**
     * Returns the 64 bits integer for the specified <code>long</code> value.
     *
     * @param  longValue the <code>long</code> value for this number.
     * @return the corresponding number.
     * @see    #longValue()
     */
    public static Integer64 valueOf(long longValue) {
        Integer64 r = FACTORY.object();
        r._value = longValue;
        return r;
    }

    /**
     * Returns the  64 bits integer number for the specified character sequence.
     *
     * @param  chars the character sequence.
     * @return the corresponding number.
     */
    public static Integer64 valueOf(CharSequence chars) {
        Integer64 r = FACTORY.object();
        r._value = TypeFormat.parseLong(chars);
        return r;
    }

    /**
     * Returns the opposite of this number.
     *
     * @return <code>-this</code>.
     */
    public Integer64 opposite() {
        Integer64 r = FACTORY.object();
        r._value = -this._value;
        return r;
    }
    
    /**
     * Returns the sum of this number with the one specified.
     *
     * @param  that the number to be added.
     * @return <code>this + that</code>.
     */
    public Integer64 plus(Integer64 that) {
        Integer64 r = FACTORY.object();
        r._value = this._value + that._value;
        return r;
    }
    /**
     * Returns the difference between this number and the one specified.
     *
     * @param  that the number to be subtracted.
     * @return <code>this - that</code>.
     */
    public Integer64 minus(Integer64 that) {
        Integer64 r = FACTORY.object();
        r._value = this._value - that._value;
        return r;
    }

    /**
     * Returns the product of this number with the one specified.
     *
     * @param  that the number multiplier.
     * @return <code>this * that</code>.
     */
    public Integer64 times(Integer64 that) {
        Integer64 r = FACTORY.object();
        r._value = this._value * that._value;
        return r;
    }

    /**
     * Throws {@link ArithmeticException}.
     */
    public Integer64 reciprocal() {
        throw new ArithmeticException("Non-modular arithmetic");
    }

    /**
     * Returns this number divided by the one specified (integer division).
     *
     * @param  that the number divisor.
     * @return <code>this / that</code>.
     */
    public Integer64 divide(Integer64 that) {
        Integer64 r = FACTORY.object();
        r._value = this._value / that._value;
        return r;
    }

    /**
     * Returns this number modulo the specified number. 
     * 
     * @param m the modulus.
     * @return <code>this % m</code>
     */
    public Integer64 mod(Integer64 m) {
        Integer64 r = FACTORY.object();
        r._value = this._value % m._value;
        return r;
    }

    /**
     * Compares the {@link #norm norm} of this number with that number.
     *
     * @return <code>|this| > |that|</code>
     */
    public boolean isLargerThan(Integer64 that) {
        return MathLib.abs(this._value) > MathLib.abs(that._value);
    }

    /**
     * Returns the norm of this number.
     *
     * @return <code>abs(this)</code>.
     */
    public Integer64 norm() {
        Integer64 r = FACTORY.object();
        r._value = MathLib.abs(this._value);
        return r;
    }

    /**
     * Returns the positive square root of this number (rounding to the 
     * nearest integer value).
     *
     * @return <code>sqrt(this)</code>.
     * @throws UnsupportedOperationException
     */
    public Integer64 sqrt() {
        Integer64 r = FACTORY.object();
        r._value = (long) MathLib.sqrt(this._value);
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
        return (that instanceof Integer64)
                && (this._value == ((Integer64) that)._value);
    }

    /**
     * Returns the hash code for this number.
     * 
     * @return the hash code value.
     */
    public int hashCode() {
        int h = (int)(_value ^ (_value >>> 32));
        h += ~(h << 9);
        h ^= (h >>> 14);
        h += (h << 4);
        return h ^ (h >>> 10);
    }

    // Implements abstract method.
    public long longValue() {
        return _value;
    }

    // Implements abstract method.
    public double doubleValue() {
        return _value;
    }

    // Implements Comparable.
    public int compareTo(Integer64 that) {
        if (this._value < that._value) {
            return -1;
        } else if (this._value > that._value) {
            return 1;
        } else {
            return 0;
        }
    }

    private static final long serialVersionUID = 1L;
}