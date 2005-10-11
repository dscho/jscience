/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.functions;
import java.util.Set;

import javolution.util.FastSet;
import javolution.lang.Text;
import javolution.lang.TextBuilder;

import org.jscience.mathematics.matrices.Operable;

/**
 * This class represents the quotient of two {@link Polynomial}.
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 * @see     <a href="http://mathworld.wolfram.com/RationalFunction.html">
 *          Rational Function -- from MathWorld</a>
 */
public class RationalFunction<O extends Operable<O>> extends Function<O> {

    /**
     * Holds the factory for rational functions.
     */
    private static final Factory<RationalFunction> FACTORY = new Factory<RationalFunction>() {

        protected RationalFunction create() {
            return new RationalFunction();
        }
    };

    /**
     * Holds the dividend.
     */
    private Polynomial<O> _dividend;

    /**
     * Holds the divisor.
     */
    private Polynomial<O> _divisor;

    /**
     * Default constructor.
     */
    private RationalFunction() {
    }

    /**
     * Returns the dividend of this rational function.
     * 
     * @return this rational function dividend. 
     */
    public Polynomial<O> getDividend() {
        return _dividend;
    }

    /**
     * Returns the divisor of this rational function.
     * 
     * @return this rational function divisor.
     */
    public Polynomial<O> getDivisor() {
        return _divisor;
    }

    /**
     * Returns the rational function from the specified dividend and divisor.
     * 
     * @param dividend the dividend value.
     * @param divisor the divisor value.
     * @return <code>dividend / divisor</code>
     */
    public static <O extends Operable<O>> RationalFunction<O> valueOf(
            Polynomial<O> dividend, Polynomial<O> divisor) {
        RationalFunction<O> rf = FACTORY.object();
        rf._dividend = dividend;
        rf._divisor = divisor;
        return rf;
    }

    /**
     * Compares this rational function against the specified object.
     * 
     * @param obj the object to compare with.
     * @return <code>true</code> if the objects are the same;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof RationalFunction) {
            RationalFunction that = (RationalFunction) obj;
            return this._dividend.equals(this._dividend)
                    && this._divisor.equals(that._divisor);
        } else {
            return false;
        }
    }

    /**
     * Returns the hash code for this rational function.
     * 
     * @return the hash code value.
     */
    public int hashCode() {
        return _dividend.hashCode() - _divisor.hashCode();
    }

    // Implements abstract method.
    public Set<Variable> getVariables() {
        FastSet<Variable> variables = FastSet.newInstance();
        variables.addAll(_dividend.getVariables());
        variables.addAll(_divisor.getVariables());
        return variables;
    }

    // Implements abstract method.
    public O evaluate() {
        return _dividend.evaluate().times(_divisor.evaluate().reciprocal());
    }

    // Implements interface.
    public Text toText() {
        TextBuilder tb = TextBuilder.newInstance();
        tb.append('(');
        tb.append(_dividend);
        tb.append(")/(");
        tb.append(_divisor);
        tb.append(')');
        return tb.toText();
    }

    // Overrides.
    public boolean move(ObjectSpace os) {
        if (super.move(os)) {
            _dividend.move(os);
            _divisor.move(os);
            return true;
        }
        return false;
    }

    // Implements Operable.
    public RationalFunction<O> plus(RationalFunction<O> that) {
        return valueOf(this._dividend.times(that._divisor).plus(
                this._divisor.times(that._dividend)),
                this._divisor.times(that._divisor));
    }

    // Overrides.
    public RationalFunction<O>  opposite() {
        return valueOf(_dividend.opposite(), _divisor);
    }

    // Overrides.
    public RationalFunction<O> times(RationalFunction<O> that) {
            return valueOf(this._dividend.times(that._dividend),
                    this._divisor.times(that._divisor));
    }

    // Implements Operable.
    public RationalFunction<O> reciprocal() {
        return valueOf(_divisor, _dividend);
    }

    // Overrides.
    public Function<O> compose(Function<O> f) {
        if (_dividend instanceof Constant) {
            return _dividend.times(_divisor.compose(f).reciprocal());
        } else if (_divisor instanceof Constant) {
            return _dividend.compose(f).times(_divisor.reciprocal());
        } else {
            return _dividend.compose(f).times(
                    _divisor.compose(f).reciprocal());
        }
    }

    // Overrides.
    public RationalFunction<O> differentiate(Variable v) {
        return valueOf(_divisor.times(_dividend.differentiate(v))
                .plus(_dividend.times(_divisor.differentiate(v)).opposite()),
                _dividend.pow(2));
    }

    private static final long serialVersionUID = 1L;
}