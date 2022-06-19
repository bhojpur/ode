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

import ode.api.ThumbnailStore;
import ode.services.server.util.ServerExecutor;
import ode.RInt;
import ode.ServerError;
import ode.api.AMD_ThumbnailStore_createThumbnail;
import ode.api.AMD_ThumbnailStore_createThumbnails;
import ode.api.AMD_ThumbnailStore_createThumbnailsByLongestSideSet;
import ode.api.AMD_ThumbnailStore_getRenderingDefId;
import ode.api.AMD_ThumbnailStore_getThumbnail;
import ode.api.AMD_ThumbnailStore_getThumbnailWithoutDefault;
import ode.api.AMD_ThumbnailStore_getThumbnailByLongestSide;
import ode.api.AMD_ThumbnailStore_getThumbnailByLongestSideDirect;
import ode.api.AMD_ThumbnailStore_getThumbnailByLongestSideSet;
import ode.api.AMD_ThumbnailStore_getThumbnailDirect;
import ode.api.AMD_ThumbnailStore_getThumbnailForSectionByLongestSideDirect;
import ode.api.AMD_ThumbnailStore_getThumbnailForSectionDirect;
import ode.api.AMD_ThumbnailStore_getThumbnailSet;
import ode.api.AMD_ThumbnailStore_isInProgress;
import ode.api.AMD_ThumbnailStore_resetDefaults;
import ode.api.AMD_ThumbnailStore_setPixelsId;
import ode.api.AMD_ThumbnailStore_setRenderingDefId;
import ode.api.AMD_ThumbnailStore_thumbnailExists;
import ode.api._ThumbnailStoreOperations;

import Ice.Current;

/**
 * Implementation of the ThumbnailStore service.
 * @see ode.api.ThumbnailStore
 */
public class ThumbnailStoreI extends AbstractCloseableAmdServant implements
        _ThumbnailStoreOperations {

    public ThumbnailStoreI(ThumbnailStore service, ServerExecutor be) {
        super(service, be);
    }

    // Interface methods
    // =========================================================================

    public void createThumbnail_async(AMD_ThumbnailStore_createThumbnail __cb,
            RInt sizeX, RInt sizeY, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, sizeX, sizeY);

    }

    public void createThumbnails_async(
            AMD_ThumbnailStore_createThumbnails __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }
    
    public void createThumbnailsByLongestSideSet_async(
            AMD_ThumbnailStore_createThumbnailsByLongestSideSet __cb, RInt size,
            List<Long> pixelsIds, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, size, pixelsIds);

    }

    public void getThumbnailByLongestSideDirect_async(
            AMD_ThumbnailStore_getThumbnailByLongestSideDirect __cb, RInt size,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, size);

    }

    public void getThumbnailByLongestSideSet_async(
            AMD_ThumbnailStore_getThumbnailByLongestSideSet __cb, RInt size,
            List<Long> pixelsIds, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, size, pixelsIds);

    }

    public void getThumbnailByLongestSide_async(
            AMD_ThumbnailStore_getThumbnailByLongestSide __cb, RInt size,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, size);

    }

    public void getThumbnailDirect_async(
            AMD_ThumbnailStore_getThumbnailDirect __cb, RInt sizeX, RInt sizeY,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, sizeX, sizeY);

    }

    public void getThumbnailForSectionByLongestSideDirect_async(
            AMD_ThumbnailStore_getThumbnailForSectionByLongestSideDirect __cb,
            int theZ, int theT, RInt size, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, theZ, theT, size);

    }

    public void getThumbnailForSectionDirect_async(
            AMD_ThumbnailStore_getThumbnailForSectionDirect __cb, int theZ,
            int theT, RInt sizeX, RInt sizeY, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, theZ, theT, sizeX, sizeY);

    }

    public void getThumbnailSet_async(AMD_ThumbnailStore_getThumbnailSet __cb,
            RInt sizeX, RInt sizeY, List<Long> pixelsIds, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, sizeX, sizeY, pixelsIds);

    }

    public void getThumbnail_async(AMD_ThumbnailStore_getThumbnail __cb,
            RInt sizeX, RInt sizeY, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, sizeX, sizeY);

    }

    public void getThumbnailWithoutDefault_async(AMD_ThumbnailStore_getThumbnailWithoutDefault __cb,
            RInt sizeX, RInt sizeY, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, sizeX, sizeY);

    }

    public void resetDefaults_async(AMD_ThumbnailStore_resetDefaults __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void setPixelsId_async(AMD_ThumbnailStore_setPixelsId __cb,
            long pixelsId, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixelsId);

    }

    public void isInProgress_async(AMD_ThumbnailStore_isInProgress __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void setRenderingDefId_async(
            AMD_ThumbnailStore_setRenderingDefId __cb, long renderingDefId,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, renderingDefId);

    }

    public void thumbnailExists_async(AMD_ThumbnailStore_thumbnailExists __cb,
            RInt sizeX, RInt sizeY, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, sizeX, sizeY);

    }

    public void getRenderingDefId_async(AMD_ThumbnailStore_getRenderingDefId __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

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