/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.number;

import org.jscience.mathematics.structure.Field;

/**
 * <p> This class represents a number implementing the {@link Field} interface.</p>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0.0, November 20, 2009
 */
public abstract class FieldNumber<T extends FieldNumber<T>> extends org.jscience.mathematics.number.Number<T> implements Field<T> {

    /**
     * Returns this number divided by the specified divisor.
     *
     * @param  n the divisor.
     * @return <code>this / n</code>
     */
    public T divide(long n) {
        return this.inverse().times(n).inverse();
    }

    /**
     * Returns this number divided by that number.
     *
     * @param  that the divisor
     * @return <code>this / that</code>
     */
    public T divide(T that) {
        return this.times(that.inverse());
    }

    /**
     * Returns this number raised at the specified exponent (which can be
     * negative).
     *
     * @param  exp the exponent.
     * @return <code>this<sup>exp</sup></code>
     */
    @Override
    public T pow(int exp) {
        final T t = (T) this;
        if (exp <= 0) {
            if (exp == 0)
                return t.divide(t);
            if (exp == Integer.MIN_VALUE) // Negative would overflow
                return t.pow(exp + 1).divide(t);
            return t.pow(-exp).inverse();
        }
        return super.pow(exp);
    }
}
