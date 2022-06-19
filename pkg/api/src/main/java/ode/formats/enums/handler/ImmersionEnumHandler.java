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

import ode.formats.enums.EnumerationException;
import ode.model.IObject;
import ode.model.Immersion;

/**
 * An enumeration handler that handles enumerations of type Correction.
 */
class ImmersionEnumHandler implements EnumerationHandler
{
    /** Class we're a handler for. */
    static final Class<? extends IObject> HANDLER_FOR = Immersion.class;

    /** Array of enumeration patterns this handler uses for searching. */
    private static final PatternSet[] searchPatterns = new PatternSet[]
        {
            new PatternSet("^\\s*oil.*$", "Oil"),
            new PatternSet("^\\s*OI.*$", "Oil"),
            new PatternSet("^\\s*W", "Water"),
            new PatternSet("^\\s*UV", "Unknown"),
            new PatternSet("^\\s*Plan.*$", "Unknown"), // TODO: Remove when .nd2 bug which puts correction into immersion is fixed
            new PatternSet("^\\s*DRY", "Air") //TODO: This needs to be changed to "Air" when immersion enum bug is fixed in 4.1
        };

    /* (non-Javadoc)
     * @see ode.formats.enums.handler.EnumerationHandler#findEnumeration(java.util.HashMap, java.lang.String)
     */
    public IObject findEnumeration(HashMap<String, IObject> enumerations,
                                 String value)
    {
        for (PatternSet x : searchPatterns)
        {
            if (x.pattern.matcher(value).matches())
            {
                IObject enumeration = enumerations.get(x.value);
                if (enumeration == null)
                {
                    throw new EnumerationException(String.format(
                            "Matched value %s with regex %s. Could not " +
                            "find resulting value in enumerations.",
                            x.pattern.pattern(), x.value), HANDLER_FOR, value);
                }
                return enumeration;
            }
        }
        return null;
    }
}