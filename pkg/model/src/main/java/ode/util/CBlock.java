package ode.util;

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

import ode.model.IObject;
import ode.model.core.Image;

/**
 * Block template used to "C"ollect the results of some function called on each
 * {@link ode.model.IObject IObject} in a collection. The {@link CBlock} can be
 * used to "map" {@link IObject} inputs to arbitrary outputs. All collection
 * valued fields on model objects have a method that will scan the collection
 * and apply the block of code. For example, {@link Image#collectPixels(CBlock)}
 */
public interface CBlock<E> {

    /**
     * invoke this block.
     * 
     * @param object
     *            An IObject (possibly null) which should be considered for
     *            mapping.
     * @return A possibly null value which is under some interpretation "mapped"
     *         to the object argument
     */
    E call(IObject object);

}