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

import ode.model.IObject;
import ode.model.meta.EventLog;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.parameters.Parameters;
import ode.services.messages.EventLogMessage;
import ode.services.messages.EventLogsMessage;
import ode.system.Roles;

import org.springframework.context.ApplicationListener;

/**
 * When an {@link EventLogMessage} of the specified type and kind is received,
 * an email is sent to all users which are returned by a given query. A number
 * of parameters are made available to the query via a {@link Parameters}
 * instance.
 */
public class ObjectMailSender extends MailSender implements
        ApplicationListener<EventLogsMessage> {

    private Class<IObject> klass;

    private String action;

    private String queryString;

    //
    // GETTERS & SETTERS
    //

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Class<IObject> getObjectClass() {
        return klass;
    }

    public void setObjectClass(Class<IObject> klass) {
        this.klass = klass;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    //
    // Main method
    //

    @Override
    public void onApplicationEvent(EventLogsMessage elm) {

        if (!isEnabled()) {
            return;
        }

        if (isEmpty(this.queryString)) {
            return;
        }

        Collection<EventLog> matches = elm.matches(klass.getName(), action);
        if (matches.isEmpty()) {
            return;
        }

        sendEmail(matches);
    }

    //
    // Main method
    //

    protected void sendEmail(Collection<EventLog> matches) {
        Parameters p = new Parameters();
        Roles roles = getRoles();
        p.addString("systemGroup", roles.getSystemGroupName());
        p.addLong("systemGroupId", roles.getSystemGroupId());
        p.addString("userGroup", roles.getUserGroupName());
        p.addLong("userGroupId", roles.getUserGroupId());
        p.addString("rootUser", roles.getRootName());
        p.addLong("rootUserId", roles.getRootId());

        StringBuilder sb = new StringBuilder();
        sb.append("Modified objects:\n");
        for (EventLog el : matches) {

            Set<String> addresses = new HashSet<String>();
            p.addId(el.getEntityId());
            sb.append(klass);
            sb.append(":");
            sb.append(el.getEntityId());
            sb.append(" - ");
            sb.append(el.getAction());
            sb.append("\n");

            for (IObject obj : getQueryService().findAllByQuery(queryString, p)) {
                if (obj instanceof Experimenter) {
                    addUser(addresses, (Experimenter) obj);
                } else if (obj instanceof ExperimenterGroup) {
                    for (Experimenter exp : ((ExperimenterGroup) obj)
                            .linkedExperimenterList()) {
                        addUser(addresses, exp);
                    }
                }
            }
            if (!addresses.isEmpty()) {
                sendBlind(addresses, String.format("%s %s notification",
                    action, klass.getSimpleName()), sb.toString());
            }
        }
    }

    protected void addUser(Set<String> addresses, Experimenter exp) {
        String email = exp.getEmail();
        if (!isEmpty(email)) {
            addresses.add(email);
        }
    }

}