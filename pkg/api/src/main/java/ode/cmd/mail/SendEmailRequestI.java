package ode.cmd.mail;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ode.system.EventContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.api.IQuery;
import ode.api.local.LocalAdmin;
import ode.parameters.Parameters;
import ode.services.mail.MailUtil;
import ode.services.util.ReadOnlyStatus;
import ode.cmd.HandleI.Cancel;
import ode.cmd.ERR;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.Response;
import ode.cmd.SendEmailRequest;
import ode.cmd.SendEmailResponse;
import ode.model.meta.Experimenter;
import org.springframework.mail.MailException;

/**
 * Callback interface allowing to send email using JavaMailSender, supporting
 * MIME messages through preparation callbacks.
 */

public class SendEmailRequestI extends SendEmailRequest implements IRequest, ReadOnlyStatus.IsAware {

    private final static Logger log = LoggerFactory
            .getLogger(SendEmailRequestI.class);

    private static final long serialVersionUID = -1L;

    private final SendEmailResponse rsp = new SendEmailResponse();

    private String sender = null;

    private List<String> recipients = new ArrayList<String>();

    private final MailUtil mailUtil;

    private Helper helper;

    public SendEmailRequestI(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }

    //
    // CMD API
    //

    public Map<String, String> getCallContext() {
        Map<String, String> all = new HashMap<String, String>();
        all.put("ode.group", "-1");
        return all;
    }

    public void init(Helper helper) {
        this.helper = helper;

        final EventContext ec = ((LocalAdmin) helper.getServiceFactory()
                .getAdminService()).getEventContextQuiet();
        if (!ec.isCurrentUserAdmin()) {
            throw helper.cancel(new ERR(), null, "no-permissions",
                    "ApiUsageException",
                    String.format("You have no permissions to send email."));
        }

        rsp.invalidusers = new ArrayList<Long>();
        rsp.invalidemails = new ArrayList<String>();

        if (!everyone && groupIds.isEmpty() && userIds.isEmpty())
            throw helper.cancel(new ERR(), null, "no-body",
                    "ApiUsageException",
                    String.format("No recipients specified."));
        this.sender = mailUtil.getSender();
        if (StringUtils.isBlank(this.sender))
            throw helper.cancel(new ERR(), null, "no-sender",
                    "ApiUsageException",
                    String.format("ode.mail.from cannot be empty."));
        if (StringUtils.isBlank(subject))
            throw helper.cancel(new ERR(), null, "no-subject",
                    "ApiUsageException",
                    String.format("Email must contain subject."));
        if (StringUtils.isBlank(body))
            throw helper.cancel(new ERR(), null, "no-body",
                    "ApiUsageException",
                    String.format("Email must contain body."));

        this.recipients = parseRecipients();
        this.recipients.addAll(parseExtraRecipients());

        rsp.total = this.recipients.size();
        rsp.success = 0;
        
        if (this.recipients.isEmpty())
            this.helper.setSteps(1);
        else
            this.helper.setSteps(this.recipients.size());
    }

    public Object step(int step) throws Cancel {
        helper.assertStep(step);

        // early exist
        try {
            this.recipients.get(step);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

        String email = this.recipients.get(step);

        try {
            mailUtil.sendEmail(this.sender, email, subject, body, html, null,
                    null);
        } catch (MailException me) {
            log.error(me.getMessage());
            rsp.invalidemails.add(email);
        }
        rsp.success+=1;
        return null;
    }

    @Override
    public void finish() throws Cancel {
        // no-op
    }

    public void buildResponse(int step, Object object) {
        helper.assertResponse(step);
        if (helper.isLast(step)) {
            helper.setResponseIfNull(rsp);
        }
    }

    public Response getResponse() {
        return helper.getResponse();
    }

    private List<String> parseRecipients() {

        /*
         * Depends on which parameters are set variants of the following query
         * should be executed:
         * 
         * select distinct e from Experimenter as e join fetch
         * e.groupExperimenterMap as map join fetch map.parent g where 1=1 //
         * hack to avoid plenty of if statement in conditions below and g.id =
         * :active // active users by default, all = false and e.id in //
         * groupIds (select m.child from GroupExperimenterMap m where
         * m.parent.id in (:gids) ) or e.id in (:eids) // userIds
         */

        Parameters p = new Parameters();

        StringBuffer sql = new StringBuffer();

        sql.append("select distinct e from Experimenter e "
                + "left outer join fetch e.groupExperimenterMap m "
                + "left outer join fetch m.parent g where 1=1 ");

        if (!inactive) {
            sql.append(" and g.id = :active ");
            p.addLong("active", helper.getServiceFactory().getAdminService()
                    .getSecurityRoles().getUserGroupId());
        }

        if (!everyone) {

            if (groupIds.size() > 0) {
                sql.append(" and e.id in ");
                sql.append(" (select m.child from GroupExperimenterMap m "
                        + " where m.parent.id in (:gids) )");
                p.addSet("gids", new HashSet<Long>(groupIds));
            }

            if (userIds.size() > 0) {
                if (groupIds.size() > 0) {
                    sql.append(" or ");
                } else {
                    sql.append(" and ");
                }

                sql.append(" e.id in (:eids)");
                p.addSet("eids", new HashSet<Long>(userIds));
            }

        }

        IQuery iquery = helper.getServiceFactory().getQueryService();

        List<Experimenter> exps = iquery.findAllByQuery(sql.toString(), p);

        Set<String> recipients = new HashSet<String>();
        for (final Experimenter e : exps) {
            if (e.getEmail() != null && mailUtil.validateEmail(e.getEmail())) {
                recipients.add(e.getEmail());
            } else {
                rsp.invalidusers.add(e.getId());
            }
        }
        return new ArrayList<String>(recipients);
    }

    private List<String> parseExtraRecipients() {
        Set<String> extraRecipients = new HashSet<String>();
        for (final String e : extra) {
            if (mailUtil.validateEmail(e)) {
                extraRecipients.add(e);
            } else {
                rsp.invalidemails.add(e);
            }
        }
        return new ArrayList<String>(extraRecipients);
    }

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        return true;
    }
}