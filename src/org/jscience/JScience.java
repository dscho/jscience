/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience;

import java.math.BigInteger;
import java.util.Date;
import java.util.Random;

import javax.measure.quantities.*;
import javax.measure.units.*;

import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;
import org.jscience.geography.coordinates.Altitude;
import org.jscience.geography.coordinates.CompoundCoordinates;
import org.jscience.geography.coordinates.LatLong;
import org.jscience.geography.coordinates.Time;
import org.jscience.geography.coordinates.UTM;
import org.jscience.geography.coordinates.XYZ;
import org.jscience.geography.coordinates.crs.CoordinatesConverter;
import org.jscience.mathematics.functions.Polynomial;
import org.jscience.mathematics.functions.Variable;
import org.jscience.mathematics.numbers.Complex;
import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.numbers.LargeInteger;
import org.jscience.mathematics.numbers.ModuloInteger;
import org.jscience.mathematics.numbers.Rational;
import org.jscience.mathematics.numbers.Real;
import org.jscience.mathematics.vectors.ComplexMatrix;
import org.jscience.mathematics.vectors.DenseMatrix;
import org.jscience.mathematics.vectors.DenseVector;
import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Matrix;
import org.jscience.mathematics.vectors.Vector;
import org.jscience.physics.measures.Measure;
import org.jscience.physics.measures.MeasureFormat;
import org.jscience.physics.models.RelativisticModel;

import javolution.lang.MathLib;
import javolution.text.TextBuilder;
import javolution.context.ConcurrentContext;
import javolution.context.LocalContext;
import javolution.context.PoolContext;
import static javax.measure.units.NonSI.*;
import static javax.measure.units.SI.*;
import static org.jscience.economics.money.Currency.*;

