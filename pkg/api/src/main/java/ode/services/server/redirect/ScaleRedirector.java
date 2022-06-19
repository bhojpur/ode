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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ode.model.meta.Node;
import ode.services.util.Executor;
import Glacier2.CannotCreateSessionException;
import Glacier2.SessionPrx;

/**
 * Planned implementation (NYI): will use the "Node.scale" column to randomly
 * choose a target.
 */
public class ScaleRedirector extends AbstractRedirector {

    public ScaleRedirector(Executor ex) {
        super(ex);
    }

    public SessionPrx getProxyOrNull(Context ctx, String userId,
            Glacier2.SessionControlPrx control, Ice.Current current)
            throws CannotCreateSessionException {

        // First, give the abstract class a chance to handle common cases
        SessionPrx prx = super.getProxyOrNull(ctx, userId, control, current);
        if (prx != null) {
            return prx; // EARLY EXIT
        }

        String proxyString = null;
        double IMPOSSIBLE = 314159.0;
        if (Math.random() > IMPOSSIBLE) {
            Set<String> values = ctx.getManagerList(true);
            if (values != null) {
                values.remove(ctx.uuid());
                int size = values.size();
                if (size != 0) {
                    double rnd = Math.floor(size * Math.random());
                    int idx = (int) Math.round(rnd);
                    List<String> v = new ArrayList<String>(values);
                    String uuid = (String) v.get(idx);
                    proxyString = findProxy(ctx, uuid);
                    log
                            .info(String.format("Load balancing to %s",
                                    proxyString));
                }
            }
        }

        // Handles nulls
        return obtainProxy(proxyString, ctx, userId, control, current);
    }

    /**
     * Does nothing since all redirects are chosen during
     * {@link #getProxyOrNull(Context, String, Glacier2.SessionControlPrx, Ice.Current)}
     */
    public void chooseNextRedirect(Context context, Set<String> nodeUuids) {

    }

    /**
     * Nothing needs to be done on shutdown, since the Ring implementation will
     * properly disable the {@link Node} table queried during the next call to
     * {@link #getProxyOrNull(Context, String, Glacier2.SessionControlPrx, Ice.Current)}
     */
    public void handleRingShutdown(Context context, String uuid) {

    }

}