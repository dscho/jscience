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


import java.io.Serializable;
import java.util.Set;

import javolution.realtime.LocalContext;
import javolution.realtime.RealtimeObject;
import javolution.util.FastMap;
import javolution.util.FastSet;
import javolution.lang.Text;
import javolution.lang.TextBuilder;

import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This abstract class represents a mapping between two sets such that
 *     there is a unique element in the second set assigned to each element
 *     in the first set. </p>
 * <p> Functions can be discrete or continuous and multivariate functions 
 *     (functions with multiple variables) are also supported as illustrated 
 *     below:<pre>
 *         // f(x, y) =  x² + x·y + 1;
 *         Polynomial x = Polynomial.valueOf(Rational.ONE, Variable.X);
 *         Polynomial y = Polynomial.valueOf(Rational.ONE, Variable.Y);
 *         Constant one = Constant.valueOf(Rational.ONE);
 *         Polynomial fx_y = (Polynomial) x.pow(2).plus(x.times(y)).plus(one);
 *         System.out.println("f(x,y) = " + fx_y);
 * 
 *         // Evaluates f(1,0);
 *         LocalContext.enter();
 *         try {
 *             Variable.X.setValue(Rational.ONE);
 *             Variable.Y.setValue(Rational.ZERO);
 *             Rational f1_0 = (Rational) fx_y.evaluate();
 *             System.out.println("f(1,0) = " + f1_0);
 *         } finally {
 *             LocalContext.exit();
 *         }
 * 
 *         // Calculates df(x,y)/dx
 *         Polynomial dfx_y = (Polynomial) fx_y.differentiate(Variable.X);
 *         System.out.println("df(x,y)/dx = " + dfx_y);
 *          
 *         > f(x,y) = [1/1]x² + [1/1]xy + [1/1]
 *         > f(1,0) = 2/1
 *         > df(x,y)/dx = [2/1]x + [1/1]y
 *     </pre></p>
 * <p> Functions are often given by formula (e.g. <code>f(x) = x²-x+1,
 *     f(x,y)= x·y</code>) but the general function instance might tabulate
 *     the values, solve an equation, etc.</p>
 * <p> Finally, {@link Function} instances are immutable and {@link Operable}
 *     with componentwise multiplication
 *     (<a href="http://www.cut-the-knot.com/do_you_know/mul_func.shtml">
 *     <i>(fg)(x) = f(x)g(x)</i></a>).</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 * @see <a href="http://mathworld.wolfram.com/Function.html">
 *      Function -- from MathWorld</a> 
 */
