package ode.system;

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

import java.util.Properties;

import ode.api.IAdmin;
import ode.api.IAnalysis;
import ode.api.IConfig;
import ode.api.IContainer;
import ode.api.IDelete;
import ode.api.ILdap;
import ode.api.IMetadata;
import ode.api.IPixels;
import ode.api.IProjection;
import ode.api.IQuery;
import ode.api.IRenderingSettings;
import ode.api.IRepositoryInfo;
import ode.api.ISession;
import ode.api.IShare;
import ode.api.ITypes;
import ode.api.IUpdate;
import ode.api.JobHandle;
import ode.api.RawFileStore;
import ode.api.RawPixelsStore;
import ode.api.Search;
import ode.api.ServiceInterface;
import ode.api.ThumbnailStore;
import ode.conditions.ApiUsageException;
import ode.model.meta.Session;
import odeis.providers.re.RenderingEngine;

import org.springframework.beans.BeansException;

/**
 * Entry point for all client calls. Provides methods to obtain proxies for all
 * remote facades.
 */
public class ServiceFactory {

    /**
     * the {@link OdeContext context instance} which this
     * {@link ServiceFactory} uses to look up all of its state.
     */
    protected OdeContext ctx;

    /**
     * default constructor which obtains the global static
     * {@link ode.system.OdeContext#CLIENT_CONTEXT client context} from
     * {@link ode.system.OdeContext}. This can be done manually by calling
     * {@link ode.system.OdeContext#getClientContext()}
     * 
     * @see OdeContext#CLIENT_CONTEXT
     * @see OdeContext#getClientContext()
     */
    public ServiceFactory() {
        if (getDefaultContext() != null) {
            this.ctx = OdeContext.getInstance(getDefaultContext());
        }
    }

    /**
     * constructor which obtains a new (non-static)
     * {@link ode.system.OdeContext#CLIENT_CONTEXT client context}, passing
     * in the {@link Properties} representation of the {@link Login} for
     * configuration.
     * 
     * @see Login#asProperties()
     * @see #ServiceFactory(Properties)
     */
    public ServiceFactory(Login login) {
        this.ctx = OdeContext.getClientContext(login.asProperties());
    }

    /**
     * constructor which obtains a new (non-static)
     * {@link ode.system.OdeContext#CLIENT_CONTEXT client context}, passing
     * in the {@link Properties} representation of the {@link Server} for
     * configuration.
     * 
     * @see Server#asProperties()
     * @see #ServiceFactory(Properties)
     */
    public ServiceFactory(Server server) {
        this.ctx = OdeContext.getClientContext(server.asProperties());
    }

    /**
     * constructor which obtains a new (non-static)
     * {@link ode.system.OdeContext#CLIENT_CONTEXT client context}, passing
     * in the {@link Properties} representation of both the {@link Server} and
     * the {@link Login} for configuration.
     * 
     * @see Login#asProperties()
     * @see #ServiceFactory(Properties)
     */
    public ServiceFactory(Server server, Login login) {
        Properties s = server.asProperties();
        Properties l = login.asProperties();
        s.putAll(l);
        this.ctx = OdeContext.getClientContext(s);
    }

    /**
     * constructor which obtains a new
     * {@link ode.system.OdeContext#CLIENT_CONTEXT client context}, passing
     * in the provided properties for configuration.
     * 
     * @see OdeContext#getClientContext(Properties)
     */
    public ServiceFactory(Properties properties) {
        this.ctx = OdeContext.getClientContext(properties);
    }

    /**
     * constructor which uses the provided {@link OdeContext} for all
     * loookups.
     */
    public ServiceFactory(OdeContext context) {
        this.ctx = context;
    }

    /**
     * constructor which finds the global static {@link OdeContext} with the
     * given name.
     * 
     * @see OdeContext#CLIENT_CONTEXT
     * @see OdeContext#MANAGED_CONTEXT
     */
    public ServiceFactory(String contextName) {
        this.ctx = OdeContext.getInstance(contextName);
    }

    // ~ Stateless services
    // =========================================================================

    public IAdmin getAdminService() {
        return getServiceByClass(IAdmin.class);
    }

    public IAnalysis getAnalysisService() {
        return getServiceByClass(IAnalysis.class);
    }

    public IConfig getConfigService() {
        return getServiceByClass(IConfig.class);
    }

    public IContainer getContainerService() {
        return getServiceByClass(IContainer.class);
    }

    public IDelete getDeleteService() {
        return getServiceByClass(IDelete.class);
    }

    public ILdap getLdapService() {
        return getServiceByClass(ILdap.class);
    }

