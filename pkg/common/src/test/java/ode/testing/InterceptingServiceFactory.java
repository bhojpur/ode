package ode.testing;

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

import ode.api.IAdmin;
import ode.api.IAnalysis;
import ode.api.IConfig;
import ode.api.IDelete;
import ode.api.ILdap;
import ode.api.IPixels;
import ode.api.IContainer;
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
import ode.system.ServiceFactory;
import odeis.providers.re.RenderingEngine;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;

/**
 * Wraps all returned services with the given interceptor;
 */
public class InterceptingServiceFactory extends ServiceFactory {

    final ServiceFactory sf;
    final MethodInterceptor[] interceptors;

    public InterceptingServiceFactory(ServiceFactory sf, MethodInterceptor... interceptors) {
        this.sf = sf;
        this.interceptors = interceptors;
    }

    @SuppressWarnings("unchecked")
    <T extends ServiceInterface> T wrap(T service) {
        ProxyFactory factory = new ProxyFactory();
        factory.setInterfaces(service.getClass().getInterfaces());
        for (MethodInterceptor i : interceptors) {
            factory.addAdvice(i);
        }
        factory.setTarget(service);
        return (T) factory.getProxy();
    }

    @Override
    protected String getDefaultContext() {
        return null;
    }

    @Override
    public void closeSession() throws ApiUsageException {
        sf.closeSession();
    }

    @Override
    public JobHandle createJobHandle() {
        return wrap(sf.createJobHandle());
    }

    @Override
    public RawFileStore createRawFileStore() {
        return wrap(sf.createRawFileStore());
    }

    @Override
    public RawPixelsStore createRawPixelsStore() {
        return wrap(sf.createRawPixelsStore());
    }

    @Override
    public RenderingEngine createRenderingEngine() {
        return wrap(sf.createRenderingEngine());
    }

    @Override
    public Search createSearchService() {
        return wrap(sf.createSearchService());
    }

    @Override
    public ThumbnailStore createThumbnailService() {
        return wrap(sf.createThumbnailService());
    }

    @Override
    public IAdmin getAdminService() {
        return wrap(sf.getAdminService());
    }

    @Override
    public IAnalysis getAnalysisService() {
        return wrap(sf.getAnalysisService());
    }

    @Override
    public IConfig getConfigService() {
        return wrap(sf.getConfigService());
    }

    @Override
    public IDelete getDeleteService() {
        return wrap(sf.getDeleteService());
    }

    @Override
    public ILdap getLdapService() {
        return wrap(sf.getLdapService());
    }

    @Override
    public IPixels getPixelsService() {
        return wrap(sf.getPixelsService());
    }

    @Override
    public IContainer getContainerService() {
        return wrap(sf.getContainerService());
    }

    @Override
    public IQuery getQueryService() {
        return wrap(sf.getQueryService());
    }

    @Override
    public IRenderingSettings getRenderingSettingsService() {
        return wrap(sf.getRenderingSettingsService());
    }

    @Override
    public IRepositoryInfo getRepositoryInfoService() {
        return wrap(sf.getRepositoryInfoService());
    }

    @Override
    public <T extends ServiceInterface> T getServiceByClass(Class<T> klass) {
        return wrap(sf.getServiceByClass(klass));
    }

    @Override
    public Session getSession() throws ApiUsageException {
        return sf.getSession();
    }

    @Override
    public ISession getSessionService() {
        return wrap(sf.getSessionService());
    }

    @Override
    public IShare getShareService() {
        return wrap(sf.getShareService());
    }

    @Override
    public ITypes getTypesService() {
        return wrap(sf.getTypesService());
    }

    @Override
    public IUpdate getUpdateService() {
        return wrap(sf.getUpdateService());
    }

    @Override
    public void setSession(Session session) throws ApiUsageException {
        sf.setSession(session);
    }

    @Override
    public String toString() {
        return sf.toString();
    }

}