/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.functions;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javolution.util.FastMap;
import javolution.util.FastSet;
import javolution.lang.Text;
import javolution.lang.TextBuilder;

import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This class represents a mathematical expression involving a sum of powers
 *     in one or more {@link Variable variables} multiplied by 
 *     coefficients (such as <code>x² + x·y + 3y²</code>).</p>
 * <p> Polynomials are also characterized by the type of variable they operate 
 *     upon. For example:
 *     <pre>   Polynomial&lt;Quantity> x = Polynomial.valueOf((Quantity)Dimensionless.ONE, Variable.X);</pre>
 *     and
 *     <pre>   Polynomial&lt;Complex> x = Polynomial.valueOf(Complex.ONE, Variable.T);</pre>
 *     are two different polynomials, the first operates on 
 *     {@link org.jscience.physics.quantities.Quantity quantities},
 *     whereas the second operates on 
 *     {@link org.jscience.mathematics.numbers.Complex complex} numbers.</p>
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 * @see     <a href="http://mathworld.wolfram.com/Polynomial.html">
 *          Polynomial -- from MathWorld</a>
 */
public class Polynomial<O extends Operable<O>> extends Function<O> {

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
    FastMap<Term, O> _terms;

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
    public static <O extends Operable<O>> Polynomial<O> valueOf(O coefficient,
            Variable variable) {
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
    public static <O extends Operable<O>> Polynomial<O> valueOf(O coefficient,
            Term term) {
        if (term != Term.CONSTANT) {
            Polynomial<O> p = FACTORY.object();
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
    public O getCoefficient(Term term) {
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
     * Returns the sum of two polynomials.
     * 
     * @param that the polynomial being added.
     * @return <code>this + that</code>
     */
    public Polynomial<O> plus(Polynomial<O> that) {
        Polynomial<O> result = FACTORY.object();
        result._terms = FastMap.newInstance();
        result._terms.putAll(this._terms);
        for (Map.Entry<Term, O> entry : that._terms.entrySet()) {
            Term term = entry.getKey();
            O thatCoef = entry.getValue();
            O thisCoef = result._terms.get(term);
            if (thisCoef != null) {
                result._terms.put(term, thisCoef.plus(thatCoef));
            } else {
                result._terms.put(term, thatCoef);
            }
        }
        return result;
    }

    /**
     * Returns the product of two polynomials.
     * 
     * @param that the polynomial multiplier.
     * @return <code>this * that</code>
     */
    public Polynomial<O> times(Polynomial<O> that) {
        Polynomial<O> result = FACTORY.object();
        result._terms = FastMap.newInstance();
        for (Map.Entry<Term, O> entry1 : this._terms.entrySet()) {
            Term t1 = entry1.getKey();
            O c1 = entry1.getValue();
            for (Map.Entry<Term, O> entry2 : that._terms.entrySet()) {
                Term t2 = entry2.getKey();
                O c2 = entry2.getValue();

                Term t = t1.times(t2);
                O c = c1.times(c2);
                O prev = result.getCoefficient(t);
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
    public Polynomial<O> compose(Polynomial<O> that) {
        Set<Variable> variables = getVariables();
        if (variables.size() != 1)
            throw new FunctionException("Single variable required: "
                    + variables);
        Variable v = variables.iterator().next();
        Polynomial<O> result = null;
        for (Map.Entry<Term, O> entry : this._terms.entrySet()) {
            Term term = entry.getKey();
            Constant<O> cst = Constant.valueOf(entry.getValue());
            int power = term.getPower(v);
            if (power > 0) {
                Polynomial<O> fn = that.pow(power);
                result = (result != null) ? result.plus(cst.times(fn)) : cst
                        .times(fn);
            } else { // power = 0
                result = (result != null) ? result.plus(cst) : cst;
            }
        }
        return result;
    }

    // Implements abstract method.
    public Set<Variable> getVariables() {
        FastSet<Variable> variables = FastSet.newInstance();
        for (Term term : _terms.keySet()) {
            variables.addAll(term.getVariables());
        }
        return variables;
    }

    // Implements abstract method.
    public O evaluate() {
        O sum = null;
        for (Map.Entry<Term, O> entry : _terms.entrySet()) {
            Term term = entry.getKey();
            O coef = entry.getValue();
            O product = (term == Term.CONSTANT) ? coef : coef.times((O) term
                    .evaluate());
            sum = (sum == null) ? product : sum.plus(product);
        }
        return sum;
    }

    // Implements interface.
    public Text toText() {
        TextBuilder tb = TextBuilder.newInstance();
        boolean first = true;
        for (Map.Entry<Term, O> entry : _terms.entrySet()) {
            tb.append(first ? "" : " + ");
            first = false;
            Term term = entry.getKey();
            O coef = entry.getValue();
            tb.append('[');
            tb.append(coef);
            tb.append(']');
            tb.append(term);
        }
        return tb.toText();
    }

    // Overrides
    public Function<O> plus(Function<O> that) {
        if (that instanceof Polynomial) 
            return plus((Polynomial<O>)that);
        return super.plus(that);
    }

    // Overrides.
    public Polynomial<O> opposite() {
        Polynomial<O> result = FACTORY.object();
        result._terms = FastMap.newInstance();
        for (Map.Entry<Term, O> entry : _terms.entrySet()) {
            result._terms.put(entry.getKey(), entry.getValue().opposite());
        }
        return result;
    }

    // Overrides.
    public Function<O> times(Function<O> that) {
        if (that instanceof Polynomial) 
            return times((Polynomial<O>)that);
        return super.times(that);
    }

    // Overrides.
    public Polynomial<O> pow(int n) {
        return (Polynomial<O>) super.pow(n);
    }
        
    // Overrides.
    public Function<O> compose(Function<O> that) {
        if (that instanceof Polynomial) 
            return compose((Polynomial<O>)that);
        return super.compose(that);
    }

    // Overrides.
    public Polynomial<O> differentiate(Variable v) {
        if (this.getOrder(v) > 0) {
            Polynomial<O> result = null;
            for (Map.Entry<Term, O> entry : this._terms.entrySet()) {
                Term term = entry.getKey();
                O coef = entry.getValue();
                int power = term.getPower(v);
                if (power > 0) {
                    O newCoef = multiply(coef, power);
                    Term newTerm = term.divide(Term.valueOf(v, 1));
                    Polynomial<O> p = valueOf(newCoef, newTerm);
                    result = (result != null) ? result.plus(p) : p;
                }
            }
            return result;
        } else { // Returns zero.
            O coef = _terms.values().iterator().next();
            return Constant.valueOf(coef.plus(coef.opposite()));
        }
    }

    private static <O extends Operable<O>> O multiply(O o, int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n: " + n
                    + " zero or negative values not allowed");
        O shift2 = o;
        O result = null;
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
    public Polynomial<O> integrate(Variable v) {
        Polynomial<O> result = null;
        for (Map.Entry<Term, O> entry : this._terms.entrySet()) {
            Term term = entry.getKey();
            O coef = entry.getValue();
            int power = term.getPower(v);
            O newCoef = multiply(coef.reciprocal(), power + 1).reciprocal();
            Term newTerm = term.times(Term.valueOf(v, 1));
            Polynomial<O> p = valueOf(newCoef, newTerm);
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