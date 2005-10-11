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

import javolution.realtime.LocalContext;
import javolution.realtime.RealtimeObject;
import javolution.util.FastSet;
import javolution.lang.Text;
import javolution.lang.TextBuilder;

import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This abstract class represents a mapping between two sets such that
 *     there is a unique element in the second set assigned to each element
 *     in the first set.</p>
 *     
 * <p> Functions can be discrete or continuous and multivariate functions 
 *     (functions with multiple variables) are also supported as illustrated 
 *     below:<pre>
 *         // f(x, y) =  x² + x·y + 1;
 *         Polynomial&lt;Rational> x = Polynomial.valueOf(Rational.ONE, Variable.X);
 *         Polynomial&lt;Rational> y = Polynomial.valueOf(Rational.ONE, Variable.Y);
 *         Constant&lt;Rational> one = Constant.valueOf(Rational.ONE);
 *         Polynomial&lt;Rational> fx_y = x.pow(2).plus(x.times(y)).plus(one);
 *         System.out.println("f(x,y) = " + fx_y);
 * 
 *         // Evaluates f(1,0);
 *         LocalContext.enter();
 *         try {
 *             Variable.X.set(Rational.ONE);
 *             Variable.Y.set(Rational.ZERO);
 *             Rational f1_0 = fx_y.evaluate();
 *             System.out.println("f(1,0) = " + f1_0);
 *         } finally {
 *             LocalContext.exit();
 *         }
 * 
 *         // Calculates df(x,y)/dx
 *         Polynomial&lt;Rational> dfx_y = fx_y.differentiate(Variable.X);
 *         System.out.println("df(x,y)/dx = " + dfx_y);
 *          
 *         > f(x,y) = [1/1]x² + [1/1]xy + [1/1]
 *         > f(1,0) = 2/1
 *         > df(x,y)/dx = [2/1]x + [1/1]y
 *     </pre></p>
 *     
 * <p> Functions are often given by formula (e.g. <code>f(x) = x²-x+1,
 *     f(x,y)= x·y</code>) but the general function instance might tabulate
 *     the values, solve an equation, etc.</p>
 *     
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
public abstract class Function<O extends Operable<O>> extends RealtimeObject 
        implements Operable<Function<O>>, Serializable {

    /**
     * Default constructor. 
     */
    protected Function() {
    }

    /**
     * Returns a set containing this function {@link Variable 
     * variables}.
     *
     * @return the variables for this function.
     */
    public abstract Set<Variable> getVariables();

    /**
     * Evaluates this function by replacing its {@link Variable 
     * variables} by their current (context-local) values.
     *
     * @return the evaluation of this function.
     * @throws FunctionException if any of this function's variable is not set.
     * @throws ClassCastException if the actual value of a variable is 
     *         incompatible with this function type.
     */
    public abstract O evaluate();

    /**
     * Returns the composition of this function with the one specified.
     *
     * @param  that the function for which the return value is passed as
     *         argument to this function.
     * @return the function <code>(this o that)</code>
     * @throws FunctionException if this function is not univariate.
     */
    public Function<O> compose(Function<O> that) {
        Set<Variable> variables = getVariables();
        if (variables.size() != 1)
            throw new FunctionException("This function is not univariate "
                    + variables);
            Compose<O> result = Compose.FACTORY.object();
            result._f = this;
            result._g = that;
            result._fv = variables.iterator().next();
            return result;
    }

    private static final class Compose<O extends Operable<O>> extends Function<O> {

        private static final long serialVersionUID = 1L;

        private static final Factory<Compose> FACTORY = new Factory<Compose>() {

            protected Compose create() {
                return new Compose();
            }
        };

        Function<O> _f, _g;

        Variable _fv;

        public Set<Variable> getVariables() {
            return _g.getVariables();
        }

        public O evaluate() {
            O o = _g.evaluate();
            LocalContext.enter();
            try {
                _fv.set(o);
                return _f.evaluate();
            } finally {
                LocalContext.exit();
            }
        }

        public Function<O> differentiate(Variable v) {
            Function<O> fd = _f.differentiate(v);
            Function<O> gd = _g.differentiate(v);
            return fd.compose(_g).times(gd); // Chain rule.
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

        public boolean move(ObjectSpace os) {
            if (super.move(os)) {
                _f.move(os);
                _g.move(os);
                return true;
            }
            return false;
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
    public Function<O> differentiate(Variable v) {
        Derivative<O> result = Derivative.FACTORY.object();
        result._f = this;
        result._v = v;
        return result;
    }

    private static final class Derivative<O extends Operable<O>> extends Function<O> {

        private static final long serialVersionUID = 1L;

        private static final Factory<Derivative> FACTORY 
            = new Factory<Derivative>() {

            protected Derivative create() {
                return new Derivative();
            }
        };

        Function<O> _f;

        Variable _v;

        public Set<Variable> getVariables() {
            return _f.getVariables();
        }

        public O evaluate() {
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

        public boolean move(ObjectSpace os) {
            if (super.move(os)) {
                _f.move(os);
                return true;
            }
            return false;
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
    public Function<O> integrate(Variable v) {
        Integral<O> result = Integral.FACTORY.object();
        result._f = this;
        result._v = v;
        return result;
    }

    private static final class Integral<O extends Operable<O>> extends Function<O> {

        private static final long serialVersionUID = 1L;

        private static final Factory<Integral> FACTORY 
            = new Factory<Integral>() {

            protected Integral create() {
                return new Integral();
            }
        };

        Function<O> _f;

        Variable _v;

        public Set<Variable> getVariables() {
            return _f.getVariables();
        }

        public O evaluate() {
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

        public boolean move(ObjectSpace os) {
            if (super.move(os)) {
                _f.move(os);
                return true;
            }
            return false;
        }
    }

    /**
     * Returns this function raised at the specified exponent.
     *
     * @param  n the exponent.
     * @return <code>this<sup>n</sup></code>
     * @throws IllegalArgumentException if <code>n &lt;= 0</code>
     */
    public Function<O> pow(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n: " + n
                    + " zero or negative values not allowed");
            Function<O> pow2 = this;
            Function<O> result = null;
            while (n >= 1) { // Iteration.
                if ((n & 1) == 1) {
                    result = (result == null) ? pow2 : result.times(pow2);
                }
                pow2 = pow2.times(pow2);
                n >>>= 1;
            }
            return result;
    }

    // Implements Operable.
    public Function<O> plus(Function<O> that) {
        Plus<O> result = Plus.FACTORY.object();
        result._f = this;
        result._g = that;
        return result;
    }

    private static final class Plus<O extends Operable<O>> extends Function<O> {

        private static final long serialVersionUID = 1L;

        private static final Factory<Plus> FACTORY = new Factory<Plus>() {

            protected Plus create() {
                return new Plus();
            }
        };

        Function<O> _f, _g;

        public Set<Variable> getVariables() {
            FastSet<Variable> variables = FastSet.newInstance();
            variables.addAll(_f.getVariables());
            variables.addAll(_g.getVariables());
            return variables;
        }

        public O evaluate() {
            return _f.evaluate().plus(_g.evaluate());
        }

        public Function<O> differentiate(Variable v) {
            Function<O> fd = _f.differentiate(v);
            Function<O> gd = _g.differentiate(v);
            return fd.plus(gd);
        }

        public Function<O> integrate(Variable v) {
            Function<O> _fi = _f.integrate(v);
            Function<O> _gi = _g.integrate(v);
            return _fi.plus(_gi);
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

        public boolean move(ObjectSpace os) {
            if (super.move(os)) {
                _f.move(os);
                _g.move(os);
                return true;
            }
            return false;
        }
    }

    // Implements Operable.
    public Function<O> opposite() {
        if (this instanceof Opposite) {
            return ((Opposite<O>) this)._f;
        } else {
            Opposite<O> result = Opposite.FACTORY.object();
            result._f = this;
            return result;
        }
    }

    private static final class Opposite<O extends Operable<O>> extends Function<O> {

        private static final long serialVersionUID = 1L;

        private static final Factory<Opposite> FACTORY = new Factory<Opposite>() {

            protected Opposite create() {
                return new Opposite();
            }
        };

        Function<O> _f;

        public Set<Variable> getVariables() {
            return _f.getVariables();
        }

        public O evaluate() {
            return _f.evaluate().opposite();
        }

        public Function<O> differentiate(Variable v) {
            Function<O> fd = _f.differentiate(v);
            return fd.opposite();
        }

        public Function<O> integrate(Variable v) {
            Function<O> _fi = _f.integrate(v);
            return _fi.opposite();
        }

        public Text toText() {
            TextBuilder tb = TextBuilder.newInstance();
            tb.append("-(");
            tb.append(_f);
            tb.append(')');
            return tb.toText();
        }

        public boolean move(ObjectSpace os) {
            if (super.move(os)) {
                _f.move(os);
                return true;
            }
            return false;
        }
    }

    // Implements Operable.
    public Function<O> times(Function<O> that) {
        Times<O> result = Times.FACTORY.object();
        result._f = this;
        result._g = that;
        return result;
    }

    private static final class Times<O extends Operable<O>> extends Function<O> {

        private static final long serialVersionUID = 1L;

        private static final Factory<Times> FACTORY = new Factory<Times>() {

            protected Times create() {
                return new Times();
            }
        };

        Function<O> _f, _g;

        public Set<Variable> getVariables() {
            FastSet<Variable> variables = FastSet.newInstance();
            variables.addAll(_f.getVariables());
            variables.addAll(_g.getVariables());
            return variables;
        }

        public O evaluate() {
            return _f.evaluate().times(_g.evaluate());
        }

        public Function<O> differentiate(Variable v) {
            Function<O> fd = _f.differentiate(v);
            Function<O> gd = _g.differentiate(v);
            return _f.times(gd).plus(fd.times(_g));
        }

        public Text toText() {
            TextBuilder tb = TextBuilder.newInstance();
            tb.append(_f);
            tb.append(')');
            if (_g instanceof Reciprocal) {
                tb.append("/");
                tb.append('(');
                tb.append(((Reciprocal<O>) _g)._f);
                tb.append(')');
            } else {
                tb.append('·');
                tb.append('(');
                tb.append(_g);
                tb.append(')');
            }
            return tb.toText();
        }

        public boolean move(ObjectSpace os) {
            if (super.move(os)) {
                _f.move(os);
                _g.move(os);
                return true;
            }
            return false;
        }
    }

    // Implements Operable.
    public Function<O> reciprocal() {
        if (this instanceof Reciprocal) {
            return ((Reciprocal<O>) this)._f;
        } else {
            Reciprocal<O> result = Reciprocal.FACTORY.object();
            result._f = this;
            return result;
        }
    }

    private static final class Reciprocal<O extends Operable<O>> extends Function<O> {

        private static final long serialVersionUID = 1L;

        private static final Factory<Reciprocal> FACTORY = new Factory<Reciprocal>() {

            protected Reciprocal create() {
                return new Reciprocal();
            }
        };

        Function<O> _f;

        public Set<Variable> getVariables() {
            return _f.getVariables();
        }

        public O evaluate() {
            return _f.evaluate().reciprocal();
        }

        public Function<O> differentiate(Variable v) {
            Function<O> fd = _f.differentiate(v);
            return fd.opposite().times(_f.times(_f).reciprocal());
        }

        public Text toText() {
            TextBuilder tb = TextBuilder.newInstance();
            tb.append("1/(");
            tb.append(_f);
            tb.append(')');
            return tb.toText();
        }

        public boolean move(ObjectSpace os) {
            if (super.move(os)) {
                _f.move(os);
                return true;
            }
            return false;
        }
    }
    
}