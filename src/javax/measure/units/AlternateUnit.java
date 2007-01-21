/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.measure.units;

import javax.measure.converters.UnitConverter;
import javax.measure.quantities.Quantity;

/**
 * <p> This class represents the units used in expressions to distinguish
 *     between quantities of a different nature but of the same dimensions.</p>
 *     
 * <p> Examples of alternate units:[code]
 *     Unit<Angle> RADIAN = new AlternateUnit<Angle>("rad", ONE);
 *     Unit<Force> NEWTON = new AlternateUnit<Force>("N", METER.multiply(KILOGRAM).divide(SECOND.pow(2)));
 *     Unit<Pressure> PASCAL = new AlternateUnit<Pressure>("Pa", NEWTON.divide(METER.pow(2)));
 *     [/code]</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 22, 2006
 */
public final class AlternateUnit<Q extends Quantity> extends DerivedUnit<Q> {

    /**
     * Holds the symbol.
     */
    private final String _symbol;

    /**
     * Holds the parent unit (a system unit).
     */
    private final Unit _parent;

    /**
     * Creates an alternate unit for the specified unit identified by the 
     * specified symbol. 
     *
     * @param symbol the symbol for this alternate unit.
     * @param parent the system unit from which this alternate unit is
     *        derived.
     * @throws IllegalArgumentException if the specified parent unit is not a 
     *         system unit (base unit, alternate unit or rational product
     *         of base units and alternate units).
     * @throws IllegalArgumentException if the specified symbol is 
     *         associated to a different unit.
     */
    public AlternateUnit(String symbol, Unit parent) {
        if (!parent.getSystemUnit().equals(parent))
            throw new IllegalArgumentException(parent + " is not a system unit");
        _symbol = symbol;
        _parent = parent;
        // Checks if the symbol is associated to a different unit.
        synchronized (Unit.SYMBOL_TO_UNIT) {
            Unit unit = Unit.SYMBOL_TO_UNIT.get(symbol);
            if (unit == null) {
                Unit.SYMBOL_TO_UNIT.put(symbol, this);
                return;
            }
            if (unit instanceof AlternateUnit) {
                AlternateUnit existingUnit = (AlternateUnit) unit;
                if (symbol.equals(existingUnit._symbol)
                        && _parent.equals(existingUnit._parent))
                    return; // OK, same unit.
            }
            throw new IllegalArgumentException("Symbol " + symbol
                    + " is associated to a different unit");
        }
    }

    /**
     * Returns the symbol for this alternate unit.
     *
     * @return this alternate unit symbol.
     */
    public final String getSymbol() {
        return _symbol;
    }

    /**
     * Returns the parent unit from which this alternate unit is derived 
     * (a system unit itself).
     *
     * @return the parent of the alternate unit.
     */
    @SuppressWarnings("unchecked")
    public final Unit<? super Q> getParent() {
        return _parent;
    }

    @Override
    public final Unit<? super Q> getSystemUnit() {
        return this;
    }

    @Override
    public final UnitConverter toSystemUnit() {
        return UnitConverter.IDENTITY;
    }

    /**
     * Indicates if this alternate unit is considered equals to the specified 
     * object (both are alternate units with equal symbol, equal base units
     * and equal converter to base units).
     *
     * @param  that the object to compare for equality.
     * @return <code>true</code> if <code>this</code> and <code>that</code>
     *         are considered equals; <code>false</code>otherwise. 
     */
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof AlternateUnit))
            return false;
        AlternateUnit thatUnit = (AlternateUnit) that;
        return this._symbol.equals(thatUnit._symbol); // Symbols are unique.
    }

    // Implements abstract method.
    public int hashCode() {
        return _symbol.hashCode();
    }

    private static final long serialVersionUID = 1L;
}