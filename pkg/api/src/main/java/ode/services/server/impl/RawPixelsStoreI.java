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

import ode.api.RawPixelsStore;
import ode.services.server.util.ServerExecutor;
import ode.ServerError;
import ode.api.AMD_RawPixelsStore_calculateMessageDigest;
import ode.api.AMD_RawPixelsStore_findMinMax;
import ode.api.AMD_RawPixelsStore_getByteWidth;
import ode.api.AMD_RawPixelsStore_getCol;
import ode.api.AMD_RawPixelsStore_getHistogram;
import ode.api.AMD_RawPixelsStore_getHypercube;
import ode.api.AMD_RawPixelsStore_getPixelsId;
import ode.api.AMD_RawPixelsStore_getPixelsPath;
import ode.api.AMD_RawPixelsStore_getPlane;
import ode.api.AMD_RawPixelsStore_getPlaneOffset;
import ode.api.AMD_RawPixelsStore_getPlaneRegion;
import ode.api.AMD_RawPixelsStore_getPlaneSize;
import ode.api.AMD_RawPixelsStore_getRegion;
import ode.api.AMD_RawPixelsStore_getRow;
import ode.api.AMD_RawPixelsStore_getRowOffset;
import ode.api.AMD_RawPixelsStore_getRowSize;
import ode.api.AMD_RawPixelsStore_getStack;
import ode.api.AMD_RawPixelsStore_getStackOffset;
import ode.api.AMD_RawPixelsStore_getStackSize;
import ode.api.AMD_RawPixelsStore_getTile;
import ode.api.AMD_RawPixelsStore_getTimepoint;
import ode.api.AMD_RawPixelsStore_getTimepointOffset;
import ode.api.AMD_RawPixelsStore_getTimepointSize;
import ode.api.AMD_RawPixelsStore_getTotalSize;
import ode.api.AMD_RawPixelsStore_isFloat;
import ode.api.AMD_RawPixelsStore_isSigned;
import ode.api.AMD_RawPixelsStore_prepare;
import ode.api.AMD_RawPixelsStore_save;
import ode.api.AMD_RawPixelsStore_setPixelsId;
import ode.api.AMD_RawPixelsStore_setPlane;
import ode.api.AMD_RawPixelsStore_setRegion;
import ode.api.AMD_RawPixelsStore_setRow;
import ode.api.AMD_RawPixelsStore_setStack;
import ode.api.AMD_RawPixelsStore_setTile;
import ode.api.AMD_RawPixelsStore_setTimepoint;
import ode.api._RawPixelsStoreOperations;
import ode.romio.PlaneDef;
import Ice.Current;

/**
 * Implementation of the RawPixelsStore service.
 * @see ode.api.RawPixelsStore
 */
