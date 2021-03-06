package ode.services.server.repo;

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

import java.io.IOException;
import java.util.List;

import loci.formats.FormatException;
import loci.formats.ImageReader;
import ode.io.bioformats.BfPixelsWrapper;
import ode.io.nio.RomioPixelBuffer;
import ode.ServerError;
import ode.api.AMD_PyramidService_getResolutionDescriptions;
import ode.api.AMD_PyramidService_getResolutionLevel;
import ode.api.AMD_PyramidService_getResolutionLevels;
import ode.api.AMD_PyramidService_getTileSize;
import ode.api.AMD_PyramidService_requiresPixelsPyramid;
import ode.api.AMD_PyramidService_setResolutionLevel;
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
import ode.api.AMD_StatefulServiceInterface_activate;
import ode.api.AMD_StatefulServiceInterface_close;
import ode.api.AMD_StatefulServiceInterface_getCurrentEventContext;
import ode.api.AMD_StatefulServiceInterface_passivate;
import ode.api._RawPixelsStoreDisp;
import ode.romio.PlaneDef;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Current;

/**
 * @deprecated may be removed in next major release
 */
@Deprecated
public class BfPixelsStoreI extends _RawPixelsStoreDisp {

    private final static Logger log = LoggerFactory.getLogger(BfPixelsStoreI.class);

    private final BfPixelsWrapper reader;

    public BfPixelsStoreI(String path, ImageReader bfReader) throws IOException, FormatException {
        reader = new BfPixelsWrapper(path, bfReader);
    }

