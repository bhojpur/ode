package ode.services.procs;

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

import ode.api.IQuery;
import ode.api.ITypes;
import ode.api.IUpdate;
import ode.model.jobs.Job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessorSkeleton implements Processor {

    private static Logger log = LoggerFactory.getLogger(ProcessorSkeleton.class);

    private IQuery query;
    private ITypes types;
    private IUpdate update;

    public void setQueryService(IQuery queryService) {
        this.query = queryService;
    }

    public void setTypesService(ITypes typesService) {
        this.types = typesService;
    }

    public void setUpdateService(IUpdate updateService) {
        this.update = updateService;
    }

    // Main methods ~
    // =========================================================================

    public Process process(long id) {
        Job job = lookup(id);
        if (accept(job)) {
            return process(job);
        } else {
            return null;
        }
    }

    public Job lookup(long id) {
        return query.find(Job.class, id);
    }

    public boolean accept(Job job) {
        return false;
    }

    public Process process(Job job) {
        return null;
    }

}
