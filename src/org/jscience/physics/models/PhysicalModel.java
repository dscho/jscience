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

import javolution.realtime.LocalContext;

import org.jscience.physics.quantities.Constants;
import org.jscience.physics.quantities.Quantity;
import org.jscience.physics.units.Unit;

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
 *     Selecting a predefined model automatically sets the {@link Quantity}
 *     current model, which specifies the default output units for quantities.
 *     </p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public abstract class PhysicalModel {

    /**
     * Holds the context key to the current model.
     */
    static final LocalContext.Variable CURRENT
        = new LocalContext.Variable(StandardModel.INSTANCE);

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
        return (PhysicalModel) CURRENT.getValue(); // Default standard.
    }

    /**
     * Sets the current model. This method is being called automatically when
     * one of the predefined models is selected.
     *
     * @param  model the context-local physical model.
     * @see    #current
     */
    protected static final void setCurrent(PhysicalModel model) {
        CURRENT.setValue(model);
    }

    /**
     * Returns the default output unit for the specified quantity.
     * The default implementation of this method returns the dimensional
     * unit of the specified quantity.
     *
     * @param  quantity the quantity for which the default output unit
     *         is returned.
     * @return <code>quantity.getSystemUnit().getDimension()</code>
     */
    public Unit unitFor(Quantity quantity) {
        return quantity.getSystemUnit().getDimension();
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
