package odeis.providers.re.codomain;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

/**
 * We consider that the image is composed of eight <code>1-bit</code> planes
 * ranging from bit-plane <code>0</code> for the least significant bit to
 * bit-plane <code>7</code> for the most significant bit. The BIT_* constants
 * cannot be modified b/c they have a meaning.
 */
public class PlaneSlicingContext extends CodomainMapContext {

    /** Identifies the bit-plane <i>0</i> i.e. <i>2^1-1</i> value. */
    public static final int BIT_ZERO = 0;

    /** Identifies the bit-plane <i>1</i> i.e. <i>2^1</i> value. */
    public static final int BIT_ONE = 2;

    /** Identifies the bit-plane <i>2</i> i.e. <i>2^2</i> value. */
    public static final int BIT_TWO = 4;

    /** Identifies the bit-plane <i>3</i> i.e. <i>2^3</i> value. */
    public static final int BIT_THREE = 8;

    /** Identifies the bit-plane <i>4</i> i.e. <i>2^4</i> value. */
    public static final int BIT_FOUR = 16;

    /** Identifies the bit-plane <i>5</i> i.e. <i>2^5</i> value. */
    public static final int BIT_FIVE = 32;

    /** Identifies the bit-plane <i>6</i> i.e. <i>2^6</i> value. */
    public static final int BIT_SIX = 64;

    /** Identifies the bit-plane <i>7</i> i.e. <i>2^7</i> value. */
    public static final int BIT_SEVEN = 128;

    /** Identifies the bit-plane <i>8</i> i.e. <i>2^8-1</i> value. */
    public static final int BIT_EIGHT = 255;

    /**
     * The constant level for bit-planes &gt; planeSelected w.r.t the
     * higher-order bits.
     */
    private int upperLimit;

    /**
     * The constant level for bit-planes &lt; planeSelected w.r.t the
     * higher-order bits.
     */
    private int lowerLimit;

    /** The value corresponding to the index of the bit-plane selected. */
    private int planeSelected;

    /**
     * The value corresponding to the index of the bit-plane ranged just before
     * the one selected.
     */
    private int planePrevious;

    /**
     * Indicates the way the bit-planes are mapped i.e. <code>false</code> if
     * the bit-planes aren't mapped to a specified level <code>true</code>
     * otherwise.
     */
    private boolean constant;

    /**
     * Controlrs if the specified value is supported.
     * 
     * @param bitPlane
     *            The value to control. Must be one of the constants defined by
     *            this class.
     * @throws IllegalArgumentException
     *             If the value is not supported.
     */
    private void verifyBitPlanes(int bitPlane) {
        switch (bitPlane) {
            case BIT_ZERO:
            case BIT_ONE:
            case BIT_TWO:
            case BIT_THREE:
            case BIT_FOUR:
            case BIT_FIVE:
            case BIT_SIX:
            case BIT_SEVEN:
            case BIT_EIGHT:
                return;
            default:
                throw new IllegalArgumentException("Not a valid plane.");
        }

    }

    /**
     * Controls if the specified value is in the interval [{@link #intervalStart},
     * {@link #intervalEnd}].
     * 
     * @param x
     *            The value to control.
     * @throws IllegalArgumentException
     *             If the value is not in the interval.
     */
    private void verifyInput(int x) {
        if (x < intervalStart || x > intervalEnd) {
            throw new IllegalArgumentException("Value not in the interval.");
        }
    }

    /** Empty private contructor used to make a copy of the object. */
    private PlaneSlicingContext() {
    }

    /**
     * Creates a new instance.
     * 
     * @param planePrevious
     *            The value corresponding to the index of the bit-plane ranged
     *            just before the one selected.
     * @param planeSelected
     *            The value corresponding to the index of the selected
     *            bit-plane.
     * @param constant
     *            Passed <code>false</code> if the bit-planes aren't mapped to
     *            a specified level <code>true</code> otherwise.
     */
    public PlaneSlicingContext(int planePrevious, int planeSelected,
            boolean constant) {
        if (planePrevious > planeSelected) {
            throw new IllegalArgumentException("Not a valid plane selection");
        }
        verifyBitPlanes(planePrevious);
        verifyBitPlanes(planeSelected);
        this.planePrevious = planePrevious;
        this.planeSelected = planeSelected;
        this.constant = constant;
    }

