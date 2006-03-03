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
import org.jscience.mathematics.structures.Ring;

import javolution.realtime.RealtimeObject;
import javolution.util.FastSet;
import javolution.lang.Immutable;
import javolution.lang.Text;
import javolution.lang.TextBuilder;

/**
 * <p> This abstract class represents a mapping between two sets such that
 *     there is a unique element in the second set assigned to each element
 *     in the first set.</p>
 *     
 * <p> Functions can be discrete or continuous and multivariate functions 
 *     (functions with multiple variables) are also supported as illustrated 
 *     below:[code]
 *         // Defines local variables.
 *         Variable.Local<Rational> varX = new Variable.Local<Rational>("x");
 *         Variable.Local<Rational> varY = new Variable.Local<Rational>("y");
 *         
 *         // f(x, y) =  x² + x·y + 1;
 *         Polynomial<Rational> x = Polynomial.valueOf(Rational.ONE, varX);
 *         Polynomial<Rational> y = Polynomial.valueOf(Rational.ONE, varY);
 *         Polynomial<Rational> fx_y = x.pow(2).plus(x.times(y)).plus(Rational.ONE);
 *         System.out.println("f(x,y) = " + fx_y);
 * 
 *         // Evaluates f(1,0);
 *         varX.set(Rational.ONE);
 *         varY.set(Rational.ZERO);
 *         System.out.println("f(1,0) = " + fx_y.evaluate());
 * 
 *         // Calculates df(x,y)/dx
 *         System.out.println("df(x,y)/dx = " + fx_y.differentiate(varX););
 *          
 *         > f(x,y) = [1/1]x² + [1/1]xy + [1/1]
 *         > f(1,0) = 2/1
 *         > df(x,y)/dx = [2/1]x + [1/1]y
 *     [/code]</p>
 *     
 * <p> Functions are often given by formula (e.g. <code>f(x) = x²-x+1,
 *     f(x,y)= x·y</code>) but the general function instance might tabulate
 *     the values, solve an equation, etc.</p>
 *     
 * <p> {@link Function} instances are immutable and form a ring
 *     with componentwise multiplication (<i>(f·g)(x) = f(x)·g(x)</i>).
 *     The inputs of a function can be about anything, but its outputs 
 *     must form a ring (identified by the parameter Y of this class).</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 * @see <a href="http://en.wikipedia.org/wiki/Function_%28mathematics%29">
 *      Wikipedia: Functions (mathematics)</a>
 */
