/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.functions;

import java.io.Serializable;
import java.util.Set;

import javolution.lang.Text;
import javolution.lang.TextBuilder;
import javolution.realtime.RealtimeObject;
import javolution.util.FastSet;

import org.jscience.mathematics.matrices.Operable;

/**
 * This class represents the term of a {@link Polynomial polynomial} 
 * such as <code>x·y²</code>. 
 * 
 * @see <a href="http://mathworld.wolfram.com/Term.html">
 *      Term -- from MathWorld</a> 
 */
public final class Term extends RealtimeObject implements Comparable<Term>,
        Serializable {

    /**
     * Holds the term factory.
     */
    private static final Factory<Term> TERM_FACTORY = new Factory<Term>() {

        protected Term create() {
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
        if (n <= 0)
            throw new IllegalArgumentException("n: " + n
                    + " zero or negative values are not allowed");
        Term term = TERM_FACTORY.object();
        term._variable = v;
        term._power = n;
        term._next = null;
        return term;
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
    public Term times(Term that) {
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
                Term result = TERM_FACTORY.object();
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
                    throw new UnsupportedOperationException("Cannot divide "
                            + left + " by " + right);
                }
            } else if (left._variable.compareTo(right._variable) < 0) {
                Term result = (Term) TERM_FACTORY.object();
                result._variable = left._variable;
                result._power = left._power;
                result._next = divide(left._next, right);
                return result;
            } else {
                throw new UnsupportedOperationException("Cannot divide " + left
                        + " by " + right);
            }
        }
    }

    /**
     * Evaluates this term by replacing its {@link Variable
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
            Operable value = _variable.get();
            if (value != null) {
                Operable pow2 = value;
                Operable result = null;
                int n = _power;
                while (n >= 1) { // Iteration.
                    if ((n & 1) == 1) {
                        result = (result == null) ? pow2 : result.times(pow2);
                    }
                    pow2 = pow2.times(pow2);
                    n >>>= 1;
                }
                return (_next == null) ? result : result
                        .times(_next.evaluate());
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
     * @param that the term to compare with.
     * @return a negative integer, zero, or a positive integer as this 
     *         term is less than, equal to, or greater than the
     *         specified term.
     * @throws ClassCastException if the specified object is not a term.
     */
    public int compareTo(Term that) {
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
     * Returns a set containing this term {@link Variable variables}.
     *
     * @return the variables for this term.
     */
    public Set<Variable> getVariables() {
        FastSet<Variable> variables = FastSet.newInstance();
        for (Term t = this; t != null; t = t._next) {
            if (t != Term.CONSTANT) {
                variables.add(t._variable);
            }
        }
        return variables;
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
                    tb.append(t._power);
                }
            }
        }
        return tb.toText();
    }

    // Overrides.
    public boolean move(ObjectSpace os) {
        if (super.move(os)) {
            if (_next != null) {
                _next.move(os);
            }
            return true;
        }
        return false;
    }

    private static final long serialVersionUID = 1L;
}