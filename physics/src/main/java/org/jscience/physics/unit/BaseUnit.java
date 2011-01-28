/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit;

import java.util.Map;
import org.unitsofmeasurement.quantity.Quantity;


/**
 * <p> This class represents the building blocks on top of which all others
 *     units are created. Base units are always unscaled metric units.</p>
 * 
 * <p> When using the standard model, all seven {@link SI} base units
 *     are dimensionally independent.</p>
 *
 * @param <Q> The type of the quantity measured by this unit.
 *
 * @see <a href="http://en.wikipedia.org/wiki/SI_base_unit">
 *       Wikipedia: SI base unit</a>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public class BaseUnit<Q extends Quantity<Q>> extends PhysicsUnit<Q> {

    /**
     * Holds the symbol.
     */
    private final String symbol;

    /**
     * Holds the associated dimension.
     */
    private final PhysicsDimension dimension;

    /**
     * Creates a new base unit having the specified symbol and dimension.
     *
     * @param symbol the symbol of this base unit.
     * @param dimension the dimension for this base unit.
     * @throws IllegalArgumentException if the specified symbol is
     *         associated to a different unit.
     */
    public BaseUnit(String symbol, PhysicsDimension dimension) {
        this.symbol = symbol;
        this.dimension = dimension;

        // Checks if the symbol is associated to a different unit.
        synchronized (PhysicsUnit.SYMBOL_TO_UNIT) {
            PhysicsUnit<?> unit = PhysicsUnit.SYMBOL_TO_UNIT.get(symbol);
            if (unit == null) {
                PhysicsUnit.SYMBOL_TO_UNIT.put(symbol, this);
                return;
            }
            if (!this.equals(unit))
                throw new IllegalArgumentException("Symbol " + symbol + " is associated to a different unit");
        }
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public final boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof BaseUnit))
            return false;
        BaseUnit thatUnit = (BaseUnit) that;
        return this.symbol.equals(thatUnit.symbol);
    }

    @Override
    public final int hashCode() {
        return symbol.hashCode();
    }

    @Override
    public PhysicsUnit<Q> toSI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AbstractUnitConverter getConverterToSI() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PhysicsDimension getDimension() {
        return dimension;
    }

    @Override
    public Map<? extends PhysicsUnit, Integer> getProductUnits() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
