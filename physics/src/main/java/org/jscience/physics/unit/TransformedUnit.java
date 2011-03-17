/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit;

import org.jscience.physics.model.PhysicsDimension;
import java.util.Map;
import org.unitsofmeasurement.quantity.Quantity;
import org.unitsofmeasurement.unit.UnitConverter;

/**
 * <p> This class represents the units derived from other units using
 *     {@linkplain UnitConverter converters}.</p>
 *
 * <p> Examples of transformed units:[code]
 *         CELSIUS = KELVIN.add(273.15);
 *         FOOT = METRE.times(3048).divide(10000);
 *         MILLISECOND = MILLI(SECOND);
 *     [/code]</p>
 *
 * <p> Transformed units have no label. But like any other units,
 *     they may have labels attached to them (see {@link org.jscience.physics.unit.SymbolMap
 *     SymbolMap}</p>
 *
 * <p> Instances of this class are created through the {@link PhysicsUnit#transform} method.</p>
 *
 * @param <Q> The type of the quantity measured by this unit.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
final class TransformedUnit<Q extends Quantity<Q>> extends PhysicsUnit<Q> {
  
	/**
     * Holds the parent unit (not a transformed unit).
     */
    private final PhysicsUnit<Q> parentUnit;

    /**
     * Holds the converter to the parent unit.
     */
    private final UnitConverter toParentUnit;

    /**
     * Creates a transformed unit from the specified (non transformed)
     * parent unit.
     *
     * @param parentUnit the system unit from which this unit is derived.
     * @param toParentUnit the converter to the parent units.
     */
    public TransformedUnit(PhysicsUnit<Q> parentUnit, UnitConverter toParentUnit) {
        this.parentUnit = parentUnit;
        this.toParentUnit = toParentUnit;
    }

    @Override
    public PhysicsDimension getDimension() {
        return parentUnit.getDimension();
    }

    @Override
    public UnitConverter getConverterToSystemUnit() {
        return parentUnit.getConverterToSystemUnit().concatenate(toParentUnit);
    }

    @Override
    public PhysicsUnit<Q> getSystemUnit() {
        return parentUnit.getSystemUnit();
    }

    @Override
    public Map<? extends PhysicsUnit, Integer> getProductUnits() {
        return parentUnit.getProductUnits();
    }

    @Override
    public int hashCode() {
        return parentUnit.hashCode() + toParentUnit.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof TransformedUnit))
            return false;
        TransformedUnit thatUnit = (TransformedUnit) that;
        return this.parentUnit.equals(thatUnit.parentUnit) &&
                this.toParentUnit.equals(thatUnit.toParentUnit);
    }
}
