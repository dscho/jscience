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
 * <p> This class represents a multiply converter. A multiply converter
 *     multiplies numeric values by a constant scale factor.</p>
 * <p> Instances of this class are immutable.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class MultiplyConverter extends Converter {

    /**
     * Holds the scale factor.
     */
    private final double _factor;

    /**
     * Creates a multiply converter with the specified scale factor.
     *
     * @param  factor the scale factor.
     */
    public MultiplyConverter(double factor) {
        _factor = factor;
    }

    // Implements abstract method.
    public Converter inverse() {
        return new MultiplyConverter(1.0 / _factor);
    }

    // Implements abstract method.
    public double convert(double amount) {
        return _factor * amount;
    }

    // Implements abstract method.
    public double derivative(double x) {
        return _factor;
    }

    // Implements abstract method.
    public boolean isLinear() {
        return true;
    }

    // Overrides (optimization).
    public Converter concatenate(Converter converter) {
        if (converter instanceof MultiplyConverter) {
            // Optimization (both multiply converters can be merged).
            double factor = _factor * ((MultiplyConverter)converter)._factor;
            return new MultiplyConverter(factor);
        } else {
            return super.concatenate(converter);
        }
    }

    private static final long serialVersionUID = 2612600240219592795L;
}