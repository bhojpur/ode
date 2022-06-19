package ode.security.auth;

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

import java.security.Permissions;

import ode.security.SecuritySystem;
import ode.services.messages.LoginAttemptMessage;
import ode.system.OdeContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Base class for most {@link PasswordProvider} implementations, providing
 * configuration for default behaviors. There is no need for a subclass to
 * use this implementation.
 */
public abstract class ConfigurablePasswordProvider implements PasswordProvider,
        PasswordUtility, ApplicationContextAware {

    final protected Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Hash implementation to use for encoding passwords to check and changed
     * passwords. Default value: MD5 (For the moment, the only supported value!)
     */
    protected final String hash;

    /**
     * Whether or not salting based on the user ID should be attempted.
     */
    protected final boolean salt;

    /**
     * If true, this implementation should return a null on
     * {@link #checkPassword(String, String, boolean)} if the user is unknown,
     * otherwise a {@link Boolean#FALSE}. Default value: false
     */
    protected final boolean ignoreUnknown;

    protected final PasswordUtil util;

    /**
     * Possibly null {@link PasswordUtil} instance which will be used as a
     * fallback for password checks if the {@link #util} instance fails.
     */
    protected /*final*/ PasswordUtil legacyUtil;

    protected OdeContext ctx;

    /**
     * Call {@link #ConfigurablePasswordProvider(PasswordUtil, boolean)}
     * with "ignoreUnknown" equal to false.
     * @param util an instance of the password utility class
     */
    public ConfigurablePasswordProvider(PasswordUtil util) {
        this(util, false);
    }

    /**
     * Call {@link #ConfigurablePasswordProvider(PasswordUtil, boolean, boolean)}
     * with "salt" equal to false.
     * @param util an instance of the password utility class
     * @param ignoreUnknown if {@link #checkPassword(String, String, boolean)} should
     * return {@code null} rather than {@link Boolean#FALSE} for unknown users
     */
    public ConfigurablePasswordProvider(PasswordUtil util, boolean ignoreUnknown) {
        this(util, ignoreUnknown, false);
    }

    public ConfigurablePasswordProvider(PasswordUtil util, boolean ignoreUnknown,
            boolean salt) {
        this.util = util;
        this.hash = "MD5";
        this.salt = salt;
        this.ignoreUnknown = ignoreUnknown;
    }

    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
        this.ctx = (OdeContext) ctx;
    }

    public void setLegacyUtil(PasswordUtil legacy) {
        this.legacyUtil = legacy;
    }

    protected Boolean loginAttempt(String user, Boolean success) {
        try {
            this.ctx.publishMessage(new LoginAttemptMessage(this, user, success));
        } catch (Throwable e) {
            log.error("LoginAttemptMessage error", e);
        }
        return success;
    }

    /**
     * Always returns false, override with specific logic.
     */
    public boolean hasPassword(String user) {
        return false;
    }

    /**
     * If this was constructed with the {@code ignoreUnknown} argument set to
     * {@code true}, returns {@code null}, since the base class knows no users.
     * Otherwise, returns {@link Boolean#FALSE} specifying that
     * authentication should fail.
     */
    public Boolean checkPassword(String user, String password, boolean readOnly) {
        if (ignoreUnknown) {
            return null;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Throws by default.
     */
    public void changePassword(String user, String password)
            throws PasswordChangeException {
        throw new PasswordChangeException(
                "Cannot change password with this implementation: "
                        + getClass().getName());
    }

    /**
     * Encodes the password as it would be encoded for a check by
     * {@link #comparePasswords(String, String)}
     */
    public String encodePassword(String newPassword) {
        return encodePassword(null, newPassword, false, util);
    }

    /**
     * Encodes the password as it would be encoded for a check by
     * {@link #comparePasswords(String, String)} salting the password
     * with the given userId if it's provided.
     * @param userId a user ID (may be {@code null})
     * @param newPassword a password
     * @return the encoded password
     */
    public String encodeSaltedPassword(Long userId, String newPassword) {
        return encodePassword(userId, newPassword, salt, util);
    }

    protected String encodePassword(Long userId, String newPassword,
            boolean salt, PasswordUtil util) {
        if (salt) {
            return util.prepareSaltedPassword(userId, newPassword);
        } else {
            return util.preparePassword(newPassword);
        }
    }

    /**
     * Compares the password provided by the user (unhashed) against the given
     * trusted password. A return value of null, however, is also possible
     * with the same semantics as a null from
     * {@link #checkPassword(String, String, boolean)}.
     *
     * For this implementation, if the trusted password is null, return
     * {@link Boolean#FALSE}. If the trusted password is empty (only
     * whitespace), return {@link Boolean#TRUE}. Otherwise return the result of
     * {@link String#equals(Object)}.
     */
    public Boolean comparePasswords(String trusted, String provided) {
        return comparePasswords(null, trusted, provided);
    }

    /**
     * Compares the password provided by the user (unhashed) against the given
     * trusted password. In general, if the trusted password is null, return
     * {@link Boolean#FALSE}. If the trusted password is empty (only
     * whitespace), return {@link Boolean#TRUE}. Otherwise return the results of
     * {@link String#equals(Object)}.
     *
     * If necessary, falls back to using a legacy password utility class if one was set by {@link #setLegacyUtil(PasswordUtil)}.

     * @param userId a user ID
     * @param trusted the user's trusted password
     * @param provided the provided password
     * @return if the provided password matches the trusted password (for which blank matches anything)
     */
    public Boolean comparePasswords(Long userId, String trusted, String provided) {
        if(comparePasswords(userId, trusted, provided, util)) {
            return true;
        } else if (legacyUtil != null) {
            if (comparePasswords(userId, trusted, provided, legacyUtil)) {
                log.error("Matched LEGACY password for Experimenter:{}!", userId);
                final String username = util.userName(userId);
                if (username != null) {
                    try {
                        changePassword(username, provided);
                        log.info("Upgraded password for Experimenter:{}", userId);
                    } catch (PasswordChangeException e) {
                        /* this password provider cannot change the password */
                    }
                }
                return true;
            }
        }
        return false;
    }

    protected boolean comparePasswords(Long userId, String trusted,
            String provided, PasswordUtil util) {
        if (trusted == null) {
            return false;
        } else if ("".equals(trusted.trim())) {
            return !util.isPasswordRequired(userId);
        } else {
            boolean salt = (userId != null && this.salt);
            String encoded = encodePassword(userId, provided, salt, util);
            if (trusted.equals(encoded)) {
                return true;
            }
            encoded = encodePassword(userId, provided, false, util);
            return trusted.equals(encoded); // ok unsalted.
        }
    }

}