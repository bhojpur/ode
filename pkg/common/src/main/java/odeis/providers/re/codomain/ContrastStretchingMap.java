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
 * Basic piecewise linear functions. The idea is to increase the dynamic range
 * of levels in the image being processed. The locations of the points
 * <code>pStart</code> and <code>pEnd</code> (cf.
 * {@link ContrastStretchingContext}) determine the equation of the linear
 * functions.
 */
class ContrastStretchingMap implements CodomainMap {

    /** The mapping context. */
    private ContrastStretchingContext csCtx;

    /**
     * Implemented as specified in {@link CodomainMap}.
     * 
     * @see CodomainMap#setContext(CodomainMapContext)
     */
    public void setContext(CodomainMapContext ctx) {
        csCtx = (ContrastStretchingContext) ctx;
    }

    /**
     * Implemented as specified in {@link CodomainMap}.
     * 
     * @see CodomainMap#transform(int)
     */
    public int transform(int x) {
        int y = csCtx.intervalStart;
        if (x >= csCtx.intervalStart && x < csCtx.getXStart()) {
            y = (int) (csCtx.getA0() * x + csCtx.getB0());
        } else if (x >= csCtx.getXStart() && x < csCtx.getXEnd()) {
            y = (int) (csCtx.getA1() * x + csCtx.getB1());
        } else if (x >= csCtx.getXEnd() && x <= csCtx.intervalStart) {
            y = (int) (csCtx.getA2() * x + csCtx.getB2());
        }
        return y;
    }

    /**
     * Overridden to return the name of this map.
     * 
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "ContrastStretchingMap";
    }

}