/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.models;

import javolution.realtime.LocalReference;

import org.jscience.physics.quantities.Constants;

/**
 * <p> This abstract class represents a physical model. Instances of this
 *     class determinate the dimensional units of the seven base quantities:
 *     {@link org.jscience.physics.quantities.Length Length}, 
 *     {@link org.jscience.physics.quantities.Mass Mass}, 
 *     {@link org.jscience.physics.quantities.Duration Duration},
 *     {@link org.jscience.physics.quantities.ElectricCurrent ElectricCurrent},
 *     {@link org.jscience.physics.quantities.Temperature Temperature} (thermodynamic),
 *     {@link org.jscience.physics.quantities.AmountOfSubstance AmountOfSubstance} and
 *     {@link org.jscience.physics.quantities.LuminousIntensity LuminousIntensity}.</p>
 * <p> To select a model, one needs only to call the model <code>select</code>
 *     static method. For example:<pre>
 *          RelativisticModel.select();
 *     </pre>
 *     Selecting a predefined model automatically sets the dimension of 
 *     the {@link org.jscience.physics.units.BaseUnit base units}.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public abstract class PhysicalModel {

    /**
     * Holds the context key to the current model.
     */
    static final LocalReference<PhysicalModel> CURRENT
        = new LocalReference<PhysicalModel>(StandardModel.INSTANCE);

    /**
     * Holds fundamental constants (package private).
     */
    final static double ePlus = Constants.ePlus.doubleValue();

    final static double hBar = Constants.hBar.doubleValue();

    final static double c = Constants.c.doubleValue();

    final static double k = Constants.k.doubleValue();

    final static double µ0 = Constants.µ0.doubleValue();

    final static double G = Constants.G.doubleValue();

    /**
     * Default constructor (allows for derivation).
     */
    protected PhysicalModel() {
    }

    /**
     * Returns the current physical model (default: instance of 
     * {@link org.jscience.physics.models.StandardModel StandardModel}).
     * The current model specifies the default output units of physical
     * quantities.
     *
     * @return the context-local physical model.
     */
    public static final PhysicalModel current() {
        return CURRENT.get(); // Default standard.
    }

    /**
     * Sets the current model. This method is being called automatically when
     * one of the predefined models is selected.
     *
     * @param  model the context-local physical model.
     * @see    #current
     */
    protected static final void setCurrent(PhysicalModel model) {
        CURRENT.set(model);
    }

    /**
     * Sets the dimensional units of the seven base quantities:
     * {@link org.jscience.physics.quantities.Length Length}, 
     * {@link org.jscience.physics.quantities.Mass Mass}, 
     * {@link org.jscience.physics.quantities.Duration Duration},
     * {@link org.jscience.physics.quantities.ElectricCurrent ElectricCurrent},
     * {@link org.jscience.physics.quantities.Temperature Temperature} (thermodynamic),
     * {@link org.jscience.physics.quantities.AmountOfSubstance AmountOfSubstance} and
     * {@link org.jscience.physics.quantities.LuminousIntensity LuminousIntensity}. 
     *
     * @see     org.jscience.physics.units.BaseUnit#setDimension
     */
    protected abstract void setPhysicalDimensions();

}
