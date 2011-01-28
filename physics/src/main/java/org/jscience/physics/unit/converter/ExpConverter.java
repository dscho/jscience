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

/**
 * <p> This class represents a exponential converter of limited precision.
 *     Such converter  is typically used to create inverse of logarithmic unit.
 *
 * <p> Instances of this class are immutable.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public final class ExpConverter extends AbstractUnitConverter {

    /**
     * Holds the logarithmic base.
     */
    private double base;

    /**
     * Holds the natural logarithm of the base.
     */
    private double logOfBase;

    /**
     * Returns a logarithmic converter having the specified base.
     *
     * @param  base the logarithmic base (e.g. <code>Math.E</code> for
     *         the Natural Logarithm).
     */
    public static ExpConverter valueOf(double base) {
        ExpConverter cvtr = FACTORY.object();
        cvtr.base = base;
        cvtr.logOfBase = Math.log(base);
        return cvtr;
    }
    private static final ObjectFactory<ExpConverter> FACTORY = new ObjectFactory<ExpConverter>() {

        @Override
        protected ExpConverter create() {
            return new ExpConverter();
        }
    };

    private ExpConverter() {
    }

    /**
     * Returns the exponential base of this converter.
     *
     * @return the exponential base (e.g. <code>Math.E</code> for
     *         the Natural Exponential).
     */
    public double getBase() {
        return base;
    }

    @Override
    public AbstractUnitConverter inverse() {
        return LogConverter.valueOf(base);
    }

    @Override
    public final String toString() {
        return "ExpConverter("+ base + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ExpConverter))
            return false;
        ExpConverter that = (ExpConverter) obj;
        return this.base == that.base;
    }

    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(base);
        return (int) (bits ^ (bits >>> 32));
    }

    @Override
    public double convert(double amount) {
            return Math.exp(logOfBase * amount);
    }

    @Override
    public BigDecimal convert(BigDecimal value, MathContext ctx) throws ArithmeticException {
        return BigDecimal.valueOf(convert(value.doubleValue())); // Reverts to double conversion.
    }

    @Override
    public boolean isLinear() {
        return false;
    }

    @Override
    public ExpConverter copy() {
        return ExpConverter.valueOf(base);
    }

}
