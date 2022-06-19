package ode.formats.enums.handler;

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
 * An enumeration handler whose purpose is to provide extra logic, such as
 * regular expression matching, for the lookup of enumerations for a specific
 * type.
 */
public interface EnumerationHandler
{

	/**
	 * Attempt to find an enumeration from an existing set of enumeration
	 * values.
	 * @param enumerations Exhaustive set of enumerations of this type.
	 * @param value Value to look for an enumeration of.
	 * @return An IObject object or <code>null</code>.
	 */
	IObject findEnumeration(HashMap<String, IObject> enumerations, String value);
}