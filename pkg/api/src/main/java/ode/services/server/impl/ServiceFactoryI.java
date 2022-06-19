package ode.services.server.impl;

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
import java.util.UUID;

import ode.system.EventContext;
import ode.api.IAdmin;
import ode.api.IShare;
import ode.api.local.LocalAdmin;
import ode.logic.HardWiredInterceptor;
import ode.services.server.fire.AopContextInitializer;
import ode.services.server.fire.Registry;
import ode.services.server.fire.TopicManager;
import ode.services.server.util.ServiceFactoryAware;
import ode.services.sessions.SessionManager;
import ode.services.util.Executor;
import ode.system.OdeContext;
import ode.system.Principal;
import ode.system.ServiceFactory;
import ode.ApiUsageException;
import ode.InternalException;
import ode.SecurityViolation;
import ode.ServerError;
import ode.api.ClientCallbackPrx;
import ode.api.ExporterPrx;
import ode.api.ExporterPrxHelper;
import ode.api.IAdminPrx;
import ode.api.IAdminPrxHelper;
import ode.api.IConfigPrx;
import ode.api.IConfigPrxHelper;
import ode.api.IContainerPrx;
import ode.api.IContainerPrxHelper;
import ode.api.ILdapPrx;
import ode.api.ILdapPrxHelper;
import ode.api.IMetadataPrx;
import ode.api.IMetadataPrxHelper;
import ode.api.IPixelsPrx;
import ode.api.IPixelsPrxHelper;
import ode.api.IProjectionPrx;
import ode.api.IProjectionPrxHelper;
import ode.api.IQueryPrx;
import ode.api.IQueryPrxHelper;
import ode.api.IRenderingSettingsPrx;
import ode.api.IRenderingSettingsPrxHelper;
import ode.api.IRepositoryInfoPrx;
import ode.api.IRepositoryInfoPrxHelper;
import ode.api.IRoiPrx;
import ode.api.IRoiPrxHelper;
import ode.api.IScriptPrx;
import ode.api.IScriptPrxHelper;
import ode.api.ISessionPrx;
import ode.api.ISessionPrxHelper;
import ode.api.ISharePrx;
import ode.api.ISharePrxHelper;
import ode.api.ITimelinePrx;
import ode.api.ITimelinePrxHelper;
import ode.api.ITypesPrx;
import ode.api.ITypesPrxHelper;
import ode.api.IUpdatePrx;
import ode.api.IUpdatePrxHelper;
import ode.api.JobHandlePrx;
import ode.api.JobHandlePrxHelper;
import ode.api.RawPixelsStorePrx;
import ode.api.RawPixelsStorePrxHelper;
import ode.api.RenderingEnginePrx;
import ode.api.RenderingEnginePrxHelper;
import ode.api.SearchPrx;
import ode.api.SearchPrxHelper;
import ode.api.ServiceFactoryPrx;
import ode.api.ServiceFactoryPrxHelper;
import ode.api.ServiceInterfacePrx;
import ode.api.ServiceInterfacePrxHelper;
import ode.api.StatefulServiceInterfacePrx;
import ode.api.StatefulServiceInterfacePrxHelper;
import ode.api.ThumbnailStorePrx;
import ode.api.ThumbnailStorePrxHelper;
import ode.api._ServiceFactoryOperations;
import ode.constants.ADMINSERVICE;
import ode.constants.CONFIGSERVICE;
import ode.constants.CONTAINERSERVICE;
import ode.constants.EXPORTERSERVICE;
import ode.constants.JOBHANDLE;
import ode.constants.LDAPSERVICE;
import ode.constants.METADATASERVICE;
import ode.constants.PIXELSSERVICE;
import ode.constants.PROJECTIONSERVICE;
import ode.constants.QUERYSERVICE;
import ode.constants.RAWFILESTORE;
import ode.constants.RAWPIXELSSTORE;
import ode.constants.RENDERINGENGINE;
import ode.constants.RENDERINGSETTINGS;
import ode.constants.REPOSITORYINFO;
import ode.constants.ROISERVICE;
import ode.constants.SCRIPTSERVICE;
import ode.constants.SEARCH;
import ode.constants.SESSIONSERVICE;
import ode.constants.SHAREDRESOURCES;
import ode.constants.SHARESERVICE;
import ode.constants.THUMBNAILSTORE;
import ode.constants.TIMELINESERVICE;
import ode.constants.TYPESSERVICE;
import ode.constants.UPDATESERVICE;
import ode.constants.topics.HEARTBEAT;
import ode.grid.SharedResourcesPrx;
import ode.grid.SharedResourcesPrxHelper;
import ode.model.IObject;
import ode.util.IceMapper;
import ode.util.ServantHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import Ice.Current;
import Ice.ObjectPrx;

