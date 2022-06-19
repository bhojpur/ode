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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ode.system.EventContext;
import ode.security.basic.CurrentDetails;
import ode.services.sessions.SessionManager;
import ode.services.sessions.SessionProvider;
import ode.services.util.ReadOnlyStatus;

import ode.system.ServiceFactory;
import ode.system.SimpleEventContext;
import ode.RType;
import ode.cmd.CurrentSessionsRequest;
import ode.cmd.CurrentSessionsResponse;
import ode.cmd.HandleI.Cancel;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.Response;
import ode.model.Session;
import ode.util.IceMapper;
import ode.util.ObjectFactoryRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Communicator;

import com.google.common.collect.ImmutableMap;

@SuppressWarnings("serial")
public class CurrentSessionsRequestI extends CurrentSessionsRequest
    implements IRequest, ReadOnlyStatus.IsAware {

    private final Logger log = LoggerFactory.getLogger(CurrentSessionsRequestI.class);

    public static class Factory extends ObjectFactoryRegistry {
        private final ObjectFactory factory;
        public Factory(final CurrentDetails current,
                final SessionManager sessionManager, final SessionProvider sessionProvider) {
            factory = new ObjectFactory(ice_staticId()) {
                @Override
                public Ice.Object create(String name) {
                    return new CurrentSessionsRequestI(
                            current, sessionManager, sessionProvider);
                }};
            }

        @Override
        public Map<String, ObjectFactory> createFactories(Communicator ic) {
            return new ImmutableMap.Builder<String, ObjectFactory>()
                    .put(ice_staticId(), factory).build();
        }
    }

    protected Helper helper;

    protected final CurrentDetails current;

    protected final SessionManager manager;

    protected final SessionProvider provider;

    protected Map<String, Map<String, Object>> contexts;

    public CurrentSessionsRequestI(CurrentDetails current,
            SessionManager manager, SessionProvider provider) {
        this.current = current;
        this.manager = manager;
        this.provider = provider;
    }

    private boolean isCurrentUserGuest = false;

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
        isCurrentUserGuest = current.isCurrentUserGuest();
    }

    public Object step(int step) throws Cancel {
        helper.assertStep(step);

        contexts = manager.getSessionData();
        if (contexts.isEmpty()) {
            return Collections.emptyList();
        }
        final ServiceFactory sf = helper.getServiceFactory();
        final List<ode.model.meta.Session> sessions = new ArrayList<>(contexts.size());
        for (final String uuid : contexts.keySet()) {
            final Long sessionId = provider.findSessionIdByUuid(uuid, sf);
            if (sessionId != null) {
                final ode.model.meta.Session session = provider.findSessionById(sessionId, sf);
                if (session != null) {
                    sessions.add(session);
                }
            }
        }
        return sessions;
    }

    @Override
    public void finish() throws Cancel {
        // no-op
    }

    @SuppressWarnings("unchecked")
    public void buildResponse(int step, Object object) {
        helper.assertResponse(step);

        List<ode.model.meta.Session> rv = (List<ode.model.meta.Session>) object;
        Map<String, Session> objects = new HashMap<String, Session>();
        IceMapper mapper = new IceMapper();
        for (ode.model.meta.Session obj : rv) {
            if (obj.isLoaded()) {
                objects.put(obj.getUuid(), (Session) mapper.map(obj));
            }
        }

        if (helper.isLast(step)) {
            final int size = contexts.size();
            CurrentSessionsResponse rsp = new CurrentSessionsResponse();
            rsp.sessions = new ArrayList<Session>(size);
            rsp.contexts = new ArrayList<ode.sys.EventContext>(size);
            final List<Map<String, RType>> dataMaps = new ArrayList<Map<String, RType>>(size);
            for (Map.Entry<String, Map<String, Object>> entry : contexts.entrySet()) {
                String uuid = entry.getKey();
                Map<String, Object> data = entry.getValue();
                EventContext orig = new SimpleEventContext(
                        (EventContext) data.get("sessionContext"));
                Session s = objects.get(uuid);
                if (s == null) {
                    // Non-admin
                    if (isCurrentUserGuest) {
                        continue;
                    }
                    rsp.sessions.add(s);
                    ode.sys.EventContext ec = new ode.sys.EventContext();
                    rsp.contexts.add(ec);
                    ec.userId = orig.getCurrentUserId();
                    ec.userName = orig.getCurrentUserName();
                    ec.groupId = orig.getCurrentGroupId();
                    ec.groupName = orig.getCurrentGroupName();
                    ec.isAdmin = orig.isCurrentUserAdmin();
                    dataMaps.add(Collections.<String, RType>emptyMap());
                } else {
                    rsp.sessions.add(s);
                    rsp.contexts.add(IceMapper.convert(orig));
                    dataMaps.add(parseData(rsp, data));
                }
            }
            rsp.data = dataMaps.toArray(new Map[dataMaps.size()]);
            helper.setResponseIfNull(rsp);
        }
    }

    private Map<String, RType> parseData(CurrentSessionsResponse rsp, Map<String, Object> data) {
        Map<String, RType> parsed = new HashMap<String, RType>();
        for (Map.Entry<String, Object> entry2 : data.entrySet()) {
            String key2 = entry2.getKey();
            Object obj2 = entry2.getValue();
            if ("class".equals(key2) || "sessionContext".equals(key2)) {
                continue;
            }
            RType wrapped = null;
            try {
                if (key2.endsWith("Time")) {
                    wrapped = ode.rtypes.rtime((Long)obj2);
                } else {
                    wrapped = ode.rtypes.wrap(obj2);
                }
            } catch (ode.ClientError ce) {
                log.warn("Failed to convert {}", obj2, ce);
                wrapped = ode.rtypes.rstring(obj2.toString());
            }
            parsed.put(key2, wrapped);
        }
        return parsed;
    }

    public Response getResponse() {
        return helper.getResponse();
    }

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        return true;
    }
}