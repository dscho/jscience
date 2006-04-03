/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.models;

import static javax.units.Dimension.LENGTH;

import javax.units.converters.UnitConverter;

/**
 * This class represents the standard model. 
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
public class StandardModel extends PhysicalModel {

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
        INSTANCE.setDimensions();
        PhysicalModel.setCurrent(INSTANCE);
    }

    @Override
    protected void setDimensions() {
        LENGTH.setEquivalentTo(LENGTH, UnitConverter.IDENTITY); // Resets.
    }

}