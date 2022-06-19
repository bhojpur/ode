package ode.cmd.admin;

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

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import Ice.Communicator;
import ode.api.local.LocalQuery;
import ode.api.local.LocalUpdate;
import ode.model.meta.Session;
import ode.parameters.Parameters;
import ode.security.AdminAction;
import ode.security.SecuritySystem;
import ode.security.basic.CurrentDetails;
import ode.services.sessions.SessionManager;
import ode.system.ServiceFactory;
import ode.RLong;
import ode.cmd.ERR;
import ode.cmd.HandleI.Cancel;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.OK;
import ode.cmd.Response;
import ode.cmd.UpdateSessionTimeoutRequest;
import ode.util.ObjectFactoryRegistry;

@SuppressWarnings("serial")
public class UpdateSessionTimeoutRequestI extends UpdateSessionTimeoutRequest
    implements IRequest {

    public static class Factory extends ObjectFactoryRegistry {
        private final ObjectFactory factory;
        public Factory(final CurrentDetails current,
                final SessionManager sessionManager,
                final SecuritySystem securitySystem,
                final long maxUserTimeToLive,
                final long maxUserTimeToIdle) {
            factory = new ObjectFactory(ice_staticId()) {
                @Override
                public Ice.Object create(String name) {
                    return new UpdateSessionTimeoutRequestI(
                            current, sessionManager, securitySystem,
                            maxUserTimeToLive, maxUserTimeToIdle);
                }};
            }

        @Override
        public Map<String, ObjectFactory> createFactories(Communicator ic) {
            return new ImmutableMap.Builder<String, ObjectFactory>()
                    .put(ice_staticId(), factory).build();
        }
    }

    protected Helper helper;

    protected LocalQuery query;

    protected LocalUpdate update;

    protected final CurrentDetails current;

    protected final SessionManager manager;

    protected final SecuritySystem security;

    protected boolean updated = false;

    protected final long maxUserTimeToLive;

    protected final long maxUserTimeToIdle;

    public UpdateSessionTimeoutRequestI(CurrentDetails current,
            SessionManager manager, SecuritySystem security,
            long maxUserTimeToLive, long maxUserTimeToIdle) {
        this.current = current;
        this.manager = manager;
        this.security = security;
        this.maxUserTimeToLive = maxUserTimeToLive;
        this.maxUserTimeToIdle = maxUserTimeToIdle;
    }

    //
    // CMD API
    //

    @Override
    public Map<String, String> getCallContext() {
        return null;
    }

    public void init(Helper helper) {
        this.helper = helper;
        this.helper.setSteps(1);
        ServiceFactory sf = this.helper.getServiceFactory();
        query = (LocalQuery) sf.getQueryService();
        update = (LocalUpdate) sf.getUpdateService();
    }

    public Object step(int step) throws Cancel {
        helper.assertStep(step);
        return updateSession();
    }

    @Override
    public void finish() throws Cancel {
        // no-op
    }

    public void buildResponse(int step, Object object) {
        helper.assertResponse(step);
        if (helper.isLast(step)) {
            manager.reload(session);
            helper.setResponseIfNull(new OK());
        }
    }

    public Response getResponse() {
        return helper.getResponse();
    }

    //
    // IMPLEMENTATION
    //

    protected Session updateSession() {
        Session s = helper.getServiceFactory().getQueryService()
                .findByQuery("select s from Session s where s.uuid = :uuid",
                new Parameters().addString("uuid", session));

        if (s == null) {
            // we assume that if the session is visible, then
            // the current user should be able to edit it.
            throw helper.cancel(new ERR(), null, "no-session");
        }

        boolean isAdmin = current.getCurrentEventContext().isCurrentUserAdmin();
        if (!isAdmin && maxUserTimeToLive != 0 && timeToLive != null
                && timeToLive.getValue() > maxUserTimeToLive) {
            timeToLive = ode.rtypes.rlong(maxUserTimeToLive);
            helper.info("Attempt to modify timeToLive beyond maximum");
        }
        if (!isAdmin && timeToIdle != null
                && timeToIdle.getValue() > maxUserTimeToIdle) {
            timeToIdle = ode.rtypes.rlong(maxUserTimeToIdle);
            helper.info("Attempt to modify timeToIdle beyond maximum");
        }
        updated |= updateField(s, Session.TIMETOLIVE, timeToLive, isAdmin);
        updated |= updateField(s, Session.TIMETOIDLE, timeToIdle, isAdmin);
        if (updated) {
            security.runAsAdmin(new AdminAction(){
                @Override
                public void runAsAdmin() {
                    update.flush();
                }});
            return s;
        } else {
            throw helper.cancel(new ERR(), null, "no-update-performed",
                    "session", session);
        }
    }

    protected boolean updateField(Session s, String field, RLong value,
            boolean isAdmin) {

        if (value == null) {
            return false;
        }

        long target = value.getValue();
        long current = ((Long) s.retrieve(field)).longValue();
        long diff = target - current;
        if (!isAdmin && diff != 0 && target <= 0) {
            throw helper.cancel(new ERR(), null, "non-admin-disabling",
                    "field", field,
                    "target", ""+target,
                    "current", ""+current);
        }

        helper.info("Modifying %s from %s to %s for %s",
                field, current, target, session);
        s.putAt(field, target);
        return true;
    }
}