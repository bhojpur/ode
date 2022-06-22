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
 * Each concrete subclass defines transformation parameters for a
 * {@link CodomainMap} implementation.
 */
public abstract class CodomainMapContext {

    /** The lower bound of the codomain interval. */
    protected int intervalStart;

    /** The upper bound of the codomain interval. */
    protected int intervalEnd;

    /**
     * Sets the codomain interval. No checks are needed as this method is
     * controlled by the <code>codomainChain</code>, which passes in
     * consistent values.
     * 
     * @param intervalStart
     *            The lower bound of the codomain interval.
     * @param intervalEnd
     *            The upper bound of the codomain interval.
     */
    public void setCodomain(int intervalStart, int intervalEnd) {
        this.intervalStart = intervalStart;
        this.intervalEnd = intervalEnd;
    }

    /**
     * This method is overridden so that objects of the same class are
     * considered the same. We need this trick to hanlde nicely
     * <code>CodomainMapContext</code> objects in collections.
     * 
     * @see Object#equals(Object)
     */
    @Override
    public final boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        return o.getClass() == getClass();
    }

    /**
     * Computes any parameter that depends on the codomain interval. The
     * <code>codomainChain</code> always calls this method after setting the
     * interval via {@link #setCodomain(int, int) setCodomain()}.
     */
    abstract void buildContext();

    /**
     * Returns an instance of the {@link CodomainMap} class that pairs up with
     * this concrete context class.
     * 
     * @return See above.
     */
    abstract CodomainMap getCodomainMap();

    /**
     * Returns a deep copy of this object.
     * 
     * @return See above.
     */
    public abstract CodomainMapContext copy();

}