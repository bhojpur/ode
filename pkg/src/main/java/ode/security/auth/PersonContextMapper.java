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

import javax.naming.directory.SearchControls;

import ode.model.meta.Experimenter;

import org.springframework.ldap.core.DirContextAdapter;

/**
 * Specialized Bhojpur ODE Experimenter context mapper.
 */
public class PersonContextMapper extends OdeModelContextMapper {

    public PersonContextMapper(LdapConfig cfg, String base) {
        this(cfg, base, null);
    }

    public PersonContextMapper(LdapConfig cfg, String base, String attribute) {
        super(cfg, base, attribute);
    }

    public String get(String attribute, DirContextAdapter context) {
        String attributeName = cfg.getUserAttribute(attribute);
        if (attributeName != null) {
            return context.getStringAttribute(attributeName);
        }
        return null;
    }

    @Override
    public Object mapFromContext(Object obj) {
        DirContextAdapter ctx = (DirContextAdapter) obj;

        Experimenter person = new Experimenter();
        person.setOdeName(get("odeName", ctx));
        person.setFirstName(get("firstName", ctx));
        person.setMiddleName(get("middleName", ctx));
        person.setLastName(get("lastName", ctx));
        person.setInstitution(get("institution", ctx));
        person.setEmail(get("email", ctx));
        person.setLdap(true);

        person.putAt(LDAP_DN, ctx.getNameInNamespace());

        if (attribute != null) {
            person.putAt(LDAP_ATTR, ctx.getAttributeSortedStringSet(attribute));
        }

        person.putAt(LDAP_PROPS, new AttributeSet(ctx));
        return person;
    }

    public SearchControls getControls() {
        final SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningObjFlag(true);
        if (attribute == null) {
            return controls;
        }

        final String inst = cfg.getUserAttribute("institution");
        final String email = cfg.getUserAttribute("email");
        final String middleName = cfg.getUserAttribute("middleName");
        final String[] attrs = new String[]{
            "dn",
            attribute,
            cfg.getUserAttribute("odeName"),
            cfg.getUserAttribute("firstName"),
            cfg.getUserAttribute("lastName"),
            inst == null ? "dn" : inst,
            email == null ? "dn" : email,
            middleName == null ? "dn" : middleName,
        };
        controls.setReturningAttributes(attrs);
        return controls;
    }
}