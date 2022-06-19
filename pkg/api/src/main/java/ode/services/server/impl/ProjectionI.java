package ode.services.server.impl;

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

import Ice.Current;
import ode.api.ServiceInterface;
import ode.services.server.util.ServerExecutor;
import ode.services.server.util.ServerOnly;
import ode.ServerError;
import ode.api.AMD_IProjection_projectPixels;
import ode.api.AMD_IProjection_projectStack;
import ode.api._IProjectionOperations;
import ode.constants.projection.ProjectionType;
import ode.model.PixelsType;

/**
 * Implementation of the IProjection service.
 * 
 * @see ode.api.IProjection
 */
public class ProjectionI
    extends AbstractAmdServant implements _IProjectionOperations, ServerOnly
{

    public ProjectionI(ServiceInterface service, ServerExecutor be)
    {
        super(service, be);
    }
    
    public void projectPixels_async(AMD_IProjection_projectPixels __cb,
            long pixelsId, PixelsType pixelsType, ProjectionType algorithm, 
            int tStart, int tEnd, List<Integer> channelList, int stepping, 
            int zStart, int zEnd, String name, Current __current)
        throws ServerError
    {
        callInvokerOnRawArgs(__cb, __current, pixelsId, pixelsType, 
                             algorithm.ordinal(), tStart, tEnd, channelList, 
                             stepping, zStart, zEnd, name);
    }

    public void projectStack_async(AMD_IProjection_projectStack __cb,
            long pixelsId, PixelsType pixelsType, ProjectionType algorithm, 
            int timepoint, int channelIndex, int stepping, int start, int end,
            Current __current) throws ServerError
    {
        callInvokerOnRawArgs(__cb, __current, pixelsId, pixelsType, 
                             algorithm.ordinal(), timepoint, channelIndex, 
                             stepping, start, end);
    }
}