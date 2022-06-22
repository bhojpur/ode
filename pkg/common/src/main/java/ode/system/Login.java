package ode.system;

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

import java.util.Properties;

import ode.conditions.ApiUsageException;

/**
 * Provides simplified handling of login properties when creating a
 * {@link ode.system.ServiceFactory}. For more complicated uses,
 * {@link java.util.Properties} can also be used. In which case, the constant
 * {@link java.lang.String strings} provided in this class can be used as the
 * keys to the {@link java.util.Properties properties instance} passed to
 * {@link ode.system.ServiceFactory#ServiceFactory(Properties)}.
 */
public class Login {

    /**
     * Java property name for use in configuration of client login.
     */
    public final static String ODE_USER = "ode.user";

    /**
     * Java property name for use in configuration of client login.
     */
    public final static String ODE_GROUP = "ode.group";

    /**
     * Java property name for use in configuration of client login.
     */
    public final static String ODE_PASS = "ode.pass";

    /**
     * Java property name for use in configuration of client login.
     */
    public final static String ODE_EVENT = "ode.event";

    /**
     * {@link Login} constant which has username and password values set
     * to null and other values set to their default. This will permit logging
     * in as an anonymous user.
     */
    public final static Login GUEST = new Login() {
        @Override
        public Properties asProperties() {
            Properties p = super.asProperties();
            p.setProperty(ODE_USER, null);
            p.setProperty(ODE_PASS, null);
            p.setProperty(ODE_GROUP, null);
            p.setProperty(ODE_EVENT, null);
            return p;
        }
    };

    private String _user, _group, _pass, _event;

    // Need at least user and password
    private Login() {
    }

    /**
     * standard constructor which leaves ODE_GROUP and ODE_EVENT null.
     * 
     * @param user
     *            {@link ode.model.meta.Experimenter#getOdeName()}. Not null.
     * @param password
     *            Cleartext password. Not null.
     */
    public Login(String user, String password) {
        if (user == null || password == null) {
            throw new ApiUsageException("User and password arguments "
                    + "to Login constructor cannot be null");
        }
        _user = user;
        _pass = password;
    }

    /**
     * extended constructor. As with {@link #Login(String, String)}, user and
     * password may not be null.
     * 
     * @param user
     *            {@link ode.model.meta.Experimenter#getOdeName()}. Not null.
     * @param password
     *            Cleartext password. Not null.
     * @param group
     *            Group name. May be null.
     * @param event
     *            Enumeration value of the EventType. May be null.
     */
    public Login(String user, String password, String group, String event) {
        this(user, password);
        _group = group;
        _event = event;
    }

    // ~ Views
    // =========================================================================

    /**
     * produces a copy of the internal fields as a {@link java.util.Properties}
     * instance. Only those keys are present for which a field is non-null.
     * 
     * @return Properties. Not null.
     */
    public Properties asProperties() {
        Properties p = new Properties();
        p.setProperty(ODE_USER, _user);
        p.setProperty(ODE_PASS, _pass);
        if (_group != null) {
            p.setProperty(ODE_GROUP, _group);
        }
        if (_event != null) {
            p.setProperty(ODE_EVENT, _event);
        }
        return p;
    }

    /**
     * simple getter for the user name passed into the constructor
     * 
     * @return {@link ode.model.meta.Experimenter#getOdeName() user name}. Not
     *         null unless Login == {@link Login#GUEST}.
     */
    public String getName() {
        return _user;
    }

    /**
     * simple getter for the password passed into the constructor
     * 
     * @return password. Not null unless Login == {@link Login#GUEST}
     */
    public String getPassword() {
        return _pass;
    }

    /**
     * simple getter for the group name passed into the constructor
     * 
     * @return {@link ode.model.meta.ExperimenterGroup#getName() group name}.
     *         May be null.
     */
    public String getGroup() {
        return _group;
    }

    /**
     * simple getter for the event type passed into the constructor
     * 
     * @return {@link ode.model.enums.EventType event type}. May be null.
     */
    public String getEvent() {
        return _event;
    }

}