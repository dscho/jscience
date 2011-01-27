/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit;

import org.jscience.physics.unit.converter.PhysicalUnitConverter;
import org.jscience.physics.unit.PhysicalUnit;
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
 * <p> Instances of this class are created through the {@link PhysicalUnit#transform} method.</p>
 *
 * @param <Q> The type of the quantity measured by this unit.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public final class TransformedUnit<Q extends Quantity<Q>> extends PhysicalUnit<Q> {
  
    /**
	 * For cross-version compatibility.
	 */
	private static final long serialVersionUID = -442449068482939939L;

	/**
     * Holds the parent unit (not a transformed unit).
     */
    private final PhysicalUnit<Q> parentUnit;

    /**
     * Holds the converter to the parent unit.
     */
    private final PhysicalUnitConverter toParentUnit;

    /**
     * Creates a transformed unit from the specified (non transformed)
     * parent unit.
     *
     * @param parentUnit the untransformed unit from which this unit is
     *        derived.
     * @param  toParentUnit the converter to the parent units.
     * @throws IllegalArgumentException if <code>toParentUnit ==
     *         {@link UnitConverter#IDENTITY UnitConverter.IDENTITY}</code>
     * @throws IllegalArgumentException if the specified unit is a
     *         transformed unit.
     */
    public TransformedUnit(PhysicalUnit<Q> parentUnit, PhysicalUnitConverter toParentUnit) {
        if (toParentUnit == PhysicalUnitConverter.IDENTITY)
            throw new IllegalArgumentException("Identity not allowed");
        if (parentUnit instanceof TransformedUnit)
            throw new IllegalArgumentException("The parent unit cannot be a transformed unit");
        this.parentUnit = parentUnit;
        this.toParentUnit = toParentUnit;
    }

    /**
     * Returns the parent unit for this unit. The parent unit is the
     * untransformed unit from which this unit is derived.
     *
     * @return the untransformed unit from which this unit is derived.
     */
    public PhysicalUnit<Q> getParentUnit() {
        return parentUnit;
    }

    /**
     * Returns the converter to the parent unit.
     *
     * @return the converter to the parent unit.
     */
    public PhysicalUnitConverter toParentUnit() {
        return toParentUnit;
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

    @Override
    public int hashCode() {
        return parentUnit.hashCode() + toParentUnit.hashCode();
    }

    @Override
    public PhysicalUnit<Q> toMetric() {
        return parentUnit.toMetric();
    }

    @Override
    public UnitConverter getConverterToMetric() {
        return parentUnit.getConverterToMetric().concatenate(toParentUnit);
    }
}
