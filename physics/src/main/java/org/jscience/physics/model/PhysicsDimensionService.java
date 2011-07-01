/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.model;

import org.unitsofmeasurement.quantity.Quantity;

/**
 * <p> This interface represents the service to retrieve the dimension
 *     given a quantity type. Bundles providing new quantity types should
 *     publish instances of this class in order for the framework to be able
 *     to determinate the dimension associated to the new quantity type.</p>
 *
 * <p> Common physics models are "Standard", "Relativistic", "HighEnergy",
 *     "Quantum" and "Natural"</p>
 *
 * <p> For convenience, the {@link PhysicsModel} class tracks such published
 *     services through the static method {@link PhysicsModel#getInstance()}.</p>
 *     
 * @see <a href="http://en.wikipedia.org/wiki/Dimensional_analysis">Wikipedia: Dimensional Analysis</a>
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public interface PhysicsDimensionService {

    /**
     * Returns the dimension for the specified quantity or <code>null</code> if
     * unknown.
     *
     * @return the corresponding dimension or <code>null</code>
     */
    <Q extends Quantity<Q>> PhysicsDimension getDimension(Class<Q> quantityType);

}