/**
 * Responsible for maintaining all servants for a single session.
 *
 * In general, try to reduce access to the {@link Ice.Current} and
 * {@link Ice.Util} objects.
 */
public final class ServiceFactoryI extends ode.cmd.SessionI implements _ServiceFactoryOperations {

    // STATIC
    // ===========

    private final static Logger log = LoggerFactory.getLogger(ServiceFactoryI.class);

    // SHARED STATE
    // ===================
    // The following elements will all be the same or at least equivalent
    // in different instances of SF attached to the same session.

    final TopicManager topicManager;

    final Registry registry;

    final List<HardWiredInterceptor> cptors;

    final AopContextInitializer initializer;

    // ~ Initialization and context methods
    // =========================================================================

    public ServiceFactoryI(Ice.Current current,
            ServantHolder holder,
            Glacier2.SessionControlPrx control, OdeContext context,
            SessionManager manager, Executor executor, Principal p,
            List<HardWiredInterceptor> interceptors,
            TopicManager topicManager, Registry registry)
            throws ApiUsageException {
        this(false, current, holder, control, context, manager, executor, p,
                interceptors, topicManager, registry, null);
    }

    public ServiceFactoryI(boolean reusedSession,
            Ice.Current current,
            ServantHolder holder,
            Glacier2.SessionControlPrx control, OdeContext context,
            SessionManager manager, Executor executor, Principal p,
            List<HardWiredInterceptor> interceptors,
            TopicManager topicManager, Registry registry, String token)
            throws ApiUsageException {
        super(reusedSession, current, holder, control, context, manager, executor, p, token);
        this.cptors = interceptors;
        this.initializer = new AopContextInitializer(new ServiceFactory(
                this.context), this.principal, this.reusedSession);
        this.topicManager = topicManager;
        this.registry = registry;
    }

    public ServiceFactoryPrx proxy() {
        return ServiceFactoryPrxHelper.uncheckedCast(adapter
                .createDirectProxy(sessionId()));
    }

    // ~ Security Context
    // =========================================================================

    @SuppressWarnings("unchecked")
    public List<IObject> getSecurityContexts(Current __current)
            throws ServerError {

        final EventContext ec = getEventContext();
        List<?> objs = executor.execute(principal,
                new Executor.SimpleWork<List>(this, "getSecurityContext") {
                    @Transactional(readOnly = true)
                    public List doWork(Session session, ServiceFactory sf) {

                        final IAdmin admin = sf.getAdminService();
                        final IShare share = sf.getShareService();
                        final List<ode.model.IObject> objs = new ArrayList<ode.model.IObject>();

                        // Groups
                        final Set<Long> added = new HashSet<Long>();
                        for (Long id : ec.getMemberOfGroupsList()) {
                            objs.add(admin.getGroup(id));
                            added.add(id);
                        }
                        for (Long id : ec.getLeaderOfGroupsList()) {
                            if (!added.contains(id)) {
                                objs.add(admin.getGroup(id));
                            }
                        }

                        // Shares
                        objs.addAll(share.getMemberShares(true));
                        objs.addAll(share.getOwnShares(true));

                        return objs;
                    }
                });
        IceMapper mapper = new IceMapper();
        return (List<IObject>) mapper.map(objs);
    }

