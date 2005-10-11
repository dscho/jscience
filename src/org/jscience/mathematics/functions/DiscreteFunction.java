/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.functions;

import java.util.SortedMap;
import java.util.Set;

import javolution.realtime.Realtime;
import javolution.util.FastSet;

import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This class represents a function defined from a mapping betweem 
 *     two sets (points and values).</p>
 *     
 * <p> A {@link Interpolator#LINEAR linear} interpolation is performed by
 *     default; although applications may use custom interpolators to estimate
 *     function values. For example:<pre>
 *       // Custom interpolator for non Operable points (e.g. java.util.Date) 
 *       DiscreteFunction sampling 
 *           = DiscreteFunction.valueOf(samples, Variable.T).
 *                setInterpolator(DATE_LINEAR);</pre>
 *     </p>
 *     
 * <p> Instance of this class can be used to approximate continuous
 *     functions or to numerically solve differential systems.</p>
 *     
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, January 15, 2005
 */
public final class DiscreteFunction<P, O extends Operable<O>> extends
        Function<O> {

    /**
     * Holds the discrete function factory.
     */
    private static final Factory<DiscreteFunction> FACTORY = new Factory<DiscreteFunction>() {
        protected DiscreteFunction create() {
            return new DiscreteFunction();
        }
    };

    /**
     * Holds the point-value entries.
     */
    private SortedMap<P, O> _pointValues;

    /**
     * Holds the variable.
     */
    private Variable _variable;

    /**
     * Holds the interpolator.
     */
    Interpolator<P, O> _interpolator = Interpolator.LINEAR;

    /**
     * Default constructor.
     */
    private DiscreteFunction() {
    }

    /**
     * Returns the {@link DiscreteFunction} for the specified point-value 
     * entries.
     * 
     * @param  pointValues the point-value entries of this function.
     * @param  variable this function variable.
     * @return the corresponding function.
     */
    public static <P, O extends Operable<O>> DiscreteFunction<P, O> 
           valueOf(SortedMap<P, O> pointValues, Variable variable) {
        DiscreteFunction<P, O> df = FACTORY.object();
        df._pointValues = pointValues;
        df._variable = variable;
        return df;
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
     * Returns the interpolator used by this discrete function
     * (default {@link Interpolator#LINEAR LINEAR}).
     *
     * @return the interpolator used to estimate the value between two points.
     */
    public Interpolator<P, O> getInterpolator() {
        return _interpolator;
    }

    /**
     * Sets a custom interpolator for this discrete function.
     *
     * @param interpolator the interpolator estimating the value between 
     *        two points.
     * @return <code>this</code>
     */
    public DiscreteFunction<P, O> setInterpolator(Interpolator<P, O> interpolator) {
        _interpolator = interpolator;
        return this;
    }

    // Implements abstract method.  
    public O evaluate() {
        P point = (P) _variable.get();
        if (point == null) {
            throw new FunctionException("Variable " + _variable + " not set");
        }
        return _interpolator.interpolate(point, _pointValues);
    }

    // Implements abstract method.  
    public Set<Variable> getVariables() {
        FastSet<Variable> variables = FastSet.newInstance();
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