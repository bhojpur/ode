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
 * Reverses the intensity levels of an image. It produces the equivalent of a
 * photographic negative. This type of transformation is suited for enhancing
 * white or gray details embedded in dark regions of an image, especially when
 * the black areas are dominant in size.
 * 
 * @deprecated This class will be renamed to InverseIntensityMap (or similar) in
 *             future versions
 */
class ReverseIntensityMap implements CodomainMap {

    /** The mapping context of this map. */
    private CodomainMapContext ctx;

    /**
     * Implemented as specified in {@link CodomainMap}.
     * 
     * @see CodomainMap#setContext(CodomainMapContext)
     */
    public void setContext(CodomainMapContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Implemented as specified in {@link CodomainMap}.
     * 
     * @see CodomainMap#transform(int)
     */
    public int transform(int x) {
        return ctx.intervalEnd - x;
    }

    /**
     * Overridden to return the name of this map.
     * 
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "ReverseIntensityMap";
    }

}