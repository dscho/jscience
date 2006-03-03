/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.units;
import javax.quantities.Quantity;
import javax.units.converters.UnitConverter;

/**
 * <p> This class represents the building blocks on top of which all others
 *     units are created. Base units are typically mutually independent. 
 *     Although, in specialized context (e.g. relativistic context), 
 *     conversions between base units is possible (see 
 *     {@link Dimension#setEquivalentTo Dimension.setEquivalentTo}).</p>
 *     
 * <p> Examples of base units:[code]
 *     Unit<Length> METER = new BaseUnit<Length>("m", Dimension.LENGTH);
 *     Unit<Mass> KILOGRAM = new BaseUnit<Mass>("kg", Dimension.MASS);
 *     Unit<Duration> SECOND = new BaseUnit<Duration>("s", Dimension.TIME);
 *     [/code]</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.2, February 9, 2006
 */
public class BaseUnit<Q extends Quantity> extends Unit<Q> {

    /**
     * Holds the symbol.
     */ 
    private final String _symbol;

    /**
     * Holds the unit dimension.
     */ 
    final Dimension _dimension;

    /**
     * Creates a new base unit having the specified symbol and dimension.
     *
     * @param symbol the symbol of this base unit.
     * @param dimension the dimension for this unit.
     * @throws IllegalArgumentException if the specified symbol is 
     *         associated to a different unit (see {@link #equals(Object)})
     */
    public BaseUnit(String symbol, Dimension dimension) {
        _symbol = symbol;
        _dimension = dimension;
        // Checks if the symbol is associated to a different unit.
        synchronized (Unit.SYMBOL_TO_UNIT) {
            Unit unit = Unit.SYMBOL_TO_UNIT.get(symbol);
            if (unit == null) {
                 Unit.SYMBOL_TO_UNIT.put(symbol, this);
                 return;
            }    
            if (!unit.equals(this))
                throw new IllegalArgumentException("Symbol " + symbol +
                        " is associated to a different unit");
        }
    }

    /**
     * Returns the unique symbol for this base unit. 
     *
     * @return this base unit symbol.
     */
    public final String getSymbol() {
        return _symbol;
    }

    /**
     * Indicates if this base unit is considered equals to the specified 
     * object (both are base units with equal symbol and identical
     * dimensions).
     *
     * @param  that the object to compare for equality.
     * @return <code>true</code> if <code>this</code> and <code>that</code>
     *         are considered equals; <code>false</code>otherwise. 
     */
    public boolean equals(Object that) {
        if (this == that) return true;
        if (!(that instanceof BaseUnit)) return false;
        BaseUnit thatUnit = (BaseUnit) that; 
        return this._symbol.equals(thatUnit._symbol) && 
                this._dimension == thatUnit._dimension; 
    }

    // Implements abstract method.
    public int hashCode() {
        return _symbol.hashCode();
    }    

    // Implements abstract method.
    public Unit<? super Q> getBaseUnits() {
        return this;
    }

    // Implements abstract method.
    public UnitConverter toBaseUnits() {
        return UnitConverter.IDENTITY;
    }

    private static final long serialVersionUID = 4709307296575248603L;
}