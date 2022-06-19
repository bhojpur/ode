package ode.services.sessions.state;

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

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.event.CacheEventListener;
import ode.system.OdeContext;

import org.springframework.beans.BeansException;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CacheFactory extends EhCacheFactoryBean implements
        ApplicationContextAware {

    private OdeContext ctx;

    private CacheEventListener[] cacheListeners;

    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        ctx = (OdeContext) arg0;
    }

    public void setCacheEventListeners(CacheEventListener[] listeners) {
        this.cacheListeners = listeners;
    }

    @Override
    public Ehcache getObject() {
        Ehcache cache = (Ehcache) super.getObject();
        registerAll(cache, cacheListeners);
        return cache;
    }

    public Ehcache createCache(CacheEventListener... listeners) {
        try {
            super.afterPropertiesSet();
            Ehcache cache = (Ehcache) getObject();
            registerAll(cache, listeners);
            return cache;
        } catch (Exception e) {
            throw new RuntimeException("Could not create cache", e);
        }
    }

    protected void registerAll(Ehcache cache, CacheEventListener... l) {
        if (l != null) {
            for (CacheEventListener listener : l) {
                cache.getCacheEventNotificationService().registerListener(
                        listener);
            }
        }
    }
}