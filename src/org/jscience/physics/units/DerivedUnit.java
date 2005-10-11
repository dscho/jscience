/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.units;

import org.jscience.physics.quantities.Quantity;

/**
 * <p> This abstract class identifies derived units. These are units that are
 *     formed as products of powers of the base units according to the algebraic
 *     relations linking the quantities concerned. The names and symbols of some
 *     units thus formed in terms of base units may be replaced by special names
 *     and symbols which can themselves be used to form expressions and symbols
 *     for other derived units.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.1, May 24, 2005
 */
public abstract class DerivedUnit<Q extends Quantity> extends Unit<Q> {

    /**
     * Default constructor.
     */
    protected DerivedUnit() {
    }
}