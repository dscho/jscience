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
public class RationalFunction extends Function {

    /**
     * Holds the factory for rational functions.
     */
    private static final Factory FACTORY = new Factory() {

        public Object create() {
            return new RationalFunction();
        }
    };

    /**
     * Holds the dividend.
     */
    private Polynomial _dividend;

    /**
     * Holds the divisor.
     */
    private Polynomial _divisor;

    /**
     * Default constructor.
     */
    private RationalFunction() {
    }

    /**
     * Returns the dividend of this rational function.
     * 
     * @return this rational function's dividend. 
     */
    public Polynomial getDividend() {
        return _dividend;
    }

    /**
     * Returns the divisor of this rational function.
     * 
     * @return this rational function's divisor.
     */
    public Polynomial getDivisor() {
        return _divisor;
    }

    /**
     * Returns the rational function from the specified dividend and divisor.
     * 
     * @param dividend the dividend value.
     * @param divisor the divisor value.
     * @return <code>dividend / divisor</code>
     */
    public static RationalFunction valueOf(Polynomial dividend, Polynomial divisor) {
            RationalFunction rf = (RationalFunction) FACTORY.object();
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
    public Set getVariables() {
        FastSet variables = FastSet.newInstance(16);
        variables.add(_dividend.getVariables());
        variables.add(_divisor.getVariables());
        return variables;
    }

    // Implements abstract method.
    public Operable evaluate() {
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
    public void move(ContextSpace cs) {
        super.move(cs);
        _dividend.move(cs);
        _divisor.move(cs);
    }

    // Implements Operable.
    public Operable plus(Operable o) {
        if (o instanceof RationalFunction) {
            RationalFunction that = (RationalFunction) o;
            return valueOf((Polynomial) this._dividend.times(that._divisor)
                    .plus(this._divisor.times(that._dividend)),
                    (Polynomial) this._divisor.times(that._divisor));
        } else {
            return super.plus(o);
        }
    }
    
    // Overrides.
    public Operable opposite() {
        return valueOf((Polynomial) _dividend.opposite(), _divisor);
    }

    // Overrides.
    public Operable times(Operable o) {
        if (o instanceof RationalFunction) {
            RationalFunction that = (RationalFunction) o;
            return valueOf((Polynomial) this._dividend.times(that._dividend),
                    (Polynomial) this._divisor.times(that._divisor));
        } else {
            return super.times(o);
        }
    }

    // Implements Operable.
    public Operable reciprocal() {
        return valueOf(_divisor, _dividend);
    }

    // Overrides.
    public Function compose(Function f) {
        if (_dividend instanceof Constant) {
            return (Function) _dividend.times(
                    _divisor.compose(f).reciprocal());
        } else if (_divisor instanceof Constant) {
            return (Function) _dividend.compose(f).times(
                    _divisor.reciprocal());
        } else {
            return (Function) _dividend.compose(f).times(
                _divisor.compose(f).reciprocal());
        }
    }

    // Overrides.
    public Function differentiate(Variable v) {
        return valueOf((Polynomial) _divisor.times(_dividend.differentiate(v))
                .plus(_dividend.times(_divisor.differentiate(v)).opposite()),
                (Polynomial) _dividend.pow(2));
    }

    private static final long serialVersionUID = 4515664495869116697L;
}