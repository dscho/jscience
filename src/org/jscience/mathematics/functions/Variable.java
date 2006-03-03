/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.functions;

import javolution.lang.Reference;

/**
 * <p> This interface represents a symbol on whose value a {@link Function}
 *     depends. If the functions is not shared between multiple-threads the 
 *     simple {@link Variable.Local} implementation can be used. 
 *     For global functions (functions used concurrently by multiple threads)
 *     the {@link Variable.Global} implementation is provided.</p>
 *   
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 * @see  Function#evaluate 
 */
public interface Variable<X> extends Reference<X> {

    /**
     * Returns the symbol for this variable.
     * 
     * @return this variable's symbol.
     */
    String getSymbol();

    /**
     * This class represents a simple {@link Variable} implementation for 
     * functions not shared between threads (e.g. non static functions).
     * Functions shared between multiple-thread should use a different 
     * implementation (such as {@link Variable.Global}. 
     */
    public static class Local<X> implements Variable<X> {

        /**
         * Holds the reference value.
         */
        private X _value;

        /**
         * Holds the variable symbol.
         */
        private final String _symbol;

        /**
         * Creates a new local variable with a unique symbol.
         * 
         * @param symbol the variable symbol.
         */
        public Local(String symbol) {
            _symbol = symbol;
        }

        public String getSymbol() {
            return _symbol;
        }

        public X get() {
            return _value;
        }

        public void set(X arg0) {
            _value = arg0;
        }
    }

    /**
     * This class represents a simple {@link Variable} implementation for 
     * functions shared between threads (e.g. static functions). 
     * The variable value is thread-local and can be set independantly
     * by concurrent threads.
     */
    public static class Global<X> implements Variable<X> {

        /**
         * Holds the reference value.
         */
        private ThreadLocal<X> _value = new ThreadLocal<X>();

        /**
         * Holds the variable symbol.
         */
        private final String _symbol;

        /**
         * Creates a new global variable with a unique symbol.
         * 
         * @param symbol the variable symbol.
         */
        public Global(String symbol) {
            _symbol = symbol;
        }

        public String getSymbol() {
            return _symbol;
        }

        public X get() {
            return _value.get();
        }

        public void set(X arg0) {
            _value.set(arg0);
        }
    }

}