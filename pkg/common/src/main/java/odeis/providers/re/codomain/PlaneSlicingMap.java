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
 * We assume that an image is composed of eight <code>1-bit</code> planes. Two
 * types of plane Slicing transformations {@link #transformConstant} and
 * {@link #transformNonConstant} are available. Let l denote the level of the
 * <code>planeSelected</code>. 1- Map all levels &lt; l to the constant
 * <code>lowerLimit</code> and the levels &gt; l to the constant
 * <code>upperLimit</code>. This transformation highlights the range l and
 * reduces all others to a constant level. 2- This transformation highlights the
 * rang l and preserves all other levels.
 */
class PlaneSlicingMap implements CodomainMap {

    /** The mapping context of this map. */
    private PlaneSlicingContext psCtx;

    /**
     * Highlights the level of the <code>planeSelected</code> and reduces all
     * others to a constant level.
     * 
     * @param x
     *            The value to transform.
     * @return The transformed value.
     */
    private int transformConstant(int x) {
        if (x < psCtx.getPlaneSelected()) {
            return psCtx.getLowerLimit();
        } else if (x > psCtx.getPlaneSelected() + 1) {
            return psCtx.getUpperLimit();
        }
        return psCtx.getPlaneSelected();
    }

    /**
     * Highlights the level of the <code>planeSelected</code> but preserves
     * all other levels.
     * 
     * @param x
     *            The value to transform.
     * @return The transformed value.
     */
    private int transformNonConstant(int x) {
        if (x > psCtx.getPlanePrevious() && x <= psCtx.getPlaneSelected()) {
            return psCtx.getPlaneSelected();
        }
        return x;
    }

    /**
     * Implemented as specified in {@link CodomainMap}.
     * 
     * @see CodomainMap#setContext(CodomainMapContext)
     */
    public void setContext(CodomainMapContext ctx) {
        psCtx = (PlaneSlicingContext) ctx;
    }

    /**
     * Implemented as specified in {@link CodomainMap}.
     * 
     * @see CodomainMap#transform(int)
     */
    public int transform(int x) {
        if (psCtx.IsConstant()) {
            return transformConstant(x);
        }
        return transformNonConstant(x);
    }

    /**
     * Overridden to return the name of this map.
     * 
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "PlaneSlicingMap";
    }

}