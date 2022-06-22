package ode.system.utests;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.StaticApplicationContext;
import org.testng.Assert;
import org.testng.annotations.Test;

import ode.system.OdeContext;

public class ContextTest {

    public final static String C = "collector";

    public final static String FOO = "foo";

    public final static String BAR = "bar";

    public final static String BAX = "bax";

    @Test
    public void test_uniqueNonStaticContext() throws Exception {
        Properties p = new Properties();
        OdeContext c1 = OdeContext.getClientContext(p);
        OdeContext c2 = OdeContext.getClientContext(p);
        OdeContext c3 = OdeContext.getClientContext(new Properties());
        Assert.assertTrue(c1 != c2);
        Assert.assertTrue(c1 != c3);
        Assert.assertTrue(c2 != c3);
    }

    @Test
    public void test_properOdeContextCtorIsUsed() throws Exception {
        StaticApplicationContext beanRef = new StaticApplicationContext();
        ConstructorArgumentValues args = new ConstructorArgumentValues();
        args.addIndexedArgumentValue(0, new ArrayList());
        args.addIndexedArgumentValue(1, Boolean.TRUE);
        args.addIndexedArgumentValue(2, new RuntimeBeanReference("parent"));
        RootBeanDefinition parent = new RootBeanDefinition(
                StaticApplicationContext.class, null, null);
        parent.setInitMethodName("refresh");
        beanRef.registerBeanDefinition("parent", parent);
        beanRef.registerBeanDefinition("test", new RootBeanDefinition(
                CtorOdeContext.class, args, null));
        beanRef.refresh();
        beanRef.getBean("test");
    }

    @Test(groups = "ticket:116")
    public void test_refreshOrdering() throws Exception {

        // checks for refresh calls.
        Collector c = new Collector();

        // a way to access this
        Map map = new HashMap();
        map.put(C, c);
        ConstructorArgumentValues args = new ConstructorArgumentValues();
        args.addGenericArgumentValue(map);

        // set our static variable
        System.getProperties().setProperty(FOO, BAR);

        // our parent who has the PropertyPlaceholder
        StaticApplicationContext parent = new StaticApplicationContext();
        parent.registerBeanDefinition("parentListener", new RootBeanDefinition(
                ParentListener.class, args, null));
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        parent.addBeanFactoryPostProcessor(ppc);
        parent.refresh();
        parent.publishEvent(new ContextRefreshedEvent(parent));
        Assert.assertTrue(c.parentRefreshed);

        // Ok that worked now moving on.
        c.reset();
        // and see if reset works
        Assert.assertFalse(c.parentRefreshed);
        Assert.assertFalse(c.childRefreshed);

        // our child who depends on the PropertyPlaceholders in the parent
        StaticApplicationContext child = new StaticApplicationContext(parent);
        child.registerBeanDefinition("childListener", new RootBeanDefinition(
                ChildListener.class, args, null));

        ConstructorArgumentValues stringArgs = new ConstructorArgumentValues();
        stringArgs.addGenericArgumentValue("${foo}");
        child.registerBeanDefinition("string", new RootBeanDefinition(
                String.class, stringArgs, null));
        child.refresh();
        Assert.assertTrue(c.childRefreshed);
        Assert.assertFalse(c.parentRefreshed);

        // ignoring for the moment
        // assertTrue( (String) child.getBean("string"), BAR.equals(
        // child.getBean( "string" )));

    }

    @Test(groups = "ticket:116")
    public void test_refreshOrdering_withOdeContext() throws Exception {

        OdeContext ctx = OdeContext
                .getInstance(ContextTest.class.getName());
        Map map = (Map) ctx.getBean("map");
        Collector c = (Collector) map.get(ContextTest.C);
        // initial test
        c.reset();
        Assert.assertFalse(c.childRefreshed);
        Assert.assertFalse(c.parentRefreshed);
        // test refresh()
        ctx.refresh();
        Assert.assertTrue(c.childRefreshed);
        Assert.assertFalse(c.parentRefreshed);
        // test refreshAll
        c.reset();
        ctx.refreshAll();
        map = (Map) ctx.getBean("map"); // have to re-get the collector
        c = (Collector) map.get(ContextTest.C); // since a new one was created.
        Assert.assertTrue(c.childRefreshed); // on parent refresh.
        Assert.assertTrue(c.parentRefreshed);
    }

}

class Collector {
    public void reset() {
        parentRefreshed = false;
        childRefreshed = false;
    }

    public boolean parentRefreshed = false;

    public boolean childRefreshed = false;
}

abstract class AbstractListener implements ApplicationListener,
        ApplicationContextAware {
    protected Map m;

    protected ApplicationContext ctx;

    public AbstractListener(Map map) {
        m = map;
    }

    public boolean isRefresh(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent
                && ((ContextRefreshedEvent) event).getApplicationContext()
                        .equals(ctx)) {

            return true;
        }
        return false;
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.ctx = applicationContext;
    }

}

class ParentListener extends AbstractListener {
    public ParentListener(Map map) {
        super(map);
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (isRefresh(event)) {
            ((Collector) m.get(ContextTest.C)).parentRefreshed = true;
        }
    }
}

class ChildListener extends AbstractListener {
    public ChildListener(Map map) {
        super(map);
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (isRefresh(event)) {
            ((Collector) m.get(ContextTest.C)).childRefreshed = true;
        }
    }
}

class CtorOdeContext extends OdeContext {

    public CtorOdeContext(String configLocation) throws BeansException {
        super(configLocation);
        throw new RuntimeException("don't use single string");
    }

    public CtorOdeContext(String[] configLocations) throws BeansException {
        super(configLocations);
        throw new RuntimeException("don't use string array");
    }

    public CtorOdeContext(String[] configLocations, boolean refresh)
            throws BeansException {
        super(configLocations, refresh);
        throw new RuntimeException("don't use string array, boolean");
    }

    public CtorOdeContext(String[] configLocations, ApplicationContext parent)
            throws BeansException {
        super(configLocations, parent);
        throw new RuntimeException("don't use string array, parent");
    }

    public CtorOdeContext(String[] configLocations, boolean refresh,
            ApplicationContext parent) throws BeansException {
        super(configLocations, refresh, parent);
    }

}