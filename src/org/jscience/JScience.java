/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Random;

import javolution.realtime.PoolContext;
import javolution.util.Reflection;
import javolution.xml.ObjectReader;
import javolution.xml.ObjectWriter;

import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;
import org.jscience.mathematics.matrices.Matrix;
import org.jscience.mathematics.matrices.Operable;
import org.jscience.mathematics.numbers.Complex;
import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.numbers.LargeInteger;
import org.jscience.physics.quantities.Acceleration;
import org.jscience.physics.quantities.AmountOfSubstance;
import org.jscience.physics.quantities.Angle;
import org.jscience.physics.quantities.AngularAcceleration;
import org.jscience.physics.quantities.AngularVelocity;
import org.jscience.physics.quantities.Area;
import org.jscience.physics.quantities.CatalyticActivity;
import org.jscience.physics.quantities.DataAmount;
import org.jscience.physics.quantities.DataRate;
import org.jscience.physics.quantities.Duration;
import org.jscience.physics.quantities.ElectricCapacitance;
import org.jscience.physics.quantities.ElectricCharge;
import org.jscience.physics.quantities.ElectricConductance;
import org.jscience.physics.quantities.ElectricCurrent;
import org.jscience.physics.quantities.ElectricInductance;
import org.jscience.physics.quantities.ElectricPotential;
import org.jscience.physics.quantities.ElectricResistance;
import org.jscience.physics.quantities.Energy;
import org.jscience.physics.quantities.Force;
import org.jscience.physics.quantities.Frequency;
import org.jscience.physics.quantities.Illuminance;
import org.jscience.physics.quantities.Length;
import org.jscience.physics.quantities.LuminousFlux;
import org.jscience.physics.quantities.LuminousIntensity;
import org.jscience.physics.quantities.MagneticFlux;
import org.jscience.physics.quantities.MagneticFluxDensity;
import org.jscience.physics.quantities.Mass;
import org.jscience.physics.quantities.Power;
import org.jscience.physics.quantities.Pressure;
import org.jscience.physics.quantities.Quantity;
import org.jscience.physics.quantities.RadiationDoseAbsorbed;
import org.jscience.physics.quantities.RadiationDoseEffective;
import org.jscience.physics.quantities.RadioactiveActivity;
import org.jscience.physics.quantities.Scalar;
import org.jscience.physics.quantities.SolidAngle;
import org.jscience.physics.quantities.Temperature;
import org.jscience.physics.quantities.Torque;
import org.jscience.physics.quantities.Velocity;
import org.jscience.physics.quantities.Volume;
import org.jscience.physics.quantities.VolumetricDensity;
import org.jscience.physics.units.NonSI;
import org.jscience.physics.units.SI;

