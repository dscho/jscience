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

import javolution.realtime.LocalContext.Variable;
import javolution.util.Text;
import javolution.util.TextBuilder;
import javolution.util.TypeFormat;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This class represents a 64 bits signed integer.</p>
 * <p> This class implements the {@link Operable} interface for modular
 *     arithmetic (ref. {@link #setModulus}).</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class Integer64 extends RealtimeNumber implements Comparable {

    /**
     * Holds the default XML representation for 64 bits integers.
     * This representation consists of a simple <code>value</code> attribute.
     */
    protected static final XmlFormat INTEGER64_XML = new XmlFormat(Integer64.class) {
        public void format(Object obj, XmlElement xml) {
            xml.setAttribute("value", ((Integer64) obj)._value);
        }

        public Object parse(XmlElement xml) {
            return Integer64.valueOf(xml.getAttribute("value", 0L));
        }
    };

    /**
     * Holds the factory used to produce 32 bits integer instances.
     */
    private static final Factory FACTORY = new Factory() {

        public Object create() {
            return new Integer64();
        }
    };

    /**
     * The 64 bits integer representing zero.
     */
    public static final Integer64 ZERO = (Integer64) valueOf(0).moveHeap();

    /**
     * The 64 bits integer one.
     */
    public static final Integer64 ONE = (Integer64) valueOf(1).moveHeap();

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
     * Returns the 64 bits integer of specified <code>long</code> value.
     *
     * @param  longValue the <code>long</code> value for this number.
     * @return the corresponding number.
     * @see    #intValue()
     */
    public static Integer64 valueOf(long longValue) {
        Integer64 r = (Integer64) FACTORY.object();
        r._value = longValue;
        return r;
    }

    /**
     * Returns the 64 bits integer for the specified character sequence.
     *
     * @param  chars the character sequence.
     * @return the corresponding number.
     */
    public static Integer64 valueOf(CharSequence chars) {
        Integer64 r = (Integer64) FACTORY.object();
        r._value = TypeFormat.parseInt(chars);
        return r;
    }

    /**
     * Returns the negation of this number.
     *
     * @return <code>-this</code>.
     */
    public Integer64 negate() {
        Integer64 r = (Integer64) FACTORY.object();
        r._value = -this._value;
        return r;
    }

    /**
     * Returns the sum of this number with the one specified.
     *
     * @param  that the number to be added.
     * @return <code>this + that</code>.
     */
    public Integer64 add(Integer64 that) {
        Integer64 r = (Integer64) FACTORY.object();
        r._value = this._value + that._value;
        return r;
    }

    /**
     * Returns the difference between this number and the one
     * specified.
     *
     * @param  that the number to be subtracted.
     * @return <code>this - that</code>.
     */
    public Integer64 subtract(Integer64 that) {
        Integer64 r = (Integer64) FACTORY.object();
        r._value = this._value - that._value;
        return r;
    }

    /**
     * Returns the product of this number with the one specified.
     *
     * @param  that the multiplier.
     * @return <code>this * that</code>.
     */
    public Integer64 multiply(Integer64 that) {
        Integer64 r = (Integer64) FACTORY.object();
        r._value = this._value * that._value;
        return r;
    }

    /**
     * Returns this number divided by the one specified.
     *
     * @param  that the divisor.
     * @return <code>this / that</code>.
     */
    public Integer64 divide(Integer64 that) {
        Integer64 r = (Integer64) FACTORY.object();
        r._value = this._value / that._value;
        return r;
    }

    /**
     * Returns this number modulo the specified number. 
     * This method always returns a positive number.
     * 
     * @param m the modulus.
     * @return <code>this mod m</code>
     * @throws ArithmeticException if <code>!m.isPositive()</code>
     */
    public Integer64 mod(Integer64 m) {
        if (m._value > 0) {
            long l = this._value % m._value;
            return l >= 0 ? valueOf(l) : valueOf(l + m._value);
        } else {
            throw new ArithmeticException("Modulus: " + m + " is not positive");
        }
    }

    /**
     * Returns the absolute value of this number.
     *
     * @return <code>abs(this)</code>.
     */
    public Integer64 abs() {
        Integer64 r = (Integer64) FACTORY.object();
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
        return (int) _value;
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
        return _value;
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
     * @return the <code>double</code> value.
     */
    public double doubleValue() {
        return _value;
    }

    /**
     * Compares two 64 bits integer numerically.
     *
     * @param  that the number to compare with.
     * @return -1, 0 or 1 as this number is numerically less than,
     *         equal to, or greater than <code>that</code>.
     */
    public int compareTo(Integer64 that) {
        if (this._value < that._value) {
            return -1;
        } else if (this._value > that._value) {
            return 1;
        } else {
            return 0;
        }
    }

    // Implements Comparable
    public int compareTo(Object that) {
        return this.compareTo((Integer64) that);
    }

    /**
     * Sets the context local modulus for modular arithmetic (used by
     * {@link Operable} operations only). If the modulus is not set 
     * the {@link #reciprocal} operation raises 
     * <code>IllegalStateException</code>.
     * 
     * @param modulus the new modulus or <code>null</code> to unset the modulus.
     * @throws IllegalArgumentException if <code>modulus <= 0</code>
     * @see javolution.realtime.LocalContext
     * @see #plus plus
     * @see #opposite opposite
     * @see #times times
     * @see #reciprocal reciprocal
     */
    public static void setModulus(Integer64 modulus) {
        if ((modulus == null) || (modulus._value > 0)) {
            MODULUS.setValue(modulus);
        } else {
            throw new IllegalArgumentException("modulus: " + modulus
                    + " is not greater than 0");
        }
    }

    private static final Variable MODULUS = new Variable();

    // Implements Operable.
    public Operable plus(Operable that) {
        Integer64 modulus = (Integer64) MODULUS.getValue();
        if (modulus != null) {
            Integer64 result = this.mod(modulus).add(
                    ((Integer64) that).mod(modulus));
            return (result.compareTo(modulus) < 0) ? result : result
                    .subtract(modulus);

        } else {
            return this.add((Integer64) that);
        }
    }

    // Implements Operable.
    public Operable opposite() {
        Integer64 modulus = (Integer64) MODULUS.getValue();
        if (modulus != null) {
            return modulus.subtract(this.mod(modulus));
        } else {
            return this.negate();
        }
    }

    // Implements Operable.
    public Operable times(Operable that) {
        Integer64 modulus = (Integer64) MODULUS.getValue();
        if (modulus != null) {
            return this.multiply((Integer64) that).mod(modulus);
        } else {
            return this.multiply((Integer64) that);
        }
    }

    // Implements Operable.
    public Operable reciprocal() {
        Integer64 modulus = (Integer64) MODULUS.getValue();
        if (modulus != null) {
            // Extended Euclidian Algorithm
            long a = this._value;
            long b = modulus._value;
            long p = 1;
            long q = 0;
            long r = 0;
            long s = 1;
            while (!(b == 0)) {
                long quot = a / b;
                long c = a % b;
                a = b;
                b = c;
                long new_r = p - (quot * r);
                long new_s = q - (quot * s);
                p = r;
                q = s;
                r = new_r;
                s = new_s;
            }
            return valueOf(p % modulus._value);
        } else {
            throw new IllegalStateException("Modulus is not set");
        }
    }

}