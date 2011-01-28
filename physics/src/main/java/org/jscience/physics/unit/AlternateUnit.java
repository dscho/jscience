/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit;

import org.unitsofmeasurement.unit.UnitConverter;

/**
 * <p> This class represents units used in expressions to distinguish
 *     between quantities of a different nature but of the same dimensions.</p>
 *
 * <p> Instances of this class are created through the
 *     {@link PhysicsUnit#alternate(String)} method.</p>
 *
 * @param <Q> The type of the quantity measured by this unit.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public final class AlternateUnit<Q extends Quantity<Q>> extends PhysicsUnit<Q> {

	/**
     * Holds the parent unit (a system unit).
     */
    private final PhysicsUnit<?> parent;

    /**
     * Holds the symbol for this unit.
     */
    private final String symbol;

    /**
     * Creates an alternate unit for the specified unit identified by the
     * specified name and symbol.
     *
     * @param symbol the symbol for this alternate unit.
     * @param parent the system unit from which this alternate unit is
     *        derived.
     * @throws UnsupportedOperationException if the parent is not
     *         an unscaled metric unit.
     * @throws IllegalArgumentException if the specified symbol is
     *         associated to a different unit.
     */
    AlternateUnit(String symbol, PhysicsUnit<?> parent) {
        if (!parent.isUnscaledMetric())
            throw new UnsupportedOperationException(parent + " is not an unscaled metric unit");
        this.parent = parent;
        this.symbol = symbol;
        // Checks if the symbol is associated to a different unit.
        synchronized (PhysicsUnit.SYMBOL_TO_UNIT) {
            PhysicsUnit<?> unit = PhysicsUnit.SYMBOL_TO_UNIT.get(symbol);
            if (unit == null) {
                PhysicsUnit.SYMBOL_TO_UNIT.put(symbol, this);
                return;
            }
            if (unit instanceof AlternateUnit<?>) {
                AlternateUnit<?> existingUnit = (AlternateUnit<?>) unit;
                if (symbol.equals(existingUnit.getSymbol()) && this.parent.equals(existingUnit.parent))
                    return; // OK, same unit.
            }
            throw new IllegalArgumentException("Symbol " + symbol + " is associated to a different unit");
        }
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public final PhysicsUnit<Q> toMetric() {
        return this;
    }

    @Override
    public final UnitConverter getConverterToMetric() {
        return UnitConverter.IDENTITY;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof AlternateUnit<?>))
            return false;
        AlternateUnit<?> thatUnit = (AlternateUnit<?>) that;
        return this.symbol.equals(thatUnit.symbol); // Symbols are unique.
    }

    @Override
    public PhysicsDimension getDimension() {
        return parent.getDimension();
    }

    @Override
    public UnitConverter getDimensionalTransform() {
        return parent.getDimensionalTransform();
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }
}
