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
 * <p> This class represents the units derived from other units using
 *     {@link UnitConverter converters}.</p>
 *     
 * <p> Examples of transformed units:[code]
 *         CELSIUS = KELVIN.add(273.15);
 *         FOOT = METER.multiply(0.3048);
 *         MILLISECOND = MILLI(SECOND); 
 *     [/code]</p>
 *     
 * <p> Transformed units have no label. But like any other units,
 *     they may have labels attached to them:[code]
 *         UnitFormat.getStandardInstance().label(FOOT, "ft");
 *     [/code]
 *     or aliases: [code]
 *         UnitFormat.getStandardInstance().alias(CENTI(METER)), "centimeter");
 *         UnitFormat.getStandardInstance().alias(CENTI(METER)), "centimetre");
 *     [/code]</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.2, February 9, 2006
 * @see     Unit#plus(double)
 * @see     Unit#times(double)
 * @see     Unit#transform(UnitConverter)
 * @see     UnitFormat
 */
public final class TransformedUnit<Q extends Quantity> extends DerivedUnit<Q> {

    /**
     * Holds the base units.
     */
    private final Unit<? super Q> _baseUnits;

    /**
     * Holds the converter to base units.
     */
    private final UnitConverter _toBaseUnits;

    /**
     * Creates a transformed unit having the specified base unit.
     *
     * @param  baseUnits the units from which this unit is derived.
     * @param  toBaseUnits the converter to the base units.
     */
    TransformedUnit(Unit<? super Q> baseUnits, UnitConverter toBaseUnits) {
        _baseUnits = baseUnits;
        _toBaseUnits = toBaseUnits;
    }
        
    /**
     * Indicates if this transformed unit is considered equals to the specified 
     * object (both are transformed units with equal base units and equal
     * converter to base units).
     *
     * @param  that the object to compare for equality.
     * @return <code>true</code> if <code>this</code> and <code>that</code>
     *         are considered equals; <code>false</code>otherwise. 
     */
    public boolean equals(Object that) {
        if (this == that) return true;
        if (!(that instanceof TransformedUnit)) return false;
        TransformedUnit thatUnit = (TransformedUnit) that; 
        return this._baseUnits.equals(thatUnit._baseUnits) &&
                 this._toBaseUnits.equals(thatUnit._toBaseUnits);
    }

    // Implements abstract method.
    public int hashCode() {
        return _baseUnits.hashCode() + _toBaseUnits.hashCode();
    }

    // Implements abstract method.
    public Unit<? super Q> getBaseUnits() {
        return _baseUnits;
    }

    // Implements abstract method.
    public UnitConverter toBaseUnits() {
        return _toBaseUnits;
    }

    private static final long serialVersionUID = 5577732467382952324L;

}