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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;

import ode.model.IObject;
import ode.services.graphs.GraphException;
import ode.services.graphs.GraphPathBean;
import ode.services.graphs.GraphPolicy;
import ode.services.util.ReadOnlyStatus;
import ode.cmd.HandleI.Cancel;
import ode.cmd.ERR;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.GraphModify2;
import ode.cmd.Response;
import ode.cmd.SkipHead;
import ode.cmd.State;
import ode.cmd.Status;

/**
 * The skip-head request performs the wrapped request twice: once in dry run mode to discover from which model objects to start,
 * and then actually starting from those objects.
 */
public class SkipHeadI extends SkipHead implements IRequest, ReadOnlyStatus.IsAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkipHeadI.class);

    private static final ImmutableSet<State> REQUEST_FAILURE_FLAGS = ImmutableSet.of(State.CANCELLED, State.FAILURE);

    private final GraphPathBean graphPathBean;
    private final GraphRequestFactory graphRequestFactory;
    private final Status graphRequestSkipStatus = new Status();
    private final Status graphRequestPerformStatus = new Status();
    private final List<Object> graphRequestSkipObjects = new ArrayList<Object>();
    private final List<Object> graphRequestPerformObjects = new ArrayList<Object>();

    private GraphModify2 graphRequestSkip;
    private GraphModify2 graphRequestPerform;

    private Helper helper;

    /**
     * Construct a new <q>skip-head</q> request; called from {@link GraphRequestFactory#getRequest(Class)}.
     * @param graphPathBean the graph path bean to use
     * @param graphRequestFactory a means of instantiating the sub-request
     */
    public SkipHeadI(GraphPathBean graphPathBean, GraphRequestFactory graphRequestFactory) {
        this.graphPathBean = graphPathBean;
        this.graphRequestFactory = graphRequestFactory;
    }

    @Override
    public Map<String, String> getCallContext() {
        return ((IRequest) request).getCallContext();
    }

    @Override
    public void init(Helper helper) {
        if (LOGGER.isDebugEnabled()) {
            final GraphUtil.ParameterReporter arguments = new GraphUtil.ParameterReporter();
            arguments.addParameter("startFrom", startFrom);
            if (request != null) {
                arguments.addParameter("request", request.getClass().getName());
            }
            arguments.addParameter("targetObjects", targetObjects);
            arguments.addParameter("childOptions", childOptions);
            arguments.addParameter("dryRun", dryRun);
            LOGGER.debug("request: " + arguments);
        }

        this.helper = helper;

        final GraphPolicy.Action startAction;
        final WrappableRequest<GraphModify2> wrappedRequest;

        if (request == null) {
            throw new RuntimeException(new GraphException("must pass a request argument"));
        } else if (!(request instanceof WrappableRequest)) {
            throw new RuntimeException(new GraphException(
                    "cannot use " + SkipHead.class.getSimpleName() + " on " + request.getClass().getSimpleName()));
        } else {
            /* create the two wrapped requests */
            final Class<? extends GraphModify2> requestClass = request.getClass();
            wrappedRequest = (WrappableRequest<GraphModify2>) request;
            startAction = wrappedRequest.getActionForStarting();
            graphRequestSkip = graphRequestFactory.getRequest(requestClass);
            graphRequestPerform = graphRequestFactory.getRequest(requestClass);
            wrappedRequest.copyFieldsTo(graphRequestPerform);
            wrappedRequest.copyFieldsTo(graphRequestSkip);
            /* the skip-head half takes on the top-level options and does not modify any model objects */
            GraphUtil.copyFields(this, graphRequestSkip);
            graphRequestSkip.dryRun = true;
            if (dryRun) {
                graphRequestPerform.dryRun = true;
            }
        }

        /* adjust the requests' graph traversal policies */
        final SetMultimap<String, Long> permissionsOverrides = HashMultimap.create();
        ((WrappableRequest<?>) graphRequestSkip).adjustGraphPolicy(new Function<GraphPolicy, GraphPolicy>() {
            @Override
            public GraphPolicy apply(GraphPolicy graphPolicy) {
                try {
                    /* adjust skip-head half to stop when it reaches the model objects from which to start */
                    return SkipHeadPolicy.getSkipHeadPolicySkip(graphPolicy, graphPathBean, startFrom, startAction,
                            permissionsOverrides);
                } catch (GraphException e) {
                    throw new RuntimeException("graph traversal policy adjustment failed: " + e, e);
                }
            }
        });
        ((WrappableRequest<?>) graphRequestPerform).adjustGraphPolicy(new Function<GraphPolicy, GraphPolicy>() {
            @Override
            public GraphPolicy apply(GraphPolicy graphPolicy) {
                /* adjust tail half to propagate permissions overrides from skip-head half */
                return SkipHeadPolicy.getSkipHeadPolicyPerform(graphPolicy, permissionsOverrides);
            }
        });

        try {
            /* initialize the two wrapped requests */
            ((IRequest) graphRequestSkip).init(helper.subhelper(graphRequestSkip, graphRequestSkipStatus));
            ((IRequest) graphRequestPerform).init(helper.subhelper(graphRequestPerform, graphRequestPerformStatus));
        } catch (Cancel c) {
            /* mark own status as canceled */
            Throwable t = c.getCause();
            if (t == null) {
                t = c;
            }
            helper.fail(new ERR(), t, "graph-fail");
            helper.getStatus().flags.add(State.CANCELLED);
            /* re-throw wrapped request Cancel */
            throw c;
        } catch (Throwable t) {
            /* cancel because of wrapped request exception */
            throw helper.cancel(new ERR(), t, "graph-fail");
        }

        /* set step count */
        graphRequestSkipStatus.steps = 1 + ((WrappableRequest<?>) graphRequestSkip).getStepProvidingCompleteResponse();
        helper.setSteps(graphRequestSkipStatus.steps + graphRequestPerformStatus.steps);
    }

    @Override
    public Object step(int step) throws Cancel {
        helper.assertStep(step);
        if (step < graphRequestSkipStatus.steps) {
            try {
                /* do a skip-head step */
                graphRequestSkipStatus.currentStep = step;
                graphRequestSkipObjects.add(((IRequest) graphRequestSkip).step(step));
            } catch (Cancel e) {
                /* the step failed, so propagate the error response to this request */
                helper.getStatus().flags.addAll(REQUEST_FAILURE_FLAGS);
                helper.setResponseIfNull(((IRequest) graphRequestSkip).getResponse());
                throw e;
            }
        } else {
            final int substep = step - graphRequestSkipStatus.steps;
            if (substep == 0) {
                /* if the skip-head half is now completed, construct its response to feed into the tail half */
                for (int i = 0; i < graphRequestSkipStatus.steps; i++) {
                    ((IRequest) graphRequestSkip).buildResponse(i, graphRequestSkipObjects.get(i));
                }
                final Response response = ((IRequest) graphRequestSkip).getResponse();
                final Map<String, List<Long>> allTargetedObjects = ((WrappableRequest<?>) graphRequestSkip).getStartFrom(response);
                graphRequestPerform.targetObjects = new HashMap<String, List<Long>>();
                /* pick out the model objects matching the startFrom classes */
                for (String startFromClassName : startFrom) {
                    final int lastDot = startFromClassName.lastIndexOf('.');
                    if (lastDot > 0) {
                        startFromClassName = startFromClassName.substring(lastDot + 1);
                    }
                    final Class<? extends IObject> startFromClass = graphPathBean.getClassForSimpleName(startFromClassName);
                    for (final Map.Entry<String, List<Long>> targetedObjectsByClass : allTargetedObjects.entrySet()) {
                        final String targetedClassName = targetedObjectsByClass.getKey();
                        final Class<? extends IObject> targetedClass;
                        try {
                            targetedClass = Class.forName(targetedClassName).asSubclass(IObject.class);
                        } catch (ClassNotFoundException cnfe) {
                            final Exception e = new IllegalStateException(
                                    "response from " + graphRequestSkip.getClass() + " refers to class " + targetedClassName);
                            throw helper.cancel(new ERR(), e, "bad-class");
                        }
                        if (startFromClass.isAssignableFrom(targetedClass)) {
                            final List<Long> ids = targetedObjectsByClass.getValue();
                            graphRequestPerform.targetObjects.put(targetedClassName, ids);
                        }
                    }
                }
            }
            if (substep < graphRequestPerformStatus.steps) {
                try {
                    /* do a tail step */
                    graphRequestPerformStatus.currentStep = substep;
                    graphRequestPerformObjects.add(((IRequest) graphRequestPerform).step(substep));
                } catch (Cancel e) {
                    /* the step failed, so propagate the error response to this request */
                    helper.getStatus().flags.addAll(REQUEST_FAILURE_FLAGS);
                    helper.setResponseIfNull(((IRequest) graphRequestPerform).getResponse());
                    throw e;
                }
            } else {
                final Exception e = new IllegalArgumentException("model object graph operation has no step " + step);
                throw helper.cancel(new ERR(), e, "bad-step");
            }
        }
        return null;
    }

    @Override
    public void finish() {
    }

    @Override
    public void buildResponse(int step, Object object) {
        helper.assertResponse(step);
        if (step == 0) {
            /* use the response of the tail half */
            final IRequest tailHalf = (IRequest) graphRequestPerform;
            for (int substep = 0; substep < graphRequestPerformStatus.steps; substep++) {
                tailHalf.buildResponse(substep, graphRequestPerformObjects.get(substep));
            }
            final Response response = tailHalf.getResponse();
            helper.setResponseIfNull(response);
        }
    }

    @Override
    public Response getResponse() {
        return helper.getResponse();
    }

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        if (request instanceof ReadOnlyStatus.IsAware) {
            return ((ReadOnlyStatus.IsAware) request).isReadOnly(readOnly);
        } else {
            return false;
        }
    }
}