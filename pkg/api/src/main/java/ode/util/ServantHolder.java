package ode.util;

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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ode.api._StatefulServiceInterfaceOperations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.MapMaker;

/**
 * Manager for all active servants in a single session.
 *
 * To reduce the need of using {@link Ice.Util#stringToIdentity(String)} and
 * {@link Ice.Util#identityToString(Ice.Identity)} the servant tries to make the
 * two usages equivalent.
 */
public class ServantHolder {

    private final static Logger log = LoggerFactory.getLogger(ServantHolder.class);

    /**
     * Note: servants are stored by String since {@link Ice.Identity} does not
     * behave properly as a key.
     */
    private final Map<String, Ice.Object> servants;

    /**
     * A lock cache that returns a {@link Lock} for each given name, constructing it only if necessary.
     */
    private final LoadingCache<String, Lock> locks = CacheBuilder.newBuilder().build(
            new CacheLoader<String, Lock>() {
                @Override
                public Lock load(String key) {
                    return new ReentrantLock();
                }
            });

    /**
     * An internal mapping to all client ids from {@link ode.cmd.SessionI} for a given
     * DB session since there is no method on {@link Ice.ObjectAdapter} to retrieve
     * all servants.
     */
    protected final Map<String, Object> clientIds;

    /**
     * Storing session for debugging purposes.
     */
    private final String session;

    /**
     * The number of servants that are allowed to be registered for a user
     * in a single session before a {@link ode.OverUsageException} is thrown.
     */
    private final int servantsPerSession;

    public ServantHolder(String session) {
        this(session, 10000);
    }

    public ServantHolder(String session, int servantsPerSession) {
        final MapMaker mapMaker = new MapMaker();
        this.servants = mapMaker.makeMap();
        this.clientIds = mapMaker.makeMap();
        this.session = session;
        this.servantsPerSession = servantsPerSession;
    }

    //
    // Session id related methods
    //

    public String getSession() {
        return this.session;
    }

    /**
     * Constructs an {@link Ice.Identity} from the current session
     * and from the given {@link String} which for
     * stateful services are defined by UUIDs.
     */
    public Ice.Identity getIdentity(String idName) {
        Ice.Identity id = new Ice.Identity();
        id.category = this.session;
        id.name = idName;
        return id;
    }

    //
    // ClientId methods
    //

    public void addClientId(String clientId) {
        clientIds.put(clientId, Boolean.TRUE);
    }

    public void removeClientId(String clientId) {
        clientIds.remove(clientId);
    }

    public Set<String> getClientIds() {
        return clientIds.keySet();
    }

    /**
     * Acquires the given lock or if necessary creates a new one.
     * @param key the lock's key
     */
    public void acquireLock(String key) {
        try {
            locks.get(key).lock();
        } catch (ExecutionException e) {
            /* cannot occur unless loading thread is interrupted */
        }
    }

    /**
     * Releases the given lock if found, otherwise throws an
     * {@link ode.conditions.InternalException}
     * @param key the lock's key
     */
    public void releaseLock(String key) {
        final Lock lock = locks.getIfPresent(key);
        if (lock == null) {
            throw new ode.conditions.InternalException("No lock found: " + key);
        }
        lock.unlock();
    }

    public Ice.Object get(Ice.Identity id) {
        return get(id.name);
    }

    public Object getUntied(Ice.Identity id) {
        Ice.Object servantOrTie = get(id.name);
         if (servantOrTie instanceof Ice.TieBase) {
             return ((Ice.TieBase) servantOrTie).ice_delegate();
         } else {
             return servantOrTie;
         }
    }

    public void put(Ice.Identity id, Ice.Object servant)
        throws ode.OverUsageException {
        final int size = servants.size();
        if (size >= servantsPerSession) {
            String msg = String.format("servantsPerSession reached for %s: %s",
                session, servantsPerSession);
            log.error(msg);
            ode.OverUsageException oue = new ode.OverUsageException();
            ode.util.IceMapper.fillServerError(oue,
                new ode.conditions.OverUsageException(msg));
            throw oue;
        }

        double percent = (100.0 * size / servantsPerSession);
        if (percent > 0 && (percent % 10) == 0) {
            log.warn(String.format("%s%% of servants used for session %s",
                (int) percent, session));
        }
        put(id.name, servant);
    }

    public Ice.Object remove(Ice.Identity id) {
        return remove(id.name);
    }

    public List<String> getServantList() {
        return new ArrayList<String>(servants.keySet());
    }

    public String getStatefulServiceCount() {
        String list = "";
        final List<String> servants = getServantList();
        for (final String idName : servants) {
            final Ice.Identity id = getIdentity(idName);
            final Object servant = getUntied(id);
            if (servant != null) {
                try {
                    if (servant instanceof _StatefulServiceInterfaceOperations) {
                        list += "\n" + idName;
                    }
                } catch (Exception e) {
                    // oh well
                }
            }
        }
        return list;
    }

    //
    // Implementation
    //

    private void put(String key, Ice.Object servant) {
        Object old = servants.put(key, servant);
        if (old == null) {
            log.debug(String.format("Added %s to %s as %s", servant, this, key));
        } else {
            log.debug(String.format("Replaced %s with %s to %s as %s", old, servant, this, key));
        }
    }

    private Ice.Object remove(String key) {
        Ice.Object servant = servants.remove(key);
        log.debug(String.format("Removed %s from %s as %s", servant, this, key));
        return servant;
    }

    private Ice.Object get(String key) {
        return servants.get(key);
    }

}