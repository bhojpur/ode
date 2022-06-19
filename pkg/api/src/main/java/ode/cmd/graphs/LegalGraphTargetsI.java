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
import java.util.Collection;
import java.util.Map;

import ode.model.IObject;
import ode.services.util.ReadOnlyStatus;
import ode.cmd.HandleI.Cancel;
import ode.cmd.ERR;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.LegalGraphTargets;
import ode.cmd.LegalGraphTargetsResponse;
import ode.cmd.Response;

/**
 * Query which model object classes are legal as targets for a request.
 */
public class LegalGraphTargetsI extends LegalGraphTargets implements IRequest, ReadOnlyStatus.IsAware {

    private final GraphRequestFactory graphRequestFactory;
    private Helper helper;

    /**
     * Construct a new legal graph targets query.
     * @param graphRequestFactory the graph request factory
     */
    public LegalGraphTargetsI(GraphRequestFactory graphRequestFactory) {
        this.graphRequestFactory = graphRequestFactory;
    }

    @Override
    public Map<String, String> getCallContext() {
        return null;
    }

    @Override
    public void init(Helper helper) {
        this.helper = helper;
        helper.setSteps(1);
    }

    @Override
    public Object step(int step) throws Cancel {
        if (step == 0) {
            try {
                return graphRequestFactory.getLegalTargets(request.getClass());
            } catch (Exception e) {
                throw helper.cancel(new ERR(), e, "graph-no-targets");
            }
        } else {
            final Exception e = new IllegalArgumentException("request has no step " + step);
            throw helper.cancel(new ERR(), e, "bad-step");
        }
    }

    @Override
    public void finish() throws Cancel {
    }

    @Override
    public void buildResponse(int step, Object object) {
        helper.assertResponse(step);
        if (step == 0) {
            final LegalGraphTargetsResponse response = new LegalGraphTargetsResponse();
            final Collection<Class<? extends IObject>> legalTargetClasses = (Collection<Class<? extends IObject>>) object;
            response.targets = new ArrayList<String>(legalTargetClasses.size());
            for (final Class<? extends IObject> legalTargetClass : legalTargetClasses) {
                response.targets.add(legalTargetClass.getName());
            }
            helper.setResponseIfNull(response);
        }
    }

    @Override
    public Response getResponse() {
        return helper.getResponse();
    }

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        return true;
    }
}