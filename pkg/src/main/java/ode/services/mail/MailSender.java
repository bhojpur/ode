package ode.services.mail;

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

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ode.api.IQuery;
import ode.model.meta.Experimenter;
import ode.parameters.Parameters;
import ode.services.util.Executor;
import ode.system.Roles;
import ode.util.SqlAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic bean with setter injection that can be used as a base class for other
 * senders.
 */
public class MailSender {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private boolean enabled;

    private Executor executor;

    private MailUtil util;

    private IQuery query;

    private Roles roles;

    private String subjectPrefix = "[Bhojpur ODE] ";

    private String defaultBody = "Automated email sent by the Bhojpur ODE server.\n";

    //
    // GETTERS & SETTERS
    //

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public MailUtil getMailUtil() {
        return util;
    }

    public void setMailUtil(MailUtil util) {
        this.util = util;
    }

    public IQuery getQueryService() {
        return query;
    }

    public void setQueryService(IQuery query) {
        this.query = query;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public String getSubjectPrefix() {
        return subjectPrefix;
    }

    public void setSubjectPrefix(String subjectPrefix) {
        this.subjectPrefix = subjectPrefix;
    }

    public String getDefaultBody() {
        return defaultBody;
    }

    public void setDefaultBody(String defaultBody) {
        this.defaultBody = defaultBody;
    }

    //
    // Helpers
    //

    protected void sendBlind(Set<String> addresses, String subject) {
        sendBlind(addresses, subject, getDefaultBody());
    }

    protected void sendBlind(Set<String> addresses, String subject, String body) {

        if (!isEnabled()) {
            // Printing warning since the enabled mail check should happen
            // as early as possible to prevent wasting resources.
            log.warn("sendBlind called when mail is disabled.");
            return;
        }

        if (addresses == null || addresses.isEmpty()) {
            log.debug("No emails found.");
            return;
        }

        for (String address : addresses) {
            try {
                // TODO: send in background thread
                getMailUtil().sendEmail(address,
                    getSubjectPrefix() + subject, body,
                    false /* not html */, null, null);
            } catch (Exception e) {
                log.error("Failed to send email: {} ", address, e);
            }
        }
    }

    protected String getUserEmail(String user) {
        Experimenter e = getQueryService().findByString(Experimenter.class,
                "odeName", user);
        if (e == null) {
            return null;
        }
        return e.getEmail();
    }

    protected void addUser(Set<String> addresses, Experimenter exp) {
        String email = exp.getEmail();
        if (!isEmpty(email)) {
            addresses.add(email);
        }
    }

    protected Set<String> getAllSystemUsers(boolean newTx) {
        Set<String> addresses = new HashSet<String>();
        if (newTx) {
          loadFromAction(addresses);
        } else {
          loadFromQuery(addresses);
        }
        return addresses;
    }

    private void loadFromQuery(Set<String> addresses) {
        for (Object[] o : getQueryService().projection(
                "select e.email from Experimenter e "
                        + "join e.groupExperimenterMap m "
                        + "join m.parent g where g.id = :id",
                new Parameters().addId(getRoles().getSystemGroupId()))) {
            if (o != null && o.length >= 1 && o[0] != null) {
                String email = o[0].toString();
                if (!email.isEmpty()) {
                    addresses.add(email);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromAction(Set<String> addresses) {
        Collection<String> rv = (Collection<String>)
        executor.executeSql(new Executor.SimpleSqlWork(this, "loadAdminEmails") {
            @Override
            @Transactional(readOnly=true)
            public Collection<String> doWork(SqlAction sql) {
                return sql.getUserEmailsByGroup(roles.getSystemGroupId());
            }
        });
        for (String email : rv) {
            if (!email.isEmpty()) {
                addresses.add(email);
            }
        }
    }

}
