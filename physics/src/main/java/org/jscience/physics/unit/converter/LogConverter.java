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
 * <p> This class represents a logarithmic converter of limited precision.
 *     Such converter  is typically used to create logarithmic unit.
 *     For example:[code]
 *     Unit<Dimensionless> BEL = Unit.ONE.transform(new LogConverter(10).inverse());
 *     [/code]</p>
 *
 * <p> Instances of this class are immutable.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public final class LogConverter extends AbstractUnitConverter {

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
    public static LogConverter valueOf(double base) {
        LogConverter cvtr = FACTORY.object();
        cvtr.base = base;
        cvtr.logOfBase = Math.log(base);
        return cvtr;
    }
    private static final ObjectFactory<LogConverter> FACTORY = new ObjectFactory<LogConverter>() {

        @Override
        protected LogConverter create() {
            return new LogConverter();
        }
    };

    private LogConverter() {
    }

    /**
     * Returns the logarithmic base of this converter.
     *
     * @return the logarithmic base (e.g. <code>Math.E</code> for
     *         the Natural Logarithm).
     */
    public double getBase() {
        return base;
    }

    @Override
    public AbstractUnitConverter inverse() {
        return ExpConverter.valueOf(base);
    }

    @Override
    public final String toString() {
        return "LogConverter("+ base + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LogConverter))
            return false;
        LogConverter that = (LogConverter) obj;
        return this.base == that.base;
    }

    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(base);
        return (int) (bits ^ (bits >>> 32));
    }

    @Override
    public double convert(double amount) {
        return Math.log(amount) / logOfBase;
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
    public LogConverter copy() {
        return LogConverter.valueOf(base);
    }
}
