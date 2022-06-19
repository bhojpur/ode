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
import java.util.Map;

import ode.model.IObject;

/**
 * Factory for all available enumeration handlers.
 */
public class EnumHandlerFactory
{
    /** A list of enumeration handlers for specific enumeration types. */
    private static Map<Class<? extends IObject>, EnumerationHandler> handlers =
	new HashMap<Class<? extends IObject>, EnumerationHandler>();

    static
    {
	handlers.put(CorrectionEnumHandler.HANDLER_FOR,
			     new CorrectionEnumHandler());
	handlers.put(ImmersionEnumHandler.HANDLER_FOR,
	            new ImmersionEnumHandler());
	handlers.put(OriginalFileFormatEnumHandler.HANDLER_FOR,
	        new OriginalFileFormatEnumHandler());
	handlers.put(DetectorTypeEnumHandler.HANDLER_FOR,
	        new DetectorTypeEnumHandler());
    }

    /** Our fall through no-op enumeration handler. */
    private EnumerationHandler noopHandler = new NoopEnumHandler();

    /**
     * Returns an enumeration handler for a specific enumeration type.
     * @param klass Enumeration type to retrieve a handler for.
     * @return See above.
     */
    public EnumerationHandler getHandler(Class<? extends IObject> klass)
    {
	return handlers.containsKey(klass)? handlers.get(klass) : noopHandler;
    }
}