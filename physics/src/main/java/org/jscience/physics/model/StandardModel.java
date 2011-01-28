/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.model;

/**
 * This class represents the standard model. 
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public class StandardModel extends PhysicsModel {

    /**
     * Holds the single instance of this class.
     */
    private final static StandardModel INSTANCE = new StandardModel();

    /**
     * Selects the standard model as the currentPhysicalModel model.
     */
    public static void select() {
        PhysicsModel.setCurrent(INSTANCE);
    }

    /**
     * Default constructor.
     */
    protected StandardModel() {
    }

}