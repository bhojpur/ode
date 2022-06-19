package ode.services.server.test.fixtures;

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

import ode.model.meta.Session;
import ode.services.util.Executor;
import ode.system.OdeContext;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.matcher.InvokeAtLeastOnceMatcher;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.springframework.beans.factory.FactoryBean;

/**
 * Since the {@link OdeContext} attempts to inject itself into the newly
 * created {@link Executor} it is necessary to first apply an
 * {@link Mock#expects(org.jmock.core.InvocationMatcher) expectation}.
 */
public class ExecutorMockFactory implements FactoryBean {

    Mock executorMock = new Mock(Executor.class);
    {
        executorMock.expects(new InvokeAtLeastOnceMatcher()).method(
                "setApplicationContext");
        executorMock.expects(new InvokeOnceMatcher()).method(
                "executeSql").will(new MockObjectTestCase(){}.returnValue(new Session()));
        executorMock.expects(new InvokeOnceMatcher()).method(
                "executeSql").will(new MockObjectTestCase(){}.returnValue(1L));
        executorMock.expects(new InvokeOnceMatcher()).method(
                "execute").will(new MockObjectTestCase(){}.returnValue(new java.util.ArrayList()));
    }

    public Object getObject() throws Exception {
        return executorMock;
    }

    public Class getObjectType() {
        return Mock.class;
    }

    public boolean isSingleton() {
        return true;
    }

}