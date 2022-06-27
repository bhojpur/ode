package ode.util;

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

import java.util.Collection;
import java.util.Iterator;

/**
 * walks an object graph producing RDF-like output.
 */
public class RdfPrinter extends ContextFilter {

    StringBuilder sb = new StringBuilder();

    public String getRdf() {
        return sb.toString();
    }

    void space() {
        sb.append(" ");
    }

    void newline() {
        sb.append("\n");
    }

    protected void entry(String field, Object o) {
        if (!Collection.class.isAssignableFrom(currentContext().getClass())) {
            sb.append(currentContext());
            space();
            sb.append(field);
            space();
            sb.append(o);
            newline();
        }
    }

    @Override
    public Object filter(String fieldId, Object o) {
        entry(fieldId, o);
        return super.filter(fieldId, o);
    }

    @Override
    public Filterable filter(String fieldId, Filterable f) {
        entry(fieldId, f);
        return super.filter(fieldId, f);
    }

    @Override
    public Collection filter(String fieldId, Collection c) {
        sb.append(currentContext() == null ? "" : currentContext());
        space();
        sb.append(fieldId);
        space();
        sb.append(" [ ");
        Collection result;
        if (c != null && c.size() > 0) {
            for (Iterator it = c.iterator(); it.hasNext();) {
                sb.append(it.next());

            }
        } else {
        }
        sb.append(" ] \n");
        return super.filter(fieldId, c);
    }
}