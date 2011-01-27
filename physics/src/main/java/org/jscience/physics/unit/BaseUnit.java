/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit;

import org.jscience.physics.model.PhysicalDimension;
import org.unitsofmeasurement.unit.UnitConverter;
import org.jscience.physics.quantity.Quantity;

/**
 * <p> This class represents the building blocks on top of which all others
 *     units are created. Base units are always unscaled metric units.</p>
 * 
 * <p> When using the {@linkplain PhysicalDimension.Model#STANDARD standard} model
 *     (default), all seven base units are dimensionally independent.</p>
 *
 * @param <Q> The type of the quantity measured by this unit.
 *
 * @see <a href="http://en.wikipedia.org/wiki/SI_base_unit">
 *       Wikipedia: SI base unit</a>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public class BaseUnit<Q extends Quantity<Q>> extends PhysicalUnit<Q> {

    /**
     * Holds the symbol.
     */
    private final String symbol;

    /** The serialVersionUID */
    private static final long serialVersionUID = 1234567654321265167L;

    /**
     * Creates a base unit having the specified symbol.
     *
     * @param symbol the symbol of this base unit.
     * @throws IllegalArgumentException if the specified symbol is
     *         associated to a different unit.
     */
    public BaseUnit(String symbol) {
        this.symbol = symbol;
        // Checks if the symbol is associated to a different unit.
        synchronized (PhysicalUnit.SYMBOL_TO_UNIT) {
            PhysicalUnit<?> unit = PhysicalUnit.SYMBOL_TO_UNIT.get(symbol);
            if (unit == null) {
                PhysicalUnit.SYMBOL_TO_UNIT.put(symbol, this);
                return;
            }
            if (!(unit instanceof BaseUnit<?>))
                throw new IllegalArgumentException("Symbol " + symbol + " is associated to a different unit");
        }
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof BaseUnit<?>))
            return false;
        BaseUnit<?> thatUnit = (BaseUnit<?>) that;
        return this.symbol.equals(thatUnit.symbol);
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }

    @Override
    public PhysicalUnit<Q> toMetric() {
        return this;
    }

    @Override
    public UnitConverter getConverterToMetric() {
        return UnitConverter.IDENTITY;
    }

    @Override
    public PhysicalDimension getDimension() {
        return PhysicalDimension.getModel().getDimension(this);
    }

    @Override
    public UnitConverter getDimensionalTransform() {
        return PhysicalDimension.getModel().getTransform(this);
    }
}
