/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.physics.units;

/**
 * <p> This class represents a logarithmic converter. Such converter 
 *     is typically used to create logarithmic unit. For example:<pre>
 *     Unit BEL = TransformedUnit.getInstance(
 *          Unit.ONE, new LogConverter(10).inverse());</pre></p>
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
        _logBase = Math.log(base);
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
        return _invLogBase * Math.log(x);
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
            return Math.exp(_logBase * x);
        }

        // Implements abstract method.
        public double derivative(double x) {
            return _logBase * Math.exp(x);
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

        private static final long serialVersionUID = 4374630714303506966L;
    }

    private static final long serialVersionUID = -6047186182914075578L;
}