    public IObject setSecurityContext(IObject obj, Current __current)
            throws ServerError {

        IceMapper mapper = new IceMapper();
        try {
            ode.model.IObject iobj = (ode.model.IObject) mapper.reverse(obj);
            ode.model.IObject old = sessionManager.setSecurityContext(
                    principal, iobj);
            return (IObject) mapper.map(old);
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    public void setSecurityPassword(final String password, Current __current)
            throws ServerError {

        final EventContext ec = getEventContext();
        final String name = ec.getCurrentUserName();
        final boolean ok = sessionManager.executePasswordCheck(name, password);
        if (!ok) {
            final String msg = "Bad password for " + name;
            log.info("setSecurityPassword: " + msg);
            throw new SecurityViolation(null, null, "Bad password for " + name);
        } else {
            this.reusedSession.set(false);
        }
    }

    protected ode.ServerError handleException(Throwable t) {
        IceMapper mapper = new IceMapper();
        Ice.UserException iue = mapper.handleException(t, context);
        if (iue instanceof ServerError) {
            return (ServerError) iue;
        } else { // This may not be necessary
            InternalException iu = new InternalException();
            iu.initCause(t);
            IceMapper.fillServerError(iu, t);
            return iu;
        }
    }

    // ~ Stateless
    // =========================================================================

    public IAdminPrx getAdminService(Ice.Current current) throws ServerError {
        return IAdminPrxHelper.uncheckedCast(getByName(ADMINSERVICE.value,
                current, true));
    }

    public IConfigPrx getConfigService(Ice.Current current) throws ServerError {
        return IConfigPrxHelper.uncheckedCast(getByName(CONFIGSERVICE.value,
                current, true));
    }

    public ILdapPrx getLdapService(Ice.Current current) throws ServerError {
        return ILdapPrxHelper.uncheckedCast(getByName(LDAPSERVICE.value,
                current));
    }

    public IPixelsPrx getPixelsService(Ice.Current current) throws ServerError {
        return IPixelsPrxHelper.uncheckedCast(getByName(PIXELSSERVICE.value,
                current));
    }

    public IContainerPrx getContainerService(Ice.Current current)
            throws ServerError {
        return IContainerPrxHelper.uncheckedCast(getByName(
                CONTAINERSERVICE.value, current));
    }

    public IProjectionPrx getProjectionService(Ice.Current current)
            throws ServerError {
        return IProjectionPrxHelper.uncheckedCast(getByName(
                PROJECTIONSERVICE.value, current));
    }

    public IQueryPrx getQueryService(Ice.Current current) throws ServerError {
        return IQueryPrxHelper.uncheckedCast(getByName(QUERYSERVICE.value,
                current));
    }

    public IRoiPrx getRoiService(Ice.Current current) throws ServerError {
        Ice.ObjectPrx prx = getByName(ROISERVICE.value, current);
        return IRoiPrxHelper.uncheckedCast(prx);
    }

    public IScriptPrx getScriptService(Ice.Current current) throws ServerError {
        Ice.ObjectPrx prx = getByName(SCRIPTSERVICE.value, current);
        return IScriptPrxHelper.uncheckedCast(prx);
    }

    public ISessionPrx getSessionService(Current current) throws ServerError {
        return ISessionPrxHelper.uncheckedCast(getByName(SESSIONSERVICE.value,
                current, true));
    }

    public ISharePrx getShareService(Current current) throws ServerError {
        return ISharePrxHelper.uncheckedCast(getByName(SHARESERVICE.value,
                current));
    }

    public ITimelinePrx getTimelineService(Ice.Current current)
            throws ServerError {
        return ITimelinePrxHelper.uncheckedCast(getByName(
                TIMELINESERVICE.value, current));
    }

    public ITypesPrx getTypesService(Ice.Current current) throws ServerError {
        return ITypesPrxHelper.uncheckedCast(getByName(TYPESSERVICE.value,
                current));
    }

    public IUpdatePrx getUpdateService(Ice.Current current) throws ServerError {
        Ice.ObjectPrx prx = getByName(UPDATESERVICE.value, current);
        return IUpdatePrxHelper.uncheckedCast(prx);

    }

    public IRenderingSettingsPrx getRenderingSettingsService(Ice.Current current)
            throws ServerError {
        return IRenderingSettingsPrxHelper.uncheckedCast(getByName(
                RENDERINGSETTINGS.value, current));
    }

    public IRepositoryInfoPrx getRepositoryInfoService(Ice.Current current)
            throws ServerError {
        return IRepositoryInfoPrxHelper.uncheckedCast(getByName(
                REPOSITORYINFO.value, current));
    }

    public IMetadataPrx getMetadataService(Ice.Current current)
            throws ServerError {
        return IMetadataPrxHelper.uncheckedCast(getByName(
                METADATASERVICE.value, current));
    }

    // ~ Stateful
    // =========================================================================

    public ExporterPrx createExporter(Current current) throws ServerError {
        return ExporterPrxHelper.uncheckedCast(createByName(
                EXPORTERSERVICE.value, current));
    }

    public JobHandlePrx createJobHandle(Ice.Current current) throws ServerError {
        return JobHandlePrxHelper.uncheckedCast(createByName(JOBHANDLE.value,
                current));
    }

    public RenderingEnginePrx createRenderingEngine(Ice.Current current)
            throws ServerError {
        return RenderingEnginePrxHelper.uncheckedCast(createByName(
                RENDERINGENGINE.value, current));
    }

    public ode.api.RawFileStorePrx createRawFileStore(Ice.Current current)
            throws ServerError {
        return ode.api.RawFileStorePrxHelper.uncheckedCast(createByName(
                RAWFILESTORE.value, current));
    }

    public RawPixelsStorePrx createRawPixelsStore(Ice.Current current)
            throws ServerError {
        return RawPixelsStorePrxHelper.uncheckedCast(createByName(
                RAWPIXELSSTORE.value, current));
    }

    public SearchPrx createSearchService(Ice.Current current)
            throws ServerError {
        return SearchPrxHelper
                .uncheckedCast(createByName(SEARCH.value, current));
    }

    public ThumbnailStorePrx createThumbnailStore(Ice.Current current)
            throws ServerError {
        return ThumbnailStorePrxHelper.uncheckedCast(createByName(
                THUMBNAILSTORE.value, current));
    }

    // ~ Other interface methods
    // =========================================================================

    public SharedResourcesPrx sharedResources(Current current)
            throws ServerError {
        return SharedResourcesPrxHelper.uncheckedCast(getByName(
                SHAREDRESOURCES.value, current));
    }

    public Ice.TieBase getTie(Ice.Identity id) {
        return (Ice.TieBase) holder.get(id);
    }

    public Object getServant(Ice.Identity id) {
        return holder.getUntied(id);
    }

    public ServiceInterfacePrx getByName(String blankname, Current dontUse)
            throws ServerError {
        return getByName(blankname, dontUse, false);

    }

    public ServiceInterfacePrx getByName(String blankname, Current dontUse,
            boolean allowGuest) throws ServerError {

        if (!allowGuest) {
            disallowGuest(blankname);
        }

        // First try to get the blankname as is in case a value from
        // activeServices is being passed back in.
        Ice.Identity immediateId = holder.getIdentity(blankname);
        if (holder.get(immediateId) != null) {
            return ServiceInterfacePrxHelper.uncheckedCast(adapter
                    .createDirectProxy(immediateId));
        }

        // in order to use a different initializer
        // for each stateless service, we need to attach modify the id.
        // idName is just the value id.name not Ice.Util.identityToString(id)
        String idName = clientId + blankname;
        Ice.Identity id = holder.getIdentity(idName);

        holder.acquireLock(idName);
        try {
            Ice.ObjectPrx prx;
            Ice.Object servant = holder.get(id);
            if (servant == null) {
                servant = createServantDelegate(blankname);
                // Previously we checked for stateful services here,
                // however the logic is the same so it shouldn't
                // cause any issues.
                prx = registerServant(id, servant);
            } else {
                prx = adapter.createDirectProxy(id);
            }
            return ServiceInterfacePrxHelper.uncheckedCast(prx);
        } finally {
            holder.releaseLock(idName);
        }
    }

    public StatefulServiceInterfacePrx createByName(String name, Current current)
            throws ServerError {
        return createByName(name, current, false);
    }

    public StatefulServiceInterfacePrx createByName(String name, Current current,
            boolean allowGuest) throws ServerError {

        if (!allowGuest) {
            disallowGuest(name);
        }

        Ice.Identity id = holder.getIdentity(UUID.randomUUID().toString() + name);
        if (null != adapter.find(id)) {
            ode.InternalException ie = new ode.InternalException();
            ie.message = name + " already registered for this adapter.";
        }

        Ice.Object servant = createServantDelegate(name);
        Ice.ObjectPrx prx = registerServant(id, servant);
        return StatefulServiceInterfacePrxHelper.uncheckedCast(prx);
    }

    public void subscribe(String topicName, ObjectPrx prx, Current __current)
            throws ServerError {

        if (topicName == null || !topicName.startsWith("/public/")) {
            throw new ode.ApiUsageException(null, null,
                    "Currently only \"/public/\" topics allowed.");
        }
        topicManager.register(topicName, prx, false);
        log.info("Registered " + prx + " for " + topicName);
    }

    public void setCallback(ClientCallbackPrx callback, Ice.Current current)
            throws ServerError {
        if (false) { // disabling because of long logins. See also
                     // #2485
            this.callback = callback;
            log.info(Ice.Util.identityToString(this.sessionId())
                    + " set callback to " + this.callback);
            try {
                subscribe(HEARTBEAT.value, callback, current);
                // Ignoring any errors on registration
                // of callbacks to permit login. Other client
                // callbacks may want to force an exception with ice_isA
                // or similar.
            } catch (RuntimeException e) {
                log.warn("Failed to subscribe " + callback, e);
                // throw e;
            } catch (ServerError e) {
                log.warn("Failed to subscribe " + callback, e);
                // throw e;
            } catch (Exception e) {
                log.warn("Failed to subscribe " + callback, e);
                // throw new RuntimeException(e);
            }
        }
    }

    public void detachOnDestroy(Ice.Current current) {
        doClose = false;
    }

    public void closeOnDestroy(Ice.Current current) {
        doClose = true;
    }

    /**
     * NB: Much of the logic here is similar to {@link #doClose} and should be
     * pushed down.
     */
    public String getStatefulServiceCount() {
        return holder.getStatefulServiceCount();
    }

    public List<String> activeServices(Current __current) {
        return holder.getServantList();
    }

    /** Doesn't take current into account */
    public EventContext getEventContext() {
        return sessionManager.getEventContext(this.principal);
    }

    /** Takes current into account */
    public EventContext getEventContext(final Ice.Current current) {
        return executor.execute(current.ctx, this.principal,
                new Executor.SimpleWork<EventContext>(this, "getEventContext") {
                    @Transactional(readOnly=true)
                    public EventContext doWork(Session session, ServiceFactory sf) {
                        return ((LocalAdmin) sf.getAdminService()).getEventContextQuiet();
                    }
                });
    }

    private boolean isGuest() {
        return executor.execute(this.principal,
                new Executor.SimpleWork<Boolean>(this, "isGuest") {
                    @Transactional(readOnly=true)
                    public Boolean doWork(Session session, ServiceFactory sf) {
                        LocalAdmin admin = (LocalAdmin) sf.getAdminService();
                        EventContext ec = admin.getEventContextQuiet();
                        long guestId = admin.getSecurityRoles().getGuestId();
                        return ec.getCurrentUserId().equals(guestId);
                    }
                });
    }

    private void disallowGuest(String service)
        throws SecurityViolation {
        if (isGuest()) {
            throw new SecurityViolation(null, null,
                "Access denied to guest user: " + service);
        }
    }

    public long keepAllAlive(ServiceInterfacePrx[] proxies, Current __current)
            throws ServerError {

        try {
            // First take measures to keep the session alive
            getEventContext();
            if (log.isDebugEnabled()) {
                log.debug("Keep all alive: " + this);
            }

            if (proxies == null || proxies.length == 0) {
                return -1; // All set to 1
            }

            long retVal = 0;
            for (int i = 0; i < proxies.length; i++) {
                ServiceInterfacePrx prx = proxies[i];
                if (prx == null) {
                    continue;
                }
                Ice.Identity id = prx.ice_getIdentity();
                if (null == holder.get(id)) {
                    retVal |= 1 << i;
                }
            }
            return retVal;
        } catch (Throwable t) {
            throw handleException(t);
        }
    }

    /**
     * Currently ignoring the individual proxies
     */
    public boolean keepAlive(ServiceInterfacePrx proxy, Current __current)
            throws ServerError {

        try {
            // First take measures to keep the session alive
            getEventContext();
            if (log.isDebugEnabled()) {
                log.debug("Keep alive: " + this);
            }

            if (proxy == null) {
                return false;
            }
            Ice.Identity id = proxy.ice_getIdentity();
            return null != holder.get(id);
        } catch (Throwable t) {
            throw handleException(t);
        }
    }

    // ~ Helpers
    // =========================================================================

        @Override
    protected void internalServantConfig(Object obj) throws ServerError {
        super.internalServantConfig(obj);
        if (obj instanceof ServiceFactoryAware) {
            ((ServiceFactoryAware) obj).setServiceFactory(this);
        }
        if (obj instanceof AbstractAmdServant) {
            AbstractAmdServant amd = (AbstractAmdServant) obj;
            amd.applyHardWiredInterceptors(cptors, initializer);
            amd.setHolder(holder);
            // TODO: amd.setApplicationContext(context);
        }

    }

}