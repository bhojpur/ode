package ode.cmd.basic;

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
import java.util.List;
import java.util.Map;

import ode.system.OdeContext;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.ListRequests;
import ode.cmd.ListRequestsRsp;
import ode.cmd.Request;
import ode.cmd.Response;
import ode.cmd.HandleI.Cancel;
import ode.util.ObjectFactoryRegistry;
import ode.util.ObjectFactoryRegistry.ObjectFactory;

public class ListRequestsI extends ListRequests implements IRequest {

    private static final long serialVersionUID = -3653081139095111039L;

    private final OdeContext ctx;

    private Helper helper;

    public ListRequestsI(OdeContext ctx) {
        this.ctx = ctx;
    }

    public Map<String, String> getCallContext() {
        return null;
    }

    public void init(Helper helper) {
        helper.setSteps(1);
        this.helper = helper;
    }

    public Object step(int i) {
        helper.assertStep(0, i);

        final Ice.Communicator ic = ctx.getBean(Ice.Communicator.class);
        final List<Request> requestTypes = new ArrayList<Request>();
        final Map<String, ObjectFactoryRegistry> registries = ctx
                .getBeansOfType(ObjectFactoryRegistry.class);
        final ListRequestsRsp rsp = new ListRequestsRsp();
        for (ObjectFactoryRegistry registry : registries.values()) {
            Map<String, ObjectFactory> factories = registry.createFactories(ic);
            for (Map.Entry<String, ObjectFactory> entry : factories.entrySet()) {
                String key = entry.getKey();
                ObjectFactory factory = entry.getValue();
                Object obj = factory.create(key);
                if (obj instanceof Request) {
                    requestTypes.add((Request) obj);
                }
            }
        }

        rsp.list = requestTypes;
        return rsp;
    }

    @Override
    public void finish() throws Cancel {
        // no-op
    }

    public void buildResponse(int step, Object object) {
        helper.assertStep(0, step);
        helper.setResponseIfNull((Response) object);
    }

    public Response getResponse() {
        return helper.getResponse();
    }
}