package ode.services.search;

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

import java.util.List;

import ode.model.IObject;
import ode.system.ServiceFactory;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Intersection {@link SearchAction} which combines two other search actions
 * into one logical unit, e.g.
 * 
 * <pre>
 * A &amp;&amp; B
 * </pre>
 * @see ode.api.Search#and()
 */
public class Intersection extends SearchAction {

    private static final long serialVersionUID = 1L;

    private final SearchAction a;

    private final SearchAction b;

    public Intersection(SearchValues values, SearchAction a, SearchAction b) {
        super(values);
        Assert.notNull(a);
        Assert.notNull(b);
        this.a = a;
        this.b = b;
    }

    @Transactional(readOnly = true)
    public Object doWork(Session session, ServiceFactory sf) {

        List<IObject> rvA;
        List<IObject> rvB;

        rvA = (List<IObject>) a.doWork(session, sf);
        b.chainedSearch(rvA);
        rvB = (List<IObject>) b.doWork(session, sf);
        rvA.retainAll(rvB);

        return rvA;
    }
}
