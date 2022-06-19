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

import ode.formats.enums.EnumerationProvider;
import ode.model.IObject;
import ode.model.Laser;
import ode.model.LaserMedium;
import ode.model.LaserType;

/**
 * A model object handler that handles objects of type Laser.
 */
class LaserHandler implements ModelObjectHandler
{
	/** Our enumeration provider. */
	private EnumerationProvider enumProvider;

	/** The class we're a handler for. */
	static final Class<? extends IObject> HANDLER_FOR = Laser.class;

	/**
	 * Default constructor.
	 * @param enumHandler Enumeration provider we are to use.
	 */
	LaserHandler(EnumerationProvider enumProvider)
	{
		this.enumProvider = enumProvider;
	}

	/* (non-Javadoc)
	 * @see ode.formats.model.handler.ModelObjectHandler#handle(ode.model.IObject)
	 */
	public IObject handle(IObject object)
	{
		Laser o = (Laser) object;
		o.setLaserMedium(enumProvider.getEnumeration(
				LaserMedium.class, "Unknown", false));
		o.setType(enumProvider.getEnumeration(
				LaserType.class, "Unknown", false));
		return object;
	}

}