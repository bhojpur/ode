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

import java.util.Collection;
import java.util.Set;

import ode.model.internal.Details;
import ode.services.graphs.GraphException;
import ode.services.graphs.GraphPolicy.Ability;
import ode.services.graphs.GraphPolicy;
import ode.services.graphs.GraphTraversal;

/**
 * A {@link ode.services.graphs.GraphTraversal.Processor} that does nothing whatsoever.
 */
public class NullGraphTraversalProcessor implements GraphTraversal.Processor {

    private final Set<GraphPolicy.Ability> requiredAbilities;

    /**
     * Construct a {@link ode.services.graphs.GraphTraversal.Processor} that does nothing whatsoever.
     * @param requiredAbilities the {@link Ability} set to be returned by {@link #getRequiredPermissions()}
     */
    public NullGraphTraversalProcessor(Set<Ability> requiredAbilities) {
        this.requiredAbilities = requiredAbilities;
    }

    @Override
    public void nullProperties(String className, String propertyName, Collection<Long> ids) { }

    @Override
    public void deleteInstances(String className, Collection<Long> ids) { }

    @Override
    public void processInstances(String className, Collection<Long> ids) { }

    @Override
    public Set<Ability> getRequiredPermissions() {
        return requiredAbilities;
    }

    @Override
    public void assertMayProcess(String className, long id, Details details) throws GraphException { }
}