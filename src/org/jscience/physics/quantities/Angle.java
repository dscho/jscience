/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.quantities;

import javolution.lang.MathLib;
import org.jscience.physics.units.Unit;
import static org.jscience.physics.units.SI.*;

/**
 * This class represents the figure formed by two lines diverging from a common
 * point. The system unit for this quantity is "rad" (Système International
 * d'Unités).
 *
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 12, 2005
 */
public class Angle extends Dimensionless {

    /**
     * Holds the acceleration unit.
     */
    private final static Unit<Angle> UNIT = RADIAN;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Angle> FACTORY = new Factory<Angle>(
            UNIT) {
        protected Angle create() {
            return new Angle();
        }
    };

    /**
     * Represents a {@link Angle} amounting to nothing.
     */
    public final static Angle ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Angle() {
    }

    /**
     * Shows {@link Angle} instances in the specified unit.
     *
     * @param unit the display unit for {@link Angle} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Angle.class, unit);
    }

    ////////////////////
    // ANGLE SPECIFIC //
    ////////////////////

    /**
     * Converts rectangular coordinates (<code>x</code>,&nbsp;<code>y</code>)
     * to polar (&rho;,&nbsp;<i>&theta;</i>).
     * This method computes the phase <i>&theta;</i> by computing an arc tangent
     * of <code>y/x</code>.
     *
     * @param  y   y coordinate
     * @param  x   x coordinate
     * @return the <i>&theta;</i> component of the point
     *         (<i>&rho;</i>,&nbsp;<i>&theta;</i>) in polar coordinates that
     *         corresponds to the point(<i>x</i>,&nbsp;<i>y</i>) in Cartesian
     *         coordinates.
     */
    public static <Q extends Quantity> Angle atan2(Q y, Q x) {
        if ((x.getMinimum() < 0.0) && (x.getMaximum() > 0.0) &&
            (y.getMinimum() < 0.0) && (y.getMaximum() > 0.0)) {
            // Encompasses (0,0), all angles are possible
            return valueOf(0, MathLib.PI, RADIAN);
        }
        double a1 = MathLib.atan2(y.getMinimum(), x.getMinimum());
        double a2 = MathLib.atan2(y.getMinimum(), x.getMaximum());
        double a3 = MathLib.atan2(y.getMaximum(), x.getMinimum());
        double a4 = MathLib.atan2(y.getMaximum(), x.getMaximum());
        // Discontinuity at -Pi
        if (x.getMaximum() <= 0.0) {
            if (a1 < 0) {
                a1 += 2 * MathLib.PI;
            }
            if (a2 < 0) {
                a2 += 2 * MathLib.PI;
            }
            if (a3 < 0) {
                a3 += 2 * MathLib.PI;
            }
            if (a4 < 0) {
                a4 += 2 * MathLib.PI;
            }
        }
        // Search for minimum
        double min = a1;
        if (a2 < min) {
            min = a2;
        }
        if (a3 < min) {
            min = a3;
        }
        if (a4 < min) {
            min = a4;
        }
        // Search for maximum
        double max = a1;
        if (a2 > max) {
            max = a2;
        }
        if (a3 > max) {
            max = a3;
        }
        if (a4 > max) {
            max = a4;
        }
        return Quantity.rangeOf(min, max, RADIAN);
    }

    /**
     * Returns the trigonometric sine of this {@link Angle}.
     *
     * @return the sine of this angle.
     */
    public Dimensionless sine() {
        return sine(this.getMinimum(), this.getMaximum());
    }

    /**
     * Returns the trigonometric cosine of this {@link Angle}.
     *
     * @return the cosine of this angle.
     */
    public Dimensionless cos() {
        return sine(this.getMinimum() + SQUARE_ANGLE, 
                this.getMaximum() + SQUARE_ANGLE);
    }
    private static final double SQUARE_ANGLE =  MathLib.PI / 2;

    /**
     * Returns the trigonometric tangent of this {@link Angle}.
     *
     * @return the tangent of this angle.
     */
    public Dimensionless tan() {
        double minRadian = this.getMinimum();
        double maxRadian = this.getMaximum();
        // Bounds min to [-Pi/2, Pi/2]
        double minBounded = MathLib.rem(minRadian, MathLib.PI);
        // Measures distance to the next discontinuity
        double nextDisc = MathLib.PI / 2 - minBounded;
        // Test if this quantity passes across a discontinuity
        if (minRadian + nextDisc < maxRadian) {
            return Quantity.rangeOf(
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Unit.ONE);
        }
        return Quantity.rangeOf(MathLib.tan(minRadian),
                MathLib.tan(maxRadian), Unit.ONE);
    }

    /**
     * Returns the trigonometric sine for the specified minimum and maximum
     * {@link Angle}.
     *
     * @param  min the angle minimumm.
     * @param  max the angle maximumm.
     * @return the sine quantity corresponding to the specified interval.
     */
    private Dimensionless sine(double min, double max) {
        // Bounds min to [-Pi, Pi]
        double minBounded = MathLib.rem(min, 2.0 * MathLib.PI);
        double nextMax = MathLib.PI / 2 - minBounded;
        if (nextMax < 0) {
            nextMax += 2 * MathLib.PI;
        }
        double nextMin = -MathLib.PI / 2 - minBounded;
        if (nextMin < 0) {
            nextMin += 2 * MathLib.PI;
        }
        double sineMin = MathLib.sin(min);
        double sineMax = MathLib.sin(max);
        if (sineMin > sineMax) { // Swaps.
            double tmp = sineMin;
            sineMin = sineMax;
            sineMax = tmp;
        }
        // Test if angle passes across a minima or a maxima
        if (min + nextMax < max) {
            sineMax = 1.0;
        }
        if (min + nextMin < max) {
            sineMin = -1.0;
        }
        return Quantity.rangeOf(sineMin, sineMax, Unit.ONE);
    }

    private static final long serialVersionUID = 1L;

}