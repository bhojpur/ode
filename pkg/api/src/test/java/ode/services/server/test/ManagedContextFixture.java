package ode.services.server.test;

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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ApplicationListener;

import ode.api.IAdmin;
import ode.logic.HardWiredInterceptor;
import ode.model.internal.Permissions;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.model.meta.Session;
import ode.security.SecuritySystem;
import ode.security.basic.PrincipalHolder;
import ode.services.server.fire.AopContextInitializer;
import ode.services.server.impl.AbstractAmdServant;
import ode.services.server.impl.AdminI;
import ode.services.server.impl.ConfigI;
import ode.services.server.impl.QueryI;
import ode.services.server.impl.ServiceFactoryI;
import ode.services.server.impl.ShareI;
import ode.services.server.impl.UpdateI;
import ode.services.server.util.ServerExecutor;
import ode.services.server.util.RegisterServantMessage;
import ode.services.sessions.SessionManager;
import ode.services.util.Executor;
import ode.system.EventContext;
import ode.system.OdeContext;
import ode.system.Principal;
import ode.system.ServiceFactory;
import ode.testing.InterceptingServiceFactory;
import ode.tools.hibernate.ExtendedMetadata;
import ode.tools.spring.InternalServiceFactory;
import ode.util.messages.MessageException;

/**
 * This fixture is copied from components/server/test/ode/server/itests/
 * Obviously copying is less clean then we would like, but for the moment,
 * sharing between test code is not supported in the ant build (Nov2008)
 * 
 * @DEV.TODO Reunite with server code.
 */
public class ManagedContextFixture {

    public final Ice.ObjectAdapter adapter;
    public final OdeContext ctx;
    public final SessionManager mgr;
    public final ExtendedMetadata em;
    public final Executor ex;
    public final ServiceFactoryI sf;
    public final ServiceFactory managedSf;
    public final ServiceFactory internalSf;
    public final SecuritySystem security;
    public final PrincipalHolder holder;
    public final LoginInterceptor login;
    public final AopContextInitializer init;
    public final ServerExecutor be;
    public final SessionManager sm;
    public final SecuritySystem ss;
    protected final List<HardWiredInterceptor> cptors;

    // Servants
    public final AdminI admin;
    public final ConfigI config;
    public final QueryI query;
    public final ShareI share;
    public final UpdateI update;

    public ManagedContextFixture() throws Exception {
        this(OdeContext.getManagedServerContext());
    }

    public void close() {
        ctx.closeAll();
    }

    public ManagedContextFixture(OdeContext ctx) throws Exception {
       this(ctx, false);
    }

    /**
     * Calls {@link #ManagedContextFixture(OdeContext, boolean, String)} with
     * private permissions.
     */
    public ManagedContextFixture(OdeContext ctx, boolean newUser) throws Exception {
        this(ctx, false, "rw----");
    }


    /**
     * Create the fixture. Based on {@link #newUser} either creates a new
     * user or logins the root user.
     * @param ctx
     * @param newUser
     */
    public ManagedContextFixture(OdeContext ctx, boolean newUser, String permissions)
        throws Exception {
        this.ctx = ctx;

        sm = (SessionManager) ctx.getBean("sessionManager");
        ss = (SecuritySystem) ctx.getBean("securitySystem");
        be = (ServerExecutor) ctx.getBean("throttlingStrategy");
        adapter = (Ice.ObjectAdapter) ctx.getBean("adapter");
        mgr = (SessionManager) ctx.getBean("sessionManager");
        em = (ExtendedMetadata) ctx.getBean("extendedMetadata");
        ex = (Executor) ctx.getBean("executor");
        security = (SecuritySystem) ctx.getBean("securitySystem");
        holder = (PrincipalHolder) ctx.getBean("principalHolder");
        login = new LoginInterceptor(holder);
        managedSf = new InterceptingServiceFactory(new ServiceFactory(ctx),
                login);
        internalSf = new InternalServiceFactory(ctx);

        cptors = HardWiredInterceptor
            .parse(new String[] { "ode.security.basic.BasicSecurityWiring" });
        HardWiredInterceptor.configure(cptors, ctx);


        setCurrentUserAndGroup("root", "system");
        if (newUser) {
            loginNewUserNewGroup(permissions);
        }




        sf = createServiceFactoryI();
        init = new AopContextInitializer(
                new ServiceFactory(ctx), login.p, new AtomicBoolean(true));

        ServiceFactory regular = new ServiceFactory(ctx);
        update = new UpdateI(regular.getUpdateService(), be);
        query = new QueryI(regular.getQueryService(), be);
        admin = new AdminI(regular.getAdminService(), be);
        config = new ConfigI(regular.getConfigService(), be);
        share = new ShareI(regular.getShareService(), be);
        configure(update, init);
        configure(query, init);
        configure(admin, init);
        configure(config, init);
        configure(share, init);

        this.ctx.addApplicationListener(
                new ApplicationListener<RegisterServantMessage>(){
                    public void onApplicationEvent(RegisterServantMessage msg) {
                        Ice.Current curr = msg.getCurrent();
                        if (curr.id.category.equals(getPrincipal().getName())) {
                            try {
                                Ice.Identity newId = new Ice.Identity(UUID.randomUUID().toString(), curr.id.name);
                                msg.setServiceFactory(newId, sf);
                            } catch (Throwable t) {
                                throw new MessageException(
                                        "ManagedContextFixture.onApplicationEvent", t);
                            }
                        }
                    }});

    }

