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
 * This class represents an electric conductance. The system unit for this
 * quantity is "A²·s³/(m²·kg)".  By default, instances of this class are showed
 * in Siemens ("S").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class ElectricConductance extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.SIEMENS.getDimension();

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new ElectricConductance();
        }
    }.useFor(SI.SIEMENS);

    /**
     * Represents an {@link ElectricConductance} amounting to nothing.
     */
    public final static ElectricConductance ZERO
        = (ElectricConductance) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected ElectricConductance() {}

    /**
     * Returns the {@link ElectricConductance} corresponding to the specified
     * quantity.
     *
     * @param  quantity a quantity compatible with {@link ElectricConductance}.
     * @return the specified quantity or a new {@link ElectricConductance} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link ElectricConductance}.
     */
    public static ElectricConductance electricConductanceOf(Quantity quantity) {
        return (ElectricConductance) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link ElectricConductance} instances in the specified unit.
     *
     * @param  unit the output unit for {@link ElectricConductance} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = -2673341111574457500L;
}
