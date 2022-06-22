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

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;

import ode.conditions.ApiUsageException;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.security.SecuritySystem;
import ode.security.auth.PasswordChangeException;
import ode.security.auth.PasswordProvider;
import ode.security.auth.PasswordUtil;
import ode.services.mail.MailUtil;
import ode.util.SqlAction;
import ode.cmd.HandleI.Cancel;
import ode.cmd.ERR;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.Response;
import ode.cmd.ResetPasswordRequest;
import ode.cmd.ResetPasswordResponse;

/**
 * Callback interface allowing to reset password for the given user.
 */

public class ResetPasswordRequestI extends ResetPasswordRequest implements
        IRequest {

    private final static Logger log = LoggerFactory
            .getLogger(ResetPasswordRequestI.class);

    private static final long serialVersionUID = -1L;

    private final ResetPasswordResponse rsp = new ResetPasswordResponse();

    private String sender = null;

    private final MailUtil mailUtil;

    private final PasswordUtil passwordUtil;

    private final SecuritySystem sec;

    private final PasswordProvider passwordProvider;

    private Helper helper;

    public ResetPasswordRequestI(MailUtil mailUtil, PasswordUtil passwordUtil,
            SecuritySystem sec, PasswordProvider passwordProvider) {
        this.mailUtil = mailUtil;
        this.passwordUtil = passwordUtil;
        this.sec = sec;
        this.passwordProvider = passwordProvider;
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
        this.sender = mailUtil.getSender();

        if (odename == null)
            throw helper.cancel(new ERR(), null, "no-odename");
        if (email == null)
            throw helper.cancel(new ERR(), null, "no-email");

        helper.allowGuests();
        this.helper.setSteps(1);
    }

    public Object step(int step) throws Cancel {
        helper.assertStep(step);
        return resetPassword();
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

    private boolean resetPassword() {

        Experimenter e = null;
        try {
            e = helper.getServiceFactory().getAdminService()
                    .lookupExperimenter(odename);
        } catch (ApiUsageException ex) {
            throw helper.cancel(new ERR(), null, "unknown-user",
                    "ApiUsageException", ex.getMessage());
        }
        final SqlAction sql = helper.getSql();
        final String emailFromDb = sql.getUserEmailByOdeName(odename);
        if (emailFromDb == null)
            throw helper.cancel(new ERR(), null, "unknown-email",
                    "ApiUsageException",
                    String.format("User has no email address."));
        else if (!emailFromDb.equals(email))
            throw helper.cancel(new ERR(), null, "not-match",
                    "ApiUsageException",
                    String.format("Email address does not match."));
        else if (passwordUtil.getDnById(e.getId()))
            throw helper.cancel(new ERR(), null, "ldap-user",
                    "ApiUsageException", String
                            .format("User is authenticated by LDAP server. "
                                    + "You cannot reset this password."));
        else {
            final long systemGroupId = sec.getSecurityRoles().getSystemGroupId();
            for (final ExperimenterGroup group : e.linkedExperimenterGroupList()) {
                if (group.getId() == systemGroupId) {
                    throw helper.cancel(new ERR(), null, "password-change-failed",
                            "PasswordChangeException",
                            "Cannot reset password of administrators. Have another administrator set the new password.");
                }
            }
            final String newPassword = passwordUtil.generateRandomPasswd();
            // FIXME
            // workaround as sec.runAsAdmin doesn't execute with the root
            // context
            // helper.getServiceFactory().getAdminService().changeUserPassword(odename,
            // newPassword);
            try {
                passwordProvider.changePassword(odename, newPassword);
                log.info("Changed password for user: " + odename);
            } catch (PasswordChangeException pce) {
                log.error(pce.getMessage());
                throw helper.cancel(new ERR(), null, "password-change-failed",
                        "PasswordChangeException",
                        String.format(pce.getMessage()));
            }

            final String prettyName = sql.getUserPrettyNameByOdeName(odename);
            String subject = "Bhojpur ODE - Reset password";
            String body = "Dear " + prettyName
                    + " (" + odename + ")" + " your new password is: "
                    + newPassword;

            try {
                mailUtil.sendEmail(sender, email, subject, body, false,
                        null, null);
                log.info("sent new password for {} to {}", odename, email);
            } catch (MailException me) {
                log.error(me.getMessage());
                throw helper.cancel(new ERR(), null, "mail-send-failed",
                        "MailException", String.format(me.getMessage()));
            }

        }

        return true;

    }

}