/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.functions;

import java.io.Serializable;
import java.util.List;
import org.jscience.mathematics.structures.Ring;
import javolution.lang.MathLib;
import javolution.text.Text;
import javolution.text.TextBuilder;
import javolution.context.RealtimeObject;
import javolution.util.FastTable;

/**
 * This class represents the term of a {@link Polynomial polynomial} 
 * such as <code>x·y²</code>. 
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
public final class Term extends RealtimeObject 
     implements Serializable, Comparable<Term> {

    /**
     * Holds the multiplicative identity.
     */
    public static Term ONE = new Term();
    
    /**
     * Holds the variables (ordered).
     */
    private FastTable<Variable> _variables = new FastTable<Variable>();

    /**
     * Holds the corresponding powers (positive and different from zero).
     */
    private int[] _powers = new int[4];

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
     * @throws IllegalArgumentException if <code>n &lt; 0</code> 
     */
    public static Term valueOf(Variable v, int n) {
        if (n == 0) return ONE;
        if (n < 0)
            throw new IllegalArgumentException("n: " + n
                    + " negative values are not allowed");
        Term term = Term.newInstance(1);
        term._variables.add(v);
        term._powers[0] = n;
        return term;
    }

    /**
     * Returns the variables for this term (lexically ordered).
     * 
     * @return this term variables.
     */
    public List<Variable> getVariables() {
        return _variables.unmodifiable();
    }

    /**
     * Returns the power of the specified variable.
     * 
     * @param var the variable for which the power is returned.
     * @return the power of the corresponding variable or <code>0</code> if 
     *         this term does not hold the specified variable.
     * @throws IllegalArgumentException if <code>(i < 0) || (i >= length())</code> 
     */
    public int getPower(Variable var) {
        int i = _variables.indexOf(var);
        return (i < 0) ? 0 : _powers[i];
    }

    /**
     * Return the product of this term with the one specified. 
     * 
     * @param that the term multiplier.
     * @return <code>this · that</code>
     * @throws IllegalArgumentException if the specified term holds a 
     *         variable having the same symbol as one of the variable of
     *         this term; but both variables are distinct.
     */
    public Term times(Term that) {
        final int thisSize = this._variables.size();
        final int thatSize = that._variables.size();
        Term result = Term.newInstance(thisSize + thatSize);
        for (int i=0, j =0;;) {
            Variable left = (i < thisSize) ? this._variables.get(i) : null;
            Variable right = (j < thatSize) ? that._variables.get(j) : null;
            if (left == null) {
                if (right == null) return result;
                result._powers[result._variables.size()] = that._powers[j++];
                result._variables.add(right);
                continue;
            }
            if (right == null) {
                result._powers[result._variables.size()] = this._powers[i++];
                result._variables.add(left);
                continue;
            }
            if (right == left) {
                result._powers[result._variables.size()] 
                               = this._powers[i++] + that._powers[j++];
                result._variables.add(right);
                continue;
            }
            final int cmp = left.getSymbol().compareTo(right.getSymbol());
            if (cmp <  0) {
                result._powers[result._variables.size()] = this._powers[i++];
                result._variables.add(left);
            } else if (cmp > 0) {
                result._powers[result._variables.size()] = that._powers[j++];
                result._variables.add(right);
            } else {
                throw new IllegalArgumentException(
                        "Found distinct variables with same symbol: " 
                            + left.getSymbol());
            }
        }
    }

    /**
     * Return the division of this term with the one specified. 
     * 
     * @param that the term divisor.
     * @return <code>this / that</code>
     * @throws UnsupportedOperationException if this division would 
     *         result in negative power.
     * @throws IllegalArgumentException if the specified term holds a 
     *         variable having the same symbol as one of the variable of
     *         this term; but both variables are distinct.
     */
    public Term divide(Term that) {
        final int thisSize = this._variables.size();
        final int thatSize = that._variables.size();
        Term result = Term.newInstance(MathLib.max(thisSize, thatSize));
        for (int i=0, j =0;;) {
            Variable left = (i < thisSize) ? this._variables.get(i) : null;
            Variable right = (j < thatSize) ? that._variables.get(j) : null;
            if (left == null) {
                if (right == null) return result;
                throw new UnsupportedOperationException(this + "/" + that + 
                        " would result in a negative power");
            }
            if (right == null) {
                result._powers[result._variables.size()] = this._powers[i++];
                result._variables.add(left);
                continue;
            }
            if (right == left) {
                final int power = this._powers[i++] - that._powers[j++];
                if (power < 0)
                    throw new UnsupportedOperationException(this + "/" + that + 
                    " would result in a negative power");
                if (power > 0) {
                    result._powers[result._variables.size()] = power;
                    result._variables.add(right);
                }
                continue;
            }
            final int cmp = left.getSymbol().compareTo(right.getSymbol());
            if (cmp <  0) {
                result._powers[result._variables.size()] = this._powers[i++];
                result._variables.add(left);
            } else if (cmp > 0) {
                throw new UnsupportedOperationException(this + "/" + that + 
                      " would result in a negative power");
            } else {
                throw new IllegalArgumentException(
                        "Found distinct variables with same symbol: " 
                            + left.getSymbol());
            }
        }
    }

    /**
     * Indicates if this term is equal to the object specified.
     *
     * @param  obj the object to compare for equality.
     * @return <code>true</code> if this term and the specified object are
     *         considered equal; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Term)) return false;
        Term that = (Term) obj;
        if (!this._variables.equals(that._variables)) return false;
        final int size = this._variables.size();
        for (int i=0; i < size; i++) {
            if (this._powers[i] != that._powers[i])
                return false;
        }
        return true;
    }

    /**
     * Returns a hash code for this term.
     *
     * @return a hash code value for this object.
     */
    public final int hashCode() {
        int h = 0;
        final int size = this._variables.size();
        for (int i=0; i < size; i++) {
            h += _variables.get(i).hashCode() * _powers[i];
        }
        return h;
    }

    /**
     * Returns the text representation of this term.
     */
    public Text toText() {
        TextBuilder tb = TextBuilder.newInstance();
        final int size = this._variables.size();
        for (int i=0; i < size; i++) {
            tb.append(_variables.get(i).getSymbol());
            int power = _powers[i];
            if (power > 1) {
                tb.append('^');
                tb.append(_powers[i]);
            }
        }
        return tb.toText();
    }

    /**
     * Compares this term with the one specified for order.
     * 
     * @param that the term to be compared to.
     * @return a negative integer, zero, or a positive integer as this term
     *         is less than, equal to, or greater than the specified term.
     */
    public int compareTo(Term that) {
        int n = Math.min(this._variables.size(), that._variables.size());
        for (int i=0; i < n; i++) {
            int cmp = this._variables.get(i).getSymbol().compareTo(
                    that._variables.get(i).getSymbol());
            if (cmp != 0) return cmp;
            cmp = that._powers[i] - this._powers[i];
            if (cmp != 0) return cmp;
        }
        return that._variables.size() - this._variables.size();
    }

    /**
     * Evaluates this term by replacing its {@link Variable
     * variables} by their current (context-local) values.
     *
     * @return the evaluation of this term or <code>null</code> if ONE.
     * @throws FunctionException if any of this term's variable is not set.
     */
    @SuppressWarnings("unchecked") 
    Ring evaluate() {
        Ring result = null;
        final int size = this._variables.size();
        for (int i=0; i < size; i++) {
            Ring pow2 = (Ring)_variables.get(i).get();
            if (pow2 == null)
                throw new FunctionException("Variable: " + _variables.get(i)
                        + " is not set");
            int n = _powers[i];
            while (n >= 1) { // Iteration.
                if ((n & 1) == 1) {
                    result = (result == null) ? pow2 : 
                        (Ring) result.times(pow2);
                }
                pow2 = (Ring) pow2.times(pow2);
                n >>>= 1;
            }
        }
        return result;
    }

    /**
     * Returns a new instance of specified capacity.
     * 
     * @param capacity the minimum number of variables.
     * @return the corresponding instance.
     */
    private static Term newInstance(int capacity) {
        Term term = FACTORY.object();
        if (term._powers.length < capacity) {
            term._powers = new int[capacity];
        }
        return term;
    }
    
    private static final Factory<Term> FACTORY = new Factory<Term>() {

        protected Term create() {
            return new Term();
        }
        protected void cleanup(Term term) {
            term._variables.reset();
        }
    };
    
    private static final long serialVersionUID = 1L;
}