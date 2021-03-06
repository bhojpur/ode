package ode.cmd.basic;

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
import java.util.List;
import java.util.Map;

import ode.services.messages.ContextMessage;
import ode.services.util.ReadOnlyStatus;
import ode.system.OdeContext;

import ode.cmd.DoAll;
import ode.cmd.DoAllRsp;
import ode.cmd.ERR;
import ode.cmd.HandleI.Cancel;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.Request;
import ode.cmd.Response;
import ode.cmd.Status;

/**
 * Permits performing multiple operations
 */
public class DoAllI extends DoAll implements IRequest, ReadOnlyStatus.IsAware {

    private static final long serialVersionUID = -323423435135556L;

    //
    // Mapping from steps to subrequests
    //

    /**
     * Pointer-like object which is saved for each
     * sub-request. The logic for properly mapping
     * from the global step number to the individual
     * substep number is done here. In order to find
     * the proper {@link X} instance, use the current
     * index:
     *
     * <pre>
     * X x = substeps.get(current);
     * </pre>
     */
    private static class X {

        /**
         * Number of steps that should be deducted from the global step
         * count in order to have the proper substep number
         */
        final int offset;

        /**
         * Sub-{@link Helper} instance for the {@link #r}
         */
        final Helper h;

        /**
         * Sub-{@link Request} instance which is to be run.
         */
        final IRequest r;

        final OdeContext ctx;

        /**
         * Calculated context which should be in effect for {@link #r}.
         */
        Map<String, String> c = null;

        X(int offset, Helper h, IRequest r, OdeContext ctx) {
            this.offset = offset;
            this.h = h;
            this.r = r;
            this.ctx = ctx;
        }

        /**
         * Run the {@link IRequest#step(int)} passing in the proper substep
         * value after being calculated via {@link #offset}
         */
        Object step(int step) {
            final int substep = step - offset;
            h.getStatus().currentStep = substep;
            return r.step(substep);
        }

        /**
         * Run the {@link IRequest#buildResponse(int, Object)} passing in the
         * proper substep value after being calculated via {@link #offset}
         */
        void buildResponse(int step, Object object) {
            r.buildResponse(step - offset, object);
        }

        /**
         * Fill in the call context for this instance ignoring nulls and empty
         * maps.
         * @param classContext The return value of {@link IRequest#getCallContext()}
         * @param callContext The corresponding instance from {@link DoAll#contexts}
         */
        void calculateContext(Map<String, String> classContext,
            Map<String, String> callContext) {
            putAll(classContext);
            putAll(callContext);
        }

        /**
         * Helper
         */
        private void putAll(Map<String, String> context) {
            if (context != null && context.size() > 0) {
                if (c == null) {
                    c = new HashMap<String, String>();
                }
                c.putAll(context);
            }
        }

        /**
         * Send a {@link PushContextMessage} to apply this context if not null.
         */
        void login() throws Throwable {
            if (c !=  null) {
                h.debug("Login: %s", c);
                ctx.publishMessage(new ContextMessage.Push(this, c));
            }
        }

        /**
         * If a {@link PushContextMessage} was sent, send a {@link PopContextMessage}
         * so that the context for following actions are not polluted.
         */
        void logout() throws Throwable {
            if (c != null) {
                ctx.publishMessage(new ContextMessage.Pop(this ,c));
            }
        }
    }

    /**
     * State-objects for each subrequest
     */
    private final List<X> substeps = new ArrayList<X>();

    /**
     * current substep.
     */
    private int current = -1;

    /**
     * step at which we flip to the next current.
     */
    private int nextAt = 0;

    /**
     * Looks up the current substep based on the total step count using
     * {@link #nextAt} to determine if {@link #current} needs to be incremented.
     * If login is true, then {@link X#login()} and {@link X#logout} will be
     * called as appropriate.
     *
     * @param step the overall step number
     * @param login if {@link X#login()} and {@link X#logout} should be called
     * @return the current substep information
     */
    private X substep(final int step, final boolean login) {
        X x = null;
        try {
            if (step == 0) {
                // Restart
                x = substeps.get(0);
                current = 0;
                nextAt = x.offset + x.h.getSteps();
                if (login) {
                    x.login();
                }
            } else if (step == nextAt) {
                // Flip to next substep. We should never have
                // a step which makes current >= substeps.size()

                X prev = substeps.get(current);
                if (login) {
                    prev.logout();
                }

                current += 1;
                x = substeps.get(current);
                nextAt = x.offset + x.h.getSteps();
                if (login) {
                    x.login();
                }
            } else {
                x = substeps.get(current);
            }
            return x;
        } catch (Throwable t) {
            throw helper.cancel(new ERR(), t, "substep-lookup-failed", "step",
                ""+step, "req", (x==null ? "null" : ""+x.r));
        }
    }

