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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import ode.api.ServiceInterface;
import ode.conditions.ApiUsageException;
import ode.util.messages.InternalMessage;
import ode.util.messages.MessageException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

/**
 * Provides static access for the creation of singleton and non-singleton
 * application contexts. Also provides context names as constant fields which
 * can be used for the lookup of particular contexts, through either
 * {@link #getInstance(String)} or
 * {@link ode.system.ServiceFactory#ServiceFactory(String)}.
 * 
 * By passing a {@link java.util.Properties} instance into the
 * {@link #getClientContext(Properties)} method, a non-static version is
 * created. Currently this is only supported for the client context.
 */
public class OdeContext extends ClassPathXmlApplicationContext {
    /**
     * identifier for an OdeContext configured in
     * classpath*:beanRefContext.xml for use by remote (via JNDI/RMI) clients.
     */
    public final static String CLIENT_CONTEXT = "ode.client";

    /**
     * identifier for an OdeContext configured in
     * classpath*:beanRefContext.xml for use by server-side processes. All
     * objects obtained from the context are in a state for immediate use.
     */
    public final static String MANAGED_CONTEXT = "ode.server";

    private static OdeContext _client;

    private static OdeContext _managed;;

    /**
     * Multicaster used by this instance. Unlike other Spring application
     * contexts, this context assumes that a "global multicaster" is configured
     * and so does not pass messages to the parent context, but assumes that
     * the multicaster will do this. In order to implement this logic, it is
     * necessary to lookup the entity by name, since all getters and fields
     * are private in the superclasses.
     */
    private ApplicationEventMulticaster multicaster;
    
    // ~ Constructors
    // =========================================================================
    
    public OdeContext(String configLocation) throws BeansException {
        super(configLocation);
    }

    public OdeContext(String[] configLocations) throws BeansException {
        super(configLocations);
    }

    public OdeContext(String[] configLocations, boolean refresh)
            throws BeansException {
        super(configLocations, refresh);
    }

    public OdeContext(String[] configLocations, ApplicationContext parent)
            throws BeansException {
        super(configLocations, parent);
    }

    public OdeContext(String[] configLocations, boolean refresh,
            ApplicationContext parent) throws BeansException {
        super(configLocations, refresh, parent);
    }

    // ~ Creation
    // =========================================================================

    private final static Object mutex = new Object();

    /**
     * create (if necessary) and return the single default client OdeContext.
     * Any two calls to this method will return the same (==) context instance.
     * 
     * @see #CLIENT_CONTEXT
     */
    public static OdeContext getClientContext() {
        synchronized (mutex) {
            if (_client == null) {
                _client = getInstance(CLIENT_CONTEXT);
            }

            return _client;
        }
    }

    /**
     * initialize a new client OdeContext (named {@link #CLIENT_CONTEXT}),
     * using the {@link #getContext(Properties, String)} method.
     * 
     * @see #getContext(Properties, String)
     * @see #CLIENT_CONTEXT
     * @see ServiceFactory#ServiceFactory(Login)
     * @see ServiceFactory#ServiceFactory(Server)
     * @see ServiceFactory#ServiceFactory(Properties)
     */
    public static OdeContext getClientContext(Properties props) {
        return getContext(props, CLIENT_CONTEXT);
    }

    /**
     * initialize a new client OdeContext using the {@link Properties}
     * provided as values for property (e.g. ${name}) replacement in Spring. Two
     * calls to this method with the same argument will return different ( =! )
     * contexts.
     * 
     * @param props
     *            Non-null properties for replacement.
     * @param context
     *            Non-null name of context to find in beanRefContext.xml
     * 
     * @see ServiceFactory#ServiceFactory(Login)
     * @see ServiceFactory#ServiceFactory(Server)
     * @see ServiceFactory#ServiceFactory(Properties)
     */
    public static OdeContext getContext(Properties props, String context) {
        if (props == null || context == null) {
            throw new ApiUsageException("Arguments may not be null.");
        }

        Properties copy = new Properties(props);
        ConstructorArgumentValues ctorArg = new ConstructorArgumentValues();
        ctorArg.addGenericArgumentValue(copy);
        BeanDefinition definition = new RootBeanDefinition(Properties.class,
                ctorArg, null);
        StaticApplicationContext staticContext = new StaticApplicationContext();
        staticContext.registerBeanDefinition("properties", definition);
        staticContext.refresh();

        OdeContext ctx = new Locator().lookup(context, staticContext);
        return ctx;
    }

    /**
     * create (if necessary) and return the single default managed OdeContext.
     * Any two calls to this method will return the same (==) context instance.
     * Managed means that the services are fully wrapped by interceptors, and
     * are essentially the services made available remotely.
     * 
     * @see #MANAGED_CONTEXT
     */
    public static OdeContext getManagedServerContext() {
        synchronized (mutex) {
            if (_managed == null) {
                _managed = getInstance(MANAGED_CONTEXT);
            }

            return _managed;
        }

    }

    /**
     * create (if necessary) and return the single default OdeContext named by
     * the beanFactoryName parameter. Any two calls to this method with the same
     * parameter will return the same (==) context instance.
     * 
     * @see #getClientContext()
     * @see #getManagedServerContext()
     */
    public static OdeContext getInstance(String beanFactoryName) {
        OdeContext ctx = (OdeContext) ContextSingletonBeanFactoryLocator
                .getInstance().useBeanFactory(beanFactoryName).getFactory();
        try {
            ctx.getBeanFactory();
        } catch (IllegalStateException ise) {
            // Here we catch IllegalStateException because it implies that
            // the bean factory isn't created yet. However, if that goes
            // wrong, we need to rollback.
            try {
                ctx.refresh();
            } catch (RuntimeException re) {
                if (ctx != null) {
                    try {
                        ctx.close();
                    } catch (Exception e) {
                        // OK ignoring to rethrow the original exception
                    }
                }
                throw re;
            }
        }
        return ctx;
    }

