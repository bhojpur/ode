package ode.services.messages;

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

import ode.system.OdeContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.ResolvableType;

/**
 * Global {@link ApplicationEventMulticaster} which can be used to integrate
 * parent and child {@link OdeContext} instances. A singleton, this instance
 * will delegate all method calls to a single static {@link SimpleApplicationEventMulticaster}.
 * 
 * @see ode.system.OdeContext
 * @see ode.system.OdeContext#publishEvent(ApplicationEvent)
 * @see ode.system.OdeContext#onRefresh()
 */
public class GlobalMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    private final static SimpleApplicationEventMulticaster _em = new SimpleApplicationEventMulticaster();

    /**
     * Keeps track of which instance this is. Only the first instance will
     * actively call {@link #multicastEvent(ApplicationEvent)}, but all
     * instances cann add to the static list of
     * {@link ApplicationListener listeners}.
     */
    public GlobalMulticaster() {
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        _em.setBeanFactory(beanFactory);
    }

    @Override
    public void addApplicationListener(ApplicationListener arg0) {
        _em.addApplicationListener(arg0);
    }

    /**
     * Multicast only if this instance was the first created.
     */
    @Override
    public void multicastEvent(ApplicationEvent arg0) {
        _em.multicastEvent(arg0);
    }

    @Override
    public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {

    }

    @Override
    public void removeAllListeners() {
        _em.removeAllListeners();
    }

    @Override
    public void removeApplicationListener(ApplicationListener arg0) {
        _em.removeApplicationListener(arg0);
    }

    @Override
    public void addApplicationListenerBean(String arg0) {
        // Disabling since our use of the context causes
        // duplicate entries now with 3.0
        //_em.addApplicationListenerBean(arg0);
    }

    @Override
    public void removeApplicationListenerBean(String arg0) {
        // Disabling since our use of the context causes
        // duplicate entries now with 3.0
        //_em.removeApplicationListenerBean(arg0);
    }

}