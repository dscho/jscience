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

import java.util.Map;
import java.util.Set;

import javolution.realtime.Realtime;
import javolution.util.FastSet;

import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This class represents a function being actually defined from a mapping 
 *     between two sets.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class DiscreteFunction extends Function {

    /**
     * Holds the factory constructing discrete function instances.
     */
    private static final Factory FACTORY = new Factory() {
        public Object create() {
            return new DiscreteFunction();
        }
    };

    /**
     * Holds the mapping.
     */
    private Map _mapping;

    /**
     * Holds the variable.
     */
    private Variable _variable;

    /**
     * Default constructor.
     */
    private DiscreteFunction() {
    }

    /**
     * Returns the {@link DiscreteFunction} defined from the specified 
     * mapping between two sets.
     * 
     * @param  mapping the index-value mapping of this function.
     * @param  variable this function variable.
     * @return the corresponding function.
     */
    public static DiscreteFunction valueOf(Map mapping, Variable variable) {
        DiscreteFunction df = (DiscreteFunction) FACTORY.object();
        df._mapping = mapping;
        df._variable = variable;
        return df;
    }

    /**
     * Returns the mapping of this discrete function.
     *
     * @return the map object as specified during construction.
     * @see #valueOf
     */
    public Map getMapping() {
        return _mapping;
    }

    // Implements abstract method.  
    public Operable evaluate() {
        Object index = _variable.getValue();
        if (index == null) { throw new FunctionException("Variable "
                + _variable + " not set"); }
        return (Operable) _mapping.get(index);
    }

    // Implements abstract method.  
    public Set getVariables() {
        FastSet variables = FastSet.newInstance(1);
        variables.add(_variable);
        return variables;
    }

    // Overrides.
    public void move(ContextSpace cs) {
        super.move(cs);
        if (_mapping instanceof Realtime) {
            ((Realtime)_mapping).move(cs);
        }
    }

    private static final long serialVersionUID = -9171049214010649558L;
}