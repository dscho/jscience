/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
/**
 * Provides support for programatic unit handling.
 *
 * <h3> Standard / Non Standard Units</h3>
 *      This package provides standard units in accordance with the 
 *       <a href="http://physics.nist.gov/Pubs/SP330/sp330.pdf">
 *      "The International System of Units (SI)"</a> as well as non-standard 
 *      units in accordance  with the <a href="http://unitsofmeasure.org/">
 *      "Uniform Code for Units of Measure (UCUM)"</a>.
 *
 * <h3>Usage examples:</h3>
 * [code]
 *
 * import org.unitofmeasurement.unit.*;
 * import org.unitofmeasurement.quantity.*;
 * 
 * import static org.jscience.physics.unit.SI.*;
 * import static org.jscience.physics.unit.SIPrefix.*;
 * import static org.jscience.physics.unit.UCUM.*;
 *
 * public class Main {
 *     public void main(String[] args) {
 *
 *         // Conversion between units (explicit way).
 *         Unit<Length> sourceUnit = KILO(METRE);
 *         Unit<Length> targetUnit = MILE;
 *         UnitConverter uc = sourceUnit.getConverterTo(targetUnit);
 *         System.out.println(uc.convert(10)); // Converts 10 km to miles.
 *
 *         // Same conversion than above, packed in one line.
 *         System.out.println(KILO(METRE).getConverterTo(MILE).convert(10));
 *
 *         // Retrieval of the SI unit (identifies the measurement type).
 *         System.out.println(REVOLUTION.divide(MINUTE).toSI());
 *
 *         // Dimension checking (allows/disallows conversions)
 *         System.out.println(ELECTRON_VOLT.isCompatible(WATT.times(HOUR)));
 *
 *         // Retrieval of the unit dimension (depends upon the current model).
 *         System.out.println(ELECTRON_VOLT.getDimension());
 *     }
 * }
 *
 * > 6.2137119223733395
 * > 6.2137119223733395
 * > rad/s
 * > true
 * > [L]²·[M]/[T]²
 * [/code]
 *
 * <h3>Unit Parameterization</h3>
 *
 *     Units are parameterized enforce compile-time checks of units/measures consistency, for example:[code]
 *
 *     Unit<Time> MINUTE = SECOND.times(60); // Ok.
 *     Unit<Time> MINUTE = METRE.times(60); // Compile error.
 *
 *     Unit<Pressure> HECTOPASCAL = HECTO(PASCAL); // Ok.
 *     Unit<Pressure> HECTOPASCAL = HECTO(NEWTON); // Compile error.
 *
 *     Measure<Time> duration = Measure.valueOf(2, MINUTE); // Ok.
 *     Measure<Time> duration = Measure.valueOf(2, CELSIUS); // Compile error.
 *
 *     long milliseconds = duration.longValue(MILLI(SECOND)); // Ok.
 *     long milliseconds = duration.longValue(POUND); // Compile error.
 *     [/code]
 *
 *     Runtime checks of dimension consistency can be done for more complex cases.
 *
 *     [code]
 *     Unit<Area> SQUARE_FOOT = FOOT.times(FOOT).asType(Area.class); // Ok.
 *     Unit<Area> SQUARE_FOOT = FOOT.times(KELVIN).asType(Area.class); // Runtime error.
 *
 *     Unit<Temperature> KELVIN = Unit.valueOf("K").asType(Temperature.class); // Ok.
 *     Unit<Temperature> KELVIN = Unit.valueOf("kg").asType(Temperature.class); // Runtime error.
 *     [/code]
 *     </p>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0.0
 */
package org.jscience.physics.unit;
