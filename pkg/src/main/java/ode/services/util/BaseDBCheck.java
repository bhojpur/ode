package ode.services.util;

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

import ode.system.PreferenceContext;
import ode.util.SqlAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Utility methods for checking if startup-time database adjustments have yet been performed.
 */
abstract class BaseDBCheck {
    private static final Logger log = LoggerFactory.getLogger(BaseDBCheck.class);

    /** executor useful for performing database adjustments */
    protected final Executor executor;

    /* current database version */
    private final String version;
    private final int patch;

    private final String configKey, configValue, configKeyValue;

    private final boolean isReadOnlyDb;

    /**
     * @param executor executor to use for configuration map check
     * @param preferences the Bhojpur ODE configuration settings
     * @param readOnly the read-only status
     */
    protected BaseDBCheck(Executor executor, PreferenceContext preferences, ReadOnlyStatus readOnly) {
        this.executor = executor;
        this.version = preferences.getProperty("ode.db.version");
        this.patch = Integer.parseInt(preferences.getProperty("ode.db.patch"));

        /* these values may depend upon version and patch above */
        configKey = "DB check " + getClass().getSimpleName();
        configValue = getCheckDone();
        configKeyValue = configKey + ": " + configValue;

        isReadOnlyDb = readOnly.isReadOnlyDb();
    }

    /**
     * @return if the database adjustment is not yet performed
     */
    private boolean isCheckRequired() {
        return (Boolean) executor.executeSql(
                new Executor.SimpleSqlWork(this, "BaseDBCheck") {
                    @Transactional(readOnly = true)
                    public Boolean doWork(SqlAction sql) {
                        return !configValue.equals(sql.configValue(configKey));
                    }
                });
    }

    /**
     * The database adjustment is to be performed.
     */
    private void checkIsStarting() {
        executor.executeSql(
                new Executor.SimpleSqlWork(this, "BaseDBCheck") {
                    @Transactional(readOnly = false)
                    public Object doWork(SqlAction sql) {
                        sql.addMessageWithinDbPatchStart(version, patch, configKeyValue);
                        return null;
                    }
                });
    }

    /**
     * The database adjustment is now performed.
     * Hereafter {@link #isCheckRequired()} should return {@code false}.
     */
    private void checkIsDone() {
        executor.executeSql(
                new Executor.SimpleSqlWork(this, "BaseDBCheck") {
                    @Transactional(readOnly = false)
                    public Object doWork(SqlAction sql) {
                        sql.addMessageWithinDbPatchEnd(version, patch, configKeyValue);
                        sql.updateOrInsertConfigValue(configKey, configValue);
                        return null;
                    }
                });
    }

    /**
     * Do the database adjustment only if not already performed.
     */
    public void start() {
        if (isCheckRequired()) {
            if (isReadOnlyDb) {
                doCheck();
            } else {
                checkIsStarting();
                doCheck();
                checkIsDone();
            }
            log.info("performed " + configKeyValue);
        } else if (log.isDebugEnabled()) {
            log.debug("skipped " + configKey);
        }
    }

    /**
     * Do the database adjustment.
     */
    protected abstract void doCheck();

    /**
     * @return a string identifying that the check is done, never {@code null}
     */
    protected String getCheckDone() {
        return "done";
    }

    /**
     * @return a string representing the version and patch of this server
     */
    protected String getOdeVersion() {
        return version + "__" + patch;
    }
}
