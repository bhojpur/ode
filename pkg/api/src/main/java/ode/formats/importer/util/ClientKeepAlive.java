package ode.formats.importer.util;

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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.IObservable;
import ode.formats.importer.IObserver;
import ode.formats.importer.ImportEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link Runnable} which keeps a {@link IceInternal.Connector}'s server-side resources
 * from timing out. <b>NOTE:</b> Upon catching an exception, the
 * <code>Connector</code> is logged out.
 */
public class ClientKeepAlive implements Runnable, IObservable
{
    /** Logger for this class. */
    private static Logger log = LoggerFactory.getLogger(ClientKeepAlive.class);

    /** The connector we're trying to keep alive. */
    private AtomicReference<ODEMetadataStoreClient> client = new AtomicReference<>();

    private final ArrayList<IObserver> observers = new ArrayList<IObserver>();

    /** Whether or not observers have been notified of logout */
    private AtomicBoolean notified = new AtomicBoolean(false);

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        ODEMetadataStoreClient client = this.client.get();
        if (client == null) {
            log.warn("No client for keep alive");
            return;
        }
        try
        {
            log.debug("pinging");
            client.ping(); // logs completion
        }
        catch (Throwable t)
        {
            log.error(
                "Exception while executing ping(), logging Connector out: ", t);
            try {
                notifyLogout();
                client.logout();
            } catch (Exception e) {
                log.error("Nested error on client.logout() " +
                        "while handling exception from ping()", e);
            }
        }
    }

    public void notifyLogout() {
        if (notified.compareAndSet(false, true)) {
            notifyObservers(new ImportEvent.LOGGED_OUT());
        }
    }

    /**
     * @return ODEMetadataStoreClient
     */
    public ODEMetadataStoreClient getClient()
    {
        return this.client.get();
    }


    /**
     * @param client - ODEMetadataStoreClient to set
     */
    public void setClient(ODEMetadataStoreClient client)
    {
        this.client.set(client);
    }

    // Observable methods

    /* (non-Javadoc)
     * @see ode.formats.importer.IObservable#addObserver(ode.formats.importer.IObserver)
     */
    public boolean addObserver(IObserver object)
    {
        return observers.add(object);
    }

    /* (non-Javadoc)
     * @see ode.formats.importer.IObservable#deleteObserver(ode.formats.importer.IObserver)
     */
    public boolean deleteObserver(IObserver object)
    {
        return observers.remove(object);

    }

    /* (non-Javadoc)
     * @see ode.formats.importer.IObservable#notifyObservers(ode.formats.importer.ImportEvent)
     */
    public void notifyObservers(ImportEvent event)
    {
        for (IObserver observer:observers)
        {
            try {
                observer.update(this, event);
            } catch (Exception e)
            {
                log.error(e.toString()); // slf4j migration: toString()
            }
        }
    }
}