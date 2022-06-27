package ode.conditions.acl;

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

import ode.model.internal.Permissions;

/**
 * User has attempted an action which is not permitted by the
 * {@link Permissions} of a given instance.
 */
public abstract class ACLViolation extends ode.conditions.SecurityViolation {

    private Class klass;

    private Long id;

    public ACLViolation(Class klass, Long id, String msg) {
        super(msg);
        this.klass = klass;
        this.id = id;
    }

    @Override
    public String getMessage() {

        String s = super.getMessage();
        if (s == null) {
            s = "";
        }

        String k = klass == null ? "No class" : klass.getName();

        String i = id == null ? "No id" : id.toString();

        int size = s.length() + k.length() + i.length();

        StringBuilder sb = new StringBuilder(size + 16);
        sb.append(k);
        sb.append(":");
        sb.append(i);
        sb.append(" -- ");
        sb.append(s);
        return sb.toString();
    }
}