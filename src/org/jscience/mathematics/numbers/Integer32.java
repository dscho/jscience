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
 * <p> This class represents a 32 bits signed integer.</p>
 * <p> This class implements the {@link Operable} interface for modular
 *     arithmetic (ref. {@link #setModulus}).</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class Integer32 extends RealtimeNumber implements Comparable {

    /**
     * Holds the default XML representation for 32 bits integers.
     * This representation consists of a simple <code>value</code> attribute.
     */
    protected static final XmlFormat INTEGER32_XML = new XmlFormat(Integer32.class) {
        public void format(Object obj, XmlElement xml) {
            xml.setAttribute("value", ((Integer32) obj)._value);
        }

        public Object parse(XmlElement xml) {
            return Integer32.valueOf(xml.getAttribute("value", 0));
        }
    };

    /**
     * Holds the factory used to produce 32 bits integer instances.
     */
    private static final Factory FACTORY = new Factory() {

        public Object create() {
            return new Integer32();
        }
    };

    /**
     * The 32 bits integer representing zero.
     */
    public static final Integer32 ZERO = (Integer32) valueOf(0).moveHeap();

    /**
     * The 32 bits integer one.
     */
    public static final Integer32 ONE = (Integer32) valueOf(1).moveHeap();

    /**
     * The 32 bits integer representing zero.
     */
    public static final Integer32 MAX_PRIME = (Integer32) valueOf(0).moveHeap();

    /**
     * The associated int value.
     */
    private int _value;

    /**
     * Default constructor.
     */
    private Integer32() {
    }

    /**
     * Returns the 32 bits integer of specified <code>int</code> value.
     *
     * @param  intValue the <code>int</code> value for this 32 bits integer.
     * @return the corresponding number.
     * @see    #intValue()
     */
    public static Integer32 valueOf(int intValue) {
        Integer32 r = (Integer32) FACTORY.object();
        r._value = intValue;
        return r;
    }

    /**
     * Returns the 32 bits integer for the specified character sequence.
     *
     * @param  chars the character sequence.
     * @return the corresponding number.
     */
    public static Integer32 valueOf(CharSequence chars) {
        Integer32 r = (Integer32) FACTORY.object();
        r._value = TypeFormat.parseInt(chars);
        return r;
    }

    /**
     * Returns the negation of this number.
     *
     * @return <code>-this</code>.
     */
    public Integer32 negate() {
        Integer32 r = (Integer32) FACTORY.object();
        r._value = -this._value;
        return r;
    }

    /**
     * Returns the sum of this number with the one specified.
     *
     * @param  that the number to be added.
     * @return <code>this + that</code>.
     */
    public Integer32 add(Integer32 that) {
        Integer32 r = (Integer32) FACTORY.object();
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
    public Integer32 subtract(Integer32 that) {
        Integer32 r = (Integer32) FACTORY.object();
        r._value = this._value - that._value;
        return r;
    }

    /**
     * Returns the product of this number with the one specified.
     *
     * @param  that the multiplier.
     * @return <code>this * that</code>.
     */
    public Integer32 multiply(Integer32 that) {
        Integer32 r = (Integer32) FACTORY.object();
        r._value = this._value * that._value;
        return r;
    }

    /**
     * Returns this number divided by the one specified.
     *
     * @param  that the divisor.
     * @return <code>this / that</code>.
     */
    public Integer32 divide(Integer32 that) {
        Integer32 r = (Integer32) FACTORY.object();
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
    public Integer32 mod(Integer32 m) {
        if (m._value > 0) {
            int i = this._value % m._value;
            return i >= 0 ? valueOf(i) : valueOf(i + m._value);
        } else {
            throw new ArithmeticException("Modulus: " + m + " is not positive");
        }
    }

    /**
     * Returns the absolute value of this number.
     *
     * @return <code>abs(this)</code>.
     */
    public Integer32 abs() {
        Integer32 r = (Integer32) FACTORY.object();
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
        return (that instanceof Integer32)
                && (this._value == ((Integer32) that)._value);
    }

    /**
     * Returns the hash code for this number.
     *
     * @return the hash code value.
     */
    public int hashCode() {
        return _value;
    }

    /**
     * Returns the value of this number as an <code>int</code>.
     *
     * @return the <code>int</code> value.
     */
    public int intValue() {
        return _value;
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
     * Compares two 32 bits integer numerically.
     *
     * @param  that the number to compare with.
     * @return -1, 0 or 1 as this numberis numerically less than,
     *         equal to, or greater than <code>that</code>.
     */
    public int compareTo(Integer32 that) {
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
        return this.compareTo((Integer32) that);
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
    public static void setModulus(Integer32 modulus) {
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
        Integer32 modulus = (Integer32) MODULUS.getValue();
        if (modulus != null) {
            Integer32 result = this.mod(modulus).add(
                    ((Integer32) that).mod(modulus));
            return (result.compareTo(modulus) < 0) ? result : result
                    .subtract(modulus);

        } else {
            return this.add((Integer32) that);
        }
    }

    // Implements Operable.
    public Operable opposite() {
        Integer32 modulus = (Integer32) MODULUS.getValue();
        if (modulus != null) {
            return modulus.subtract(this.mod(modulus));
        } else {
            return this.negate();
        }
    }

    // Implements Operable.
    public Operable times(Operable that) {
        Integer32 modulus = (Integer32) MODULUS.getValue();
        if (modulus != null) {
            return this.multiply((Integer32) that).mod(modulus);
        } else {
            return this.multiply((Integer32) that);
        }
    }

    // Implements Operable.
    public Operable reciprocal() {
        Integer32 modulus = (Integer32) MODULUS.getValue();
        if (modulus != null) {
            // Extended Euclidian Algorithm
            int a = this._value;
            int b = modulus._value;
            int p = 1;
            int q = 0;
            int r = 0;
            int s = 1;
            while (!(b == 0)) {
                int quot = a / b;
                int c = a % b;
                a = b;
                b = c;
                int new_r = p - (quot * r);
                int new_s = q - (quot * s);
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