package ode.services.server.util;

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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import ode.model.core.OriginalFile;
import ode.services.server.fire.Registry;
import ode.services.server.impl.ServiceFactoryI;
import ode.services.scripts.ScriptRepoHelper;
import ode.system.OdeContext;
import ode.system.Roles;
import ode.api.ServiceFactoryPrx;
import ode.constants.namespaces.NSDYNAMIC;
import ode.grid.JobParams;
import ode.grid.ParamsHelper;
import ode.grid.ParamsHelper.Acquirer;
import ode.model.ExperimenterGroupI;
import ode.util.IceMapper;

import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import Ice.UserException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Caching replacement of {@link ode.grid.ParamsHelper} which maintains an
 * {@link OriginalFile} ID+SHA1-to-{@link JobParams} mapping in memory rather
 * than storing ParseJob instances in the database. All scripts are read once on
 * start and them subsequently based on the ode.scripts.cache.cron setting.
 * {@link JobParams} instances may be removed from the cache based on the
 * ode.scripts.cache.spec setting. If a key is not present in the cache on
 * {@link #getParams(Long, String, Current)} then an attempt will be made to
 * load it. Any exceptions thrown will be propagated to the caller.
 */
public class ParamsCache implements ApplicationContextAware {

    /**
     * Thrown by {@link ParamsCache#_load(Long)} when a found
     * {@link JobsParams} object has the {@link NSDYNAMIC} namespace.
     * In that case, the value <em>should not</em> be stored in the
     * cache but should be regenerated for each call.
     */
    @SuppressWarnings("serial")
    private static class DynamicException extends Exception {

        private final JobParams params;

        DynamicException(JobParams params) {
            this.params = params;
        }
    }

    private final static Logger log = LoggerFactory
            .getLogger(ParamsCache.class);

    private final LoadingCache<Key, JobParams> cache;

    private final Registry reg;

    private final Roles roles;

    private final ScriptRepoHelper scripts;

    private/* final */OdeContext ctx;

    public ParamsCache(Registry reg, Roles roles,
            ScriptRepoHelper scripts,
            String spec) {
        this.reg = reg;
        this.roles = roles;
        this.scripts = scripts;
        this.cache = CacheBuilder.from(spec).build(
                new CacheLoader<Key, JobParams>() {
                    public JobParams load(Key key) throws Exception {
                        return lookup(key);
                    }
                });
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
        this.ctx = (OdeContext) ctx;
    }

    //
    // Public API
    //

    /**
     * Lookup a cached {@link JobParams} instance for the given key. If none
     * is present, then an attempt will be made to load one, possibly throwing
     * a {@link UserException}.
     *
     * @param id
     * @param sha1
     * @param curr
     * @return See above.
     * @throws UserException
     */
    public JobParams getParams(Long id, String sha1, Ice.Current curr) throws UserException {
        Slf4JStopWatch get = sw("get." + id);
        try {
            return cache.get(new Key(id, sha1, curr));
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DynamicException) {
                return ((DynamicException) cause).params;
            }
            UserException ue = new IceMapper().handleException(cause, ctx);
            log.warn("Error on scripts cache lookup", ue);
            throw ue;
        } finally {
            get.stop();
        }
    }

    /**
     * Remove a cached {@link JobParams} instance.
     * 
     * @param id The id of the job.
     */
    public void removeParams(Long id) {
        if (id == null) {
            return;
        }
        Set<Key> matching = new HashSet<Key>(cache.asMap().keySet());
        Iterator<Key> it = matching.iterator();
        while (it.hasNext()) {
            if (id.equals(it.next().id)) {
                it.remove();
            }
        }
        cache.invalidateAll(matching);
    }

    /**
     * Called by the {@link LoadingCache} when a cache-miss occurs.
     */
    public JobParams lookup(Key key) throws Exception {
        return _load(key);
    }

    /**
     * Called from a Quartz cron trigger for periodically reloading all scripts.
     */
    public void lookupAll() throws Exception {
        _load(null);
    }

    //
    // HELPERS
    //

    private Slf4JStopWatch sw(String suffix) {
        return new Slf4JStopWatch("ode.scripts.cache." + suffix);
    }

    /**
     * Internal loading method which uses a {@link Loader} to create
     * a session as root and perform the necessary script invocation.
     */
    private JobParams _load(Key key) throws Exception {
        Slf4JStopWatch load = sw(key == null ? "all" : Long.toString(key.id));
        Loader loader = null;
        try {
            if (key != null) {
                // May not return null!
                loader = new UserLoader(reg, ctx, key);
                JobParams params = loader.createParams(key);
                if (isDynamic(params)) {
                    throw new DynamicException(params);
                }
                return params;
            } else {
                loader = new RootLoader(reg, ctx, roles);
                Slf4JStopWatch list = sw("list");
                List<OriginalFile> files = scripts.loadAll(true);
                list.stop();
                for (OriginalFile file : files) {
                    try {
                        Slf4JStopWatch single = sw(file.getId().toString());
                        Key newkey = new Key(file.getId(), file.getHash());
                        JobParams params = loader.createParams(newkey);
                        if (!isDynamic(params)) {
                            cache.put(newkey, params);
                        }
                        single.stop();
                    } catch (ode.ValidationException ve) {
                        // Likely an invalid script
                        log.warn("Failed to load params for {}",
                                file.getId(), ve);
                    } catch (Exception e) {
                        log.error("Failed to load params for {}",
                                file.getId(), e);
                    }
                }
                log.info("New size of scripts cache: {} ({} ms.)",
                        cache.size(), load.getElapsedTime());
                return null;
            }
        } finally {
            load.stop();
            if (loader != null) {
                loader.close();
            }
        }
    }

    private boolean isDynamic(JobParams params) throws DynamicException {
        if (params.namespaces != null) {
            if (params.namespaces.contains(NSDYNAMIC.value)) {
                return true;
            }
        }
        return false;
    }

    /** Simple state class for holding the various instances needed for
     * logging in and creating a {@link JobParams} instance.
     */
    private static abstract class Loader {

        final Registry reg;
        final OdeContext ctx;
        ServiceFactoryI sf;
        ParamsHelper helper;

        Loader(Registry reg, OdeContext ctx) throws Exception {
            this.reg = reg;
            this.ctx = ctx;
        }

        abstract ServiceFactoryI getFactory() throws Exception;

        ServiceFactoryI lookupFactory(FindServiceFactoryMessage msg) throws UserException {
            try {
                ctx.publishMessage(msg);
                return msg.getServiceFactory();
            } catch (Throwable t) {
                throw new IceMapper().handleException(t, ctx);
            }
        }

        ParamsHelper getHelper() throws Exception {

            if (helper != null) {
                return helper;
            }

            if (sf == null) {
                sf = getFactory();
            }

            // From ScriptI.java
            Acquirer acq = (Acquirer) sf.getServant(sf
                    .sharedResources(getCurrent()).ice_getIdentity());
            helper = new ParamsHelper(acq, sf.getExecutor(),
                    sf.getPrincipal());
            return helper;
        }

        /**
         * Call {@link ParamsHelper#generateScriptParams(long, boolean, Ice.Current)}
         * either as the current admin user or if this is a user script, creating
         * a temporary loader with just that context.
         */
        JobParams createParams(Key key) throws Exception {
            ParamsHelper helper = getHelper();
            Ice.Current curr = getCurrent();
            return helper.generateScriptParams(key.id, false, curr);
        }

        abstract Ice.Current getCurrent();

        abstract void close();

    }

    /**
     * Subclass for when no {@link Ice.Current} is available. Uses "root" as
     * the login and creates a new session which <em>must</em> be closed.
     */
    private static class RootLoader extends Loader {

        final String root;
        final Long gid;
        Ice.Current curr;

        RootLoader(Registry reg, OdeContext ctx, Roles roles) throws Exception {
            this(reg, ctx, roles, null);
        }

        RootLoader(Registry reg, OdeContext ctx, Roles roles, Long gid)
                throws Exception {
            super(reg, ctx);
            this.root = roles.getRootName();
            this.gid = gid;
        }

        ServiceFactoryI getFactory() throws Exception {

            Ice.Identity id;

            ServiceFactoryPrx prx = reg.getInternalServiceFactory(
                 root, null, 3, 1,
                 UUID.randomUUID().toString());
            id = prx.ice_getIdentity();
            ServiceFactoryI sf = lookupFactory(new FindServiceFactoryMessage(this, id));
            curr = sf.newCurrent(id, "loadScripts");
            if (gid != null) {
                sf.setSecurityContext(new ExperimenterGroupI(gid, false), curr);
            }
            return sf;
        }

        Ice.Current getCurrent() {
            return curr;
        }

        void close() {
            if (sf != null) {
                sf.destroy(null);
            }
        }
    }

    /**
     * Simpler subclass which uses the {@link Ice.Current} stored within a
     * {@link Key} instance.
     */
    private static class UserLoader extends Loader {

        final Key key;

        UserLoader(Registry reg, OdeContext ctx, Key key) throws Exception {
            super(reg, ctx);
            this.key = key;
        }

        ServiceFactoryI getFactory() throws Exception {
            return lookupFactory(new FindServiceFactoryMessage(this, key.curr));
        }

        Ice.Current getCurrent() {
            return key.curr;
        }
        void close() {
            // no-op
        }
    }

    /**
     * Simple Set/Map-compatible class for storing instances based on a
     * (Long, String) tuple. An additional possibly null {@link Ice.Current}
     * instance can also be stored but will not effect {@link #equals(Object)}
     * or {@link #hashCode()} calculation.
     */
    private static class Key {

        final Long id;
        final String sha1;
        final Ice.Current curr;

        Key(Long id, String sha1) {
            this(id, sha1, null);
        }

        Key(Long id, String sha1, Ice.Current curr) {
            this.id = id;
            this.sha1 = sha1;
            this.curr = curr;
        }

        @Override
        public int hashCode() {
            final int prime = 113;
            int result = 1;
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            result = prime * result + ((sha1 == null) ? 0 : sha1.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Key other = (Key) obj;
            if (id == null) {
                if (other.id != null)
                    return false;
            } else if (!id.equals(other.id))
                return false;
            if (sha1 == null) {
                if (other.sha1 != null)
                    return false;
            } else if (!sha1.equals(other.sha1))
                return false;
            return true;
        }
    }

}