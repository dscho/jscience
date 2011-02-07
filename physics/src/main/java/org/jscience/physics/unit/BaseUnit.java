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
import org.jscience.physics.model.PhysicsModel;
import org.jscience.physics.unit.converter.AbstractUnitConverter;
import org.unitsofmeasurement.quantity.Quantity;
import org.unitsofmeasurement.unit.UnitConverter;


/**
 * <p> This class represents the building blocks on top of which all others
 *     units are created. Base units are always unscaled metric units.</p>
 * 
 * <p> When using the standard model, all seven {@link SI} base units
 *     are dimensionally independent.</p>
 *
 * <p> The actual dimension of a base unit is determined by the current
      {@link PhysicsModel physical model}.</p>
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
     * Creates a base unit having the specified symbol.
     *
     * @param symbol the symbol of this base unit.
     */
    public BaseUnit(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public PhysicsUnit<Q> getSystemUnit() {
        return this;
    }

    @Override
    public UnitConverter getConverterToSystemUnit() throws UnsupportedOperationException {
        return AbstractUnitConverter.IDENTITY;
    }

    @Override
    public Map<? extends PhysicsUnit, Integer> getProductUnits() {
        return null;
    }

    @Override
    public PhysicsDimension getDimension() {
        return PhysicsModel.getCurrent().getDimension(this);
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
}
