/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.functions;

import javolution.realtime.LocalReference;
import javolution.util.FastMap;

import org.jscience.mathematics.matrices.Operable;

/**
 * This class represents a symbol on whose value a {@link Function}
 * depends. The actual value can be context-local. For example:<pre>
 * 
 *     // f(x) = x² + x + 1, calculate f(1+2i)
 *     Polynomial x = Polynomial.valueOf(Complex.ONE, Variable.X);
 *     Constant one = Constant.valueOf(Complex.ONE);
 *     Polynomial fx = (Polynomial) x.pow(2).plus(x).plus(one);
 *     LocalContext.enter();
 *     try {
 *         Variable.X.set(Complex.valueOf(1, 2));
 *         Complex result = fx.evaluate();
 *         System.out.println("f(1+2i) = " + result);
 *     } finally {
 *         LocalContext.exit();
 *     }
 * 
 *     > f(1+2i) = -1.0 + 6.0i 
 * </pre></p>
 * Instances of this class are unique (the implementation guarantees
 * that all instances have a different symbol).
 *  
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 * @see  <a href="http://mathworld.wolfram.com/Variable.html">
 *       Variable -- from MathWorld</a>
 * @see  Function#evaluate 
 */
public final class Variable extends LocalReference<Operable> implements
        Comparable<Variable> {

    /**
     * Holds existing variables.
     */
    private static FastMap<String, Variable> Collection = new FastMap<String, Variable>();

    /**
     * The variable with symbol <code>"x"</code>.
     */
    public static final Variable X = Variable.getInstance("x");

    /**
     * The variable with symbol <code>"y"</code>.
     */
    public static final Variable Y = Variable.getInstance("y");

    /**
     * The variable with symbol <code>"z"</code>.
     */
    public static final Variable Z = Variable.getInstance("z");

    /**
     * The variable with symbol <code>"t"</code>.
     */
    public static final Variable T = Variable.getInstance("t");

    /**
     * Holds the variable symbol.
     */
    private final String _symbol;

    /**
     * Creates a variable with the specified symbol.
     * 
     * @param symbol the symbol.
     */
    private Variable(String symbol) {
        _symbol = symbol;
    }

    /**
     * Returns the variable having the specified symbol.
     * 
     * @param symbol the symbol of the variable to return.
     * @return an existing or a new variable.
     * @throws IllegalArgumentException if <code>symbol.length() == 0</code>
     */
    public static Variable getInstance(String symbol) {
        if (symbol.length() == 0)
            throw new IllegalArgumentException(
                    "Empty string symbol not allowed");
        synchronized (Variable.Collection) {
            Variable v = Variable.Collection.get(symbol);
            if (v != null)
                return v;
            v = new Variable(symbol);
            Variable.Collection.put(symbol, v);
            return v;
        }
    }

    /**
     * Returns the symbol for this variable.
     * 
     * @return this variable's symbol.
     */
    public String getSymbol() {
        return _symbol;
    }

    /**
     * Compares this variable with the specified variable for order.
     *
     * @param that the variable to compare with.
     * @return a negative integer, zero, or a positive integer as this 
     *         variable is less than, equal to, or greater than the
     *         specified variable.
     * @throws ClassCastException if the specified object is not a variable.
     */
    public int compareTo(Variable that) {
        return this._symbol.compareTo(that._symbol);
    }

    /**
     * Returns the string representation of this variable.
     * 
     * @return this variable's symbol.
     */
    public String toString() {
        return _symbol;
    }

    /**
     * Overrides <code>readResolve()</code> to ensure that deserialization
     * maintains variable's unicity.
     */
    protected Object readResolve() {
        return getInstance(_symbol);
    }

    private static final long serialVersionUID = 1L;
}