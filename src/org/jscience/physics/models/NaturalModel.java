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
import org.jscience.physics.units.SI;
import org.jscience.physics.units.Unit;

/**
 * This class represents the natural model.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class NaturalModel extends PhysicalModel {

    /**
     * Holds the single instance of this class.
     */
    private final static NaturalModel INSTANCE = new NaturalModel();

    /**
     * Selects the natural model as the current model.
     */
    public static void select() {
        INSTANCE.setPhysicalDimensions();
        PhysicalModel.setCurrent(INSTANCE);
    }

    /**
     * Sets the dimensional units of the seven base quantities as follow:
     * <ul>
     * <li>{@link org.jscience.physics.quantities.Length} : <code>"1"</code></li>
     * <li>{@link org.jscience.physics.quantities.Mass} : <code>"1"</code></li>
     * <li>{@link org.jscience.physics.quantities.Duration Duration} : <code>"1"</code></li>
     * <li>{@link org.jscience.physics.quantities.ElectricCurrent ElectricCurrent} : <code>"1"</code></li>
     * <li>{@link org.jscience.physics.quantities.Temperature Temperature} : <code>"1"</code></li>
     * <li>{@link org.jscience.physics.quantities.AmountOfSubstance AmountOfSubstance} : <code>"mol"</code></li>
     * <li>{@link org.jscience.physics.quantities.LuminousIntensity LuminousIntensity} : <code>"cd"</code></li>
     * </ul>
     *
     * @see     org.jscience.physics.units.BaseUnit#setDimension
     */
    protected final void setPhysicalDimensions() {

        // H_BAR (SECOND * JOULE = SECOND * (KILOGRAM / C^2 )) = 1
        // SPEED_OF_LIGHT (METER / SECOND) = 1
        // BOLTZMANN (JOULE / KELVIN = (KILOGRAM / C^2 ) / KELVIN) = 1
        // MAGNETIC CONSTANT (NEWTON / AMPERE^2) = 1
        // GRAVITATIONAL CONSTANT (METER^3 / KILOGRAM / SECOND^2) = 1
        SI.SECOND.setDimension(Unit.ONE, new MultiplyConverter((c * c)
                * Math.sqrt(c / (hBar * G))));
        SI.METER.setDimension(Unit.ONE, new MultiplyConverter(c
                * Math.sqrt(c / (hBar * G))));
        SI.KILOGRAM.setDimension(Unit.ONE, new MultiplyConverter(Math.sqrt(G
                / (hBar * c))));
        SI.KELVIN.setDimension(Unit.ONE, new MultiplyConverter(k
                * Math.sqrt(G / (hBar * c)) / (c * c)));
        SI.AMPERE.setDimension(Unit.ONE, new MultiplyConverter(Math
                .sqrt(µ0 * G)
                / (c * c)));
        SI.MOLE.setDimension(SI.MOLE, Converter.IDENTITY);
        SI.CANDELA.setDimension(SI.CANDELA, Converter.IDENTITY);
    }
}
