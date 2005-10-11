/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.quantities;
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
     * Holds the associated unit.
     */
    private final static Unit<ElectricConductance> UNIT = SI.SIEMENS;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<ElectricConductance> FACTORY = new Factory<ElectricConductance>(
            UNIT) {
        protected ElectricConductance create() {
            return new ElectricConductance();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link ElectricConductance} amounting to nothing.
     */
    public final static ElectricConductance ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected ElectricConductance() {
    }

    /**
     * Shows {@link ElectricConductance} instances in the specified unit.
     *
     * @param unit the display unit for {@link ElectricConductance} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(ElectricConductance.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
