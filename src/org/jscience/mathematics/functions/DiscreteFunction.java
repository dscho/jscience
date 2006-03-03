/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.functions;

import java.util.SortedMap;
import java.util.Set;

import org.jscience.mathematics.structures.Ring;

import javolution.realtime.Realtime;
import javolution.util.FastSet;

/**
 * <p> This class represents a function defined from a mapping betweem 
 *     two sets (points and values).</p>
 *     
 * <p> Instance of this class can be used to approximate continuous
 *     functions or to numerically solve differential systems.</p>
 *     
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
public final class DiscreteFunction<P, V extends Ring> extends Function<P, V> {

    /**
     * Holds the point-value entries.
     */
    private SortedMap<P, V> _pointValues;

    /**
     * Holds the variable.
     */
    private Variable<P> _variable;

    /**
     * Holds the interpolator.
     */
    Interpolator<P, V> _interpolator;

    /**
     * Creates the discrete function for the specified point-value entries.
     * 
     * @param  pointValues the point-value entries of this function.
     * @param  interpolator the interpolator.
     * @param  variable this function variable.
     */
    public DiscreteFunction(SortedMap<P, V> pointValues, Interpolator<P, V> interpolator, Variable variable) {
        _pointValues = pointValues;
        _variable = variable;
        _interpolator = interpolator;
    }

    /**
     * Returns the point-value entries of this discrete function.
     *
     * @return the point-value mapping.
     */
    public SortedMap getPointValues() {
        return _pointValues;
    }

    /**
     * Returns the interpolator used by this discrete function.
     *
     * @return the interpolator used to estimate the value between two points.
     */
    public Interpolator<P, V> getInterpolator() {
        return _interpolator;
    }

    // Implements abstract method.  
    public V evaluate() {
        P point = _variable.get();
        if (point == null) {
            throw new FunctionException("Variable " + _variable + " not set");
        }
        return _interpolator.interpolate(point, _pointValues);
    }

    // Implements abstract method.  
    public Set<Variable<P>> getVariables() {
        FastSet<Variable<P>> variables = FastSet.newInstance();
        variables.add(_variable);
        return variables;
    }

    // Overrides.
    public boolean move(ObjectSpace os) {
        if (super.move(os)) {
            if (_pointValues instanceof Realtime) {
                ((Realtime) _pointValues).move(os);
            }
            return true;
        }
        return false;
    }

    private static final long serialVersionUID = 1L;
}