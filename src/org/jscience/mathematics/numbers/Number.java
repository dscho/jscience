/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.numbers;

import java.io.Serializable;
import javolution.realtime.RealtimeObject;

/**
 * <p> This class represents an immutable number.</p>
 * 
 * <p> Instances of this class are created using  
 *     {@link javolution.realtime.RealtimeObject.Factory factories}.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 6, 2004
 */
public abstract class Number<T extends Number<T>> extends RealtimeObject implements
        Numeric<T>, Comparable<T>, Serializable {

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
     * Returns the difference between this number and the one specified.
     *
     * @param  that the number to be subtracted.
     * @return <code>this - that</code>.
     */
    public T minus(T that) {
        return this.plus(that.opposite());
    }

    /**
     * Returns this number divided by the one specified.
     *
     * @param  that the number divisor.
     * @return <code>this / that</code>.
     */
    public T divide(T that) {
        return this.times(that.reciprocal());
    }

    /**
     * Returns the absolute value of this number (equivalent to  
     * {@link Numeric#norm()}.
     *
     * @return <code>|this|</code>.
     */
    public T abs() {
        return (T) this.norm();
    }

    /**
     * Returns the inverse value of this number (equivalent to 
     * {@link #reciprocal()}.
     *
     * @return <code>1 / this</code>.
     */
    public T inverse() {
        return reciprocal();
    }

    /**
     * Indicates if this number is ordered before that number
     * (convenience method).
     *
     * @return <code>this.compareTo(that) < 0</code>.
     */
    public final boolean isLessThan(T that) {
        return this.compareTo(that) < 0;
    }

    /**
     * Indicates if this number is ordered after that number
     * (convenience method).
     *
     * @return <code>this.compareTo(that) > 0</code>.
     */
    public final boolean isGreaterThan(T that) {
        return this.compareTo(that) > 0;
    }

    /**
     * Returns this numeric raised at the specified exponent.
     *
     * @param  exp the exponent.
     * @return <code>this<sup>exp</sup></code>
     */
    public T pow(int exp) {
        if (exp > 0) {
            T pow2 = (T) this;
            T result = null;
            while (exp >= 1) { // Iteration.
                if ((exp & 1) == 1) {
                    result = (result == null) ? pow2 : result.times(pow2);
                }
                pow2 = pow2.times(pow2);
                exp >>>= 1;
            }
            return result;
        } else if (exp < 0) {
            return this.pow(-exp).reciprocal();
        } else { // exp == 0
            return this.times(this.reciprocal()); // Identity.
        }
    }

    /**
     * Returns the value of this number as a <code>byte</code>.
     * This may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>byte</code>.
     */
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
        return (int)longValue();
    }

    /**
     * Returns the value of this number as a <code>float</code>.
     * This may involve rounding.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>float</code>.
     */
    public final float floatValue() {
        return (float)doubleValue();
    }

}