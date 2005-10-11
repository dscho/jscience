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
 * This class represents the extent of a planar region or of the surface of
 * a solid measured in square units. The system unit for this quantity
 * is "m²".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Area extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<Area> UNIT = SI.SQUARE_METER;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Area> FACTORY = new Factory<Area>(
            UNIT) {
        protected Area create() {
            return new Area();
        }
    };

    /**
     * Represents a {@link Area} amounting to nothing.
     */
    public final static Area ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Area() {
    }

    /**
     * Shows {@link Area} instances in the specified unit.
     *
     * @param unit the display unit for {@link Area} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Area.class, unit);
    }

    ///////////////////
    // AREA SPECIFIC //
    ///////////////////

    /**
     * Returns the area of a circle sector (slice of a circle).
     *
     * @param  radius the circle radius.
     * @param  theta the central angle.
     * @return the area of the specified circle sector.
     */
    public static Area valueOf(Length radius, Angle theta) {
        // Returns (theta / 2.0) * radius^2
        return theta.times(radius.times(radius)).times(0.5).to(UNIT);
    }

    private static final long serialVersionUID = 1L;

}
