package ode.security;

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

import java.util.Set;

import ode.model.meta.Node;
import ode.system.Principal;
import ode.system.ServiceFactory;
import ode.util.SqlAction;

/**
 * Provider for {@link Node} objects which is responsible for persisting and
 * populating such entities.
 */
public interface NodeProvider {

    Set<String> getManagerList(final boolean onlyActive);

    /**
     * Retrieves a given manager node ID.
     * @param managerUuid manager node UUID to retrieve
     * @param sql active SQL context which can be used to make queries
     * @return See above.
     */
    long getManagerIdByUuid(String managerUuid, SqlAction sql);

    /**
     * Retrieves a given manager node.
     * @param managerUuid manager node UUID to retrieve
     * @param sf current session's service factory
     * @return See above.
     */
    Node getManagerByUuid(String managerUuid, ServiceFactory sf);

    /**
     * Closes all sessions for a given manager node.
     *
     * @param managerUuid manager node UUID to close sessions for
     * @return number of sessions affected by the closure
     */
    int closeSessionsForManager(final String managerUuid);

    /**
     * Sets a given manager node as down.
     *
     * @param managerUuid manager node UUID to set as down
     */
    void setManagerDown(final String managerUuid);

    /**
     * Adds a manager node.
     *
     * @param managerUuid manager node UUID to add
     * @param proxyString manager node proxy connection string
     * @return populated node entity.
     */
    Node addManager(String managerUuid, String proxyString);

    /**
     * Retrieves the current active principal.
     *
     * @return See above.
     */
    Principal principal();
}