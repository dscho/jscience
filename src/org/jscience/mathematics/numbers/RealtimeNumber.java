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

import java.io.Serializable;
import javolution.realtime.RealtimeObject;

import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This class represents a {@link javolution.realtime.Realtime real-time}
 *     number.</p>
 * <p> Instances of this class should be created using a 
 *     {@link javolution.realtime.RealtimeObject.Factory Factory}.</p>
 * <p> Instances of this class are immutable. But like any real-time object,
 *     instances allocated in a pool context must be {@link #export exported}
 *     if referenced after exiting the pool context.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public abstract class RealtimeNumber extends RealtimeObject implements
        Operable, Serializable {

    /**
     * Returns the value of this number as an <code>int</code>.
     * This may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>int</code>.
     */
    public abstract int intValue();

    /**
     * Returns the value of this number as a <code>long</code>.
     * This may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>long</code>.
     */
    public abstract long longValue();

    /**
     * Returns the value of this number as a <code>float</code>.
     * This may involve rounding.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>float</code>.
     */
    public abstract float floatValue();

    /**
     * Returns the value of this number as a <code>double</code>.
     * This may involve rounding.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>double</code>.
     */
    public abstract double doubleValue();

    /**
     * Returns this number raised at the specified exponent.
     *
     * @param  exp the exponent.
     * @return <code>this<sup>exp</sup></code>
     */
    public Operable pow(int exp) {
        if (exp > 0) {
            Operable pow2 = this;
            Operable result = null;
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
    public byte byteValue() {
        return (byte) intValue();
    }

    /**
     * Returns the value of this number as a <code>short</code>.
     * This may involve rounding or truncation.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type <code>short</code>.
     */
    public short shortValue() {
        return (short) intValue();
    }
}