/**
 * <p> This class represents the <b>J</b>Science library; it contains the
 *    {@link #main} method for versionning, self-tests, and performance 
 *    analysis.</p>
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
public final class JScience {

    /**
     * Holds the version information.
     */
    public final static String VERSION = "@VERSION@";

    /**
     * Default constructor.
     */
    private JScience() {// Forbids derivation.
    }

    /**
     * The library {@link #main} method. The archive <codejscience.jar</code>
     * is auto-executable.
     * <ul>
     * <li><code>java [-cp javolution.jar] -jar jscience.jar version</code>
     * to output version information.</li>
     * <li><code>java [-cp javolution.jar] -jar jscience.jar test</code> to
     * perform self-tests.</li>
     * <li><code>java [-cp javolution.jar] -jar jscience.jar perf</code> for
     * performance analysis.</li>
     * </ul>
     * 
     * @param args the option arguments.
     * @throws Exception if a problem occurs.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("JScience - Java(TM) Tools and Libraries for"
                + " the Advancement of Sciences");
        System.out.println("Version " + VERSION + " (http://jscience.org)");
        System.out.println("");
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
        System.out
                .println("Usage: java [-cp javolution.jar] -jar jscience.jar [arg]");
        System.out.println("where arg is one of:");
        System.out.println("    version (to show version information)");
        System.out.println("    test    (to perform self-tests)");
        System.out.println("    perf    (to run benchmark)");
    }
    
    /**
     * Performs simple tests.
     * 
     * @throws Exception if a problem occurs.
     */
    private static void testing() throws Exception {
        System.out.println("Testing...");   
        {
            System.out.println("");
            System.out.println("Exact Measurements");
            Measure<Mass> m0 = Measure.valueOf(100, POUND);
            Measure<Mass> m1 = m0.times(33).divide(2);
            Measure<ElectricCurrent> m2 = Measure.valueOf("234 mA").to(
                    MICRO(AMPERE));
            System.out.println("m0 = " + m0);
            System.out.println("m1 = " + m1);
            System.out.println("m2 = " + m2);

            System.out.println("");
            System.out.println("Inexact Measurements");
            Measure<Mass> m3 = Measure.valueOf(100.0, POUND);
            Measure<Mass> m4 = m0.divide(3);
            Measure<ElectricCurrent> m5 = Measure.valueOf("234 mA").to(AMPERE);
            Measure<Temperature> t0 = Measure.valueOf(-7.3, 0.5, CELSIUS);
            System.out.println("m3 = " + m3);
            System.out.println("m4 = " + m4);
            System.out.println("m5 = " + m5);
            System.out.println("t0 = " + t0);

            System.out.println("");
            System.out.println("Interval measurements");
            Measure<Volume> m6 = Measure.valueOf(20, 0.1, LITER);
            Measure<Frequency> m7 = Measure.rangeOf(10, 11, KILO(HERTZ));
            System.out.println("m6 = " + m6);
            System.out.println("m7 = " + m7);

            System.out.println("");
            System.out.println("Measure.equals (identical) / Measure.approximates " +
                    "(takes into account errors such as numeric errors)");
            Measure<Frequency> m8 = Measure.valueOf(9000, HERTZ);
            Measure<Frequency> m10 = m8.divide(3).times(3); // Still exact.
            Measure<Frequency> m11 = m8.divide(7).times(7); // No more exact.
            System.out.println("m8 = " + m8);
            System.out.println("m10 = " + m10);
            System.out.println("m11 = " + m11);
            System.out.println("(m10 == m8) = " + m10.equals(m8));
            System.out.println("(m10 ≅ m8) = " + m10.approximates(m8));
            System.out.println("(m11 == m8) = " + m11.equals(m8));
            System.out.println("(m11 ≅ m8) = " + m11.approximates(m8));

            System.out.println("");
            System.out.println("MeasureFormat - Plus/Minus Error (3 digits error)");
            MeasureFormat.setInstance(MeasureFormat
                    .getPlusMinusErrorInstance(3));
            System.out.println("m3 = " + m3);
            System.out.println("m4 = " + m4);
            System.out.println("m5 = " + m5);

            System.out.println("");
            System.out.println("MeasureFormat - Bracket Error (2 digits error)");
            MeasureFormat.setInstance(MeasureFormat.getBracketErrorInstance(2));
            System.out.println("m3 = " + m3);
            System.out.println("m4 = " + m4);
            System.out.println("m5 = " + m5);

            System.out.println("");
            System.out.println("MeasureFormat - Exact Digits Only");
            MeasureFormat.setInstance(MeasureFormat.getExactDigitsInstance());
            System.out.println("m3 = " + m3);
            System.out.println("m4 = " + m4);
            System.out.println("m5 = " + m5);

            System.out.println("");
            System.out.println("Numeric Errors");
            {
                Measure<Length> x = Measure.valueOf(1.0, METER);
                Measure<Velocity> v = Measure.valueOf(0.01, METER_PER_SECOND);
                Measure<Duration> t = Measure.valueOf(1.0, MICRO(SECOND));
                long ns = System.nanoTime();
                for (int i = 0; i < 10000000; i++) {
                    x = x.plus(v.times(t));
                }
                ns = System.nanoTime() - ns;
                MeasureFormat.setInstance(MeasureFormat
                        .getExactDigitsInstance());
                System.out.println(x
                        + " ("
                        + Measure.valueOf(ns, 0.5, NANO(SECOND)).to(
                                MILLI(SECOND)) + ")");
            }
            {
                double x = 1.0; // m
                double v = 0.01; // m/s
                double t = 1E-6; // s
                for (int i = 0; i < 10000000; i++) {
                    x += v * t; // Note: Most likely the compiler get v * t out of the loop.
                }
                System.out.println(x);
            }
            MeasureFormat.setInstance(MeasureFormat
                    .getPlusMinusErrorInstance(2));
        }
        {
            System.out.println("");
            System.out.println("Physical Models");
            // Selects a relativistic model for dimension checking (typically at start-up).
            RelativisticModel.select(); 

            // Length and Duration can be added.
            Measure<Length> x = Measure.valueOf(100, NonSI.INCH);
            x = x.plus(Measure.valueOf("2.3 µs")).to(METER); 
            System.out.println(x); 
               
            // Energy is compatible with mass (E=mc2)
            Measure<Mass> m = Measure.valueOf("12 GeV").to(KILOGRAM); 
            System.out.println(m); 
        }

        {
            System.out.println("");
            System.out.println("Money/Currencies");
            ///////////////////////////////////////////////////////////////////////
            // Calculates the cost of a car trip in Europe for an American tourist.
            ///////////////////////////////////////////////////////////////////////

            // Use currency symbols instead of ISO-4217 codes.
            UnitFormat.getStandardInstance().label(USD, "$"); // Use "$" symbol instead of currency code ("USD")
            UnitFormat.getStandardInstance().label(EUR, "€"); // Use "€" symbol instead of currency code ("EUR")

            // Sets exchange rates.
            Currency.setReferenceCurrency(USD);
            EUR.setExchangeRate(1.17); // 1.0 € = 1.17 $

            // Calculates trip cost.
            Measure<?> carMileage = Measure.valueOf(20, MILE
                    .divide(GALLON_LIQUID_US)); // 20 mi/gal.
            Measure<?> gazPrice = Measure.valueOf(1.2, EUR.divide(LITER)); // 1.2 €/L
            Measure<Length> tripDistance = Measure.valueOf(400, KILO(SI.METER)); // 400 km
            Measure<Money> tripCost = tripDistance.divide(carMileage).times(
                    gazPrice).to(USD);
            // Displays cost.
            System.out.println("Trip cost = " + tripCost + " ("
                    + tripCost.to(EUR) + ")");
        }
        {
            System.out.println("");
            System.out.println("Matrices/Vectors");

            Measure<ElectricResistance> R1 = Measure.valueOf(100, 1, OHM); // 1% precision. 
            Measure<ElectricResistance> R2 = Measure.valueOf(300, 3, OHM); // 1% precision.
            Measure<ElectricPotential> U0 = Measure.valueOf(28, 0.01, VOLT); // ±0.01 V fluctuation.

            // Equations:  U0 = U1 + U2       |1  1  0 |   |U1|   |U0|
            //             U1 = R1 * I    =>  |-1 0  R1| * |U2| = |0 |
            //             U2 = R2 * I        |0 -1  R2|   |I |   |0 |
            //
            //                                    A      *  X   =  B
            //
            DenseMatrix<Measure<?>> A = DenseMatrix.valueOf(new Measure<?>[][] {
                    { Measure.ONE, Measure.ONE, Measure.valueOf(0, OHM) },
                    { Measure.ONE.opposite(), Measure.ZERO, R1 },
                    { Measure.ZERO, Measure.ONE.opposite(), R2 } });
            DenseVector<Measure<?>> B = DenseVector.valueOf(new Measure<?>[] { U0, Measure.valueOf(0,
                    VOLT), Measure.valueOf(0, VOLT)});
            Vector<Measure<?>> X = A.solve(B);
            System.out.println(X);
            System.out.println(X.get(2).to(MILLI(AMPERE)));
        }
        {
            System.out.println("");
            System.out.println("Polynomials");

            // Defines two local variables (x, y).
            Variable<Complex> varX = new Variable.Local<Complex>("x");
            Variable<Complex> varY = new Variable.Local<Complex>("y");

            // f(x) = 1 + 2x + ix²
            Polynomial<Complex> x = Polynomial.valueOf(Complex.ONE, varX);
            Polynomial<Complex> fx = x.pow(2).times(Complex.I).plus(
                    x.times(Complex.valueOf(2, 0)).plus(Complex.ONE));
            System.out.println(fx);
            System.out.println(fx.pow(2));
            System.out.println(fx.differentiate(varX));
            System.out.println(fx.integrate(varY));
            System.out.println(fx.compose(fx));

            // Calculates expression.
            varX.set(Complex.valueOf(2, 3));
            System.out.println(fx.evaluate());
        }

        {
            System.out.println("");
            System.out.println("Coordinates Conversions");

            // Simple Lat/Long to UTM conversion.
            CoordinatesConverter<LatLong, UTM> latLongToUTM = LatLong.CRS
                    .getConverterTo(UTM.CRS);
            LatLong latLong = LatLong.valueOf(34.34, 23.56, DEGREE_ANGLE);
            UTM utm = latLongToUTM.convert(latLong);
            System.out.println(utm);

            // Converts any projected coordinates to Latitude/Longitude.
            //Coordinates<ProjectedCRS> coord2d = utm;
            //ProjectedCRS<Coordinates> crs = coord2d.getCoordinateReferenceSystem();
            //CoordinatesConverter<Coordinates, LatLong> cvtr = crs.getConverterTo(LatLong.CRS);
            //latLong = cvtr.convert(coord2d);
            //System.out.println(latLong);

            // Compound coordinates.
            Altitude alt = Altitude.valueOf(2000, FOOT);
            CompoundCoordinates<LatLong, Altitude> latLongAlt = new CompoundCoordinates<LatLong, Altitude>(
                    latLong, alt);
            System.out.println(latLongAlt);

            // Converts compound coordinates (3-D) to XYZ (GPS).
            XYZ xyz = latLongAlt.getCoordinateReferenceSystem().getConverterTo(
                    XYZ.CRS).convert(latLongAlt);
            System.out.println(xyz);

            // Even more compounding...
            Time time = Time.valueOf(new Date());
            CompoundCoordinates<CompoundCoordinates, Time> latLongAltTime = new CompoundCoordinates<CompoundCoordinates, Time>(
                    latLongAlt, time);
            System.out.println(latLongAltTime);
        }

        {
            System.out.println("");
            System.out.println("Numbers");

            Real two = Real.valueOf(2); // 2.0000..00 (100 zeros after decimal point).
            Real sqrt2 = two.sqrt();
            System.out.println("sqrt(2)   = " + sqrt2);
            System.out.println("Precision = " + sqrt2.getPrecision()
                    + " digits.");

            LargeInteger dividend = LargeInteger.valueOf("3133861182986538201");
            LargeInteger divisor = LargeInteger.valueOf("25147325102501733369");
            Rational rational = Rational.valueOf(dividend, divisor);
            System.out.println("rational  = " + rational);

            ModuloInteger m = ModuloInteger.valueOf("233424242346");
            LocalContext.enter(); // Avoids impacting others threads.
            try {
                ModuloInteger.setModulus(LargeInteger.valueOf("31225208137"));
                ModuloInteger inv = m.inverse();
                System.out.println("inverse modulo = " + inv);

                ModuloInteger one = inv.times(m);
                System.out.println("verification: one = " + one);

            } finally {
                LocalContext.exit();
            }

        }
    }
    
    /**
     * Measures performance.
     */
    private static void benchmark() throws Exception {
        System.out.println("Benchmark...");
        //ConcurrentContext.setEnabled(false);
        //PoolContext.setEnabled(false);      
 
        Object[] results = new Object[10000];

        System.out.println("");
        System.out.println("Numerics Operations");

        System.out.print("Float64 add: ");
        startTime();
        for (int i = 0; i < 1000; i++) {
            PoolContext.enter();
            Float64 x = Float64.ONE;
            for (int j = 0; j < results.length; j++) {
                results[j] = x.plus(x);
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
                results[j] = x.plus(x);
            }
            PoolContext.exit();
        }
        endTime(1000 * results.length);

        System.out.print("Measure (mass) add: ");
        startTime();
        for (int i = 0; i < 1000; i++) {
            PoolContext.enter();
            Measure<Mass> x = Measure.valueOf(1.0, SI.KILOGRAM);
            for (int j = 0; j < results.length; j++) {
                results[j] = x.plus(x);
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
                results[j] = x.times(x);
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
                results[j] = x.times(x);
            }
            PoolContext.exit();
        }
        endTime(1000 * results.length);

        System.out.print("Measure (mass) multiply: ");
        startTime();
        for (int i = 0; i < 1000; i++) {
            PoolContext.enter();
            Measure<Mass> x = Measure.valueOf(1.0, SI.KILOGRAM);
            for (int j = 0; j < results.length; j++) {
                results[j] = x.times(x);
            }
            PoolContext.exit();
        }
        endTime(1000 * results.length);

        System.out.println();
        System.out.println("LargeInteger (PoolContext) versus BigInteger");
        BigInteger big = BigInteger.probablePrime(1024, new Random());
        byte[] bytes = big.toByteArray();
        LargeInteger large = LargeInteger.valueOf(bytes, 0, bytes.length);

        System.out.print("LargeInteger (1024 bits) addition: ");
        startTime();
        for (int i = 0; i < 100; i++) {
            PoolContext.enter();
            for (int j = 0; j < results.length; j++) {
                results[j] = large.plus(large);
            }
            PoolContext.exit();
        }
        endTime(100 * results.length);

        System.out.print("BigInteger (1024 bits) addition: ");
        startTime();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < results.length; j++) {
                results[j] = big.add(big);
            }
        }
        endTime(100 * results.length);

        System.out.print("LargeInteger (1024 bits) multiplication: ");
        startTime();
        for (int i = 0; i < 10; i++) {
            PoolContext.enter();
            for (int j = 0; j < results.length; j++) {
                results[j] = large.times(large);
            }
            PoolContext.exit();
        }
        endTime(10 * results.length);

        System.out.print("BigInteger (1024 bits) multiplication: ");
        startTime();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < results.length; j++) {
                results[j] = big.multiply(big);
            }
        }
        endTime(10 * results.length);

        System.out.println();
        System.out.println("Matrix<Float64> and Matrix<Complex> versus "
                + "non-parameterized matrix (double)");
        final int size = 500;

        System.out.print("Non-parameterized matrix (double based)"
                + " 500x500 multiplication: ");
        double[][] values = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                values[i][j] = MathLib.random();
            }
        }
        MatrixDouble PM = new MatrixDouble(values);
        PM.times(PM); // Warming up.
        startTime();
        PM.times(PM);
        endTime(1);

        System.out.print("Matrix<Float64> 500x500 multiplication: ");
        double[][] floats = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                floats[i][j] = MathLib.random();
            }
        }
        Matrix<Float64> FM = Float64Matrix.valueOf(floats);
        FM.times(FM); // Warming up.
        startTime();
        FM.times(FM);
        endTime(1);

        System.out.print("Matrix<Complex> 500x500 multiplication: ");
        Complex[][] complexes = new Complex[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                complexes[i][j] = Complex.valueOf(MathLib.random(), MathLib
                        .random());
            }
        }
        Matrix<Complex> CM = ComplexMatrix.valueOf(complexes);
        CM.times(CM); // Warming up.
        startTime();
        CM.times(CM);
        endTime(1);

        System.out.print("Matrix<Measure> 500x500 multiplication: ");
        Measure<?>[][] measures = new Measure<?>[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                measures[i][j] = Measure.valueOf(
                        MathLib.random(Long.MIN_VALUE, Long.MAX_VALUE), Unit.ONE);
            }
        }
        DenseMatrix<Measure<?>> MM = DenseMatrix.valueOf(measures);
        startTime();
        MM.times(MM);
        endTime(1);

        System.out.println();
        System.out.println("More performance analysis in future versions...");
    }

    private static final class MatrixDouble {
        double[][] o;

        int m; // Nbr of rows.

        int n; // Nbr of columns.

        MatrixDouble(double[][] elements) {
            o = elements;
            m = elements.length;
            n = elements[0].length;
        }

        MatrixDouble times(MatrixDouble that) {
            if (that.m != this.n) 
                throw new Error("Wrong dimensions");
            MatrixDouble M = new MatrixDouble(new double[this.m][that.n]);
            double[] thatColj = new double[n];
            for (int j = 0; j < that.n; j++) {
               for (int k = 0; k < n; k++) {
                  thatColj[k] = that.o[k][j];
               }
               for (int i = 0; i < m; i++) {
                  double[] thisRowi = o[i];
                  double s = 0;
                  for (int k = 0; k < n; k++) {
                     s += thisRowi[k]*thatColj[k];
                  }
                  M.o[i][j] = s;
               }
            }
            return M;
        }
    }

    private static void startTime() {
        _time = System.nanoTime();
    }

    /**
     * Ends measuring time and display the execution time per iteration.
     * 
     * @param iterations
     *            the number iterations performed since {@link #startTime}.
     */
    public static void endTime(int iterations) {
        long nanoSeconds = System.nanoTime() - _time;
        long picoDuration = nanoSeconds * 1000 / iterations;
        long divisor;
        String unit;
        if (picoDuration > 1000 * 1000 * 1000 * 1000L) { // 1 s
            unit = " s";
            divisor = 1000 * 1000 * 1000 * 1000L;
        } else if (picoDuration > 1000 * 1000 * 1000L) {
            unit = " ms";
            divisor = 1000 * 1000 * 1000L;
        } else if (picoDuration > 1000 * 1000L) {
            unit = " us";
            divisor = 1000 * 1000L;
        } else {
            unit = " ns";
            divisor = 1000L;
        }
        TextBuilder tb = TextBuilder.newInstance();
        tb.append(picoDuration / divisor);
        int fracDigits = 4 - tb.length(); // 4 digits precision.
        tb.append(".");
        for (int i = 0, j = 10; i < fracDigits; i++, j *= 10) {
            tb.append((picoDuration * j / divisor) % 10);
        }
        System.out.println(tb.append(unit));
    }

    private static long _time;

}