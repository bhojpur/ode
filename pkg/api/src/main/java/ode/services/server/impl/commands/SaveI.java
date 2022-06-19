package ode.services.server.impl.commands;

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

import java.util.Map;

import ode.api.IUpdate;
import ode.model.IObject;
import ode.api.Save;
import ode.api.SaveRsp;
import ode.cmd.ERR;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.Response;
import ode.cmd.HandleI.Cancel;
import ode.util.IceMapper;

/**
 * Permits saving a single IObject instance.
 */
public class SaveI extends Save implements IRequest {

    private static final long serialVersionUID = -3434345656L;

    private Helper helper;

    public Map<String, String> getCallContext() {
        return null;
    }

    public void init(Helper helper) {
        this.helper = helper;
        this.helper.setSteps(1);
    }

    public Object step(int step) {
        helper.assertStep(0, step);
        try {
            IceMapper mapper = new IceMapper();
            IObject iobj = (IObject) mapper.reverse(this.obj);
            IUpdate update = helper.getServiceFactory().getUpdateService();
            helper.info("saveAndReturnObject(%s)", iobj);
            return update.saveAndReturnObject(iobj);
        }
        catch (Throwable t) {
            // TODO: Should probably not catch throwable, but more specific
            // conditions.
            throw helper.cancel(new ERR(), t, "failed", "obj",
                    String.format("%s", this.obj));
        }
    }

    @Override
    public void finish() throws Cancel {
        // no-op
    }

    public void buildResponse(int step, Object object) {
        helper.assertStep(0, step);
        if (helper.isLast(step)) {
            IceMapper mapper = new IceMapper();
            SaveRsp rsp = new SaveRsp(
                    (ode.model.IObject) mapper.map((IObject) object));
            helper.setResponseIfNull(rsp);
        }
    }

    public Response getResponse() {
        return helper.getResponse();
    }
}