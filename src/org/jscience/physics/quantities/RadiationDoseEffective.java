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
 * This class represents the effective (or "equivalent") dose of radiation
 * received by a human or some other living organism. The system unit for
 * this quantity is "Sv".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class RadiationDoseEffective extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.SIEVERT;

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new RadiationDoseEffective();
        }
    };

    /**
     * Represents a {@link RadiationDoseEffective} amounting to nothing.
     */
    public final static RadiationDoseEffective ZERO
        = (RadiationDoseEffective) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected RadiationDoseEffective() {}

    /**
     * Returns the {@link RadiationDoseEffective} corresponding to the specified
     * quantity.
     *
     * @param  quantity a quantity compatible with
     *         {@link RadiationDoseEffective}.
     * @return the specified quantity or a new {@link RadiationDoseEffective} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to
     *         {@link RadiationDoseEffective}.
     */
    public static RadiationDoseEffective radiationDoseEffectiveOf(
            Quantity quantity) {
        return (RadiationDoseEffective) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link RadiationDoseEffective} instances in the specified unit.
     *
     * @param  unit the output unit for {@link RadiationDoseEffective}
     *         instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = -7179499473102632760L;
}
