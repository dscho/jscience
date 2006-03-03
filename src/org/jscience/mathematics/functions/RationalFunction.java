/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.functions;
import java.util.Set;

import org.jscience.mathematics.structures.Field;

import javolution.util.FastSet;
import javolution.lang.Text;
import javolution.lang.TextBuilder;

/**
 * This class represents the quotient of two {@link Polynomial}, 
 * it is also a {@link Field field} (invertible).
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
public class RationalFunction<F extends Field<F>> extends Function<F, F> implements Field<Function<F, F>>{

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
    private Polynomial<F> _dividend;

    /**
     * Holds the divisor.
     */
    private Polynomial<F> _divisor;

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
    public Polynomial<F> getDividend() {
        return _dividend;
    }

    /**
     * Returns the divisor of this rational function.
     * 
     * @return this rational function divisor.
     */
    public Polynomial<F> getDivisor() {
        return _divisor;
    }

    /**
     * Returns the rational function from the specified dividend and divisor.
     * 
     * @param dividend the dividend value.
     * @param divisor the divisor value.
     * @return <code>dividend / divisor</code>
     */
    public static <F extends Field<F>> RationalFunction<F> valueOf(
            Polynomial<F> dividend, Polynomial<F> divisor) {
        RationalFunction<F> rf = FACTORY.object();
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
    public Set<Variable<F>> getVariables() {
        FastSet<Variable<F>> variables = FastSet.newInstance();
        variables.addAll(_dividend.getVariables());
        variables.addAll(_divisor.getVariables());
        return variables;
    }

    // Implements abstract method.
    public F evaluate() {
        return _dividend.evaluate().times(_divisor.evaluate().inverse());
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
    public RationalFunction<F> plus(RationalFunction<F> that) {
        return valueOf(this._dividend.times(that._divisor).plus(
                this._divisor.times(that._dividend)),
                this._divisor.times(that._divisor));
    }

    // Overrides.
    public RationalFunction<F>  opposite() {
        return valueOf(_dividend.opposite(), _divisor);
    }

    // Overrides.
    public RationalFunction<F> times(RationalFunction<F> that) {
            return valueOf(this._dividend.times(that._dividend),
                    this._divisor.times(that._divisor));
    }

    // Implements Operable.
    public RationalFunction<F> reciprocal() {
        return valueOf(_divisor, _dividend);
    }

    // Overrides.
    public RationalFunction<F> differentiate(Variable v) {
        return valueOf(_divisor.times(_dividend.differentiate(v))
                .plus(_dividend.times(_divisor.differentiate(v)).opposite()),
                _dividend.pow(2));
    }

    private static final long serialVersionUID = 1L;

    public Function<F, F> inverse() {
        RationalFunction<F> rf = FACTORY.object();
        rf._dividend = _divisor;
        rf._divisor = _dividend;
        return rf;
    }
}