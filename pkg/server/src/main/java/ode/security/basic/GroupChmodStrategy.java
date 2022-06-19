package ode.security.basic;

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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import ode.conditions.ApiUsageException;
import ode.conditions.GroupSecurityViolation;
import ode.conditions.InternalException;
import ode.conditions.SecurityViolation;
import ode.model.IObject;
import ode.model.internal.Permissions;
import ode.model.internal.Permissions.Right;
import ode.model.internal.Permissions.Role;
import ode.model.meta.ExperimenterGroup;
import ode.security.ChmodStrategy;
import ode.services.messages.ContextMessage;
import ode.services.messages.EventLogMessage;
import ode.system.OdeContext;
import ode.tools.hibernate.ExtendedMetadata;
import ode.tools.hibernate.SessionFactory;
import ode.util.SqlAction;
import ode.util.Utils;

/**
 * {@link ChmodStrategy} which only permits modifying
 * the permissions on groups.
 */
public class GroupChmodStrategy implements ChmodStrategy,
        ApplicationContextAware {

    /**
     * States whether or not the permissions passed in have a reduction of
     * one of the read permissions. If so, then more checks will be needed
     * during {@link GroupChmodStrategy#check(IObject, Object)}.
     */
    private static class PermDrop {

        static final Role u = Role.USER;
        static final Role g = Role.GROUP;
        static final Role a = Role.WORLD;
        static final Right r = Right.READ;

        final Permissions oldPerms; // = trusted.getDetails().getPermissions();
        final Permissions newPerms; // = Permissions.parseString(permissions);

        final boolean reduceGroup;

        // Dropping world permissions should not incur any issues since
        // this simply means that external users will no longer be allowed
        // to log into the group. There data can remain in the group
        // and still be viewed by other group members.
        // final boolean reduceWorld; // IGNORED.

        PermDrop(ExperimenterGroup trusted, String permissions) {
            oldPerms = trusted.getDetails().getPermissions();
            newPerms = Permissions.parseString(permissions);

            if (!newPerms.isGranted(u, r)) {
                throw new GroupSecurityViolation("Cannot remove user read: "
                        + trusted);
            }

            if (oldPerms.isGranted(g, r) && !newPerms.isGranted(g, r)) {
                reduceGroup = true;
            }
            else {
                reduceGroup = false;
            }

        }

        boolean found() {
            return reduceGroup;
        }
    }

    /**
     * Opaque object passed out to consumers of the
     * {@link ChmodStrategy#getChecks(IObject, String)} method. When passed
     * back in, these are responsible for checking what DB state may possible
     * disallow a chmod to be performed.
     */
    private static class Check {
        final long groupID;
        final String perms;
        final Class<?> k;
        final String[][] lockChecks;
        final PermDrop drop;

        Check(long groupID, String perms, Class<?> k, String[][] lockChecks,
                PermDrop drop) {
            this.groupID = groupID;
            this.perms = perms;
            this.k = k;
            this.lockChecks = lockChecks;
            this.drop = drop;
        }

        public Map<String, Long> run(Session session, ExtendedMetadata em) {
            StringBuilder sb = new StringBuilder();
            sb.append("x.details.group.id = ");
            sb.append(groupID);
            sb.append(" and ");
            sb.append("y.details.group.id = ");
            sb.append(groupID);

            if (drop.reduceGroup) {
                sb.append(" and x.details.owner.id <> y.details.owner.id");
            }

            return em.countLocks(session, null, lockChecks, sb.toString());
        }
    }

    private final static Logger log = LoggerFactory.getLogger(GroupChmodStrategy.class);

    private final BasicACLVoter voter;

    private final SessionFactory osf;

    private final SqlAction sql;

    private final ExtendedMetadata em;

    private/* final */OdeContext ctx;

    public GroupChmodStrategy(BasicACLVoter voter, SessionFactory osf,
            SqlAction sql, ExtendedMetadata em) {
        this.voter = voter;
        this.osf = osf;
        this.sql = sql;
        this.em = em;
    }

    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
        this.ctx = (OdeContext) ctx;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object[] getChecks(IObject obj, String permissions) {

        ExperimenterGroup trusted = load(obj);
        if (!voter.allowChmod(trusted)) {
            throw new SecurityViolation("chmod not permitted");
        }

        PermDrop drop = new PermDrop(trusted, permissions);
        if (!drop.found()) {
            return new Object[0]; // none needed.
        }

        List<Object> checks = new ArrayList<Object>();
        Collection<String> classeNames = em.getClasses();
        for (String className : classeNames) {
            Class k = em.getHibernateClass(className);
            if (voter.sysTypes.isSystemType(k)) {
                continue; // Skip experimenters, etc.
            }
            String[][] lockChecks = em.getLockChecks(k);
            checks.add(new Check(trusted.getId(), permissions, k, lockChecks,
                    drop));
        }

        return checks.toArray(new Object[checks.size()]);

    }

    public void chmod(IObject obj, String permissions) {
        handleGroupChange(obj, Permissions.parseString(permissions));
    }

    /**
     * Here we used the checks returned from {@link ExtendedMetadata} to iterate
     * through every non-system table and check that it has no FKs which point
     * to back to its rows and violate the read permissions which are being
     * reduced.
     */
    public void check(IObject obj, Object check) {
        if (!(check instanceof Check)) {
            throw new InternalException("Bad check:" + check);
        }
        Check c = ((Check) check);
        Map<String, Long> counts = performRun(c);

        long total = counts.get("*");
        if (total > 0) {
            throw new SecurityViolation(String.format(
                    "Cannot change permissions on %s to %s due to locks:\n%s",
                    obj, c.perms, counts));
        }
    }

    private Map<String, Long> performRun(Check c) {
        // Perform the operation across all groups.
        Map<String, Long> counts = null;
        Map<String, String> grpCtx = new HashMap<String, String>();
        grpCtx.put("ode.group", "-1");

        try {
            ctx.publishMessage(new ContextMessage.Push(this, grpCtx));
            try {
                counts = c.run(osf.getSession(), em);
            } finally {
                ctx.publishMessage(new ContextMessage.Pop(this, grpCtx));
            }
        } catch (Throwable t) {
            log.error("Could not perform check!", t);
            throw new InternalException("Could not perform check! See server logs");
        }

        return counts;
    }

    // Helpers
    // =========================================================================

    private ExperimenterGroup load(IObject obj) {

        if (!(obj instanceof ExperimenterGroup)) {
            throw new SecurityViolation("Only groups allowed");
        }

        if (obj.getId() == null) {
            throw new ApiUsageException("ID cannot be null");
        }

        final Session s = osf.getSession();
        return (ExperimenterGroup) s.get(ExperimenterGroup.class, obj.getId());
    }

    private void handleGroupChange(IObject obj, Permissions newPerms) {

        final ExperimenterGroup group = load(obj);
        if (newPerms == null) {
            throw new ApiUsageException("PERMS cannot be null");
        }

        final Permissions oldPerms = group.getDetails().getPermissions();

        if (oldPerms.sameRights(newPerms)) {
            log.debug(String.format("Ignoring unchanged permissions: %s",
                    newPerms));
            return;
        }

        final Long internal = (Long) Utils.internalForm(newPerms);

        sql.changeGroupPermissions(obj.getId(), internal);
        log.info(String.format("Changed permissions for %s to %s", obj.getId(),
                internal));
        eventlog(obj.getId(), newPerms.toString());

    }

    private void eventlog(long id, String perms) {

        EventLogMessage elm = new EventLogMessage(this, String.format(
                "CHMOD(%s)", perms), ExperimenterGroup.class,
                Collections.singletonList(id));

        try {
            ctx.publishMessage(elm);
        }
        catch (Throwable t) {
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            else {
                throw new RuntimeException(t);
            }
        }

    }

}