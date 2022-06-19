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

import java.util.HashMap;
import java.util.Map;

/**
 * A classifier that assigns an entity to the classification listed for the most specific group of which the entity is a member.
 * Consider entities themselves to be singleton groups.
 * Implemented more for obvious correctness than for performance on large data.
 * @param <G> the type of the groups of which entities may be members, and also of the entities themselves
 * @param <C> the classes to which entities may be assigned
 */
class SpecificityClassifier<G, C> {

    /**
     * Tests group membership. The membership relation must be irreflexive, asymmetric, and transitive.
     * @param <G> the type of the groups of which entities may be members, and also of the entities themselves
     */
    interface ContainmentTester<G> {

        /**
         * Report if one group is a proper superset of the other.
         * @param parent a group that may contain another
         * @param child the other group
         * @return if the first group contains the other without being the same as it
         */
        boolean isProperSupersetOf(G parent, G child);
    }

    private final ContainmentTester<G> tester;
    private final Map<G, C> classOfGroup = new HashMap<G, C>();

    /**
     * Create a classifier that uses the given definition of group membership.
     * @param tester the group membership tester
     */
    SpecificityClassifier(ContainmentTester<G> tester) {
        this.tester = tester;
    }

    /**
     * Assert that specific groups are of the given classification.
     * @param classification a classification
     * @param groupsInClass the groups that are of this classification
     * @throws IllegalArgumentException if any of the groups are already of a different classification
     */
    void addClass(C classification, Iterable<G> groupsInClass) throws IllegalArgumentException {
        for (final G group : groupsInClass) {
            final C previousClass = classOfGroup.put(group, classification);
            if (!(previousClass == null || previousClass.equals(classification))) {
                throw new IllegalArgumentException("cannot classify " + group + " as " + classification +
                        " because it is also " + previousClass);
            }
        }
    }

    /**
     * Classify the given group.
     * @param group a group
     * @return its classification, or {@code null} if it could not be classified
     */
    C getClass(G group) {
        final C directLookup = classOfGroup.get(group);
        if (directLookup != null) {
            return directLookup;
        }
        Map.Entry<G, C> bestGroupClassification = null;
        for (final Map.Entry<G, C> currentGroupClassification : classOfGroup.entrySet()) {
            if (tester.isProperSupersetOf(currentGroupClassification.getKey(), group) &&
                    (bestGroupClassification == null ||
                    tester.isProperSupersetOf(bestGroupClassification.getKey(), currentGroupClassification.getKey()))) {
                bestGroupClassification = currentGroupClassification;
            }
        }
        return bestGroupClassification == null ? null : bestGroupClassification.getValue();
    }
}