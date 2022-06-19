package ode.services.server.test.utests;

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

import java.util.List;

import ode.api.IQuery;
import ode.services.server.util.IceMethodInvoker;
import ode.services.throttling.Callback;
import ode.api.AMD_IQuery_findAllByQuery;
import ode.model.IObject;
import ode.util.IceMapper;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CallbackTest extends MockObjectTestCase {

    Mock mock;
    IQuery query;
    IceMethodInvoker invoker;
    Ice.Current current;

    @BeforeMethod
    public void init() {
        mock = mock(IQuery.class);
        query = (IQuery) mock.proxy();
        invoker = new IceMethodInvoker(IQuery.class, null);
        current = new Ice.Current();
        current.operation = "findAllByQuery";
    }

    @Test
    public void testVisibilityOfCalbackMethods() throws Exception {
        _AMD_IQuery_findAllByQuery_expectResponse amd = new _AMD_IQuery_findAllByQuery_expectResponse();
        Callback cb = new Callback(query, invoker, new IceMapper(), amd,
                current, "query", null);
        mock.expects(once()).method("findAllByQuery");
        cb.run(null);
        assertTrue(amd.response);
    }

    private static class _AMD_IQuery_findAllByQuery_expectResponse implements
            AMD_IQuery_findAllByQuery {

        boolean response = false;

        public void ice_exception(Exception ex) {
            throw new RuntimeException("Exception thrown: ", ex);
        }

        public void ice_response(List<IObject> __ret) {
            response = true;
        }

    }
}