    public IPixels getPixelsService() {
        return getServiceByClass(IPixels.class);
    }

    public IProjection getProjectionService() {
        return getServiceByClass(IProjection.class);
    }

    public IQuery getQueryService() {
        return getServiceByClass(IQuery.class);
    }

    public IShare getShareService() {
        return getServiceByClass(IShare.class);
    }

    public ITypes getTypesService() {
        return getServiceByClass(ITypes.class);
    }

    public IUpdate getUpdateService() {
        return getServiceByClass(IUpdate.class);
    }

    public IRenderingSettings getRenderingSettingsService() {
        return getServiceByClass(IRenderingSettings.class);
    }

    public IRepositoryInfo getRepositoryInfoService() {
        return getServiceByClass(IRepositoryInfo.class);
    }

    public IMetadata getMetadataService() {
        return getServiceByClass(IMetadata.class);
    }
    
    // ~ Stateful services
    // =========================================================================

    /**
     * create a new {@link JobHandle} proxy. This proxy will have to be
     * initialized using {@link JobHandle#attach(long)} or
     * {@link JobHandle#submit(ode.model.jobs.Job)}.
     */
    public JobHandle createJobHandle() {
        return getServiceByClass(JobHandle.class);
    }

    /**
     * create a new {@link RawPixelsStore} proxy. This proxy will have to be
     * initialized using {@link RawPixelsStore#setPixelsId(long, boolean)}
     */
    public RawPixelsStore createRawPixelsStore() {
        return getServiceByClass(RawPixelsStore.class);
    }

    /**
     * create a new {@link RawFileStore} proxy. This proxy will have to be
     * initialized using {@link RawFileStore#setFileId(long)}
     */
    public RawFileStore createRawFileStore() {
        return getServiceByClass(RawFileStore.class);
    }

    /**
     * create a new {@link RenderingEngine} proxy. This proxy will have to be
     * initialized using {@link RenderingEngine#lookupPixels(long)} and
     * {@link RenderingEngine#load()}
     */
    public RenderingEngine createRenderingEngine() {
        return getServiceByClass(RenderingEngine.class);
    }

    /**
     * create a new {@link Search} proxy.
     */
    public Search createSearchService() {
        return getServiceByClass(Search.class);
    }

    /**
     * create a new {@link ThumbnailStore} proxy. This proxy will have to be
     * initialized using {@link ThumbnailStore#setPixelsId(long)}
     */
    public ThumbnailStore createThumbnailService() {
        return getServiceByClass(ThumbnailStore.class);
    }

    // ~ Sessions
    // =========================================================================

    public ISession getSessionService() {
        return getServiceByClass(ISession.class);
    }

    public Session getSession() throws ApiUsageException {
        return getSessionInitializer().getSession();
    }

    public void setSession(Session session) throws ApiUsageException {
        SessionInitializer si = getSessionInitializer();
        si.setSession(session);
    }

    public void closeSession() throws ApiUsageException {
        ISession is = getSessionService();
        SessionInitializer si = getSessionInitializer();
        if (si.hasSession()) {
            Session s = si.getSession();
            try {
                is.closeSession(s);
            } finally {
                si.setSession(null);
            }
        }
    }

    protected SessionInitializer getSessionInitializer() {
        SessionInitializer si;
        try {
            si = (SessionInitializer) this.ctx.getBean("init");
        } catch (Exception e) {
            throw new ApiUsageException("This ServiceFactory is not configured "
                    + "for sessions");
        }
        return si;
    }

    // ~ Helpers
    // =========================================================================

    /**
     * looks up services based on the current {@link #getPrefix() prefix} and
     * the class name of the service type.
     */
    public <T extends ServiceInterface> T getServiceByClass(Class<T> klass) {
        try {
            return klass.cast(this.ctx.getBean(getPrefix() + klass.getName()));
        } catch (BeansException be) {
            if (be.getCause() instanceof RuntimeException) {
                throw (RuntimeException) be.getCause();
            } else {
                throw be;
            }
        }
    }

    /**
     * used by {@link #getServiceByClass(Class)} to find the correct service
     * proxy in the {@link #ctx}
     * 
     * @return a {@link String}, usually "internal-" or "managed-"
     */
    protected String getPrefix() {
        return "managed-";
    }

    /**
     * used when no {@link OdeContext context} name is provided to the
     * constructor. Subclasses can override to allow for easier creation.
     * 
     * @return name of default context as found in beanRefContext.xml.
     */
    protected String getDefaultContext() {
        return OdeContext.CLIENT_CONTEXT;
    }
}