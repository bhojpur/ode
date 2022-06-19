package ode.services.server.redirect;

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
import ode.services.server.fire.Ring;
import ode.services.server.fire.SessionManagerI;
import ode.system.Principal;
import Glacier2.CannotCreateSessionException;
import Glacier2.SessionPrx;

/**
 * Strategy interface for helping a {@link Ring} instance decide whether to
 * redirect {@link SessionPrx} creation to another {@link SessionManagerI}
 * instance. The {@link Ring} instance is passed in
 */
public interface Redirector {

    /**
     * Interface implemented by Ring instances to allow passing in the context
     * necessary for making strategy decisions.
     */
    public interface Context {

        /**
         * The UUID for the local node which will be used as the redirect lookup
         * string for this {@link Context}.
         */
        String uuid();

        /**
         * String representation of the proxy to the local node which may be
         * contacted to create new sessions.
         * 
         * @return See above.
         */
        String getDirectProxy();

        /**
         * {@link Principal} instance which can be used for internal calls the
         * Executor.
         */
        Principal principal();

        /**
         * Active communicator for use by the {@link Redirector} instance.
         */
        Ice.Communicator getCommunicator();
        
        /**
         * Return all known managers in the current cluster context, possibly
         * filtering out the inactive ones.
         */
        Set<String> getManagerList(boolean activeOnly);
    }

    /**
     * Create or retrieve and returns a {@link SessionPrx} which the current
     * method takes control of. If it is not returned, then it should be
     * properly destroyed.
     * 
     * @param userId
     *            Not null.
     * @param control
     * @param current
     * @return Possibly null.
     * @throws CannotCreateSessionException
     */
    public SessionPrx getProxyOrNull(Context context, String userId,
            Glacier2.SessionControlPrx control, Ice.Current current)
            throws CannotCreateSessionException;

    /**
     * Gives the {@link Redirector} a chance to configure the next appropriate
     * redirect based on the {@link Set} of current {@link Node} uuids.
     */
    public void chooseNextRedirect(Context context, Set<String> nodeUuids);

    /**
     * Gives the {@link Redirector} a chance to remove the current {@link Ring}
     * when it is being shutdown.
     */
    public void handleRingShutdown(Context context, String uuid);

}