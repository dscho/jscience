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
 * This class represents the moment of a force. The system unit for this
 * quantity is "N·m".
 * <p> Note: The Newton-metre ("N·m") is also a way of exressing a Joule (unit
 *     of energy). However, torque is not energy. So, to avoid confusion, we
 *     will use the units "N·m" for torque and not "J". This distinction occurs
 *     due to the scalar nature of energy and the vector nature of torque.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Torque extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<Torque> UNIT = SI.NEWTON.times(SI.METER);

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Torque> FACTORY = new Factory<Torque>(UNIT) {
        protected Torque create() {
            return new Torque();
        }
    };

    /**
     * Represents a {@link Torque} amounting to nothing.
     */
    public final static Torque ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Torque() {
    }

    /**
     * Shows {@link Torque} instances in the specified unit.
     *
     * @param unit the display unit for {@link Torque} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Torque.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
