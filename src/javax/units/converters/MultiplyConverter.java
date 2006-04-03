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
 * <p> This class represents a multiply converter. A multiply converter
 *     multiplies numeric values by a constant scale factor.</p>
 * <p> Instances of this class are immutable.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 2, 2006
 */
public final class MultiplyConverter extends UnitConverter {

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
    public UnitConverter inverse() {
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
    public UnitConverter concatenate(UnitConverter converter) {
        if (converter instanceof MultiplyConverter) {
            // Optimization (both multiply converters can be merged).
            double factor = _factor * ((MultiplyConverter)converter)._factor;
            return new MultiplyConverter(factor);
        } else {
            return super.concatenate(converter);
        }
    }

    private static final long serialVersionUID = 1L;
}