public abstract class Function extends RealtimeObject implements Operable,
        Serializable {

    /**
     * Default constructor. 
     */
    protected Function() {
    }

    /**
     * Returns a set containing this function {@link Function.Variable 
     * variables}.
     *
     * @return the variables for this function.
     */
    public abstract Set getVariables();

    /**
     * Evaluates this function by replacing its {@link Function.Variable 
     * variables} by their current (context-local) values.
     *
     * @return the evaluation of this function.
     * @throws FunctionException if any of this function's variable is not set.
     * @throws ClassCastException if the actual value of a variable is 
     *         incompatible with this function type.
     */
    public abstract Operable evaluate();

    /**
     * Returns the composition of this function with the one specified.
     *
     * @param  that the function for which the return value is passed as
     *         argument to this function.
     * @return the function <code>(this o that)</code>
     * @throws FunctionException if this function is not univariate.
     */
    public Function compose(Function that) {
        Set variables = getVariables();
        if (variables.size() == 1) {
            Compose result = (Compose) Compose.FACTORY.object();
            result._f = this;
            result._g = that;
            result._fv = (Variable) variables.iterator().next();
            return result;
        } else {
            throw new FunctionException("This function is not univariate "
                    + variables);
        }
    }

    private static final class Compose extends Function {

        private static final long serialVersionUID = -3907019647877829070L;

        private static final Factory FACTORY = new Factory() {

            public Object create() {
                return new Compose();
            }
        };

        Function _f, _g;

        Variable _fv;

        public Set getVariables() {
            return _g.getVariables();
        }

        public Operable evaluate() {
            Operable o = _g.evaluate();
            LocalContext.enter();
            try {
                _fv.setValue(o);
                return _f.evaluate();
            } finally {
                LocalContext.exit();
            }
        }

        public Function differentiate(Variable v) {
            Function fd = _f.differentiate(v);
            Function gd = _g.differentiate(v);
            return (Function) fd.compose(_g).times(gd); // Chain rule.
        }

        public Text toText() {
            TextBuilder tb = TextBuilder.newInstance();
            tb.append('(');
            tb.append(_f);
            tb.append(" o ");
            tb.append(_g);
            tb.append(')');
            return tb.toText();
        }

        public void move(ContextSpace cs) {
            super.move(cs);
            _f.move(cs);
            _g.move(cs);
        }
    }

    /**
     * Returns the first derivative of this function with respect to 
     * the specified variable. 
     * 
     * @param  v the variable for which the derivative is calculated.
     * @return <code>d[this]/dv</code>
     * @see <a href="http://mathworld.wolfram.com/Derivative.html">
     *      Derivative -- from MathWorld</a>
     * @throws FunctionException if the derivative is undefined.
     */
    public Function differentiate(Variable v) {
        Derivative result = (Derivative) Derivative.FACTORY.object();
        result._f = this;
        result._v = v;
        return result;
    }

    private static final class Derivative extends Function {

        private static final long serialVersionUID = -2123966512020326147L;

        private static final Factory FACTORY = new Factory() {

            public Object create() {
                return new Derivative();
            }
        };

        Function _f;

        Variable _v;

        public Set getVariables() {
            return _f.getVariables();
        }

        public Operable evaluate() {
            throw new FunctionException("Derivative of " + _f + " undefined");
        }

        public Text toText() {
            TextBuilder tb = TextBuilder.newInstance();
            tb.append("d[");
            tb.append(_f);
            tb.append("]/d");
            tb.append(_v.getSymbol());
            return tb.toText();
        }

        public void move(ContextSpace cs) {
            super.move(cs);
            _f.move(cs);
        }
    }

    /**
     * Returns an integral of this function with respect to 
     * the specified variable. 
     * 
     * @param  v the variable for which the integral is calculated.
     * @return <code>S[this·dv]</code>
     * @see <a href="http://mathworld.wolfram.com/Integral.html">
     *      Integral -- from MathWorld</a>
     */
    public Function integrate(Variable v) {
        Integral result = (Integral) Integral.FACTORY.object();
        result._f = this;
        result._v = v;
        return result;
    }

    private static final class Integral extends Function {

        private static final long serialVersionUID = 5844280302911716276L;

        private static final Factory FACTORY = new Factory() {

            public Object create() {
                return new Integral();
            }
        };

        Function _f;

        Variable _v;

        public Set getVariables() {
            return _f.getVariables();
        }

        public Operable evaluate() {
            throw new FunctionException("Integral of " + _f + " undefined");
        }

        public Text toText() {
            TextBuilder tb = TextBuilder.newInstance();
            tb.append("S[");
            tb.append(_f);
            tb.append("·d");
            tb.append(_v.getSymbol());
            tb.append(']');
            return tb.toText();
        }

        public void move(ContextSpace cs) {
            super.move(cs);
            _f.move(cs);
        }
    }

    /**
     * Returns this function raised at the specified exponent.
     *
     * @param  n the exponent.
     * @return <code>this<sup>n</sup></code>
     * @throws IllegalArgumentException if <code>n &lt;= 0</code>
     */
    public Function pow(int n) {
        if (n > 0) {
            Function pow2 = this;
            Function result = null;
            while (n >= 1) { // Iteration.
                if ((n & 1) == 1) {
                    result = (result == null) ? pow2 : (Function) result
                            .times(pow2);
                }
                pow2 = (Function) pow2.times(pow2);
                n >>>= 1;
            }
            return result;
        } else {
            throw new IllegalArgumentException("n: " + n
                    + " zero or negative values not allowed");
        }
    }

    // Implements Operable.
    public Operable plus(Operable that) {
        Plus result = (Plus) Plus.FACTORY.object();
        result._f = this;
        result._g = (Function) that;
        return result;
    }

    private static final class Plus extends Function {

        private static final long serialVersionUID = 2620456140022752028L;

        private static final Factory FACTORY = new Factory() {

            public Object create() {
                return new Plus();
            }
        };

        Function _f, _g;

        public Set getVariables() {
            Set fVars = _f.getVariables();
            Set gVars = _g.getVariables();
            FastSet variables = FastSet
                    .newInstance(fVars.size() + gVars.size());
            variables.addAll(fVars);
            variables.addAll(gVars);
            return variables;
        }

        public Operable evaluate() {
            return _f.evaluate().plus(_g.evaluate());
        }

        public Function differentiate(Variable v) {
            Function fd = _f.differentiate(v);
            Function gd = _g.differentiate(v);
            return (Function) fd.plus(gd);
        }

        public Function integrate(Variable v) {
            Function _fi = _f.integrate(v);
            Function _gi = _g.integrate(v);
            return (Function) _fi.plus(_gi);
        }

        public Text toText() {
            TextBuilder tb = TextBuilder.newInstance();
            tb.append(_f);
            if (_g instanceof Opposite) {
                tb.append(_g);
            } else {
                tb.append('+');
                tb.append(_g);
            }
            return tb.toText();
        }

        public void move(ContextSpace cs) {
            super.move(cs);
            _f.move(cs);
            _g.move(cs);
        }
    }

    // Implements Operable.
    public Operable opposite() {
        if (this instanceof Opposite) {
            return ((Opposite) this)._f;
        } else {
            Opposite result = (Opposite) Opposite.FACTORY.object();
            result._f = this;
            return result;
        }
    }

    private static final class Opposite extends Function {

        private static final long serialVersionUID = -2850948576764177796L;

        private static final Factory FACTORY = new Factory() {

            public Object create() {
                return new Opposite();
            }
        };

        Function _f;

        public Set getVariables() {
            return _f.getVariables();
        }

        public Operable evaluate() {
            return _f.evaluate().opposite();
        }

        public Function differentiate(Variable v) {
            Function fd = _f.differentiate(v);
            return (Function) fd.opposite();
        }

        public Function integrate(Variable v) {
            Function _fi = _f.integrate(v);
            return (Function) _fi.opposite();
        }

        public Text toText() {
            TextBuilder tb = TextBuilder.newInstance();
            tb.append("-(");
            tb.append(_f);
            tb.append(')');
            return tb.toText();
        }

        public void move(ContextSpace cs) {
            super.move(cs);
            _f.move(cs);
        }
    }

    // Implements Operable.
    public Operable times(Operable that) {
        Times result = (Times) Times.FACTORY.object();
        result._f = this;
        result._g = (Function) that;
        return result;
    }

    private static final class Times extends Function {

        private static final long serialVersionUID = 6464378319761697689L;

        private static final Factory FACTORY = new Factory() {

            public Object create() {
                return new Times();
            }
        };

        Function _f, _g;

        public Set getVariables() {
            Set fVars = _f.getVariables();
            Set gVars = _g.getVariables();
            FastSet variables = FastSet
                    .newInstance(fVars.size() + gVars.size());
            variables.addAll(fVars);
            variables.addAll(gVars);
            return variables;
        }

        public Operable evaluate() {
            return _f.evaluate().times(_g.evaluate());
        }

        public Function differentiate(Variable v) {
            Function fd = _f.differentiate(v);
            Function gd = _g.differentiate(v);
            return (Function) _f.times(gd).plus(fd.times(_g));
        }

        public Text toText() {
            TextBuilder tb = TextBuilder.newInstance();
            tb.append(_f);
            tb.append(')');
            if (_g instanceof Reciprocal) {
                tb.append("/");
                tb.append('(');
                tb.append(((Reciprocal) _g)._f);
                tb.append(')');
            } else {
                tb.append('·');
                tb.append('(');
                tb.append(_g);
                tb.append(')');
            }
            return tb.toText();
        }

        public void move(ContextSpace cs) {
            super.move(cs);
            _f.move(cs);
            _g.move(cs);
        }
    }

    // Implements Operable.
    public Operable reciprocal() {
        if (this instanceof Reciprocal) {
            return ((Reciprocal) this)._f;
        } else {
            Reciprocal result = (Reciprocal) Reciprocal.FACTORY.object();
            result._f = this;
            return result;
        }
    }

    private static final class Reciprocal extends Function {

        private static final long serialVersionUID = 975634017276018495L;

        private static final Factory FACTORY = new Factory() {

            public Object create() {
                return new Reciprocal();
            }
        };

        Function _f;

        public Set getVariables() {
            return _f.getVariables();
        }

        public Operable evaluate() {
            return _f.evaluate().reciprocal();
        }

        public Function differentiate(Variable v) {
            Function fd = _f.differentiate(v);
            return (Function) fd.opposite().times(_f.times(_f).reciprocal());
        }

        public Text toText() {
            TextBuilder tb = TextBuilder.newInstance();
            tb.append("1/(");
            tb.append(_f);
            tb.append(')');
            return tb.toText();
        }

        public void move(ContextSpace cs) {
            super.move(cs);
            _f.move(cs);
        }
    }

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
     *         Variable.X.setValue(Complex.valueOf(1, 2));
     *         Complex result = (Complex) fx.evaluate();
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
     * @see  <a href="http://mathworld.wolfram.com/Variable.html">
     *       Variable -- from MathWorld</a>
     * @see  Function#evaluate 
     */
    public static final class Variable extends LocalContext.Variable implements
            Comparable {

        /**
         * Holds existing variables.
         */
        private static FastMap _Instances = new FastMap();

        /**
         * The variable with symbol <code>"x"</code>.
         */
        public static final Variable X = valueOf("x");

        /**
         * The variable with symbol <code>"y"</code>.
         */
        public static final Variable Y = valueOf("y");

        /**
         * The variable with symbol <code>"z"</code>.
         */
        public static final Variable Z = valueOf("z");

        /**
         * The variable with symbol <code>"t"</code>.
         */
        public static final Variable T = valueOf("t");

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
        public static Variable valueOf(String symbol) {
            synchronized (_Instances) {
                if (symbol.length() > 0) {
                    Variable v = (Variable) _Instances.get(symbol);
                    if (v == null) {
                        v = new Variable(symbol);
                        _Instances.put(symbol, v);
                    }
                    return v;
                } else {
                    throw new IllegalArgumentException(
                            "Empty string symbol not allowed");
                }
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
        public int compareTo(Object that) {
            return this._symbol.compareTo(((Variable) that)._symbol);
        }

        /**
         * Returns the string representation of this variable.
         * 
         * @return this variable's symbol.
         */
        public String toString() {
            return _symbol;
        }
    }
}