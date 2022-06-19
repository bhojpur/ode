package ode.formats.model.handler;

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

import ode.formats.enums.EnumerationProvider;
import ode.model.IObject;

/**
 * Factory for all available model handlers.
 */
public class ModelObjectHandlerFactory
{
    /** A list of model object handlers for specific types. */
    private static Map<Class<? extends IObject>, ModelObjectHandler> handlers =
	new HashMap<Class<? extends IObject>, ModelObjectHandler>();

    /**
     * Default constructor.
     * @param enumProvider Enumeration provider for this factory to use.
     */
    public ModelObjectHandlerFactory(EnumerationProvider enumProvider)
    {
	handlers.put(ObjectiveHandler.HANDLER_FOR,
			     new ObjectiveHandler(enumProvider));
	handlers.put(DetectorHandler.HANDLER_FOR,
			     new DetectorHandler(enumProvider));
	handlers.put(ArcHandler.HANDLER_FOR,
			     new ArcHandler(enumProvider));
	handlers.put(FilamentHandler.HANDLER_FOR,
			     new FilamentHandler(enumProvider));
	handlers.put(LaserHandler.HANDLER_FOR,
			     new LaserHandler(enumProvider));
	handlers.put(LogicalChannelHandler.HANDLER_FOR,
	        new LogicalChannelHandler(enumProvider));
        handlers.put(MicroscopeHandler.HANDLER_FOR,
                new MicroscopeHandler(enumProvider));
        handlers.put(ExperimentHandler.HANDLER_FOR,
                new ExperimentHandler(enumProvider));
    }

    /** Our fall through no-op enumeration handler. */
    private ModelObjectHandler noopHandler = new NoopModelObjectHandler();

    /**
     * Returns an enumeration handler for a specific enumeration type.
     * @param klass Enumeration type to retrieve a handler for.
     * @return See above.
     */
    public ModelObjectHandler getHandler(Class<? extends IObject> klass)
    {
	return handlers.containsKey(klass)? handlers.get(klass) : noopHandler;
    }
}