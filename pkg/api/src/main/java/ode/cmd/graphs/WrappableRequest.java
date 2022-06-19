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

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ode.services.graphs.GraphPolicy;
import ode.cmd.IRequest;
import ode.cmd.Response;

/**
 * Requests that can be wrapped by a {@link SkipHeadI} request.
 */
public interface WrappableRequest<X> extends IRequest {
    /**
     * Copy the fields of this request to that of the given request.
     * @param request the target of the field copy
     */
    void copyFieldsTo(X request);

    /**
     * Transform the currently applicable graph policy for this request by the given function.
     * Must be called before {@link #init(ode.cmd.Helper)}.
     * @param adjuster a transformation function for graph policies
     */
    void adjustGraphPolicy(Function<GraphPolicy, GraphPolicy> adjuster);

    /**
     * Get the step of this request that suffices for assembling the request's response.
     * It is presumed that checking the permissibility of the planned operation occurs afterward.
     * @return a step number
     */
    int getStepProvidingCompleteResponse();

    /**
     * @return the action associated with nodes qualifying as start objects
     */
    GraphPolicy.Action getActionForStarting();

    /**
     * From the response of the head-skipping request, determine which model objects are the targets of the operation.
     * @param response the head-skipping request's response
     * @return the model objects to target
     */
    Map<String, List<Long>> getStartFrom(Response response);
}