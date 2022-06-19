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

import ode.api.ServiceInterface;
import ode.services.server.util.ServerExecutor;

import ode.api.AMD_PyramidService_getResolutionDescriptions;
import ode.api.AMD_PyramidService_getResolutionLevel;
import ode.api.AMD_PyramidService_getResolutionLevels;
import ode.api.AMD_PyramidService_getTileSize;
import ode.api.AMD_PyramidService_requiresPixelsPyramid;
import ode.api.AMD_PyramidService_setResolutionLevel;
import ode.api.PyramidService;
import ode.api.ResolutionDescription;
import ode.util.IceMapper;
import ode.util.IceMapper.ReturnMapping;

import Ice.Current;

/**
 * Specialization of {@link AbstractAmdServant} to be used by any services which
 * provide the {@link PyramidService} interface.
 */
public abstract class AbstractPyramidServant extends AbstractCloseableAmdServant {

    public AbstractPyramidServant(ServiceInterface service, ServerExecutor be) {
        super(service, be);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ode.api._PyramidServiceOperations#getResolutionLevels_async(ode.api
     * .AMD_PyramidService_getResolutionLevels, Ice.Current)
     */
    public void getResolutionLevels_async(
            AMD_PyramidService_getResolutionLevels __cb, Current __current) {
        callInvokerOnRawArgs(__cb, __current);
    }

    /*
     * (non-Javadoc)
     *
     * @see ode.api._PyramidServiceOperations#getTileSize_async(ode.api.
     * AMD_PyramidService_getTileSize, Ice.Current)
     */
    public void getTileSize_async(AMD_PyramidService_getTileSize __cb,
            Current __current) {
        callInvokerOnRawArgs(__cb, __current);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ode.api._PyramidServiceOperations#requiresPixelsPyramid_async(ode.api.
     * AMD_PyramidService_requiresPixelsPyramid, Ice.Current)
     */
    public void requiresPixelsPyramid_async(
            AMD_PyramidService_requiresPixelsPyramid __cb, Current __current) {
        callInvokerOnRawArgs(__cb, __current);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ode.api._PyramidServiceOperations#setResolutionLevel_async(ode.api
     * .AMD_PyramidService_setResolutionLevel, int, Ice.Current)
     */
    public void setResolutionLevel_async(
            AMD_PyramidService_setResolutionLevel __cb, int resolutionLevel,
            Current __current) {
        callInvokerOnRawArgs(__cb, __current, resolutionLevel);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ode.api._PyramidServiceOperations#getResolutionLevel_async(ode.api
     * .AMD_PyramidService_getResolutionLevel, Ice.Current)
     */
    public void getResolutionLevel_async(
            AMD_PyramidService_getResolutionLevel __cb, Current __current) {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getResolutionDescriptions_async(
            AMD_PyramidService_getResolutionDescriptions __cb, Current __current) {
        IceMapper mapper = new IceMapper(RESOLUTION_DESCRIPTIONS);
        callInvokerOnMappedArgs(mapper, __cb, __current);
    }

    /**
     * This is a fairly brittle mapping from the {@code List<List<Integer>>} created by
     * the PixelBuffers to the {@code List<ResolutionDescription>} which is remotely
     * provided by Bhojpur ODE server. The assumption is that much of these two levels will
     * be refactored together and therefore that shouldn't be a long-term
     * problem.
     */
    public final static ReturnMapping RESOLUTION_DESCRIPTIONS = new ReturnMapping() {
        public Object mapReturnValue(IceMapper mapper, Object value)
        throws Ice.UserException {

            if (value == null) {
                return null;
            }

            @SuppressWarnings("unchecked")
            List<List<Integer>> sizesArr = (List<List<Integer>>) value;
            ResolutionDescription[] rv = new ResolutionDescription[sizesArr.size()];
            for (int i = 0; i < rv.length; i++) {
                List<Integer> sizes = sizesArr.get(i);
                ResolutionDescription rd = new ResolutionDescription();
                rd.sizeX = sizes.get(0);
                rd.sizeY = sizes.get(1);
                rv[i] = rd;
            }

            return rv;
        }
    };

    //
    // Close logic
    //

    @Override
    protected void preClose(Current current) throws Throwable {
        // no-op
    }

    @Override
    protected void postClose(Current current) {
        // no-op
    }

}