    public void calculateMessageDigest_async(
            AMD_RawPixelsStore_calculateMessageDigest __cb, Current __current)
            throws ServerError {
        try {
            __cb.ice_response(reader.getMessageDigest());
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getByteWidth_async(AMD_RawPixelsStore_getByteWidth __cb,
            Current __current) throws ServerError {
        try {
            __cb.ice_response(reader.getByteWidth());
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getHypercube_async(AMD_RawPixelsStore_getHypercube __cb,
            List<Integer> offset, List<Integer> size, List<Integer> step,
            Current __current) throws ServerError {

        try {
            int hypercubeSize = RomioPixelBuffer.safeLongToInteger(
                    reader.getHypercubeSize(offset,size,step));
            byte[] cube = new byte[hypercubeSize];
            reader.getHypercube(offset,size,step,cube);
            reader.swapIfRequired(cube);
            __cb.ice_response(cube);
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getCol_async(AMD_RawPixelsStore_getCol __cb, int x, int z,
            int c, int t, Current __current) throws ServerError {
        try {
            byte[] col = new byte[reader.getColSize()];
            reader.getCol(x,z,c,t,col);
            reader.swapIfRequired(col);
            __cb.ice_response(col);
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getPlaneOffset_async(AMD_RawPixelsStore_getPlaneOffset __cb,
            int z, int c, int t, Current __current) throws ServerError {
        try {
            __cb.ice_response(reader.getPlaneOffset(z,c,t));
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getPlaneRegion_async(AMD_RawPixelsStore_getPlaneRegion __cb,
            int z, int c, int t, int size, int offset, Current __current)
            throws ServerError {
        throw new UnsupportedOperationException("NYI");

    }

    public void getPlaneSize_async(AMD_RawPixelsStore_getPlaneSize __cb,
            Current __current) throws ServerError {
        try {
            __cb.ice_response(reader.getPlaneSize());
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getPlane_async(AMD_RawPixelsStore_getPlane __cb, int z, int c,
            int t, Current __current) throws ServerError {
        try {
            int size = RomioPixelBuffer.safeLongToInteger(
                    reader.getPlaneSize());
            byte[] plane = new byte[size];
            reader.getPlane(z,c,t,plane);
            reader.swapIfRequired(plane);
            __cb.ice_response(plane);
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getRegion_async(AMD_RawPixelsStore_getRegion __cb, int size,
            long offset, Current __current) throws ServerError {
        throw new UnsupportedOperationException(
                "Not yet supported, raise ticket to implement if required");
    }

    public void getRowOffset_async(AMD_RawPixelsStore_getRowOffset __cb, int y,
            int z, int c, int t, Current __current) throws ServerError {
        try {
            __cb.ice_response(reader.getRowOffset(y,z,c,t));
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getRowSize_async(AMD_RawPixelsStore_getRowSize __cb,
            Current __current) throws ServerError {
        try {
            __cb.ice_response(reader.getRowSize());
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getRow_async(AMD_RawPixelsStore_getRow __cb, int y, int z,
            int c, int t, Current __current) throws ServerError {
        try {
            byte[] row = new byte[reader.getRowSize()];
            reader.getRow(y,z,c,t,row);
            reader.swapIfRequired(row);
            __cb.ice_response(row);
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getStackOffset_async(AMD_RawPixelsStore_getStackOffset __cb,
            int c, int t, Current __current) throws ServerError {
        try {
            __cb.ice_response(reader.getStackOffset(c,t));
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getStackSize_async(AMD_RawPixelsStore_getStackSize __cb,
            Current __current) throws ServerError {
        try {
            __cb.ice_response(reader.getStackSize());
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getStack_async(AMD_RawPixelsStore_getStack __cb, int c, int t,
            Current __current) throws ServerError {
        try {
            int size = RomioPixelBuffer.safeLongToInteger(
                    reader.getStackSize());
            byte[] stack = new byte[size];
            reader.getStack(c,t,stack);
            reader.swapIfRequired(stack);
            __cb.ice_response(stack);
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getTimepointOffset_async(
            AMD_RawPixelsStore_getTimepointOffset __cb, int t, Current __current)
            throws ServerError {
        try {
            __cb.ice_response(reader.getTimepointOffset(t));
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getTimepointSize_async(
            AMD_RawPixelsStore_getTimepointSize __cb, Current __current)
            throws ServerError {
        try {
            __cb.ice_response(reader.getTimepointSize());
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getTimepoint_async(AMD_RawPixelsStore_getTimepoint __cb, int t,
            Current __current) throws ServerError {
        try {
            int size = RomioPixelBuffer.safeLongToInteger(reader.getTimepointSize());
            byte[] timepoint = new byte[size];
            reader.getTimepoint(t,timepoint);
            reader.swapIfRequired(timepoint);
            __cb.ice_response(timepoint);
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getTotalSize_async(AMD_RawPixelsStore_getTotalSize __cb,
            Current __current) throws ServerError {
        try {
            __cb.ice_response(reader.getTotalSize());
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void isFloat_async(AMD_RawPixelsStore_isFloat __cb, Current __current)
            throws ServerError {
        try {
            __cb.ice_response(reader.isFloat());
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void isSigned_async(AMD_RawPixelsStore_isSigned __cb,
            Current __current) throws ServerError {
        try {
            __cb.ice_response(reader.isSigned());
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void prepare_async(AMD_RawPixelsStore_prepare __cb,
            List<Long> pixelsIds, Current __current) throws ServerError {
        throw new UnsupportedOperationException("NYI");
    }

    public void save_async(AMD_RawPixelsStore_save __cb, Current __current)
            throws ServerError {
        throw new UnsupportedOperationException("NYI");
    }

    public void setPixelsId_async(AMD_RawPixelsStore_setPixelsId __cb,
            long pixelsId, boolean bypassOriginalFile, Current __current)
            throws ServerError {
        throw new UnsupportedOperationException("Cannot write to repository");
    }

    public void getPixelsId_async(AMD_RawPixelsStore_getPixelsId __cb, Current __current)
            throws ServerError {
        throw new UnsupportedOperationException("NYI");
    }

    public void getPixelsPath_async(AMD_RawPixelsStore_getPixelsPath __cb, Current __current)
            throws ServerError {
        throw new UnsupportedOperationException("NYI");
    }

    public void setPlane_async(AMD_RawPixelsStore_setPlane __cb, byte[] buf,
            int z, int c, int t, Current __current) throws ServerError {
        throw new UnsupportedOperationException("Cannot write to repository");
    }

    public void setRegion_async(AMD_RawPixelsStore_setRegion __cb, int size,
            long offset, byte[] buffer, Current __current) throws ServerError {
        throw new UnsupportedOperationException("Cannot write to repository");
    }

    public void setRow_async(AMD_RawPixelsStore_setRow __cb, byte[] buf, int y,
            int z, int c, int t, Current __current) throws ServerError {
        throw new UnsupportedOperationException("Cannot write to repository");
    }

    public void setStack_async(AMD_RawPixelsStore_setStack __cb, byte[] buf,
            int z, int c, int t, Current __current) throws ServerError {
        throw new UnsupportedOperationException("Cannot write to repository");
    }

    public void setTimepoint_async(AMD_RawPixelsStore_setTimepoint __cb,
            byte[] buf, int t, Current __current) throws ServerError {
        throw new UnsupportedOperationException("Cannot write to repository");
    }
    
    public void getHistogram_async(AMD_RawPixelsStore_getHistogram __cb,
            int[] channels, int binCount, boolean globalRange, PlaneDef plane, Current __current) throws ServerError {
        throw new UnsupportedOperationException("NYI");
    }

    public void findMinMax_async(AMD_RawPixelsStore_findMinMax __cb,
            int[] channels, Current __current) throws ServerError {
        throw new UnsupportedOperationException("NYI");
    }
    
    public void activate_async(AMD_StatefulServiceInterface_activate __cb,
            Current __current) throws ServerError {
        throw new UnsupportedOperationException("NYI");
    }

    public void close_async(AMD_StatefulServiceInterface_close __cb,
            Current __current) throws ServerError {
        try {
            reader.close();
        } catch (Exception e) {
            __cb.ice_exception(e);
        }
    }

    public void getCurrentEventContext_async(
            AMD_StatefulServiceInterface_getCurrentEventContext __cb,
            Current __current) throws ServerError {
        throw new UnsupportedOperationException("NYI");
    }

    public void passivate_async(AMD_StatefulServiceInterface_passivate __cb,
            Current __current) throws ServerError {
        throw new UnsupportedOperationException("NYI");
    }

    /* (non-Javadoc)
     * @see ode.api._PyramidServiceOperations#getResolutionLevels_async(ode.api.AMD_PyramidService_getResolutionLevels, Ice.Current)
     */
    public void getResolutionLevels_async(
            AMD_PyramidService_getResolutionLevels __cb, Current __current)
            throws ServerError
    {
        throw new UnsupportedOperationException("NYI");
    }

    public void getResolutionDescriptions_async(
            AMD_PyramidService_getResolutionDescriptions __cb, Current __current)
            throws ServerError
    {
        throw new UnsupportedOperationException("NYI");
    }

    /* (non-Javadoc)
     * @see ode.api._PyramidServiceOperations#getTileSize_async(ode.api.AMD_PyramidService_getTileSize, Ice.Current)
     */
    public void getTileSize_async(AMD_PyramidService_getTileSize __cb,
            Current __current) throws ServerError
    {
        throw new UnsupportedOperationException("NYI");
    }

    /* (non-Javadoc)
     * @see ode.api._PyramidServiceOperations#requiresPixelsPyramid_async(ode.api.AMD_PyramidService_requiresPixelsPyramid, Ice.Current)
     */
    public void requiresPixelsPyramid_async(AMD_PyramidService_requiresPixelsPyramid __cb,
            Current __current) throws ServerError
    {
        throw new UnsupportedOperationException("NYI");
    }

    /* (non-Javadoc)
     * @see ode.api._PyramidServiceOperations#setResolutionLevel_async(ode.api.AMD_PyramidService_setResolutionLevel, int, Ice.Current)
     */
    public void setResolutionLevel_async(
            AMD_PyramidService_setResolutionLevel __cb, int resolutionLevel,
            Current __current) throws ServerError
    {
        throw new UnsupportedOperationException("NYI");
    }

    /* (non-Javadoc)
     * @see ode.api._PyramidServiceOperations#getResolutionLevel_async(ode.api.AMD_PyramidService_getResolutionLevel, Ice.Current)
     */
    public void getResolutionLevel_async(
            AMD_PyramidService_getResolutionLevel __cb, Current __current)
            throws ServerError
    {
        throw new UnsupportedOperationException("NYI");
    }

    /* (non-Javadoc)
     * @see ode.api._RawPixelsStoreOperations#getTile_async(ode.api.AMD_RawPixelsStore_getTile, int, int, int, int, int, int, int, Ice.Current)
     */
    public void getTile_async(AMD_RawPixelsStore_getTile __cb, int z, int c,
            int t, int x, int y, int w, int h, Current __current)
            throws ServerError
    {
        throw new UnsupportedOperationException("NYI");
    }

    /* (non-Javadoc)
     * @see ode.api._RawPixelsStoreOperations#setTile_async(ode.api.AMD_RawPixelsStore_setTile, byte[], int, int, int, int, int, int, int, Ice.Current)
     */
    public void setTile_async(AMD_RawPixelsStore_setTile __cb, byte[] buf,
            int z, int c, int t, int x, int y, int w, int h, Current __current)
            throws ServerError
    {
        throw new UnsupportedOperationException("NYI");
    }
}