    /**
     * Implemented as specified by superclass.
     * 
     * @see CodomainMapContext#buildContext()
     */
    @Override
    void buildContext() {
    }

    /**
     * Implemented as specified by superclass.
     * 
     * @see CodomainMapContext#getCodomainMap()
     */
    @Override
    CodomainMap getCodomainMap() {
        return new PlaneSlicingMap();
    }

    /**
     * Implemented as specified by superclass.
     * 
     * @see CodomainMapContext#copy()
     */
    @Override
    public CodomainMapContext copy() {
        PlaneSlicingContext copy = new PlaneSlicingContext();
        copy.intervalEnd = intervalEnd;
        copy.intervalStart = intervalStart;
        copy.upperLimit = upperLimit;
        copy.lowerLimit = lowerLimit;
        copy.planeSelected = planeSelected;
        copy.planePrevious = planePrevious;
        copy.constant = constant;
        return copy;
    }

    /**
     * Returns the value corresponding to the index of the bit-plane ranged just
     * before the one selected.
     * 
     * @return See above.
     */
    public int getPlanePrevious() {
        return planePrevious;
    }

    /**
     * Returns the value corresponding to the index of the selected bit-plane.
     * 
     * @return See above.
     */
    public int getPlaneSelected() {
        return planeSelected;
    }

    /**
     * Returns the type of bit-planes mapping. Returns <code>false</code> if
     * the bit-planes aren't mapped to a specified level <code>true</code>
     * otherwise.
     * 
     * @return See above.
     */
    public boolean IsConstant() {
        return constant;
    }

    /**
     * Returns the constant level for bit-planes &lt; planeSelected w.r.t the
     * higher-order bits.
     * 
     * @return See above.
     */
    public int getLowerLimit() {
        return lowerLimit;
    }

    /**
     * Returns the constant level for bit-planes &gt; planeSelected w.r.t the
     * higher-order bits.
     * 
     * @return See above.
     */
    public int getUpperLimit() {
        return upperLimit;
    }

    /**
     * Set the limits.
     * 
     * @param lowerLimit
     *            The value (in [intervalStart, intervalEnd]) used to set the
     *            level of the bit-plane &lt; bit-plane selected.
     * @param upperLimit
     *            The value (in [intervalStart, intervalEnd]) used to set the
     *            level of the bit-plane &gt; bit-plane selected.
     */
    public void setLimits(int lowerLimit, int upperLimit) {
        verifyInput(lowerLimit);
        verifyInput(upperLimit);
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    /**
     * Sets the value (in [intervalStart, intervalEnd]) used to set the level of
     * the bit-plane &lt; bit-plane selected.
     * 
     * @param v
     *            The value to set.
     */
    public void setLowerLimit(int v) {
        verifyInput(v);
        lowerLimit = v;
    }

    /**
     * Sets the value (in [intervalStart, intervalEnd]) used to set the level of
     * the bit-plane &gt; bit-plane selected.
     * 
     * @param v
     *            The value to set.
     */
    public void setUpperLimit(int v) {
        verifyInput(v);
        upperLimit = v;
    }

    /**
     * Sets the value of the selected planes i.e. the previous plane and the
     * selected one.
     * 
     * @param planePrevious
     *            The plane to set.
     * @param planeSelected
     *            The plane to set.
     * @throws IllegalArgumentException
     *             If the value of the previous is not greater than the value of
     *             the selected one.
     */
    public void setPlanes(int planePrevious, int planeSelected) {
        if (planePrevious > planeSelected) {
            throw new IllegalArgumentException("Not a valid plane selection");
        }
        verifyBitPlanes(planePrevious);
        verifyBitPlanes(planeSelected);
        this.planePrevious = planePrevious;
        this.planeSelected = planeSelected;
    }

    /**
     * Sets the type of mapping.
     * 
     * @param b
     *            The value to set.
     */
    public void setConstant(boolean b) {
        constant = b;
    }

}