    //
    // Primary state
    //

    private final List<Status> statuses = new ArrayList<Status>();

    private final List<Response> responses = new ArrayList<Response>();

    /**
     * Helper instance for this class. Will create a number of sub-helper
     * instances for each request.
     */
    private Helper helper;

    //
    // For publishing messages
    //
    //

    private final OdeContext ctx;

    public DoAllI(OdeContext ctx) {
        this.ctx = ctx;
    }

    //
    // IRequest methods
    //

    public Map<String, String> getCallContext() {
        return null;
    }

    public void init(Helper helper) {
        this.helper = helper;
        int steps = 0;
        try {

            Map<String, String> allgroups = new HashMap<String, String>();
            allgroups.put("ode.group", "-1");

            for (int i = 0; i < this.requests.size(); i++) {
                final Request req = requests.get(i);
                final Status substatus = new Status();
                final Helper subhelper = helper.subhelper(req, substatus);
                if (req instanceof IRequest) {
                    IRequest ireq = (IRequest) req;
                    final X x = new X(steps, subhelper, ireq, ctx);
                    try {
                        x.calculateContext(ireq.getCallContext(),
                                (contexts == null || contexts.length <= i)
                                    ? null : contexts[i]);
                        x.login();
                        try {
                            ireq.init(subhelper);
                            statuses.add(substatus);
                            substeps.add(x);
                            long intermediate = substatus.steps;
                            if ((intermediate + steps) > Integer.MAX_VALUE) {
                                throw helper.cancel(new ERR(), null, "too-many-steps",
                                    "Steps", ""+intermediate,
                                    "Message", "Too many steps found! Try fewer actions in one command");
                            }
                            steps += intermediate;
                        } finally {
                            x.logout();
                        }
                    } catch (Cancel c) {
                        throw subcancel(c, x);
                    }
                }
                else {
                    throw helper.cancel(new ERR(), null, "bad-request",
                        "type", req.ice_id());
                }
            }
        }  catch (Cancel c) {
            throw c; // just re-throw
        } catch (Throwable t) {
            helper.cancel(new ERR(), t, "bad-init");
        }
        helper.setSteps(steps);
    }

    public Object step(int step) {
        helper.assertStep(step);
        final X x = substep(step, true);
        try {
            return x.step(step);
        }
        catch (Cancel c) {
            throw subcancel(c, x);
        }
    }

    public void finish() {
        for (X x : substeps) {
            try {
                x.login();
                try {
                    x.r.finish();
                } finally {
                    x.logout();
                }
            } catch (Cancel c) {
                throw subcancel(c, x);
            } catch (Throwable t) {
                helper.cancel(new ERR(), t, "bad-finish");
            }
        }
    }

    public void buildResponse(int step, Object object) {
        helper.assertResponse(step);
        final X x = substep(step, false);
        x.buildResponse(step, object);

        if (helper.isLast(step)) {
            for (Request subreq : requests) {
                // Again, must be an irequest
                IRequest ireq = (IRequest) subreq;
                responses.add(ireq.getResponse());
            }
            DoAllRsp rsp = new DoAllRsp(responses, statuses);
            helper.setResponseIfNull(rsp);
        }
    }

    public Response getResponse() {
        return helper.getResponse();
    }

    protected Cancel subcancel(Cancel c, X x) {
        final Response subrsp = x.h.getResponse();
        final Status substatus = x.h.getStatus();
        final Status status = helper.getStatus();
        helper.setResponseIfNull(subrsp);
        status.flags.addAll(substatus.flags);
        if (status.parameters == null) {
            status.parameters = new HashMap<String, String>();
        }
        if (substatus.parameters != null) {
            status.parameters.putAll(substatus.parameters);
        }
        status.parameters.put("subrequest", ""+subrsp);
        status.category = ice_id();
        status.name = "subcancel";
        throw c;
    }

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        for (final Request request : requests) {
            if (!(request instanceof ReadOnlyStatus.IsAware && ((ReadOnlyStatus.IsAware) request).isReadOnly(readOnly))) {
                return false;
            }
        }
        return true;
    }
}