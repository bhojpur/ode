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
 * Declares the interface common to all codomain maps. Subclasses encapsulates a
 * parameterized map:
 * <p>
 * <code>
 * y = f(x, p[1], ..., p[n])
 * </code>
 * </p>
 * <p>
 * The actual values of the parameters <code>p[k]</code> are defined by the
 * {@link CodomainMapContext} which is passed to the
 * {@link #setContext(CodomainMapContext) setContext} method.
 * </p>
 */
public interface CodomainMap {

    /**
     * Sets the parameters used to write the equation of the specified codomain
     * transformation.
     * 
     * @param cxt
     *            Specifies the parameters.
     */
    public void setContext(CodomainMapContext cxt);

    /**
     * Performs the transformation.
     * 
     * @param x
     *            The input value.
     * @return The output value, y.
     */
    public int transform(int x);

}