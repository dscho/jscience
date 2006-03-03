/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.functions;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.jscience.mathematics.structures.GroupMultiplicative;
import org.jscience.mathematics.structures.Ring;

import javolution.util.FastMap;
import javolution.util.FastSet;
import javolution.lang.Text;
import javolution.lang.TextBuilder;

/**
 * <p> This class represents a mathematical expression involving a sum of powers
 *     in one or more {@link Variable variables} multiplied by 
 *     coefficients (such as <code>x² + x·y + 3y²</code>).</p>
 *     
 * <p> Polynomials are also characterized by the type of variable they operate 
 *     upon. For example:[code]
 *           Polynomial<Measure> x = Polynomial.valueOf(new Measure(1, SI.METER), Variable.X);
 *     and
 *           Polynomial<Complex> x = Polynomial.valueOf(Complex.ONE, Variable.T);[/code]
 *     are two different polynomials, the first operates on 
 *     {@link org.jscience.physics.measures.Measure measurable quantities},
 *     whereas the second operates on 
 *     {@link org.jscience.mathematics.numbers.Complex complex} numbers.</p>
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
public class Polynomial<R extends Ring<R>> extends Function<R, R> {

    /**
     * Holds the factory constructing polynomial instances.
     */
    private static final Factory<Polynomial> FACTORY = new Factory<Polynomial>() {

        protected Polynomial create() {
            return new Polynomial();
        }
    };

    /**
     * Holds the terms of this polynomial with their associated coefficients.
     */
    FastMap<Term, R> _terms;

    /**
     * Default constructor.
     */
    Polynomial() {
    }

    /**
     * Returns an univariate polynomial of degree one with the specified 
     * coefficient multiplier.
     * 
     * @param coefficient the coefficient for the variable of degree 1.  
     * @param variable the variable for this polynomial.
     * @return <code>valueOf(coefficient, Term.valueOf(variable, 1))</code>
     */
    public static <R extends Ring<R>> Polynomial<R> valueOf(R coefficient,
            Variable<R> variable) {
        return valueOf(coefficient, Term.valueOf(variable, 1));
    }

    /**
     * Returns a polynomial corresponding to the specified {@link Term term}
     * with the specified coefficient multiplier.
     * 
     * @param coefficient the coefficient multiplier.  
     * @param term the term multiplicand.
     * @return <code>coefficient * term</code>
     */
    public static <R extends Ring<R>> Polynomial<R> valueOf(R coefficient,
            Term term) {
        if (term != Term.CONSTANT) {
            Polynomial<R> p = FACTORY.object();
            p._terms = FastMap.newInstance();
            p._terms.put(term, coefficient);
            return p;
        } else {
            return Constant.valueOf(coefficient);
        }
    }

    /**
     * Returns the terms of this polynomial.
     * 
     * @return this polynomial's terms.
     */
    public Collection<Term> getTerms() {
        return _terms.keySet();
    }

    /**
     * Returns the coefficient for the specified term.
     * 
     * @param term the term for which the coefficient is returned.
     * @return the coefficient for the specified term or <code>null</code>
     *         if this polynomial does not contain the specified term.
     */
    public R getCoefficient(Term term) {
        return _terms.get(term);
    }

    /**
     * Returns the order of this polynomial for the specified variable.
     * 
     * @return the polynomial order relative to the specified variable.
     */
    public int getOrder(Variable v) {
        int order = 0;
        for (Term term : _terms.keySet()) {
            int power = term.getPower(v);
            if (power > order) {
                order = power;
            }
        }
        return order;
    }

    /**
     * Compares this polynomial against the specified object.
     * 
     * @param that the object to compare with.
     * @return <code>true</code> if the objects are the same;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Object that) {
        if (that instanceof Polynomial) {
            return this._terms.equals(((Polynomial) that)._terms);
        } else {
            return false;
        }
    }

    /**
     * Returns the hash code for this polynomial.
     * 
     * @return the hash code value.
     */
    public int hashCode() {
        return _terms.hashCode();
    }

    /**
     * Returns the sum of this polynomial with a constant polynomial 
     * having the specified value (convenience method).
     * 
     * @param constantValue the value of the constant polynomial to add.
     * @return <code>this + Constant.valueOf(constantValue)</code>
     */
    public Polynomial<R> plus(R constantValue) {
        return this.plus(Constant.valueOf(constantValue));
    }

    /**
     * Returns the sum of two polynomials.
     * 
     * @param that the polynomial being added.
     * @return <code>this + that</code>
     */
    public Polynomial<R> plus(Polynomial<R> that) {
        Polynomial<R> result = FACTORY.object();
        result._terms = FastMap.newInstance();
        result._terms.putAll(this._terms);
        for (Map.Entry<Term, R> entry : that._terms.entrySet()) {
            Term term = entry.getKey();
            R thatCoef = entry.getValue();
            R thisCoef = result._terms.get(term);
            if (thisCoef != null) {
                result._terms.put(term, thisCoef.plus(thatCoef));
            } else {
                result._terms.put(term, thatCoef);
            }
        }
        return result;
    }

    /**
     * Returns the product of this polynomial with a constant polynomial 
     * having the specified value (convenience method).
     * 
     * @param constantValue the value of the constant polynomial to multiply.
     * @return <code>this · Constant.valueOf(constantValue)</code>
     */
    public Polynomial<R> times(R constantValue) {
        return this.times(Constant.valueOf(constantValue));
    }

    /**
     * Returns the product of two polynomials.
     * 
     * @param that the polynomial multiplier.
     * @return <code>this · that</code>
     */
    public Polynomial<R> times(Polynomial<R> that) {
        Polynomial<R> result = FACTORY.object();
        result._terms = FastMap.newInstance();
        for (Map.Entry<Term, R> entry1 : this._terms.entrySet()) {
            Term t1 = entry1.getKey();
            R c1 = entry1.getValue();
            for (Map.Entry<Term, R> entry2 : that._terms.entrySet()) {
                Term t2 = entry2.getKey();
                R c2 = entry2.getValue();

                Term t = t1.times(t2);
                R c = c1.times(c2);
                R prev = result.getCoefficient(t);
                if (prev != null) {
                    result._terms.put(t, prev.plus(c));
                } else {
                    result._terms.put(t, c);
                }
            }
        }
        return result;
    }

    /**
     * Returns the composition of this polynomial with the one specified.
     *
     * @param  that the polynomial for which the return value is passed as
     *         argument to this function.
     * @return the polynomial <code>(this o that)</code>
     * @throws FunctionException if this function is not univariate.
     */
    public Polynomial<R> compose(Polynomial<R> that) {
        Set<Variable<R>> variables = getVariables();
        if (variables.size() != 1)
            throw new FunctionException("Single variable required: "
                    + variables);
        Variable v = variables.iterator().next();
        Polynomial<R> result = null;
        for (Map.Entry<Term, R> entry : this._terms.entrySet()) {
            Term term = entry.getKey();
            Constant<R> cst = Constant.valueOf(entry.getValue());
            int power = term.getPower(v);
            if (power > 0) {
                Polynomial<R> fn = that.pow(power);
                result = (result != null) ? result.plus(cst.times(fn)) : cst
                        .times(fn);
            } else { // power = 0
                result = (result != null) ? result.plus(cst) : cst;
            }
        }
        return result;
    }

    // Implements abstract method.
    public Set<Variable<R>> getVariables() {
        FastSet variables = FastSet.newInstance();
        for (Term term : _terms.keySet()) {
            variables.addAll(term.getVariables());
        }
        return variables;
    }

    // Implements abstract method.
    public R evaluate() {
        R sum = null;
        for (Map.Entry<Term, R> entry : _terms.entrySet()) {
            Term term = entry.getKey();
            R coef = entry.getValue();
            R product = (term == Term.CONSTANT) ? coef : coef.times((R) term
                    .evaluate());
            sum = (sum == null) ? product : sum.plus(product);
        }
        return sum;
    }

    // Implements interface.
    public Text toText() {
        TextBuilder tb = TextBuilder.newInstance();
        boolean first = true;
        for (Map.Entry<Term, R> entry : _terms.entrySet()) {
            tb.append(first ? "" : " + ");
            first = false;
            Term term = entry.getKey();
            R coef = entry.getValue();
            tb.append('[');
            tb.append(coef);
            tb.append(']');
            tb.append(term);
        }
        return tb.toText();
    }

    // Overrides
    public Function<R, R> plus(Function<R, R> that) {
        if (that instanceof Polynomial) 
            return plus((Polynomial<R>)that);
        return super.plus(that);
    }

    // Overrides.
    public Polynomial<R> opposite() {
        Polynomial<R> result = FACTORY.object();
        result._terms = FastMap.newInstance();
        for (Map.Entry<Term, R> entry : _terms.entrySet()) {
            result._terms.put(entry.getKey(), entry.getValue().opposite());
        }
        return result;
    }

    // Overrides.
    public Function<R, R> times(Function<R, R> that) {
        if (that instanceof Polynomial) 
            return times((Polynomial<R>)that);
        return super.times(that);
    }

    // Overrides.
    public Polynomial<R> pow(int n) {
        return (Polynomial<R>) super.pow(n);
    }
        
    // Overrides.
    public <Z> Function<Z, R> compose(Function<Z, ? extends Ring> that) {
        if (that instanceof Polynomial) 
            return (Function) compose((Polynomial)that);
        return super.compose(that);
    }

    // Overrides.
    public Polynomial<R> differentiate(Variable v) {
        if (this.getOrder(v) > 0) {
            Polynomial<R> result = null;
            for (Map.Entry<Term, R> entry : this._terms.entrySet()) {
                Term term = entry.getKey();
                R coef = entry.getValue();
                int power = term.getPower(v);
                if (power > 0) {
                    R newCoef = multiply(coef, power);
                    Term newTerm = term.divide(Term.valueOf(v, 1));
                    Polynomial<R> p = valueOf(newCoef, newTerm);
                    result = (result != null) ? result.plus(p) : p;
                }
            }
            return result;
        } else { // Returns zero.
            R coef = _terms.values().iterator().next();
            return Constant.valueOf(coef.plus(coef.opposite()));
        }
    }

    private static <R extends Ring<R>> R multiply(R o, int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n: " + n
                    + " zero or negative values not allowed");
        R shift2 = o;
        R result = null;
        while (n >= 1) { // Iteration.
            if ((n & 1) == 1) {
                result = (result == null) ? shift2 : result.plus(shift2);
            }
            shift2 = shift2.plus(shift2);
            n >>>= 1;
        }
        return result;
    }

    // Overrides.
    public Polynomial<R> integrate(Variable v) {
        Polynomial<R> result = null;
        for (Map.Entry<Term, R> entry : this._terms.entrySet()) {
            Term term = entry.getKey();
            R coef = entry.getValue();
            int power = term.getPower(v);
            R newCoef = (R)((GroupMultiplicative)multiply((R)((GroupMultiplicative)coef).inverse(), power + 1)).inverse();
            Term newTerm = term.times(Term.valueOf(v, 1));
            Polynomial<R> p = valueOf(newCoef, newTerm);
            result = (result != null) ? result.plus(p) : p;
        }
        return result;
    }

    // Overrides.
    public boolean move(ObjectSpace os) {
        if (super.move(os)) {
            _terms.move(os);
            return true;
        }
        return false;
    }

    private static final long serialVersionUID = 1L;
}