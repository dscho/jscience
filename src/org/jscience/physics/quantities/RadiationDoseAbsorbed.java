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
 * This class represents the amount of energy deposited per unit of
 * mass. The system unit for this quantity is "Gy".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class RadiationDoseAbsorbed extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.GRAY;

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new RadiationDoseAbsorbed();
        }
    };

    /**
     * Represents a {@link RadiationDoseAbsorbed} amounting to nothing.
     */
    public final static RadiationDoseAbsorbed ZERO
        = (RadiationDoseAbsorbed) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected RadiationDoseAbsorbed() {}

    /**
     * Returns the {@link RadiationDoseAbsorbed} corresponding to the specified
     * quantity.
     *
     * @param  quantity a quantity compatible with
     *         {@link RadiationDoseAbsorbed}.
     * @return the specified quantity or a new {@link RadiationDoseAbsorbed} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to
     *         {@link RadiationDoseAbsorbed}.
     */
    public static RadiationDoseAbsorbed radiationDoseAbsorbedOf(
            Quantity quantity) {
        return (RadiationDoseAbsorbed) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link RadiationDoseAbsorbed} instances in the specified unit.
     *
     * @param  unit the output unit for {@link RadiationDoseAbsorbed} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = 6833294413629008983L;
}
