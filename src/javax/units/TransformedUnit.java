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
     * Holds the parent unit (not a transformed unit).
     */
    private final Unit<? super Q> _parentUnit;

    /**
     * Holds the converter to the parent unit.
     */
    private final UnitConverter _toParentUnit;

    /**
     * Creates a transformed unit from the specified parent unit.
     *
     * @param parentUnit the untransformed unit from which this unit is 
     *        derived.
     * @param  toParentUnit the converter to the parent units.
     */
    TransformedUnit(Unit<? super Q> parentUnit, UnitConverter toParentUnit) {
        _parentUnit = parentUnit;
        _toParentUnit = toParentUnit;
    }
        
    /**
     * Returns the parent unit for this unit. The parent unit is the 
     * untransformed unit from which this unit is derived. It typically
     * identifies the unit type.
     *
     * @return the untransformed unit from which this unit is derived.
\     */
    public Unit<? super Q> getParentUnit() {
        return _parentUnit;
    }
        
    /**
     * Returns the converter to the parent unit.
     *
     * @return the converter to the parent unit.
\     */
    public UnitConverter toParentUnit() {
        return _toParentUnit;
    }
        
    /**
     * Indicates if this transformed unit is considered equals to the specified 
     * object (both are transformed units with equal parent unit and equal
     * converter to parent unit).
     *
     * @param  that the object to compare for equality.
     * @return <code>true</code> if <code>this</code> and <code>that</code>
     *         are considered equals; <code>false</code>otherwise. 
     */
    public boolean equals(Object that) {
        if (this == that) return true;
        if (!(that instanceof TransformedUnit)) return false;
        TransformedUnit thatUnit = (TransformedUnit) that; 
        return this._parentUnit.equals(thatUnit._parentUnit) &&
                 this._toParentUnit.equals(thatUnit._toParentUnit);
    }

    // Implements abstract method.
    public int hashCode() {
        return _parentUnit.hashCode() + _toParentUnit.hashCode();
    }

    // Implements abstract method.
    public Unit<? super Q> getBaseUnits() {
        return _parentUnit.getBaseUnits();
    }

    // Implements abstract method.
    public UnitConverter toBaseUnits() {
        return _parentUnit.toBaseUnits().concatenate(_toParentUnit);
    }

    private static final long serialVersionUID = 1L;

}