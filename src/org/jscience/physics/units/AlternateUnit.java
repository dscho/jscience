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
 * <p> This class represents an alternate unit. Alternate units are used
 *     in expressions to distinguish between quantities of a different nature
 *     but of the same dimensions (e.g. angle <code>rad</code>, 
 *     angular acceleration <code>rad/s²</code>).</p>
 * <p> Instances of this class are created using the {@link Unit#alternate}
 *     method.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 * @see     Unit#alternate
 */
public final class AlternateUnit extends DerivedUnit {

    /**
     * Holds the base units for this alternate unit.
     */
    private final Unit _baseUnits;

    /**
     * Creates an alternate unit using the specified symbol.
     *
     * @param  baseUnits the base units for this alternate unit.
     * @param  symbol the symbol for this alternate unit.
     */
    private AlternateUnit(String symbol, Unit baseUnits) {
        super(symbol);
        _baseUnits = baseUnits;
    }

    /**
     * Returns an alternate for this unit. If the alternate unit does not
     * already exist, then it is created.
     *
     * @param  symbol the symbol of the alternate unit.
     * @param  systemUnit the system unit from which to derive the alternate
     *         unit being returned.
     * @return the alternate unit.
     * @throws IllegalArgumentException if the specified symbol is currently
     *         associated to a different unit.
     * @throws UnsupportedOperationException if the specified unit is not
     *         a system unit.
     */
    static AlternateUnit getInstance(String symbol,
                                     Unit systemUnit) {
        AlternateUnit newUnit
            = new AlternateUnit(symbol, getBaseUnits(systemUnit));
        return (AlternateUnit) getInstance(newUnit); // Ensures unicity.
    }
    private static Unit getBaseUnits(Unit systemUnit) {
        if (systemUnit instanceof BaseUnit) {
            return systemUnit;
        } else if (systemUnit instanceof AlternateUnit) {
            return ((AlternateUnit)systemUnit)._baseUnits;
        } else if (systemUnit instanceof ProductUnit) {
            ProductUnit product = (ProductUnit) systemUnit;
            Unit baseUnits = ONE;
            for (int i=0; i < product.size(); i++) {
                ProductUnit.Element e = product.get(i);
                Unit unit = getBaseUnits(e.getUnit());
                unit = unit.pow(e.getPow());
                unit = unit.root(e.getRoot());
                baseUnits = baseUnits.multiply(unit);
            }
            return baseUnits;
        } else {
            throw new UnsupportedOperationException(
                "Unit: " + systemUnit + " is not a system unit");
        }
    }

    /**
     * Returns the symbol for this alternate unit.
     *
     * @return this alternate unit's symbol.
     */
    public final String getSymbol() {
        return _symbol;
    }

    // Implements abstract method.
    public Unit getSystemUnit() {
        return this; // Alternate units are system units.
    }

    // Implements abstract method.
    public boolean equals(Object that) {
        return (this == that) || (
            (that instanceof AlternateUnit) &&
            (((AlternateUnit)that)._baseUnits == _baseUnits) &&
            ((AlternateUnit)that)._symbol.equals(_symbol));
    }

    // Implements abstract method.
    int calculateHashCode() { 
        return _symbol.hashCode();
    }

    // Implements abstract method.
    Unit getCtxDimension() {
        return _baseUnits.getCtxDimension();
    }

    // Implements abstract method.
    Converter getCtxToDimension() {
        return _baseUnits.getCtxToDimension();
    }

    private static final long serialVersionUID = 8827914772630423686L;
}