public abstract class Function<X, Y extends Ring> extends RealtimeObject 
        implements Ring<Function<X, Y>>, Immutable {

    /**
     * Default constructor. 
     */
    protected Function() {
    }

    /**
     * Returns the variables (or arguments) of this function.
     *
     * @return the variables for this function.
     */
    public abstract Set<Variable<X>> getVariables();

    /**
     * Evaluates this function using its {@link Variable variables} current
     * values.
     *
     * @return the evaluation of this function.
     * @throws FunctionException if any of this function's variable is not set.
     */
    public abstract Y evaluate();
    
    /**
     * Returns the composition of this function with the one specified.
     *
     * @param  that the function for which the return value is passed as
     *         argument to this function.
     * @return the function <code>(this o that)</code>
     * @throws FunctionException if this function is not univariate.
     */
    public <Z> Function<Z, Y> compose(Function<Z, ?> that) {
        Set<Variable<X>> variables = getVariables();
        if (variables.size() != 1)
            throw new FunctionException("This function is not univariate "
                    + variables);
            Compose result = Compose.FACTORY.object();
            result._f = this;
            result._g = that;
            result._fv = variables.iterator().next();
            return result;
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
    public Function<X, Y> differentiate(Variable v) {
        Derivative result = Derivative.FACTORY.object();
        result._f = this;
        result._v = v;
        return result;
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
    public Function<X, Y> integrate(Variable v) {
        Integral result = Integral.FACTORY.object();
        result._f = this;
        result._v = v;
        return result;
    }

    /**
     * Returns this function raised at the specified exponent.
     *
     * @param  n the exponent.
     * @return <code>this<sup>n</sup></code>
     * @throws IllegalArgumentException if <code>n &lt;= 0</code>
     */
    public Function<X, Y> pow(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n: " + n
                    + " zero or negative values not allowed");
            Function<X, Y> pow2 = this;
            Function<X, Y> result = null;
            while (n >= 1) { // Iteration.
                if ((n & 1) == 1) {
                    result = (result == null) ? pow2 : result.times(pow2);
                }
                pow2 = pow2.times(pow2);
                n >>>= 1;
            }
            return result;
    }

    public Function<X, Y> plus(Function<X, Y> that) {
        Plus result = Plus.FACTORY.object();
        result._f = this;
        result._g = that;
        return result;
    }

    public Function<X, Y> opposite() {
        Function f = (Function) this;
        if (f instanceof Opposite) {
            return ((Opposite)f)._f;
        } else {
            Opposite result = Opposite.FACTORY.object();
            result._f = this;
            return result;
        }
    }

    public Function<X, Y> times(Function<X, Y> that) {
        Times result = Times.FACTORY.object();
        result._f = this;
        result._g = that;
        return result;
    }
    
    private static final class Compose extends Function {

        private static final Factory<Compose> FACTORY = new Factory<Compose>() {

            protected Compose create() {
                return new Compose();
            }
        };

        Function _f, _g;

        Variable _fv;

        public Set<Variable> getVariables() {
            return _g.getVariables();
        }

        public Ring evaluate() {
            Object o = _g.evaluate();
            Object prev = _fv.get();
            _fv.set(o);
            try {
               return _f.evaluate();
            } finally {
                _fv.set(prev);
            }
        }

        public Function differentiate(Variable v) {
            Function fd = _f.differentiate(v);
            Function gd = _g.differentiate(v);
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

        public Object times(Object that) {
            return super.times((Function)that);
        }

        public Object plus(Object that) {
            return super.plus((Function)that);
        }
    }

    private static final class Derivative extends Function {

        private static final long serialVersionUID = 1L;

        private static final Factory<Derivative> FACTORY 
            = new Factory<Derivative>() {

            protected Derivative create() {
                return new Derivative();
            }
        };

        Function _f;

        Variable _v;

        public Set<Variable> getVariables() {
            return _f.getVariables();
        }

        public Ring evaluate() {
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

        public Object times(Object that) {
            return super.times((Function)that);
        }

        public Object plus(Object that) {
            return super.plus((Function)that);
        }
    }

    private static final class Integral extends Function {

        private static final long serialVersionUID = 1L;

        private static final Factory<Integral> FACTORY 
            = new Factory<Integral>() {

            protected Integral create() {
                return new Integral();
            }
        };

        Function _f;

        Variable _v;

        public Set<Variable> getVariables() {
            return _f.getVariables();
        }

        public Ring evaluate() {
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

        public Object times(Object that) {
            return super.times((Function)that);
        }

        public Object plus(Object that) {
            return super.plus((Function)that);
        }
    }


    private static final class Plus extends Function {

        private static final long serialVersionUID = 1L;

        private static final Factory<Plus> FACTORY = new Factory<Plus>() {

            protected Plus create() {
                return new Plus();
            }
        };

        Function _f, _g;

        public Set<Variable> getVariables() {
            FastSet<Variable> variables = FastSet.newInstance();
            variables.addAll(_f.getVariables());
            variables.addAll(_g.getVariables());
            return variables;
        }

        public Ring evaluate() {
            return (Ring)_f.evaluate().plus(_g.evaluate());
        }

        public Function differentiate(Variable v) {
            Function fd = _f.differentiate(v);
            Function gd = _g.differentiate(v);
            return fd.plus(gd);
        }

        public Function integrate(Variable v) {
            Function _fi = _f.integrate(v);
            Function _gi = _g.integrate(v);
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

        public Object times(Object that) {
            return super.times((Function)that);
        }

        public Object plus(Object that) {
            return super.plus((Function)that);
        }
    }

    private static final class Opposite extends Function {

        private static final long serialVersionUID = 1L;

        private static final Factory<Opposite> FACTORY = new Factory<Opposite>() {

            protected Opposite create() {
                return new Opposite();
            }
        };

        Function _f;

        public Set<Variable> getVariables() {
            return _f.getVariables();
        }

        public Ring evaluate() {
            return (Ring) _f.evaluate().opposite();
        }

        public Function differentiate(Variable v) {
            Function fd = _f.differentiate(v);
            return fd.opposite();
        }

        public Function integrate(Variable v) {
            Function _fi = _f.integrate(v);
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

        public Object times(Object that) {
            return super.times((Function)that);
        }

        public Object plus(Object that) {
            return super.plus((Function)that);
        }
    }


    private static final class Times extends Function {

        private static final long serialVersionUID = 1L;

        private static final Factory<Times> FACTORY = new Factory<Times>() {

            protected Times create() {
                return new Times();
            }
        };

        Function _f, _g;

        public Set<Variable> getVariables() {
            FastSet<Variable> variables = FastSet.newInstance();
            variables.addAll(_f.getVariables());
            variables.addAll(_g.getVariables());
            return variables;
        }

        public Ring evaluate() {
            return (Ring) _f.evaluate().times(_g.evaluate());
        }

        public Function differentiate(Variable v) {
            Function fd = _f.differentiate(v);
            Function gd = _g.differentiate(v);
            return _f.times(gd).plus(fd.times(_g));
        }

        public Text toText() {
            TextBuilder tb = TextBuilder.newInstance();
            tb.append(_f);
            tb.append(')');
                tb.append('·');
                tb.append('(');
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

        public Object times(Object that) {
            return super.times((Function)that);
        }

        public Object plus(Object that) {
            return super.plus((Function)that);
        }
    }
    
}