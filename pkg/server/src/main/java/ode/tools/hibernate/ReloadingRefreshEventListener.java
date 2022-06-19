package ode.tools.hibernate;

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

// Java imports
import java.util.Map;

// Third-party imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.HibernateException;
import org.hibernate.event.EventSource;
import org.hibernate.event.RefreshEvent;
import org.hibernate.event.RefreshEventListener;
import org.hibernate.util.IdentityMap;

// Application-internal dependencies
import ode.conditions.ApiUsageException;
import ode.model.IObject;

/**
 * responsible for responding to {@link RefreshEvent}. in particular in
 * reloading the {@link IObject#unload() unloaded} entities.
 */
public class ReloadingRefreshEventListener implements RefreshEventListener {

    private static final long serialVersionUID = 4292680015211981832L;

    private static Logger log = LoggerFactory
            .getLogger(ReloadingRefreshEventListener.class);

    /**
     * @see RefreshEventListener#onRefresh(RefreshEvent)
     */
    @SuppressWarnings("unchecked")
    public void onRefresh(RefreshEvent event) throws HibernateException {
        onRefresh(event, IdentityMap.instantiate(10));
    }

    /**
     * @see RefreshEventListener#onRefresh(RefreshEvent, Map)
     */
    @SuppressWarnings("unchecked")
    public void onRefresh(RefreshEvent event, Map refreshedAlready)
            throws HibernateException {
        IObject orig = (IObject) event.getObject();

        if (orig.getId() == null) {
            throw new ApiUsageException(
                    "Transient entities cannot be refreshed.");
        }

        if (HibernateUtils.isUnloaded(orig)) {
            final EventSource source = event.getSession();
            log("Reloading unloaded entity:", orig.getClass(), ":", orig
                    .getId());
            Object obj = source.load(orig.getClass(), orig.getId());
            refreshedAlready.put(orig, obj);
            return; // EARLY EXIT!
        }
    }

    // ~ Helpers
    // =========================================================================

    private void log(Object... objects) {
        if (log.isDebugEnabled() && objects != null && objects.length > 0) {
            StringBuilder sb = new StringBuilder(objects.length * 16);
            for (Object obj : objects) {
                sb.append(obj.toString());
            }
            log.debug(sb.toString());
        }
    }
}