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

/**
 * This class represents the relativistic model.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class RelativisticModel extends PhysicalModel {

    /**
     * Holds the single instance of this class.
     */
    private final static RelativisticModel INSTANCE = new RelativisticModel();

    /**
     * Selects the relativistic model as the current model.
     */
    public static void select() {
        INSTANCE.setPhysicalDimensions();
        PhysicalModel.setCurrent(INSTANCE);
    }

    /**
     * Sets the dimensional units of the seven base quantities as follow:
     * <ul>
     * <li>{@link org.jscience.physics.quantities.Length Length} : <code>"s"</code></li>
     * <li>{@link org.jscience.physics.quantities.Mass Mass} : <code>"eV"</code></li>
     * <li>{@link org.jscience.physics.quantities.Duration Duration} : <code>"s"</code></li>
     * <li>{@link org.jscience.physics.quantities.ElectricCurrent ElectricCurrent} : <code>"A"</code></li>
     * <li>{@link org.jscience.physics.quantities.Temperature Temperature} : <code>"K"</code></li>
     * <li>{@link org.jscience.physics.quantities.AmountOfSubstance AmountOfSubstance} : <code>"mol"</code></li>
     * <li>{@link org.jscience.physics.quantities.LuminousIntensity LuminousIntensity} : <code>"cd"</code></li>
     * </ul>
     *
     * @see     org.jscience.physics.units.BaseUnit#setDimension
     */
    protected final void setPhysicalDimensions() {

        // SPEED_OF_LIGHT (METER / SECOND) = 1
        SI.METER.setDimension(SI.SECOND, new MultiplyConverter(1 / c));

        // ENERGY = m²·kg/s² = kg·c²
        SI.KILOGRAM.setDimension(NonSI.ELECTRON_VOLT, new MultiplyConverter(c
                * c / ePlus));

        SI.SECOND.setDimension(SI.SECOND, Converter.IDENTITY);
        SI.KELVIN.setDimension(SI.KELVIN, Converter.IDENTITY);
        SI.AMPERE.setDimension(SI.AMPERE, Converter.IDENTITY);
        SI.MOLE.setDimension(SI.MOLE, Converter.IDENTITY);
        SI.CANDELA.setDimension(SI.CANDELA, Converter.IDENTITY);
    }
}