public class RawPixelsStoreI extends AbstractPyramidServant implements
        _RawPixelsStoreOperations {

    public RawPixelsStoreI(RawPixelsStore service, ServerExecutor be) {
        super(service, be);
    }

    // Interface methods
    // =========================================================================

    public void calculateMessageDigest_async(
            AMD_RawPixelsStore_calculateMessageDigest __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void getByteWidth_async(AMD_RawPixelsStore_getByteWidth __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void getPlaneOffset_async(AMD_RawPixelsStore_getPlaneOffset __cb,
            int z, int c, int t, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, z, c, t);

    }

    public void getPlaneRegion_async(AMD_RawPixelsStore_getPlaneRegion __cb,
            int z, int c, int t, int size, int offset, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, z, c, t, size, offset);

    }

    public void getPlaneSize_async(AMD_RawPixelsStore_getPlaneSize __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void getPlane_async(AMD_RawPixelsStore_getPlane __cb, int z, int c,
            int t, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, z, c, t);

    }

    public void getHypercube_async(AMD_RawPixelsStore_getHypercube __cb,
            List<Integer> offset, List<Integer> size, List<Integer> step, 
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, offset, size, step);
    }
    
    public void getRegion_async(AMD_RawPixelsStore_getRegion __cb, int size,
            long offset, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, size, offset);

    }

    public void getRowOffset_async(AMD_RawPixelsStore_getRowOffset __cb, int y,
            int z, int c, int t, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, y, z, c, t);

    }

    public void getRowSize_async(AMD_RawPixelsStore_getRowSize __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void getRow_async(AMD_RawPixelsStore_getRow __cb, int y, int z,
            int c, int t, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, y, z, c, t);

    }

    public void getStackOffset_async(AMD_RawPixelsStore_getStackOffset __cb,
            int c, int t, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, c, t);

    }

    public void getStackSize_async(AMD_RawPixelsStore_getStackSize __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void getStack_async(AMD_RawPixelsStore_getStack __cb, int c, int t,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, c, t);

    }

    public void getTimepointOffset_async(
            AMD_RawPixelsStore_getTimepointOffset __cb, int t, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, t);

    }

    public void getTimepointSize_async(
            AMD_RawPixelsStore_getTimepointSize __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void getTimepoint_async(AMD_RawPixelsStore_getTimepoint __cb, int t,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, t);

    }

    public void getTotalSize_async(AMD_RawPixelsStore_getTotalSize __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void isFloat_async(AMD_RawPixelsStore_isFloat __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void isSigned_async(AMD_RawPixelsStore_isSigned __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void setPixelsId_async(AMD_RawPixelsStore_setPixelsId __cb,
            long pixelsId, boolean bypassOriginalFile, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixelsId, bypassOriginalFile);

    }

    public void getPixelsId_async(AMD_RawPixelsStore_getPixelsId __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void getPixelsPath_async(AMD_RawPixelsStore_getPixelsPath __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }
    
	public void prepare_async(AMD_RawPixelsStore_prepare __cb,
            List<Long> pixelsIds, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixelsIds);

    }

    public void setPlane_async(AMD_RawPixelsStore_setPlane __cb, byte[] buf,
            int z, int c, int t, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, buf, z, c, t);

    }

    public void setRegion_async(AMD_RawPixelsStore_setRegion __cb, int size,
            long offset, byte[] buffer, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, size, offset, buffer);

    }

    public void setRow_async(AMD_RawPixelsStore_setRow __cb, byte[] buf, int y,
            int z, int c, int t, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, buf, y, z, c, t);

    }

    public void setStack_async(AMD_RawPixelsStore_setStack __cb, byte[] buf,
            int z, int c, int t, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, buf, z, c, t);

    }

    public void setTimepoint_async(AMD_RawPixelsStore_setTimepoint __cb,
            byte[] buf, int t, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, buf, t);

    }
    
    public void getHistogram_async(AMD_RawPixelsStore_getHistogram __cb,
            int[] channels, int binCount, boolean globalRange, PlaneDef plane,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, channels, binCount, globalRange, plane);
    }
    
    public void findMinMax_async(AMD_RawPixelsStore_findMinMax __cb,
            int[] channels, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, channels);
    }

    public void getCol_async(AMD_RawPixelsStore_getCol __cb, int x, int z,
            int c, int t, Current __current) throws ServerError
    {
        callInvokerOnRawArgs(__cb, __current, x, z, c, t);

    }

    public void save_async(AMD_RawPixelsStore_save __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    /* (non-Javadoc)
     * @see ode.api._RawPixelsStoreOperations#getTile_async(ode.api.AMD_RawPixelsStore_getTile, int, int, int, int, int, int, int, Ice.Current)
     */
    public void getTile_async(AMD_RawPixelsStore_getTile __cb, int z, int c,
            int t, int x, int y, int w, int h, Current __current)
            throws ServerError
    {
        callInvokerOnRawArgs(__cb, __current, z, c, t, x, y, w, h);
    }

    /* (non-Javadoc)
     * @see ode.api._RawPixelsStoreOperations#setTile_async(ode.api.AMD_RawPixelsStore_setTile, byte[], int, int, int, int, int, int, int, Ice.Current)
     */
    public void setTile_async(AMD_RawPixelsStore_setTile __cb, byte[] buf, int z, int c,
            int t, int x, int y, int w, int h, Current __current)
            throws ServerError
    {
        callInvokerOnRawArgs(__cb, __current, buf, z, c, t, x, y, w, h);
    }
}