/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.functions;

import org.jscience.mathematics.structures.Ring;

import javolution.util.FastMap;

/**
 * <p> This class represents a constant function (polynomial of degree 0).<p>
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
public final class Constant<R extends Ring<R>>  extends Polynomial<R> {

    /**
     * Holds the factory constructing constant instances.
     */
    private static final Factory<Constant> FACTORY = new Factory<Constant>() {
        protected Constant create() {
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
    public static <R extends Ring<R>> Constant<R> valueOf(R value) {
        Constant<R> cst = FACTORY.object();
        cst._terms = FastMap.newInstance();
        cst._terms.put(Term.CONSTANT, value);
        return cst;
    }
 
    /**
     * Returns the constant value for this function.
     *
     * @return <code>getCoefficient(Term.CONSTANT)</code>
     */
    public R getValue() {
        return getCoefficient(Term.CONSTANT);
    }

    private static final long serialVersionUID = -4946995524238749339L;
}