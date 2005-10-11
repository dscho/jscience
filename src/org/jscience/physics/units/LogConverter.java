/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.units;

import javolution.lang.MathLib;

/**
 * <p> This class represents a logarithmic converter. Such converter 
 *     is typically used to create logarithmic unit. For example:<pre>
 *     Unit BEL = Unit.ONE.transform(new LogConverter(10).inverse());
 *     </pre></p>
 * <p> Instances of this class are immutable.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class LogConverter extends Converter {

    /**
     * Holds the logarithmic base.
     */
    private final double _base;

    /**
     * Holds the natural logarithm of the base.
     */
    private final double _logBase;

    /**
     * Holds the inverse of the natural logarithm of the base.
     */
    private final double _invLogBase;

    /**
     * Holds the inverse of this converter.
     */
    private final Inverse _inverse = new Inverse();

    /**
     * Creates a logarithmic converter having the specified base.
     * 
     * @param  base the logarithmic base (e.g. <code>Math.E</code> for
     *         the Natural Logarithm).
     */
    public LogConverter(double base) {
        _base = base;
        _logBase = MathLib.log(base);
        _invLogBase = 1.0 / _logBase;
    }

    /**
     * Returns the logarithmic base of this converter.
     *
     * @return the logarithmic base (e.g. <code>Math.E</code> for
     *         the Natural Logarithm).
     */
    public double getBase() {
        return _base;
    }

    // Implements abstract method.
    public Converter inverse() {
        return _inverse;
    }

    // Implements abstract method.
    public double convert(double x) {
        return _invLogBase * MathLib.log(x);
    }

    // Implements abstract method.
    public double derivative(double x) {
        return _invLogBase / x;
    }

    // Implements abstract method.
    public boolean isLinear() {
        return false;
    }

    // Overrides.
    public boolean equals(Object obj) {
        // Check equality to float precision (allows for some inaccuracies)
        return (obj instanceof LogConverter)
                && (Float.floatToIntBits((float) ((LogConverter) obj)._base) == Float
                        .floatToIntBits((float) _base));
    }

    // Overrides.
    public int hashCode() {
        return Float.floatToIntBits((float) _base);
    }

    /**
     * This inner class represents the inverse of the logarithmic converter
     * (exponentiation converter).
     */
    private class Inverse extends Converter {

        // Implements abstract method.
        public Converter inverse() {
            return LogConverter.this;
        }

        // Implements abstract method.
        public double convert(double x) {
            return MathLib.exp(_logBase * x);
        }

        // Implements abstract method.
        public double derivative(double x) {
            return _logBase * MathLib.exp(x);
        }

        // Implements abstract method.
        public boolean isLinear() {
            return false;
        }

        // Overrides.
        public boolean equals(Object obj) {
            return (obj instanceof Inverse)
                    && LogConverter.this.equals(((Inverse) obj).inverse());
        }

        // Overrides.
        public int hashCode() {
            return LogConverter.this.hashCode() * 31;
        }

        private static final long serialVersionUID = 1L;
    }

    private static final long serialVersionUID = 1L;
}