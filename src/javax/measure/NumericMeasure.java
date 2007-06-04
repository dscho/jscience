/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2007 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.measure;

import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

/**
 * This class represents a numeric quantity.
 *  
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 4.0, February 25, 2006
 */
class NumericMeasure<N extends Number, Q extends Quantity> extends Measure<N, Q>  {

    /**
     * Creates a new measure for the specified value stated in the specified
     * unit.
     * 
     * @param value the numerical value.
     * @param unit the measurement unit.
     */
    public NumericMeasure(N value, Unit<Q> unit) {
        super(value, unit);
    }
    
    // Implements Measurable<Q>
    public final double doubleValue(Unit<Q> unit) {
        if (unit.equals(this.getUnit()))
            return this.getValue().doubleValue();
        UnitConverter cvtr = this.getUnit().getConverterTo(unit);
        return cvtr.convert(this.getValue().doubleValue());
    }

    // Implements Measurable<Q>
    public final long longValue(Unit<Q> unit) throws ArithmeticException {
        if (unit.equals(this.getUnit()))
            return this.getValue().longValue();
        double doubleValue = doubleValue(unit);
        if ((doubleValue >= Long.MIN_VALUE) && (doubleValue <= Long.MAX_VALUE))
            return Math.round(doubleValue);
        throw new ArithmeticException(doubleValue + " " + unit
                + " cannot be represented as long");
    }

    private static final long serialVersionUID = 1L;
}