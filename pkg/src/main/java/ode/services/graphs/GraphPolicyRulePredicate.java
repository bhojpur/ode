package ode.services.graphs;

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

import org.hibernate.Session;

import ode.model.IObject;

/**
 * A plug-in for graph policy rule matches whereby an object may be matched against named values.
 */
public interface GraphPolicyRulePredicate {
    /**
     * @return the name of this predicate
     */
    String getName();

    /**
     * Once this instance is submitted to {@link GraphPolicy#registerPredicate(GraphPolicyRulePredicate)} then this method is called by
     * {@link GraphPolicy#noteDetails(org.hibernate.Session, IObject, String, long)}.
     * @param session the Hibernate session, for obtaining more information about the object
     * @param object an unloaded model object that may satisfy this predicate
     * @param realClass the real class name of the object
     * @param id the ID of the object
     */
    void noteDetails(Session session, IObject object, String realClass, long id);

    /**
     * If this predicate is satisfied by the given object.
     * @param object a model object
     * @param parameter the parameter that the object must match
     * @return if the object satisfies this predicate
     * @throws GraphException if the predicate could not be tested
     */
    boolean isMatch(GraphPolicy.Details object, String parameter) throws GraphException;
}
