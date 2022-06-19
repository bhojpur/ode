package ode.formats.utests;

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

import ode.ServerError;
import ode.api.MetadataStorePrx;
import ode.api.ServiceInterfacePrx;
import ode.api.ServiceFactoryPrx;
import ode.constants.METADATASTORE;

import org.jmock.Mock;
import org.jmock.core.stub.DefaultResultStub;

public class TestServiceFactory
{

    /**
     * Using an instance method to return a proxy.
     * Rather than using the instance itself, we create
     * an actual mock which saves us from needing to
     * implement each method, which breaks fairly
     * often. Only the methods which need overriding
     */
    public ServiceFactoryPrx proxy()
    {
        Mock mock = new Mock(ServiceFactoryPrx.class);
        mock.setDefaultStub(new DefaultResultStub());
        return (ServiceFactoryPrx) mock.proxy();
    }

    public ServiceInterfacePrx getByName(String arg0) throws ServerError
    {
	if (arg0.equals(METADATASTORE.value))
	{
            Mock mock = new Mock(MetadataStorePrx.class);
            mock.setDefaultStub(new DefaultResultStub());
            return (MetadataStorePrx) mock.proxy();
	}
        return null;
    }

}