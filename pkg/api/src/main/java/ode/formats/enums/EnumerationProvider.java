package ode.formats.enums;

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

import java.util.HashMap;

import ode.model.IObject;

/**
 * An enumeration provider, whose job is to make Bhojpur ODE enumerations available
 * to a consumer based on a set of criteria. Fundamentally, concrete
 * implementations are designed to isolate consumers from the semantics of
 * Bhojpur ODE services such as IQuery and IObject and to provide a consistent, server
 * agnostic API to unit test code.
 */
public interface EnumerationProvider
{
    /**
     * Retrieves an enumeration.
     * @param klass Enumeration's base class from <code>ode.model.enums</code>.
     * @param value Enumeration's string value.
     * @param loaded <code>true</code> if the enumeration returned should be
     * loaded, otherwise <code>false</code>.
     * @return Enumeration object.
     */
	<T extends IObject> T getEnumeration(Class<T> klass, String value,
		               boolean loaded);

    /**
     * Retrieves all enumerations of a specific type.
     * @param klass Enumeration's base class from <code>ode.model.enums</code>.
     * @return Enumeration object.
     */
	<T extends IObject> HashMap<String, T> getEnumerations(Class<T> klass);
}