    private ServiceFactoryI createServiceFactoryI()
            throws ode.ApiUsageException {
        Ice.Current current = new Ice.Current();
        current.adapter = adapter;
        current.ctx = new HashMap<String, String>();
        current.ctx.put(ode.constants.CLIENTUUID.value, UUID.randomUUID().toString());
        ServiceFactoryI factory = new ServiceFactoryI(current,
                new ode.util.ServantHolder(getPrincipal().getName()), null, ctx, mgr, ex,
                getPrincipal(), new ArrayList<HardWiredInterceptor>(), null, null);
        return factory;
    }

    protected void configure(AbstractAmdServant servant,
            AopContextInitializer ini) {
        servant.setApplicationContext(ctx);
        servant.applyHardWiredInterceptors(cptors, ini);
    }

    public void tearDown() throws Exception {
        ctx.close();
    }

    // UTILITIES
    // =========================================================================

    public String uuid() {
        return UUID.randomUUID().toString();
    }

    // LOGIN / PERMISSIONS
    // =========================================================================

    public long newGroup() {
        return newGroup(Permissions.USER_PRIVATE);
    }

    public long newGroup(Permissions permissions) {
        IAdmin admin = managedSf.getAdminService();
        String groupName = uuid();
        ExperimenterGroup g = new ExperimenterGroup();
        g.getDetails().setPermissions(permissions);
        g.setName(groupName);
        return admin.createGroup(g);
    }

    public void addUserToGroup(long user, long group) {
        addUserToGroup(user, group, false);
    }

    public void addUserToGroup(long user, long group, boolean admin) {
        final IAdmin iAdmin = managedSf.getAdminService();
        final Experimenter e = new Experimenter(user, false);
        final ExperimenterGroup g = new ExperimenterGroup(group, false);

        iAdmin.addGroups(e, g);
        if (admin) {
            iAdmin.addGroupOwners(g, e);
        }
    }

    /**
     * Create a new user in the given group
     */
    public String newUser(String group) {
        IAdmin admin = managedSf.getAdminService();
        Experimenter e = new Experimenter();
        String uuid = uuid();
        e.setOdeName(uuid);
        e.setFirstName("managed");
        e.setMiddleName("context");
        e.setLastName("test");
        admin.createUser(e, group);
        return uuid;
    }

    /**
     * Login a new user into a new private group and return
     */
    public String loginNewUserNewGroup() {
        return loginNewUserNewGroup("rw----");
    }

    /**
     * Long a new user into a new group with the given permissions and return
     */
    public String loginNewUserNewGroup(String perms) {
        IAdmin admin = managedSf.getAdminService();
        String groupName = uuid();
        ExperimenterGroup g = new ExperimenterGroup();
        g.setName(groupName);
        g.getDetails().setPermissions(Permissions.parseString(perms));
        admin.createGroup(g);
        String name = newUser(groupName);
        setCurrentUser(name);
        return name;
    }

    public String getCurrentUser() {
        return managedSf.getAdminService().getEventContext()
                .getCurrentUserName();
    }

    public EventContext getCurrentEventContext() {
        return managedSf.getAdminService().getEventContext();
    }

    public void setCurrentUser(String user) {
        setCurrentUserAndGroup(user, "user");
    }

    public void setCurrentUserAndGroup(String user, String group) {
        Principal p = new Principal(user, group, "Test");
        Session s = mgr.createWithAgent(p, "ManagedFixture", "127.0.0.1");
        p = new Principal(s.getUuid(), group, "Test");
        login.p = p;
    }

    public Principal getPrincipal() {
        return login.p;
    }
}