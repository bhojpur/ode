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

/**
 * Central {@link PasswordProvider} which uses the "password" table in the
 * central Bhojpur ODE database. When setting a password, stores password as-is
 * into database, so it must be properly hashed already.
 */

public class JdbcHashedPasswordProvider extends JdbcPasswordProvider {

    public JdbcHashedPasswordProvider(PasswordUtil util) {
        super(util);
    }

    public JdbcHashedPasswordProvider(PasswordUtil util, boolean ignoreUnknown) {
        super(util);
    }

    @Override
    public void changePassword(String user, String md5password)
            throws PasswordChangeException {
        changePassword(user, md5password, PasswordUtil.METHOD.CLEAR);
    }
}