/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit.converter;

import java.math.BigDecimal;
import java.math.MathContext;
import javolution.context.ObjectFactory;
import org.unitsofmeasurement.unit.UnitConverter;

/**
 * <p> This class represents a converter adding a constant offset
 *     to numeric values (<code>double</code> based).</p>
 *
 * <p> Instances of this class are immutable.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public final class AddConverter extends PhysicalUnitConverter {

    /**
     * Holds the offset.
     */
    private double offset;

    /**
     * Returns an additive converter having the specified offset.
     *
     * @param  offset the offset value.
     * @throws IllegalArgumentException if offset is <code>0.0</code>
     *         (would result in identity converter).
     */
    public static AddConverter valueOf(double offset) {
        if (offset == 0.0)
            throw new IllegalArgumentException("Would result in identity converter");
        AddConverter cvtr = FACTORY.object();
        cvtr.offset = offset;
        return cvtr;
    }
    private static final ObjectFactory<AddConverter> FACTORY = new ObjectFactory<AddConverter>() {

        @Override
        protected AddConverter create() {
            return new AddConverter();
        }
    };

    private AddConverter() {
    }

    /**
     * Returns the offset value for this add converter.
     *
     * @return the offset value.
     */
    public double getOffset() {
        return offset;
    }

    @Override
    public UnitConverter concatenate(UnitConverter converter) {
        if (!(converter instanceof AddConverter))
            return super.concatenate(converter);
        double newOffset = offset + ((AddConverter) converter).offset;
        return newOffset == 0.0 ? IDENTITY : AddConverter.valueOf(newOffset);
    }

    @Override
    public AddConverter inverse() {
        return AddConverter.valueOf(-offset);
    }

    @Override
    public double convert(double value) {
        return value + offset;
    }

    @Override
    public BigDecimal convert(BigDecimal value, MathContext ctx) throws ArithmeticException {
        return value.add(BigDecimal.valueOf(offset), ctx);
    }

    @Override
    public final String toString() {
        return "AddConverter(" + offset + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AddConverter)) {
            return false;
        }
        AddConverter that = (AddConverter) obj;
        return this.offset == that.offset;
    }

    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(offset);
        return (int) (bits ^ (bits >>> 32));
    }

    @Override
    public boolean isLinear() {
        return false;
    }

    @Override
    public AddConverter copy() {
        return AddConverter.valueOf(offset);
    }
    
}
