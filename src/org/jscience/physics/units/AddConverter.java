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
 * <p> This class represents an add converter. An add converter adds
 *     a constant offset to numeric values.</p>
 * <p> Instances of this class are immutable.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class AddConverter extends Converter {

    /**
     * Holds the offset.
     */
    private final double _offset;

    /**
     * Creates an add converter with the specified offset.
     *
     * @param  offset the offset value.
     */
    public AddConverter(double offset) {
        _offset = offset;
    }

    /**
     * Returns the offset value for this add converter.
     *
     * @return the offset value.
     */
    public double getOffset() {
        return _offset;
    }

    // Implements abstract method.
    public Converter inverse() {
        return new AddConverter(-_offset);
    }

    // Implements abstract method.
    public double convert(double x) {
        return x + _offset;
    }

    // Implements abstract method.
    public double derivative(double x) {
        return 1.0;
    }

    // Implements abstract method.
    public boolean isLinear() {
        return false;
    }

    // Overrides (optimization).
    public Converter concatenate(Converter converter) {
        if (converter instanceof AddConverter) {
            // Optimization (both adding converters can be merged).
            double offset
                = _offset + ((AddConverter)converter).getOffset();
            return new AddConverter(offset);
        } else {
            return super.concatenate(converter);
        }
    }

    // Overrides.
    public boolean equals(Object obj) {
        // Check equality to float precision (allows for some inaccuracies)
	return (obj instanceof AddConverter) &&
            (Float.floatToIntBits((float)((AddConverter)obj)._offset) ==
             Float.floatToIntBits((float)_offset));
    }

    // Overrides.
    public int hashCode() {
	return Float.floatToIntBits((float)_offset);
    }

    private static final long serialVersionUID = 6002090310665218885L;
}