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

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javolution.realtime.RealtimeObject;
import javolution.util.FastMap;
import javolution.util.FastSet;
import javolution.lang.Text;
import javolution.lang.TextBuilder;
import javolution.lang.TypeFormat;

import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This class represents a mathematical expression involving a sum of powers
 *     in one or more {@link Function.Variable variables} multiplied by 
 *     coefficients (such as <code>x² + x·y + 3y²</code>).</p>
 * <p> Polynomials are also characterized by the type of variable they operate 
 *     upon. For example:
 *     <pre>   Polynomial x = Polynomial.valueOf(Scalar.ONE, Variable.X);</pre>
 *     and
 *     <pre>   Polynomial x = Polynomial.valueOf(Complex.ONE, Variable.X);</pre>
 *     are two different polynomials, the first operates on 
 *     {@link org.jscience.physics.quantities.Scalar dimensionless} quantities,
 *     whereas the second operates on 
 *     {@link org.jscience.mathematics.numbers.Complex complex} numbers.</p>
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 * @see     <a href="http://mathworld.wolfram.com/Polynomial.html">
 *          Polynomial -- from MathWorld</a>
 */
public class Polynomial extends Function {

    /**
     * Holds the factory constructing polynomial instances.
     */
    private static final Factory FACTORY = new Factory() {

        public Object create() {
            return new Polynomial();
        }
    };

