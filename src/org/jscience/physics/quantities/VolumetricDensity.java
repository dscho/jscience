/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.physics.quantities;
import org.jscience.physics.units.ConversionException;
import org.jscience.physics.units.SI;
import org.jscience.physics.units.Unit;

/**
 * This class represents a mass per unit volume of a substance under
 * specified conditions of pressure and temperature. The system unit for
 * this quantity is "kg/m³".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class VolumetricDensity extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.KILOGRAM.divide(SI.METER.pow(3));

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new VolumetricDensity();
        }
    };

    /**
     * Represents a {@link VolumetricDensity} amounting to nothing.
     */
    public final static VolumetricDensity ZERO
        = (VolumetricDensity) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected VolumetricDensity() {}

    /**
     * Returns the {@link VolumetricDensity} corresponding to the specified
     * quantity.
     *
     * @param  quantity a quantity compatible with {@link VolumetricDensity}.
     * @return the specified quantity or a new {@link VolumetricDensity} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link VolumetricDensity}.
     */
    public static VolumetricDensity volumetricDensityOf(Quantity quantity) {
        return (VolumetricDensity) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link VolumetricDensity} instances in the specified unit.
     *
     * @param  unit the output unit for {@link VolumetricDensity} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = 8784418785328234327L;
}