/**
 * <p> This class represents the jScience library; it contains the library
 *     optional {@link #initialize} method as well as a {@link #main} method for
 *     versionning, self-tests, and performance analysis.</p>
 * <p> Initialization is performed automatically when quantities classes are
 *     used for the first time or explicitely by calling the 
 *     {@link #initialize} static method.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class JScience {

    /**
     * Holds the version information.
     */
    public final static String VERSION = "@VERSION@";

    /**
     * Indicates if this library has been initialized.
     */
    private static boolean IsInitialized;

    /**
     * Default constructor.
     */
    private JScience() {// Forbids derivation.
    }

    /**
     * The library {@link #main} method.
     * The archive <codejscience.jar</code> is auto-executable.
     * <ul>
     * <li><code>java -Xbootclasspath/a:javolution.jar -jar jscience.jar version</code> 
     *     to output version information.</li>
     * <li><code>java -Xbootclasspath/a:javolution.jar -jar jscience.jar test</code> 
     *     to perform self-tests.</li>
     * <li><code>java -Xbootclasspath/a:javolution.jar -jar jscience.jar perf</code> 
     *     for performance analysis.</li>
     * </ul>
     *
     * @param  args the option arguments.
     * @throws Exception if a problem occurs.
     */
    public static void main(String[] args) throws Exception {
        System.out.println(
                "jScience - Java(TM) Tools and Libraries for the Advancement of Sciences "
                + "(http://jscience.org/)");
        if (args.length > 0) {
            if (args[0].equals("version")) {
                System.out.println("Version " + VERSION);
                return;
            } else if (args[0].equals("test")) {
                testing();
                return;
            } else if (args[0].equals("perf")) {
                benchmark();
                return;
            }
        }
        System.out.println("Usage: java -Xbootclasspath/a:javolution.jar -jar jscience.jar [arg]");
        System.out.println("where arg is one of:");
        System.out.println("    version (to show version information)");
        System.out.println("    test    (to perform self-tests)");
        System.out.println("    perf    (to run benchmark)");
    }

    /**
     * Initializes library classes. This method is automatically called when the
     * {@link Quantity}classes are initialized. It ensures that:
     * <ol>
     * <li>The unit database is loaded (e.g. <code>Unit.valueOf("ft")</code>
     *     returns <code>NonSI.FOOT</code>).</li>
     * <li>Predefined quantity sub-classes are mapped to their respective units
     *     (e.g. <code>Quantity.valueOf(1, SI.METER)</code> returns a
     *     <code>Length</code> instance).</li>
     * <li>Execution time is more predictable as class initialization has
     *     already been performed.</li>
     * </ol>
     * 
     * <p><i>Note: It the library is extended (e.g. new quantity sub-classes), it
     *             is the responsibility of the application to ensure that the new classes
     *             are properly initialized as well. </i></p>
     */
    public static synchronized void initialize() {
        if (!JScience.IsInitialized) { //  Performs initialization only once.
            JScience.IsInitialized = true;

            // Forces initialization through references to static members.
            // The temporary object is volatile to prevent compiler
            // optimization.

            // Initializes physical quantities.
            JScience.Volatile = Acceleration.ZERO;
            JScience.Volatile = AmountOfSubstance.ZERO;
            JScience.Volatile = Angle.ZERO;
            JScience.Volatile = AngularAcceleration.ZERO;
            JScience.Volatile = AngularVelocity.ZERO;
            JScience.Volatile = Area.ZERO;
            JScience.Volatile = CatalyticActivity.ZERO;
            JScience.Volatile = DataAmount.ZERO;
            JScience.Volatile = DataRate.ZERO;
            JScience.Volatile = Duration.ZERO;
            JScience.Volatile = ElectricCapacitance.ZERO;
            JScience.Volatile = ElectricCharge.ZERO;
            JScience.Volatile = ElectricConductance.ZERO;
            JScience.Volatile = ElectricCurrent.ZERO;
            JScience.Volatile = ElectricInductance.ZERO;
            JScience.Volatile = ElectricPotential.ZERO;
            JScience.Volatile = ElectricResistance.ZERO;
            JScience.Volatile = Energy.ZERO;
            JScience.Volatile = Force.ZERO;
            JScience.Volatile = Frequency.ZERO;
            JScience.Volatile = Illuminance.ZERO;
            JScience.Volatile = Length.ZERO;
            JScience.Volatile = LuminousFlux.ZERO;
            JScience.Volatile = LuminousIntensity.ZERO;
            JScience.Volatile = MagneticFlux.ZERO;
            JScience.Volatile = MagneticFluxDensity.ZERO;
            JScience.Volatile = Mass.ZERO;
            JScience.Volatile = Power.ZERO;
            JScience.Volatile = Pressure.ZERO;
            JScience.Volatile = RadiationDoseAbsorbed.ZERO;
            JScience.Volatile = RadiationDoseEffective.ZERO;
            JScience.Volatile = RadioactiveActivity.ZERO;
            JScience.Volatile = Scalar.ZERO;
            JScience.Volatile = SolidAngle.ZERO;
            JScience.Volatile = Temperature.ZERO;
            JScience.Volatile = Torque.ZERO;
            JScience.Volatile = Velocity.ZERO;
            JScience.Volatile = Volume.ZERO;
            JScience.Volatile = VolumetricDensity.ZERO;

            // Initializes money package.
            JScience.Volatile = Money.ZERO;
        }
    }

    static volatile Object Volatile;

    /**
     * Performs simple tests.
     * 
     * @throws Exception if a problem occurs.
     */
    private static void testing() throws Exception {
        System.out.println();
        System.out.print("Testing...");
        Matrix M = Matrix.valueOf(new Operable[][] {
                { Scalar.ZERO, Scalar.ONE },
                { Quantity.valueOf(2, NonSI.BAR), Scalar.valueOf(1.234) },
                { Quantity.valueOf(22.336, Currency.EUR),
                        Quantity.valueOf(2, Currency.USD) },
                { Quantity.valueOf(1, NonSI.FOOT),
                        Quantity.valueOf(3, NonSI.POUND) }, });

        // Writes Matrix.
        ObjectWriter ow = ObjectWriter.newInstance();
        ow.setNamespace("", "org.jscience.physics.quantities");
        ow.setNamespace("money", "org.jscience.economics.money");
        ow.setNamespace("math", "org.jscience.mathematics.matrices");

        StringWriter out = new StringWriter();
        ow.write(M, out);
        //System.err.println(out.getBuffer().toString()); // For Debugging

        // Read Matrix.
        StringReader in = new StringReader(out.getBuffer().toString());
        ObjectReader or = ObjectReader.newInstance();
        Matrix R = (Matrix) or.read(in);
        //System.err.println(R); // For Debugging

        if (Quantity.approxEquals(M, R)) {
            System.out.println("Success");
            System.exit(0);
        } else {
            System.out.println("Failed");
            System.exit(-1);
        }
    }

    /**
     * Measures performance.
     */
    private static void benchmark() throws Exception {
        System.out.println("Benchmark...");

        System.out.println();
        System.out.print("Initialization: ");
        startTime();
        initialize();
        endTime(1);
        Object[] results = new Object[10000];
        Duration.showAs(SI.MICRO(SI.SECOND));

        System.out.println("");
        System.out.println("Numerics Operations");

        System.out.print("Float64 add: ");
        startTime();
        for (int i = 0; i < 1000; i++) {
            PoolContext.enter();
            Float64 x = Float64.ONE;
            for (int j = 0; j < results.length; j++) {
                results[j] = x.add(x);
            }
            PoolContext.exit();
        }
        endTime(1000 * results.length);

        System.out.print("Complex add: ");
        startTime();
        for (int i = 0; i < 1000; i++) {
            PoolContext.enter();
            Complex x = Complex.valueOf(1.0, 2.0);
            for (int j = 0; j < results.length; j++) {
                results[j] = x.add(x);
            }
            PoolContext.exit();
        }
        endTime(1000 * results.length);

        System.out.print("Quantity (mass) add: ");
        startTime();
        for (int i = 0; i < 1000; i++) {
            PoolContext.enter();
            Quantity x = Quantity.valueOf(1, SI.KILOGRAM);
            for (int j = 0; j < results.length; j++) {
                results[j] = x.add(x);
            }
            PoolContext.exit();
        }
        endTime(1000 * results.length);

        System.out.print("Float64 multiply: ");
        startTime();
        for (int i = 0; i < 1000; i++) {
            PoolContext.enter();
            Float64 x = Float64.valueOf(1.0);
            for (int j = 0; j < results.length; j++) {
                results[j] = x.multiply(x);
            }
            PoolContext.exit();
        }
        endTime(1000 * results.length);

        System.out.print("Complex multiply: ");
        startTime();
        for (int i = 0; i < 1000; i++) {
            PoolContext.enter();
            Complex x = Complex.valueOf(1.0, 2.0);
            for (int j = 0; j < results.length; j++) {
                results[j] = x.multiply(x);
            }
            PoolContext.exit();
        }
        endTime(1000 * results.length);

        System.out.print("Quantity (mass) multiply: ");
        startTime();
        for (int i = 0; i < 1000; i++) {
            PoolContext.enter();
            Quantity x = Quantity.valueOf(1, SI.KILOGRAM);
            for (int j = 0; j < results.length; j++) {
                results[j] = x.multiply(x);
            }
            PoolContext.exit();
        }
        endTime(1000 * results.length);

        System.out.println();
        System.out.println("LargeInteger (PoolContext) versus BigInteger");

        System.out.print("BigInteger (1024 bits) add: ");
        BigInteger big = BigInteger.probablePrime(1024, new Random());
        startTime();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < results.length; j++) {
                results[j] = big.add(big);
            }
        }
        endTime(100 * results.length);

        System.out.print("LargeInteger (1024 bits) add: ");
        byte[] bytes = big.toByteArray();
        LargeInteger large = LargeInteger.valueOf(bytes, 0, bytes.length);
        startTime();
        for (int i = 0; i < 100; i++) {
            PoolContext.enter();
            for (int j = 0; j < results.length; j++) {
                results[j] = large.add(large);
            }
            PoolContext.exit();
        }
        endTime(100 * results.length);

        System.out.println();
        System.out.println("More performance analysis in future versions...");
    }

    private static void startTime() {
        _time = microTime();
    }

    private static void endTime(int iterations) throws IOException {
        Quantity duration = Quantity.valueOf(microTime() - _time, 500,
                SI.MICRO(SI.SECOND)).divide(iterations);
        System.out.println(duration);
    }

    private static long _time;

    public static long microTime() {
        if (NANO_TIME_METHOD != null) { // JRE 1.5+
            Number time = (Number) NANO_TIME_METHOD.invoke(null);
            return time.longValue() / 1000;
        } else { // Use the less accurate time in milliseconds.
            return System.currentTimeMillis() * 1000;
        }
    }

    private static final Reflection.Method NANO_TIME_METHOD = Reflection
            .getMethod("java.lang.System.nanoTime()");
}