/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.model;

import java.util.Map;
import javolution.context.LocalContext;
import org.jscience.physics.unit.converter.PhysicsUnitConverter;

/**
 * <p> This class represents the model used for dimensional analysis.</p>
 *
 * <p> The default model is {@link StandardModel Standard}. Applications may
 *     use one of the predefined model or create their own.
 *     [code]
 *     PhysicsModel relativistic = new PhysicsModel() {
 *         public Dimension getFundamentalDimension(PhysicsDimension dimension) {
 *             if (dimension.equals(PhysicsDimension.LENGTH)) return PhysicsDimension.TIME; // Consider length derived from time.
 *                 return super.getDimension(dimension); // Returns product of fundamental dimension.
 *             }
 *             public UnitConverter getDimensionalTransform(PhysicsDimension dimension) {
 *                 if (dimension.equals(PhysicsDimension.LENGTH)) return new RationalConverter(1, 299792458); // Converter (1/C) from LENGTH SI unit (m) to TIME SI unit (s).
 *                 return super.getDimensionalTransform(dimension);
 *             }
 *     };
 *     LocalContext.enter();
 *     try {
 *         PhysicsModel.setCurrent(relativistic); // Current thread use the relativistic model.
 *         SI.KILOGRAM.getConverterToAny(SI.JOULE); // Allowed.
 *         ...
 *     } finally {
 *         LocalContext.exit();
 *     }
 *     [/code]</p>
 *     
 * @see <a href="http://en.wikipedia.org/wiki/Dimensional_analysis">Wikipedia: Dimensional Analysis</a>
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public abstract class PhysicsModel {

    /**
     * Holds the getCurrent model.
     */
    private static LocalContext.Reference<PhysicsModel> Current = new LocalContext.Reference<PhysicsModel>(new StandardModel());

    /**
     * Returns the physics model used by the current thread
     * (by default an instance of {@link StandardModel}).
     *
     * @return the getCurrent physical model.
     * @see LocalContext
     */
    public static PhysicsModel getCurrent() {
        return PhysicsModel.Current.get();
    }

    /**
     * Sets the current physics model (local to the current thread when executing
     * within a {@link LocalContext}).
     *
     * @param  model the context-local physics model.
     * @see    #getCurrent
     */
    public static void setCurrent(PhysicsModel model) {
        PhysicsModel.Current.set(model);
    }

    /**
     * Default constructor (allows for derivation).
     */
    protected PhysicsModel() {
    }

    /**
     * Returns the fundamental dimension for the one specified.
     * If the specified dimension is a dimensional product, the dimensional
     * product its fundamental dimensions is returned.
     * Physical quantities are considered commensurate only if their
     * fundamental dimensions are equals using the current physics model.
     *
     * @param dimension the dimension for which the fundamental dimension is returned.
     * @return <code>this</code> or a rational product of fundamental dimension.
     */
    public PhysicsDimension getFundamentalDimension(PhysicsDimension dimension) {
        Map<? extends PhysicsDimension, Integer> dimensions = dimension.getProductDimensions();
        if (dimensions == null) return dimension; // Fundamental dimension.
        // Dimensional Product.
        PhysicsDimension fundamentalProduct = PhysicsDimension.NONE;
        for (Map.Entry<? extends PhysicsDimension, Integer> e : dimensions.entrySet()) {
             fundamentalProduct = fundamentalProduct.multiply(this.getFundamentalDimension(e.getKey())).pow(e.getValue());
        }
        return fundamentalProduct;
    }

    /**
     * Returns the dimensional transform of the specified dimension.
     * If the specified dimension is a fundamental dimension or
     * a product of fundamental dimensions the identity converter is
     * returned; otherwise the converter from the system unit (SI) of
     * the specified dimension to the system unit (SI) of its fundamental
     * dimension is returned.
     *
     * @param dimension the dimension for which the dimensional transform is returned.
     * @return the dimensional transform (identity for fundamental dimensions).
     */
    public PhysicsUnitConverter getDimensionalTransform(PhysicsDimension dimension) {
        Map<? extends PhysicsDimension, Integer> dimensions = dimension.getProductDimensions();
        if (dimensions == null) return PhysicsUnitConverter.IDENTITY; // Fundamental dimension.
        // Dimensional Product.
        PhysicsUnitConverter toFundamental = PhysicsUnitConverter.IDENTITY;
        for (Map.Entry<? extends PhysicsDimension, Integer> e : dimensions.entrySet()) {
            PhysicsUnitConverter cvtr = this.getDimensionalTransform(e.getKey());
            if (!(cvtr.isLinear()))
                throw new UnsupportedOperationException("Non-linear dimensional transform");
            int pow = e.getValue();
            if (pow < 0) { // Negative power.
                pow = -pow;
                cvtr = cvtr.inverse();
            }
            for (int j = 0; j < pow; j++) {
                toFundamental = toFundamental.concatenate(cvtr);
            }
        }
        return toFundamental;
    }

}
