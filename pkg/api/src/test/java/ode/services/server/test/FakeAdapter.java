package ode.services.server.test;

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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jmock.Mock;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;

import ode.services.server.repo.LegacyRepositoryI;

import ode.grid.InternalRepositoryPrx;

public class FakeAdapter implements Stub {

    private final Map<String, Object> servants = new HashMap<String, Object>();

    private final Map<String, Object> proxies = new HashMap<String, Object>();

    public StringBuffer describeTo(StringBuffer arg0) {
        return arg0;
    }

    public Object invoke(Invocation arg0) throws Throwable {
        if (arg0.invokedMethod.getName().equals("add")) {
            return ice_add(arg0.parameterValues);
        } else if (arg0.invokedMethod.getName().equals("createDirectProxy")) {
            return ice_createDirectProxy(arg0.parameterValues);
        } else if (arg0.invokedMethod.getName().equals("find")) {
            return ice_find(arg0.parameterValues);
        } else {
            throw new RuntimeException("Unknown method: "
                    + arg0.invokedMethod);
        }
    }

    private Object ice_add(List parameterValues) {
        Ice.Object servant = (Ice.Object) parameterValues.get(0);
        Ice.Identity id = (Ice.Identity) parameterValues.get(1);
        String key = Ice.Util.identityToString(id);
        servants.put(key, servant);
        return null;
    }

    private Object ice_createDirectProxy(List parameterValues) throws Exception {
        Ice.Identity id = (Ice.Identity) parameterValues.get(0);
        String key = Ice.Util.identityToString(id);
        Object proxy = proxies.get(key);
        if (proxy == null) {
            Object servant = servants.get(key);
            Mock mock = new Mock(findProxyClass(servant));
            mock.setDefaultStub(new FakeProxy(id, servant));
            proxy = mock.proxy();
            proxies.put(key, proxy);
        }
        return proxy;

    }

    private Class<?> findProxyClass(Object servant) throws Exception {
        if (servant instanceof LegacyRepositoryI) {
            return InternalRepositoryPrx.class;
        } else if (servant instanceof Ice.TieBase) {
            String name = servant.getClass().getName();
            String[] parts = name.split("\\.");
            name = parts[parts.length-1]; // Take last part
            name = name.substring(1); // strip leading "_"
            name = name.substring(0, name.length()-3); // strip trailing "Tie"
            parts[parts.length-1] = name + "Prx"; // append prx for iface.
            return Class.forName(StringUtils.join(parts, "."));
        } else {
            throw new RuntimeException(
                    "Don't know how to find proxy class for: " +
                    servant);
        }
    }

    private Object ice_find(List parameterValues) {
        Ice.Identity id = (Ice.Identity) parameterValues.get(0);
        String key = Ice.Util.identityToString(id);
        return servants.get(key);
    }

    public Ice.Object findByProxy(Ice.ObjectPrx prx) {
        Ice.Identity id = prx.ice_getIdentity();
        String key = Ice.Util.identityToString(id);
        return (Ice.Object) servants.get(key);
    }
}

class FakeProxy implements Stub {

    private final Ice.Identity id;

    private final Object servant;

    FakeProxy(Ice.Identity id, Object servant) {
        this.id = id;
        this.servant = servant;
    }

    public StringBuffer describeTo(StringBuffer arg0) {
        return arg0;
    }

    public Object invoke(Invocation arg0) throws Throwable {
        if (arg0.invokedMethod.getName().equals("ice_getIdentity")) {
            return this.id;

        }
        Method m = servant.getClass().getMethod(arg0.invokedMethod.getName());
        return m.invoke(servant, arg0.parameterValues.toArray());
    }

}