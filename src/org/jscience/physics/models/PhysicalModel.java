/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.models;

import javax.units.Unit;

import org.jscience.physics.measures.Measure;

/**
 * <p> This abstract class represents a physical model. Instances of this
 *     class determinate the dimensional units of the seven base quantities:
 *     {@link javax.quantities.Length Length}, 
 *     {@link javax.quantities.Mass Mass}, 
 *     {@link javax.quantities.Duration Duration},
 *     {@link javax.quantities.ElectricCurrent ElectricCurrent},
 *     {@link javax.quantities.Temperature Temperature} (thermodynamic),
 *     {@link javax.quantities.AmountOfSubstance AmountOfSubstance} and
 *     {@link javax.quantities.LuminousIntensity LuminousIntensity}.</p>
 *     
 * <p> To select a model, one needs only to call the model <code>select</code>
 *     static method. For example:[code]
 *          RelativisticModel.select();
 *     [/code]</p>
 *     
 * <p> Selecting a predefined model automatically sets the dimension of 
 *     the {@link javax.units.BaseUnit base units} and the default display
 *     units for {@link Measure measurements}.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
public abstract class PhysicalModel {

//    /**
//     * Holds fundamental constants (package private).
//     */
//    final static double ePlus = Constants.ePlus.doubleValue();
//
//    final static double hBar = Constants.hBar.doubleValue();
//
//    final static double c = Constants.c.doubleValue();
//
//    final static double k = Constants.k.doubleValue();
//
//    final static double µ0 = Constants.µ0.doubleValue();
//
//    final static double G = Constants.G.doubleValue();
//
    
    private static PhysicalModel Current = StandardModel.INSTANCE;
    /**
     * Default constructor (allows for derivation).
     */
    protected PhysicalModel() {
    }

    /**
     * Returns the current physical model (default: instance of 
     * {@link StandardModel}).
     *
     * @return the context-local physical model.
     */
    public static final PhysicalModel current() {
        return PhysicalModel.Current;
    }

    /**
     * Sets the current model. This method is being called automatically when
     * one of the predefined models is selected.
     *
     * @param  model the context-local physical model.
     * @see    #current
     */
    protected static final void setCurrent(PhysicalModel model) {
        PhysicalModel.Current = model;
    }

    /**
     * Returns the preferred display unit for the specified measurement
     * (default <code>measure.getUnit()</code>).
     * 
     * @param measure the measurement for which the display unit is returned.
     * @return  the model preferred unit for the specified measurement. 
     * @see  javax.units.Dimension
     */
    public Unit getDisplayUnitFor(Measure measure) {
        return measure.getUnit();
    }

    /**
     * Sets the dimensional units of the seven base quantities:
     *     {@link javax.quantities.Length Length}, 
     *     {@link javax.quantities.Mass Mass}, 
     *     {@link javax.quantities.Duration Duration},
     *     {@link javax.quantities.ElectricCurrent ElectricCurrent},
     *     {@link javax.quantities.Temperature Temperature} (thermodynamic),
     *     {@link javax.quantities.AmountOfSubstance AmountOfSubstance} and
     *     {@link javax.quantities.LuminousIntensity LuminousIntensity}.</p>
     *
     * @see  javax.units.Dimension
     */
    protected abstract void setDimensions();

}