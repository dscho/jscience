/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.mathematics.functions;

import javolution.util.FastMap;

import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This class represents a constant function (polynomial of degree 0).<p>
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class Constant extends Polynomial {

    /**
     * Holds the factory constructing constant instances.
     */
    private static final Factory FACTORY = new Factory() {
        public Object create() {
            return new Constant();
        }
    };

    /**
     * Default constructor.
     */
    Constant() {}

    /**
     * Returns a constant function of specified value.
     * 
     * @param value the value returned by this function.
     * @return the corresponding constant function.
     */
    public static Constant valueOf(Operable value) {
        Constant cst = (Constant) FACTORY.object();
        cst._terms = FastMap.newInstance(1);
        cst._terms.put(Term.CONSTANT, value);
        return cst;
    }
 
    /**
     * Returns the constant value for this function.
     *
     * @return <code>getCoefficient(Term.CONSTANT)</code>
     */
    public Operable getValue() {
        return getCoefficient(Term.CONSTANT);
    }

    private static final long serialVersionUID = -4946995524238749339L;
}