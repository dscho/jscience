/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.physics.models;

import org.jscience.physics.units.Converter;
import org.jscience.physics.units.MultiplyConverter;
import org.jscience.physics.units.NonSI;
import org.jscience.physics.units.SI;
import org.jscience.physics.units.Unit;

/**
 * This class represents the high-energy model.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class HighEnergyModel extends PhysicalModel {

    /**
     * Holds the single instance of this class.
     */
    private final static HighEnergyModel INSTANCE = new HighEnergyModel();

    /**
     * Selects the high-energy model as the current model.
     */
    public static void select() {
        INSTANCE.setPhysicalDimensions();
        PhysicalModel.setCurrent(INSTANCE);
    }

    /**
     * Sets the dimensional units of the seven base quantities as follow:
     * <ul>
     * <li>{@link org.jscience.physics.quantities.Length Length} : <code>"ns"</code></li>
     * <li>{@link org.jscience.physics.quantities.Mass Mass} : <code>"GeV"</code></li>
     * <li>{@link org.jscience.physics.quantities.Duration Duration} : <code>"ns"</code></li>
     * <li>{@link org.jscience.physics.quantities.ElectricCurrent ElectricCurrent} : <code>"1/ns"</code></li>
     * <li>{@link org.jscience.physics.quantities.Temperature Temperature} : <code>"GeV"</code></li>
     * <li>{@link org.jscience.physics.quantities.AmountOfSubstance AmountOfSubstance} : <code>"mol"</code></li>
     * <li>{@link org.jscience.physics.quantities.LuminousIntensity LuminousIntensity} : <code>"cd"</code></li>
     * </ul>
     *
     * @see     org.jscience.physics.units.BaseUnit#setDimension
     */
    protected final void setPhysicalDimensions() {

        // SPEED_OF_LIGHT (METER / SECOND) = 1
        SI.SECOND.setDimension(SI.NANO(SI.SECOND), new MultiplyConverter(1E9));
        SI.METER.setDimension(SI.NANO(SI.SECOND),
                new MultiplyConverter(1E9 / c));

        // ENERGY = m²·kg/s² = kg·c²
        SI.KILOGRAM.setDimension(SI.GIGA(NonSI.ELECTRON_VOLT),
                new MultiplyConverter(c * c / ePlus / 1E9));

        // BOLTZMANN (JOULE / KELVIN = (KILOGRAM / C^2 ) / KELVIN) = 1
        SI.KELVIN.setDimension(SI.GIGA(NonSI.ELECTRON_VOLT),
                new MultiplyConverter(k / ePlus / 1E9));

        // ELEMENTARY_CHARGE (SECOND * AMPERE) = 1
        SI.AMPERE.setDimension(Unit.ONE.divide(SI.NANO(SI.SECOND)),
                new MultiplyConverter(1E-9 / ePlus));

        SI.MOLE.setDimension(SI.MOLE, Converter.IDENTITY);
        SI.CANDELA.setDimension(SI.CANDELA, Converter.IDENTITY);
    }
}