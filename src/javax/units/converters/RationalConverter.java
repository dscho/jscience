/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.units.converters;


/**
 * <p> This class represents an exact scaling converter. 
 * <p> Instances of this class are immutable.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 2, 2006
 */
public final class RationalConverter extends UnitConverter {

    /**
     * Holds the converter dividend.
     */
    private final long _dividend;

    /**
     * Holds the converter divisor.
     */
    private final long _divisor;

    /**
     * Creates a rational converter with the specified dividend and 
     * divisor.
     *
     * @param dividend the dividend.
     * @param divisor the divisor.
     */
    public RationalConverter(long dividend, long divisor) {
        _dividend = dividend;
        _divisor = divisor;
    }

    /**
     * Returns the dividend for this rational converter.
     *
     * @return this converter dividend.
     */
    public long getDividend() {
        return _dividend;
    }

    /**
     * Returns the divisor for this rational converter.
     *
     * @return this converter divisor.
     */
    public long getDivisor() {
        return _divisor;
    }

    // Implements abstract method.
    public RationalConverter inverse() {
        return new RationalConverter(_divisor, _dividend);
    }

    // Implements abstract method.
    public double convert(double amount) {
        return amount * _dividend / _divisor;
    }

    // Implements abstract method.
    public double derivative(double x) {
        return ((double)_dividend) / _divisor;
    }

    // Implements abstract method.
    public boolean isLinear() {
        return true;
    }

    // Overrides (optimization).
    public UnitConverter concatenate(UnitConverter converter) {
        if (converter instanceof RationalConverter) {
            // Optimization (both rational converters can be merged).
            RationalConverter that = (RationalConverter)converter;
            long dividend = this._dividend * that._dividend;
            long divisor = this._divisor * that._divisor;
            long gcd = gcd(dividend, divisor);
            return new RationalConverter(dividend / gcd, divisor / gcd);
        } else if (converter instanceof MultiplyConverter) {
            // Optimization (both multiply converters can be merged).
            MultiplyConverter that = (MultiplyConverter)converter;
            return new MultiplyConverter(this.derivative(0) * that.derivative(0));
        } else {
            return super.concatenate(converter);
        }
    }

    /**
     * Returns the greatest common divisor (Euclid's algorithm).
     *
     * @param  m the first number.
     * @param  nn the second number.
     * @return the greatest common divisor.
     */
    private static long gcd(long m, long n) {
        if (n == 0L) {
            return m;
        } else {
            return gcd(n, m % n);
        }
    }

    private static final long serialVersionUID = 1L;
}