/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.numbers;

import org.jscience.mathematics.structures.Ring;
import javolution.lang.Immutable;
import javolution.text.Text;
import javolution.context.RealtimeObject;

/**
 * <p> This class represents an immutable number.</p>
 * 
 * <p> Instances of this class are typically created using real-time 
 *     {@link javolution.context.RealtimeObject.Factory factories}.</p>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 * @see <a href="http://en.wikipedia.org/wiki/Number">
 *      Wikipedia: Number</a>
 */
public abstract class Number<T extends Number<T>> extends RealtimeObject
        implements Ring<T>, Comparable<T>, Immutable {

    /**
     * Compares the magnitude of this number with that number.
     *
     * @return <code>|this| > |that|</code>
     */
    public abstract boolean isLargerThan(T that);

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
     * Returns the difference between this number and the one specified.
     *
     * @param  that the number to be subtracted.
     * @return <code>this - that</code>.
     */
    public T minus(T that) {
        return this.plus(that.opposite());
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
            throw new IllegalArgumentException("exp: " + exp
                    + " should be a positive number");
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
     * @param obj the object to be compared with.
     * @return <code>true</code> if this number and the specified argument
     *         represent the same number; <code>false</code> otherwise.
     */
    public abstract boolean equals(Object obj);

    /**
     * Returns the hash code for this number (consistent with 
     * {@link #equals(Object)}.
     *
     * @return this number hash code.
     */
    public abstract int hashCode();

    /**
     * Returns the textual representation of this real-time object
     * (equivalent to <code>toString</code> except that the returned value
     * can be allocated from the local context space).
     * 
     * @return this object's textual representation.
     */
    public abstract Text toText();

    /**
     * Moves this real-time object to the specified object space.
     * 
     * @param os the object space to move this real-time object to.
     * @return <code>true</code> if the move has to be propagated to 
     *         external real-time references; <code>false</code> otherwise.
     */
    public boolean move(ObjectSpace os) {
        return super.move(os);
    }

}