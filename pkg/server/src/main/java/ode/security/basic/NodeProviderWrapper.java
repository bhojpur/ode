package ode.security.basic;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.BeanCreationException;

import ode.model.meta.Node;
import ode.security.NodeProvider;
import ode.services.util.ReadOnlyStatus;
import ode.system.Principal;
import ode.system.ServiceFactory;
import ode.util.SqlAction;

/**
 * A node provider that offers a unified view of multiple underlying node providers.
 * @param <P> node providers that adjust according to read-only status
 */
public class NodeProviderWrapper<P extends NodeProvider & ReadOnlyStatus.IsAware> implements NodeProvider {

    private final List<P> read, write;

    /**
     * Construct a new node provider.
     * @param readOnly the read-only status
     * @param providers the node providers to wrap: the earlier providers are tried first and at least one provider must support
     * write operations according to {@link ode.services.util.ReadOnlyStatus.IsAware#isReadOnly(ReadOnlyStatus)}
     */
    public NodeProviderWrapper(ReadOnlyStatus readOnly, List<P> providers) {
        read = providers;
        write = new ArrayList<P>(read.size());
        for (final P provider : read) {
            if (!provider.isReadOnly(readOnly)) {
                write.add(provider);
            }
        }
        if (write.isEmpty()) {
            throw new BeanCreationException("must be given a read-write node provider");
        }
    }

    @Override
    public Set<String> getManagerList(boolean onlyActive) {
        final Set<String> rvs = new HashSet<>();
        for (final P provider : read) {
            final Set<String> rv = provider.getManagerList(onlyActive);
            if (rv != null) {
                rvs.addAll(rv);
            }
        }
        return rvs;
    }

    @Override
    public long getManagerIdByUuid(String managerUuid, SqlAction sql) {
        for (final P provider : read) {
            final long managerId = provider.getManagerIdByUuid(managerUuid, sql);
            if (managerId != 0) {
                return managerId;
            }
        }
        return 0;
    }

    @Override
    public Node getManagerByUuid(String managerUuid, ServiceFactory sf) {
        for (final P provider : read) {
            final Node manager = provider.getManagerByUuid(managerUuid, sf);
            if (manager != null) {
                return manager;
            }
        }
        return null;
    }

    @Override
    public int closeSessionsForManager(String managerUuid) {
        int rv = 0;
        for (final P provider : write) {
            rv += provider.closeSessionsForManager(managerUuid);
        }
        return rv;
    }

    @Override
    public void setManagerDown(String managerUuid) {
        for (final P provider : write) {
            provider.setManagerDown(managerUuid);
        }
    }

    @Override
    public Node addManager(String managerUuid, String proxyString) {
        return write.get(0).addManager(managerUuid, proxyString);
    }

    @Override
    public Principal principal() {
        return write.get(0).principal();
    }
}