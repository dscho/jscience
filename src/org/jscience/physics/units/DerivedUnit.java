/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.physics.units;

/**
 * <p> This abstract class identifies derived units. These are units that are
 *     formed as products of powers of the base units according to the algebraic
 *     relations linking the quantities concerned. The names and symbols of some
 *     units thus formed in terms of base units may be replaced by special names
 *     and symbols which can themselves be used to form expressions and symbols
 *     for other derived units.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public abstract class DerivedUnit extends Unit {

    /**
     * Base constructor.
     *
     * @param  symbol the unit's symbol or <code>null</code> if none.
     */
    DerivedUnit(String symbol) {
        super(symbol);
    }
}