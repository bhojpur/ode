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

import java.util.Set;

import ode.model.IObject;

import org.springframework.ldap.core.ContextMapper;

/**
 * Parent class for any ODE model-to-LDAP mappers. Contains common logic.
 */
public abstract class OdeModelContextMapper implements ContextMapper {

    static final String LDAP_DN = "LDAP_DN";

    static final String LDAP_ATTR = "LDAP_ATTR";

    static final String LDAP_PROPS = "LDAP_PROPS";

    final LdapConfig cfg;

    final String base;

    final String attribute;

    public OdeModelContextMapper(LdapConfig cfg, String base, String attribute) {
        this.cfg = cfg;
        this.base = base;
        this.attribute = attribute;
    }

    @Override
    public abstract Object mapFromContext(Object obj);

    public <T extends IObject> String getDn(T modelObj) {
        return (String) modelObj.retrieve(LDAP_DN);
    }

    @SuppressWarnings("unchecked")
    public <T extends IObject> Set<String> getAttribute(T modelObj) {
        return (Set<String>) modelObj.retrieve(LDAP_ATTR);
    }

    public <T extends IObject> AttributeSet getAttributeSet(T modelObj) {
        return (AttributeSet) modelObj.retrieve(LDAP_PROPS);
    }
}