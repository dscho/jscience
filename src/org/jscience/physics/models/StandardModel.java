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

import java.util.Collections;
import java.util.Map;

import javolution.util.FastMap;

import org.jscience.physics.quantities.Quantity;
import org.jscience.physics.units.Converter;
import org.jscience.physics.units.SI;
import org.jscience.physics.units.Unit;

/**
 * This class represents the standard model. Physical quantities are displayed
 * using the "Système International d'Unités" (SI).
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
     * Holds the system units to SI units mapping.
     */
    private final static Map SYSTEM_TO_SI = Collections
            .synchronizedMap(new FastMap());

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
     * Returns the default output unit for the specified quantity.
     * This method returns the system unit of the specified quantity or one
     * of the following {@link SI} units:
     * <ul>
     * <li><code>"kat"</code> for <code>"mol/s"</code>.</li>
     * <li><code>"F"</code> for <code>"s<sup>4</sup>·A²/(m²·kg)"</code>.</li>
     * <li><code>"C"</code> for <code>"s·A"</code>.</li>
     * <li><code>"S"</code> for <code>"A²·s³/(m²·kg)"</code>.</li>
     * <li><code>"H"</code> for <code>"m²·kg/(s²·A²)"</code>.</li>
     * <li><code>"V"</code> for <code>"m²·kg/(s³·A)"</code>.</li>
     * <li><code>"Ohm"</code> for <code>"m²·kg/(s³·A²)"</code>.</li>
     * <li><code>"J"</code> for <code>"m²·kg/s²"</code>.</li>
     * <li><code>"N"</code> for <code>"m·kg/s²"</code>.</li>
     * <li><code>"Hz"</code> for <code>"1/s"</code>.</li>
     * <li><code>"lx"</code> for <code>"cd·sr/m²"</code>.</li>
     * <li><code>"lm"</code> for <code>"cd·sr"</code>.</li>
     * <li><code>"Wb"</code> for <code>"m²·kg/(s²·A)"</code>.</li>
     * <li><code>"T"</code> for <code>"kg/(s²·A)"</code>.</li>
     * <li><code>"W"</code> for <code>"m²·kg/s³"</code>.</li>
     * <li><code>"Pa"</code> for <code>"kg/(m·s²)"</code>.</li>
     * </ul>
     *
     * @param  quantity the quantity for which the default output unit
     *         is returned.
     * @return <code>quantity.getSystemUnit()</code> or one of the
     *         {@link SI} units above.
     */
    public Unit unitFor(Quantity quantity) {
        Unit systemUnit = quantity.getSystemUnit();
        Unit siUnit = (Unit) SYSTEM_TO_SI.get(systemUnit);
        return (siUnit != null) ? siUnit : systemUnit;
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
        SI.SECOND.setDimension(SI.SECOND, Converter.IDENTITY);
        SI.METER.setDimension(SI.METER, Converter.IDENTITY);
        SI.KILOGRAM.setDimension(SI.KILOGRAM, Converter.IDENTITY);
        SI.KELVIN.setDimension(SI.KELVIN, Converter.IDENTITY);
        SI.AMPERE.setDimension(SI.AMPERE, Converter.IDENTITY);
        SI.MOLE.setDimension(SI.MOLE, Converter.IDENTITY);
        SI.CANDELA.setDimension(SI.CANDELA, Converter.IDENTITY);
    }

    static {
        // Mapping to system units.
        //
        SYSTEM_TO_SI.put(SI.KATAL.getDimension(), SI.KATAL);
        SYSTEM_TO_SI.put(SI.FARAD.getDimension(), SI.FARAD);
        SYSTEM_TO_SI.put(SI.COULOMB.getDimension(), SI.COULOMB);
        SYSTEM_TO_SI.put(SI.SIEMENS.getDimension(), SI.SIEMENS);
        SYSTEM_TO_SI.put(SI.HENRY.getDimension(), SI.HENRY);
        SYSTEM_TO_SI.put(SI.VOLT.getDimension(), SI.VOLT);
        SYSTEM_TO_SI.put(SI.OHM.getDimension(), SI.OHM);
        SYSTEM_TO_SI.put(SI.JOULE.getDimension(), SI.JOULE);
        SYSTEM_TO_SI.put(SI.NEWTON.getDimension(), SI.NEWTON);
        SYSTEM_TO_SI.put(SI.HERTZ.getDimension(), SI.HERTZ);
        SYSTEM_TO_SI.put(SI.CANDELA.multiply(SI.STERADIAN).divide(
                SI.METER.pow(2)), SI.LUX);
        SYSTEM_TO_SI.put(SI.CANDELA.multiply(SI.STERADIAN), SI.LUMEN);
        SYSTEM_TO_SI.put(SI.WEBER.getDimension(), SI.WEBER);
        SYSTEM_TO_SI.put(SI.TESLA.getDimension(), SI.TESLA);
        SYSTEM_TO_SI.put(SI.WATT.getDimension(), SI.WATT);
        SYSTEM_TO_SI.put(SI.PASCAL.getDimension(), SI.PASCAL);
    }
}