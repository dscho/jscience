/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.number;

import java.math.BigDecimal;
import org.jscience.mathematics.structure.Ring;
import javolution.lang.Realtime;
import javolution.lang.ValueType;
import javolution.text.Text;
import javolution.text.TextBuilder;
import javolution.text.TextFormat;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

/**
 * <p> This class represents a {@link javolution.lang.ValueType value-type}
 *     number.</p>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 * @see <a href="http://en.wikipedia.org/wiki/Number">
 *      Wikipedia: Number</a>
 */
public abstract class Number<T extends Number<T>> extends java.lang.Number
        implements Ring<T>, Comparable<T>, Realtime, ValueType, XMLSerializable {

 
    /**
     * Returns the value of this number as a <code>long</code>.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>long</code>.
     */
    public abstract long longValue();

    /**
     * Returns the value of this number as a <code>double</code>.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>double</code>.
     */
    public abstract double doubleValue();

    /**
     * Returns the value of this number as a <code>BigDecimal</code>.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>double</code>.
     */
    public abstract BigDecimal decimalValue();

    /**
     * Compares this number with the specified number for order.  Returns a
     * negative integer, zero, or a positive integer as this number is less
     * than, equal to, or greater than the specified number. 
     * Implementation must ensure that this method is consistent with equals 
     * <code>(x.compareTo(y)==0) == (x.equals(y))</code>,  
     * 
     * @param that the number to be compared.
     * @return a negative integer, zero, or a positive integer as this number
     *        is less than, equal to, or greater than the specified number.
     */
    public abstract int compareTo(T that);

    /**
     * Indicates if this number is ordered before that number
     * (convenience method).
     *
     * @param that the number to compare with.
     * @return <code>this.compareTo(that) < 0</code>.
     */
    public final boolean isLessThan(T that) {
        return this.compareTo(that) < 0;
    }

    /**
     * Indicates if this number is ordered after that number
     * (convenience method).
     *
     * @param that the number to compare with.
     * @return <code>this.compareTo(that) > 0</code>.
     */
    public final boolean isGreaterThan(T that) {
        return this.compareTo(that) > 0;
    }

    /**
     * Compares the absolute value of two numbers.
     *
     * @param that the number to be compared with.
     * @return <code>|this| > |that|</code>
     * @see #abs()
     */
    public final boolean isLargerThan(T that) {
        return this.abs().compareTo(that.abs()) > 0;
    }

    /**
     * Returns the absolute value of this number (e.g. modulus for
     * complex numbers).
     *
     * @return <code>|this|</code>.
     */
    @SuppressWarnings("unchecked")
    public abstract T abs();

    /**
     * Returns the difference between this number and the one specified.
     *
     * @param  that the number to be subtracted.
     * @return <code>this - that</code>.
     */
    public T minus(T that) {
        return this.plus(that.opposite());
    }

    /**
     * Returns this number multiplied by the specified factor.
     *
     * @param  n the multiplier.
     * @return <code>this * n</code>
     */
    @SuppressWarnings("unchecked")
    public T times(long n) {
        final T t = (T) this;
        if (n <= 0) {
            if (n == 0)
                return t.minus(t);
            if (n == Long.MIN_VALUE) // Negative would overflow
                return t.times(n + 1).minus(t);
            return t.times(-n);
        }
        if (n == 1)
            return t;
        if (n == 2)
            return t.plus(t);
        if (n == 3)
            return t.plus(t).plus(t);
        long halfN = n >> 1;
        return this.times(halfN).plus(this.times(n - halfN));
    }

    /**
     * Returns this number raised at the specified positive exponent.
     *
     * @param  exp the positive exponent.
     * @return <code>this<sup>exp</sup></code>
     * @throws IllegalArgumentException if <code>exp &lt;= 0</code> 
     */
    @SuppressWarnings("unchecked")
    public T pow(int exp) {
        if (exp <= 0)
            throw new IllegalArgumentException("exp: " + exp + " should be a positive number");
        final T t = (T) this;
        if (exp == 1)
            return t;
        if (exp == 2)
            return t.times(t);
        if (exp == 3)
            return t.times(t).times(t);
        int halfExp = exp >> 1;
        return this.pow(halfExp).times(this.pow(exp - halfExp));
    }

    /**
     * Returns the value of this number as a <code>byte</code>.
     * This may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>byte</code>.
     */
    @Override
    public final byte byteValue() {
        return (byte) longValue();
    }

    /**
     * Returns the value of this number as a <code>short</code>.
     * This may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>short</code>.
     */
    @Override
    public final short shortValue() {
        return (short) longValue();
    }

    /**
     * Returns the value of this number as an <code>int</code>.
     * This may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>int</code>.
     */
    public final int intValue() {
        return (int) longValue();
    }

    /**
     * Returns the value of this number as a <code>float</code>.
     * This may involve rounding.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>float</code>.
     */
    public final float floatValue() {
        return (float) doubleValue();
    }

    /**
     * Indicates if this number is equals to the specified object.
     *
     * The default implementation returns <code>false</code> if the specified
     * argument  and this number are not of the same class. Otherwise, it
     * returns {@link #compareTo(org.jscience.mathematics.number.Number)
     * compareTo(obj) == 0</code>
     *
     * @param obj the object to be compared with.
     * @return <code>true</code> if this number and the specified argument
     *         represent the same number; <code>false</code> otherwise.
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!this.getClass().equals(obj.getClass()))
            return false;
        return compareTo((T) obj) == 0;
    }

    /**
     * Returns the hash code for this number (consistent with 
     * {@link #equals(Object)}.
     * The default implementation is:
     * [code]
     *     long bits = Double.doubleToLongBits(doubleValue());
     *     return (int) (bits^(bits>>>32));
     * [/code]
     *
     * @return this number hash code.
     */
    @Override
    public final int hashCode() {
        long bits = Double.doubleToLongBits(doubleValue());
        return (int) (bits ^ (bits >>> 32));
    }

    /**
     * Returns the textual representation of this number.
     * This method cannot be overriden, sub-classes should define their own
     * textual format which will automatically be used here.
     *
     * @return <code>TextFormat.getInstance(this.getClass()).format(this)</code>
     * @see TextFormat#getInstance
     */
    public final Text toText() {
        TextFormat<Number> textFormat = TextFormat.getInstance(this.getClass());
        return textFormat.format(this);
    }

    /**
     * Returns the text representation of this number as a
     * <code>java.lang.String</code>.
     * This method cannot be overriden, sub-classes should define their own
     * textual format which will automatically be used here.
     *
     * @return <code>TextFormat.getInstance(this.getClass()).formatToString(this)</code>
     * @see TextFormat#getInstance
     */
    @Override
    public final String toString() {
        TextFormat<Number> textFormat = TextFormat.getInstance(this.getClass());
        return textFormat.formatToString(this);
    }

    /**
     * Returns a copy of this number 
     * {@link javolution.context.AllocatorContext allocated} 
     * by the calling thread (possibly on the stack).
     *     
     * @return an identical and independant copy of this number.
     */
    public abstract T copy();
}
