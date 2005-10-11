/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.models;

import org.jscience.physics.units.Converter;
import static org.jscience.physics.units.Dimension.*;
import org.jscience.physics.units.SI;

/**
 * This class represents the standard model. 
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 * @see     SI
 */
public final class StandardModel extends PhysicalModel {

    /**
     * Holds the single instance of this class.
     */
    final static StandardModel INSTANCE = new StandardModel();

    /**
     * Default constructor (allows for derivation).
     */
    protected StandardModel() {
    }

    /**
     * Selects the standard model as the current model.
     */
    public static void select() {
        INSTANCE.setPhysicalDimensions();
        PhysicalModel.setCurrent(INSTANCE);
    }

    /**
     * Sets the dimensional units of the seven base quantities as follow:
     * <ul>
     * <li>{@link org.jscience.physics.quantities.Length Length} : <code>"m"</code></li>
     * <li>{@link org.jscience.physics.quantities.Mass Mass} : <code>"kg"</code></li>
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
        SI.SECOND.setDimension(TIME, Converter.IDENTITY);
        SI.METER.setDimension(LENGTH, Converter.IDENTITY);
        SI.KILOGRAM.setDimension(MASS, Converter.IDENTITY);
        SI.KELVIN.setDimension(TEMPERATURE, Converter.IDENTITY);
        SI.AMPERE.setDimension(ELECTRIC_CURRENT, Converter.IDENTITY);
        SI.MOLE.setDimension(AMOUNT_OF_SUBSTANCE, Converter.IDENTITY);
        SI.CANDELA.setDimension(LUMINOUS_INTENSITY, Converter.IDENTITY);
    }

}