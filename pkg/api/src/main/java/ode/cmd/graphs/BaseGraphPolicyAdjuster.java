package ode.cmd.graphs;

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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import ode.model.IObject;
import ode.services.graphs.GraphException;
import ode.services.graphs.GraphPolicy;
import ode.services.graphs.GraphPolicyRulePredicate;

/**
 * The base class assists adjustment of an existing graph traversal policy.
 */

public abstract class BaseGraphPolicyAdjuster extends GraphPolicy {

    private final GraphPolicy graphPolicy;

    /**
     * Construct a new graph policy adjuster.
     * @param graphPolicy the graph policy that is to be adjusted
     */
    public BaseGraphPolicyAdjuster(GraphPolicy graphPolicy) {
        this.graphPolicy = graphPolicy;
    }

    /**
     * An opportunity to adjust each model object before the graph policy reviews it.
     * @param object the model object before review
     * @return if this object's details were changed by this adjustment
     */
    protected boolean isAdjustedBeforeReview(Details object) {
        return false;
    }

    /**
     * An opportunity to adjust each model object after the graph policy reviews it.
     * @param object the model object after review
     * @return if this object's details were changed by this adjustment
     */
    protected boolean isAdjustedAfterReview(Details object) {
        return false;
    }

    @Override
    public void registerPredicate(GraphPolicyRulePredicate predicate) {
        graphPolicy.registerPredicate(predicate);
    }

    @Override
    public GraphPolicy getCleanInstance() {
        return graphPolicy.getCleanInstance();
    }

    @Override
    public void setCondition(String name) {
        graphPolicy.setCondition(name);
    }

    @Override
    public boolean isCondition(String name) {
        return graphPolicy.isCondition(name);
    }

    @Override
    public void noteDetails(Session session, IObject object, String realClass, long id) {
        graphPolicy.noteDetails(session, object, realClass, id);
    }

    @Override
    public final Set<Details> review(Map<String, Set<Details>> linkedFrom, Details rootObject, Map<String, Set<Details>> linkedTo,
            Set<String> notNullable, boolean isErrorRules) throws GraphException {
        /* note all the model objects that may be adjusted in review */
        final Set<Details> allTerms = GraphPolicy.allObjects(linkedFrom.values(), rootObject, linkedTo.values());
        /* allow isAdjustedBeforeReview to adjust objects before review */
        final Set<Details> changedTerms = new HashSet<Details>();
        for (final Details object : allTerms) {
            if (isAdjustedBeforeReview(object)) {
                changedTerms.add(object);
            }
        }
        /* do the review */
        changedTerms.addAll(graphPolicy.review(linkedFrom, rootObject, linkedTo, notNullable, isErrorRules));
        /* allow isAdjustedAfterReview to adjust objects after review */
        for (final Details object : allTerms) {
            if (isAdjustedAfterReview(object)) {
                changedTerms.add(object);
            }
        }
        return changedTerms;
    }
}