/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
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
public final class Constant<O extends Operable<O>>  extends Polynomial<O> {

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
    public static <O extends Operable<O>> Constant<O> valueOf(O value) {
        Constant<O> cst = FACTORY.object();
        cst._terms = FastMap.newInstance();
        cst._terms.put(Term.CONSTANT, value);
        return cst;
    }
 
    /**
     * Returns the constant value for this function.
     *
     * @return <code>getCoefficient(Term.CONSTANT)</code>
     */
    public O getValue() {
        return getCoefficient(Term.CONSTANT);
    }

    private static final long serialVersionUID = -4946995524238749339L;
}