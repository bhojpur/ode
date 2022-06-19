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

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ode.cmd.ERR;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.OK;
import ode.cmd.Response;
import ode.cmd.Timing;
import ode.cmd.HandleI.Cancel;

/**
 * Diagnostic tool for testing call overhead.
 */
public class TimingI extends Timing implements IRequest {

    private static final long serialVersionUID = -1L;

    private final CountDownLatch latch = new CountDownLatch(1);

    private Helper helper;

    public Map<String, String> getCallContext() {
        return null;
    }

    public void init(Helper helper) {
        this.helper = helper;
        if (this.steps > 1000000) {
            helper.cancel(new ERR(), null, "too-many-steps",
            		"steps", ""+this.steps);
        } else if (this.millisPerStep > 5*60*1000) {
            helper.cancel(new ERR(), null, "too-long-steps",
            		"millisPerStep", ""+millisPerStep);
        } else if ((this.millisPerStep * this.steps) > 5*60*1000) {
            helper.cancel(new ERR(), null, "too-long",
            		"millisPerStep", ""+this.millisPerStep,
            		"steps", ""+this.steps);
        }
        this.helper.setSteps(this.steps);
    }

    public Object step(int step) {
        helper.assertStep(step);
        try {
			latch.await(millisPerStep, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			helper.debug("Interrupted");
		}
        return null;
    }

    @Override
    public void finish() throws Cancel {
        // no-op
    }

    public void buildResponse(int step, Object object) {
        helper.assertResponse(step);
        if (helper.isLast(step)) {
            helper.setResponseIfNull(new OK());
        }
    }

    public Response getResponse() {
        return helper.getResponse();
    }

}