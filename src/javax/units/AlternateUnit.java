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
 * @version 1.1, January 22, 2006
 */
public final class AlternateUnit<Q extends Quantity> extends DerivedUnit<Q> {

    /**
     * Holds the symbol.
     */ 
    private final String _symbol;

    /**
     * Holds the base units.
     */ 
    private final Unit _baseUnits;

    /**
     * Holds the converter to base units.
     */ 
    private final UnitConverter _toBaseUnits;
    
    /**
     * Creates an alternate unit for the specified unit identified by the 
     * specified symbol. 
     *
     * @param symbol the symbol for this alternate unit.
     * @param parent the unit for which an alternate unit is created.
     * @throws IllegalArgumentException if the specified symbol is 
     *         associated to a different unit (see {@link #equals(Object)})
     */
     public AlternateUnit(String symbol, Unit parent) {
         _symbol = symbol;
         _baseUnits = parent.getBaseUnits();
         _toBaseUnits = parent.toBaseUnits();
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
      * Returns the symbol for this alternate unit.
      *
      * @return this alternate unit symbol.
      */
     public final String getSymbol() {
         return _symbol;
     }

     // Implements abstract method.
     @SuppressWarnings("unchecked")
    public final Unit<? super Q> getBaseUnits() {
         return _baseUnits;
     }

     // Implements abstract method.
     public final UnitConverter toBaseUnits() {
         return _toBaseUnits;
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
        if (this == that) return true;
        if (!(that instanceof AlternateUnit)) return false;
        AlternateUnit thatUnit = (AlternateUnit) that; 
        return this._symbol.equals(thatUnit._symbol) &&
           this._baseUnits.equals(thatUnit._baseUnits) &&
           this._toBaseUnits.equals(thatUnit._toBaseUnits);
    }

    // Implements abstract method.
    public int hashCode() { 
        return _symbol.hashCode();
    }

    private static final long serialVersionUID = 1L;
}