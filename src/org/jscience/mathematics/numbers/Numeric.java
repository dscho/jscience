/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.numbers;

import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This interface identifies {@link Operable operables} for which the 
 *     length, size, or extent can be calculated (see {@link #norm()}).</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 6, 2004
 * @see <a href="http://mathworld.wolfram.com/Norm.html">
 *      Norm -- from MathWorld</a> 
 */
public interface Numeric<T extends Numeric<T>> extends Operable<T> {

    /**
     * Compares the {@link #norm norm} of this numeric with that numeric.
     *
     * @param that the numeric to be compared with.
     * @return <code>|this| > |that|</code>
     */
    boolean isLargerThan(T that);

    /**
     * Returns the norm (or magnitude) of this numeric.
     *
     * @return <code>|this|</code>
     */
    T norm();

    /**
     * Returns the square root of this numeric.
     *
     * @return the numeric <code>sqrt</code> such as 
     *         <code>sqrt.times(sqrt) = this</code>
     * @throws ArithmeticException if the square root cannot be calculated.
     */
    T sqrt();
    
}