    // ~ Utilities
    // =========================================================================

    /**
     * Uses the methods of this context's {@link BeanFactory} to autowire any
     * Object based on the given beanName.
     * 
     * @see org.springframework.beans.factory.config.AutowireCapableBeanFactory#applyBeanPropertyValues(java.lang.Object,
     *      java.lang.String)
     */
    public void applyBeanPropertyValues(Object target, String beanName) {
        this.getAutowireCapableBeanFactory().applyBeanPropertyValues(target,
                beanName);
    }

    /**
     * Uses the methods of this context's {@link BeanFactory} to autowire any
     * Object based on the service class. This is used by
     * {@link SelfConfigurableService} instances to acquire dependencies.
     * 
     * @see SelfConfigurableService
     * @see org.springframework.beans.factory.config.AutowireCapableBeanFactory#applyBeanPropertyValues(java.lang.Object,
     *      java.lang.String)
     */
    public void applyBeanPropertyValues(Object target,
            Class<? extends ServiceInterface> beanInterface) {
        // TODO: it would be better to have this as <? extends
        // SelfConfigurableService>
        // but there are issues because of the ApplicationContextAware. Perhaps
        // we can combine them later.
        applyBeanPropertyValues(target, "internal-" + beanInterface.getName());
    }

    /**
     * refreshes all the nested OdeContexts within this instance. This is
     * useful when using a static context, and {@link Properties} which were
     * pulled from {@link System#getProperties()} have been changed.
     * 
     * If this is a server-side instance ({@link #MANAGED_CONTEXT}), this may
     * take a significant amount of time.
     * 
     * @see org.springframework.context.ConfigurableApplicationContext#refresh()
     */
    public void refreshAll() {
        ApplicationContext ac = this;
        List<ConfigurableApplicationContext> list = new LinkedList<ConfigurableApplicationContext>();
        while (ac instanceof ConfigurableApplicationContext) {
            list.add((ConfigurableApplicationContext) ac);
            ac = ac.getParent();
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            list.get(i).refresh();
        }

    }

    /**
     * Calls {@link #refreshAll()} if {@link #isRunning()} throws an
     * {@link IllegalStateException}.
     */
    public void refreshAllIfNecessary() {
        try {
            isRunning();
        } catch (IllegalStateException ise) {
            refreshAll();
        }
    }

    /**
     * closes all the nested OdeContexts within this instance.
     * 
     * If this is a server-side instance ({@link #MANAGED_CONTEXT}), this may
     * take a significant amount of time.
     * 
     * @see org.springframework.context.ConfigurableApplicationContext#close()
     */
    public void closeAll() {
        ApplicationContext ac = this;
        List<ConfigurableApplicationContext> list = new LinkedList<ConfigurableApplicationContext>();
        while (ac instanceof ConfigurableApplicationContext) {
            list.add((ConfigurableApplicationContext) ac);
            ac = ac.getParent();
        }

        for (int i = 0; i < list.size(); i++) {
            list.get(i).close();
        }

    }

    public String getProperty(String propertyName) {
        PreferenceContext pc = (PreferenceContext) getBean("preferenceContext");
        return pc.getProperty(propertyName);
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        multicaster.multicastEvent(event);
    }
    
    /**
     * Convenience method around
     * {@link #publishEvent(org.springframework.context.ApplicationEvent)} which
     * catches all {@link MessageException} and unwraps the contained
     * {@link Throwable} instance and rethrows.
     * 
     * @param msg
     * @throws Throwable
     */
    public void publishMessage(InternalMessage msg) throws Throwable {
        try {
            publishEvent(msg);
        } catch (MessageException me) {
            throw me.getException();
        }
    }
    
    @Override
    protected void onRefresh() throws BeansException {
        super.onRefresh();
        multicaster = (ApplicationEventMulticaster) getBean("applicationEventMulticaster");
    }

    // ~ Non-singleton locator
    // =========================================================================

    /**
     * provides access to the protected methods of
     * {@link org.springframework.context.access.ContextSingletonBeanFactoryLocator}
     * which cannot be used externally.
     */
    protected static class Locator extends ContextSingletonBeanFactoryLocator {
        // copied from ContextSingletonBeanFactoryLocator
        private static final String BEANS_REFS_XML_NAME = "classpath*:beanRefContext.xml";

        public Locator() {
            super(null);
        }

        /**
         * uses
         * {@link ContextSingletonBeanFactoryLocator#createDefinition(java.lang.String, java.lang.String)}
         * and
         * {@link ContextSingletonBeanFactoryLocator#initializeDefinition(org.springframework.beans.factory.BeanFactory)}
         * to create a new context from a given definition.
         */
        public OdeContext lookup(String selector, ApplicationContext parent) {
            ConfigurableApplicationContext beanRefContext = (ConfigurableApplicationContext) createDefinition(
                    BEANS_REFS_XML_NAME, "manual");
            initializeDefinition(beanRefContext);

            BeanDefinition definition = beanRefContext.getBeanFactory()
                    .getBeanDefinition(selector);
            ValueHolder holder = definition.getConstructorArgumentValues()
                    .getGenericArgumentValue(List.class);
            List<TypedStringValue> files = (List<TypedStringValue>) holder
                    .getValue();
            List<String> fileStrings = new ArrayList<String>(files.size());
            for (TypedStringValue tsv : files) {
                fileStrings.add(tsv.getValue());
            }

            OdeContext c = new OdeContext(fileStrings
                    .toArray(new String[0]), true, parent);

            return c;
        }

    }

}