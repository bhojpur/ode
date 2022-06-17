package ode.services.util;

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

import ode.conditions.SessionException;
import ode.model.meta.Session;
import ode.services.sessions.SessionManager;
import ode.system.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Thread which can be started and will appropriately acquire a session, then
 * use the {@link Executor} to complete its work.
 */
public abstract class ExecutionThread implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(ExecutionThread.class);

    final protected SessionManager manager;
    final protected Executor executor;
    final protected Executor.Work work;
    final protected Principal principal;
    private Principal sessionPrincipal = null;
    private Session session = null;

    /**
     * Main constructor. No arguments can be null.
     */
    public ExecutionThread(SessionManager manager, Executor executor,
            Executor.Work work, Principal principal) {
        Assert.notNull(manager);
        Assert.notNull(executor);
        Assert.notNull(work);
        Assert.notNull(principal);
        this.manager = manager;
        this.executor = executor;
        this.work = work;
        this.principal = principal;
    }

    /**
     * Initializes the {@link Session} for this {@link Thread} if necessary,
     * then calls {@link #doRun()}.
     */
    public final void run() {
        sessionInit();
        doRun();
    }

    public final Principal getPrincipal() {
        return sessionPrincipal;
    }

    /**
     */
    public abstract void doRun();

    protected final void sessionInit() {

        if (sessionPrincipal != null) {
            try {
                this.manager.getEventContext(sessionPrincipal);
            } catch (SessionException e) {
                sessionPrincipal = null;
            }
        }

        if (sessionPrincipal == null) {
            session = this.manager.createWithAgent(principal, "ExecutionThread", null);
            sessionPrincipal = new Principal(session.getUuid(), principal
                    .getGroup(), principal.getEventType());
        }
    }

}
