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

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import ode.util.Utils;

import org.springframework.util.Assert;

/**
 * Example password provider which uses the given file as password lookup. All
 * entries in the file are of the form: username=password, where password is in
 * whatever encoding is configured for the {@link PasswordProvider provider}.
 * Changing passwords is not supported.
 */
public class FilePasswordProvider extends ConfigurablePasswordProvider {

    /**
     * Flat file read on each invocation with name, value pairs in Java
     * {@link java.util.Properties} notation.
     */
    final protected File file;

    public FilePasswordProvider(PasswordUtil util, File file) {
        super(util);
        this.file = file;
        Assert.notNull(file);
    }

    public FilePasswordProvider(PasswordUtil util, File file, boolean ignoreUnknown) {
        super(util, ignoreUnknown);
        this.file = file;
        Assert.notNull(file);
    }

    @Override
    public boolean hasPassword(String user) {
        Properties p = getProperties();
        return p.containsKey(user);
    }

    @Override
    public Boolean checkPassword(String user, String password, boolean readOnly) {
        Properties p = getProperties();
        return loginAttempt(user, doCheckPassword(user, password, p, readOnly));
    }

    protected Boolean doCheckPassword(String user, String password, Properties p, boolean readOnly) {
        if (!p.containsKey(user)) {
            // Do the default on unknown
            return super.checkPassword(user, password, readOnly);
        } else {
            String currentPassword = p.getProperty(user);
            return comparePasswords(currentPassword, password);
        }
    }

    protected Properties getProperties() {
        Properties p = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            p.load(fis);
            return p;
        } catch (Exception e) {
            throw new RuntimeException("Could not read file: " + file);
        } finally {
            Utils.closeQuietly(fis);
        }

    }

}