    /**
     * Holds the terms of this polynomial with their associated coefficients.
     */
    FastMap _terms;

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
    public static Polynomial valueOf(Operable coefficient, Variable variable) {
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
    public static Polynomial valueOf(Operable coefficient, Term term) {
        if (term != Term.CONSTANT) {
            Polynomial p = (Polynomial) FACTORY.object();
            p._terms = FastMap.newInstance(1);
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
    public Collection getTerms() {
        return _terms.keySet();
    }

    /**
     * Returns the coefficient for the specified term.
     * 
     * @param term the term for which the coefficient is returned.
     * @return the coefficient for the specified term or <code>null</code>
     *         if this polynomial does not contain the specified term.
     */
    public Operable getCoefficient(Term term) {
        return (Operable) _terms.get(term);
    }

    /**
     * Returns the order of this polynomial for the specified variable.
     * 
     * @return the polynomial order relative to the specified variable.
     */
    public int getOrder(Variable v) {
        int order = 0;
        for (Iterator i = _terms.keySet().iterator(); i.hasNext();) {
            Term term = (Term) i.next();
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

    // Implements abstract method.
    public Set getVariables() {
        FastSet variables = FastSet.newInstance(16);
        for (Iterator i = _terms.keySet().iterator(); i.hasNext();) {
            Term term = (Term) i.next();
            if (term != Term.CONSTANT) {
                for (Term t = term; t != null; t = t._next) {
                    variables.add(t._variable);
                }
            }
        }
        return variables;
    }

    // Implements abstract method.
    public Operable evaluate() {
        Operable sum = null;
        for (Iterator i = _terms.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            Term term = (Term) entry.getKey();
            Operable coef = (Operable) entry.getValue();
            Operable product = (term == Term.CONSTANT) ? coef : coef.times(term
                    .evaluate());
            sum = (sum == null) ? product : sum.plus(product);
        }
        return sum;
    }

    // Implements interface.
    public Text toText() {
        TextBuilder tb = TextBuilder.newInstance();
        boolean first = true;
        for (Iterator i = _terms.entrySet().iterator(); i.hasNext();) {
            tb.append(first ? "" : " + ");
            first = false;
            Map.Entry entry = (Map.Entry) i.next();
            Term term = (Term) entry.getKey();
            Operable coef = (Operable) entry.getValue();
            tb.append('[');
            tb.append(coef);
            tb.append(']');
            tb.append(term);
        }
        return tb.toText();
    }

    // Implements Operable.
    public Operable plus(Operable o) {
        if (o instanceof Polynomial) {
            Polynomial that = (Polynomial) o;
            Polynomial result = (Polynomial) FACTORY.object();
            result._terms = FastMap.newInstance(this._terms.size()
                    + that._terms.size());
            result._terms.putAll(this._terms);
            for (Iterator i = that._terms.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                Term term = (Term) entry.getKey();
                Operable thatCoef = (Operable) entry.getValue();
                Operable thisCoef = (Operable) result._terms.get(term);
                if (thisCoef != null) {
                    result._terms.put(term, thisCoef.plus(thatCoef));
                } else {
                    result._terms.put(term, thatCoef);
                }
            }
            return result;
        } else {
            return super.plus(o);
        }
    }

    // Overrides.
    public Operable opposite() {
        Polynomial result = (Polynomial) FACTORY.object();
        result._terms = FastMap.newInstance(this._terms.size());
        for (Iterator i = _terms.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            result._terms.put(entry.getKey(), ((Operable) entry.getValue())
                    .opposite());
        }
        return result;
    }

    // Overrides.
    public Operable times(Operable o) {
        if (o instanceof Polynomial) {
            Polynomial that = (Polynomial) o;
            Polynomial result = (Polynomial) FACTORY.object();
            result._terms = FastMap.newInstance(this._terms.size()
                    * that._terms.size());
            for (Iterator i = this._terms.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry1 = (Map.Entry) i.next();
                Term t1 = (Term) entry1.getKey();
                Operable c1 = (Operable) entry1.getValue();
                for (Iterator j = that._terms.entrySet().iterator(); j
                        .hasNext();) {
                    Map.Entry entry2 = (Map.Entry) j.next();
                    Term t2 = (Term) entry2.getKey();
                    Operable c2 = (Operable) entry2.getValue();

                    Term t = t1.multiply(t2);
                    Operable c = c1.times(c2);
                    Operable prev = result.getCoefficient(t);
                    if (prev != null) {
                        result._terms.put(t, prev.plus(c));
                    } else {
                        result._terms.put(t, c);
                    }
                }
            }
            return result;
        } else {
            return super.times(o);
        }
    }

    // Overrides.
    public Function compose(Function f) {
        Set variables = getVariables();
        if (variables.size() == 1) {
            Variable v = (Variable) variables.iterator().next();
            Operable result = null;
            for (Iterator i = this._terms.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                Term term = (Term) entry.getKey();
                Constant cst = Constant.valueOf((Operable) entry.getValue());
                int power = term.getPower(v);
                if (power > 0) {
                    Function fn = f.pow(power);
                    result = (result != null) ? result.plus(cst.times(fn))
                            : cst.times(fn);
                } else { // power = 0
                    result = (result != null) ? result.plus(cst) : cst;
                }
            }
            return (Function) result;
        } else {
            throw new FunctionException("Single variable required: "
                    + variables);
        }
    }

    // Overrides.
    public Function differentiate(Variable v) {
        if (this.getOrder(v) > 0) {
            Operable result = null;
            for (Iterator i = this._terms.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                Term term = (Term) entry.getKey();
                Operable coef = (Operable) entry.getValue();
                int power = term.getPower(v);
                if (power > 0) {
                    Operable newCoef = multiply(coef, power);
                    Term newTerm = term.divide(Term.valueOf(v, 1));
                    Polynomial p = valueOf(newCoef, newTerm);
                    result = (result != null) ? result.plus(p) : p;
                }
            }
            return (Function) result;
        } else { // Returns zero.
            Operable coef = (Operable) _terms.values().iterator().next();
            return Constant.valueOf(coef.plus(coef.opposite()));
        }
    }

    private static Operable multiply(Operable o, int n) {
        if (n > 0) {
            Operable shift2 = o;
            Operable result = null;
            while (n >= 1) { // Iteration.
                if ((n & 1) == 1) {
                    result = (result == null) ? shift2 : result.plus(shift2);
                }
                shift2 = shift2.plus(shift2);
                n >>>= 1;
            }
            return result;
        } else {
            throw new IllegalArgumentException("n: " + n
                    + " zero or negative values not allowed");
        }
    }

    // Overrides.
    public Function integrate(Variable v) {
        Operable result = null;
        for (Iterator i = this._terms.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            Term term = (Term) entry.getKey();
            Operable coef = (Operable) entry.getValue();
            int power = term.getPower(v);
            Operable newCoef = multiply(coef.reciprocal(), power + 1)
                    .reciprocal();
            Term newTerm = term.multiply(Term.valueOf(v, 1));
            Polynomial p = valueOf(newCoef, newTerm);
            result = (result != null) ? result.plus(p) : p;
        }
        return (Function) result;
    }

    // Overrides.
    public void move(ContextSpace cs) {
        super.move(cs);
        _terms.move(cs);
    }

    /**
     * This class represents the term of a {@link Polynomial polynomial} 
     * such as <code>x·y²</code>. 
     * 
     * @see <a href="http://mathworld.wolfram.com/Term.html">
     *      Term -- from MathWorld</a> 
     */
    public static final class Term extends RealtimeObject implements
            Comparable, Serializable {

        /**
         * Holds the term factory.
         */
        private static final Factory TERM_FACTORY = new Factory() {

            public Object create() {
                return new Term();
            }
        };

        /**
         * Holds the term representing no variables.
         */
        public static final Term CONSTANT = new Term();

        /**
         * Holds the first variable.
         */
        private Variable _variable;

        /**
         * Holds the variable exponent.
         */
        private int _power;

        /**
         * Holds the next variables (ordered).
         */
        private Term _next;

        /**
         * Default constructor.
         */
        private Term() {
        }

        /**
         * Return the term corresponding to the specified variable raised to
         * the specified power.
         * 
         * @param v the variable.
         * @param n the power. 
         * @return the term for <code>v<sup>n</sup></code>
         * @throws IllegalArgumentException if <code>n &lt;= 0</code> 
         */
        public static Term valueOf(Variable v, int n) {
            if (n > 0) {
                Term term = (Term) TERM_FACTORY.object();
                term._variable = v;
                term._power = n;
                term._next = null;
                return term;
            } else {
                throw new IllegalArgumentException("n: " + n
                        + " zero or negative values are not allowed");
            }
        }

        /**
         * Return the power of the specified variable within this term.
         * 
         * @param v the variable.
         * @return the the power of v or <code>0</code> if v is not part of
         *         this term. 
         */
        public int getPower(Variable v) {
            for (Term t = this; t != null; t = t._next) {
                if (v == t._variable) {
                    return t._power;
                }
            }
            return 0;
        }

        /**
         * Return the product of this term with the one specified. 
         * 
         * @param that the term multiplier.
         * @return <code>this·that</code>
         */
        public Term multiply(Term that) {
            if (this == CONSTANT) {
                return that;
            } else if (that == CONSTANT) {
                return this;
            } else {
                return multiply(this, that);
            }
        }

        private static Term multiply(Term left, Term right) {
            if (left == null) {
                return right;
            } else if (right == null) {
                return left;
            } else {
                if (left._variable == right._variable) {
                    Term result = (Term) TERM_FACTORY.object();
                    result._variable = left._variable;
                    result._power = left._power + right._power;
                    result._next = multiply(left._next, right._next);
                    return result;
                } else if (left._variable.compareTo(right._variable) > 0) {
                    return multiply(right, left); //Swaps.
                } else {
                    Term result = (Term) TERM_FACTORY.object();
                    result._variable = left._variable;
                    result._power = left._power;
                    result._next = multiply(left._next, right);
                    return result;
                }
            }
        }

        /**
         * Return the division of this term with the one specified. 
         * 
         * @param that the term divisor.
         * @return <code>this/that</code>
         * @throws UnsupportedOperationException if this division would 
         *         result in negative power.
         */
        public Term divide(Term that) {
            if (that == CONSTANT) {
                return this;
            } else if (this == CONSTANT) {
                throw new UnsupportedOperationException("Cannot divide " + this
                        + " by " + that);
            } else {
                Term result = divide(this, that);
                return result != null ? result : CONSTANT;
            }
        }

        private static Term divide(Term left, Term right) {
            if (right == null) {
                return left;
            } else if (left == null) {
                throw new UnsupportedOperationException("Cannot divide " + left
                        + " by " + right);
            } else {
                if (left._variable == right._variable) {
                    int power = left._power - right._power;
                    if (power > 0) {
                        Term result = (Term) TERM_FACTORY.object();
                        result._variable = left._variable;
                        result._power = power;
                        result._next = divide(left._next, right._next);
                        return result;
                    } else if (power == 0) { // Cancels out, ignores.    
                        return divide(left._next, right._next);
                    } else {
                        throw new UnsupportedOperationException(
                                "Cannot divide " + left + " by " + right);
                    }
                } else if (left._variable.compareTo(right._variable) < 0) {
                    Term result = (Term) TERM_FACTORY.object();
                    result._variable = left._variable;
                    result._power = left._power;
                    result._next = divide(left._next, right);
                    return result;
                } else {
                    throw new UnsupportedOperationException("Cannot divide "
                            + left + " by " + right);
                }
            }
        }

        /**
         * Evaluates this term by replacing its {@link Function.Variable
         * variables} by their current (context-local) values.
         *
         * @return the evaluation of this term or <code>null</code> if constant
         *         term.
         * @throws FunctionException if any of this term's variable is not set.
         */
        public Operable evaluate() {
            if (this == CONSTANT) {
                return null;
            } else {
                Operable value = (Operable) _variable.getValue();
                if (value != null) {
                    Operable pow2 = value;
                    Operable result = null;
                    int n = _power;
                    while (n >= 1) { // Iteration.
                        if ((n & 1) == 1) {
                            result = (result == null) ? pow2 : result
                                    .times(pow2);
                        }
                        pow2 = pow2.times(pow2);
                        n >>>= 1;
                    }
                    return (_next == null) ? result : result.times(_next
                            .evaluate());
                } else {
                    throw new FunctionException("Variable: " + _variable
                            + " is undefined");
                }
            }
        }

        /**
         * Indicates if this term is equal to the object specified.
         *
         * @param  o the object to compare for equality.
         * @return <code>true</code> if this term and the specified object are
         *         considered equal; <code>false</code> otherwise.
         */
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if ((this == CONSTANT) || (o == CONSTANT)) {
                return false;
            } else if (o instanceof Term) {
                Term that = (Term) o;
                // Variable must be equal with same powers.
                if ((this._variable == that._variable)
                        && (this._power == that._power)) {
                    if (this._next != null) {
                        return this._next.equals(that._next);
                    } else if (that._next != null) {
                        return that._next.equals(null);
                    } else { // null == null
                        return true;
                    }
                } else { // Not the same variable or different powers.
                    return false;
                }
            } else {
                return false;
            }
        }

        /**
         * Returns a hash code for this term.
         *
         * @return a hash code value for this object.
         */
        public final int hashCode() {
            if (this == CONSTANT) {
                return 0;
            } else {
                int h = 0;
                for (Term t = this; t != null; t = t._next) {
                    h += t._variable.hashCode() * t._power;
                }
                return h;
            }
        }

        /**
         * Compares this term with the specified term for order.
         *
         * @param o the term to compare with.
         * @return a negative integer, zero, or a positive integer as this 
         *         term is less than, equal to, or greater than the
         *         specified term.
         * @throws ClassCastException if the specified object is not a term.
         */
        public int compareTo(Object o) {
            Term that = (Term) o;
            if (this == CONSTANT) {
                return (that == CONSTANT) ? 0 : -1;
            } else if (that == CONSTANT) {
                return 1;
            } else {
                // Compares variables then power.
                if (this._variable == that._variable) {
                    if (this._power == that._power) {
                        if (this._next == null) {
                            return -1;
                        } else if (that._next == null) {
                            return 1;
                        } else {
                            return this._next.compareTo(that._next);
                        }
                    } else { // Different powers.
                        return this._power - that._power;
                    }
                } else { // Different variables.
                    return this._variable.compareTo(that._variable);
                }
            }
        }

        /**
         * Returns the  text representation of this term.
         */
        public Text toText() {
            TextBuilder tb = TextBuilder.newInstance();
            if (this != CONSTANT) {
                for (Term t = this; t != null; t = t._next) {
                    tb.append(t._variable.getSymbol());
                    switch (t._power) {
                    case 1:
                        break;
                    case 2:
                        tb.append('²');
                        break;
                    case 3:
                        tb.append('³');
                        break;
                    default:
                        try {
                            TypeFormat.format(t._power, tb);
                        } catch (IOException ioError) {
                            throw new InternalError();
                        }
                    }
                }
            }
            return tb.toText();
        }

        // Overrides.
        public void move(ContextSpace cs) {
            super.move(cs);
            if (_next != null) {
                _next.move(cs);
            }
        }

        private static final long serialVersionUID = 5633349037145361607L;
    }

    private static final long serialVersionUID = -5133759075731011138L;
}