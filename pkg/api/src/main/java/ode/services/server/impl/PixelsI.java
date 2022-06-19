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

import ode.api.IPixels;
import ode.services.server.util.ServerExecutor;
import ode.RInt;
import ode.ServerError;
import ode.api.AMD_IPixels_copyAndResizeImage;
import ode.api.AMD_IPixels_copyAndResizePixels;
import ode.api.AMD_IPixels_createImage;
import ode.api.AMD_IPixels_getAllEnumerations;
import ode.api.AMD_IPixels_getBitDepth;
import ode.api.AMD_IPixels_getEnumeration;
import ode.api.AMD_IPixels_loadRndSettings;
import ode.api.AMD_IPixels_retrievePixDescription;
import ode.api.AMD_IPixels_retrieveAllRndSettings;
import ode.api.AMD_IPixels_retrieveRndSettings;
import ode.api.AMD_IPixels_retrieveRndSettingsFor;
import ode.api.AMD_IPixels_saveRndSettings;
import ode.api.AMD_IPixels_setChannelGlobalMinMax;
import ode.api._IPixelsOperations;
import ode.model.PixelsType;
import ode.model.RenderingDef;

import Ice.Current;

/**
 * Implementation of the IPixels service.
 * 
 * @see ode.api.IPixels
 */
public class PixelsI extends AbstractAmdServant implements _IPixelsOperations {

    public PixelsI(IPixels service, ServerExecutor be) {
        super(service, be);
    }

    // Interface methods
    // =========================================================================

    public void copyAndResizeImage_async(AMD_IPixels_copyAndResizeImage __cb,
            long imageId, RInt sizeX, RInt sizeY, RInt sizeZ, RInt sizeT,
            List<Integer> channelList, String methodology, boolean copyStats,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, imageId, sizeX, sizeY, sizeZ,
                sizeT, channelList, methodology, copyStats);
    }

    public void copyAndResizePixels_async(AMD_IPixels_copyAndResizePixels __cb,
            long pixelsId, RInt sizeX, RInt sizeY, RInt sizeZ, RInt sizeT,
            List<Integer> channelList, String methodology, boolean copyStats,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixelsId, sizeX, sizeY, sizeZ,
                sizeT, channelList, methodology, copyStats);
    }

    public void createImage_async(AMD_IPixels_createImage __cb, int sizeX,
            int sizeY, int sizeZ, int sizeT, List<Integer> channelList,
            PixelsType pixelsType, String name, String description,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, sizeX, sizeY, sizeZ, sizeT,
                channelList, pixelsType, name, description);
    }

    public void getAllEnumerations_async(AMD_IPixels_getAllEnumerations __cb,
            String enumClass, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, enumClass);
    }

    public void getBitDepth_async(AMD_IPixels_getBitDepth __cb,
            PixelsType type, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, type);
    }

    public void getEnumeration_async(AMD_IPixels_getEnumeration __cb,
            String enumClass, String value, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, enumClass, value);
    }

    public void loadRndSettings_async(AMD_IPixels_loadRndSettings __cb,
            long renderingSettingsId, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, renderingSettingsId);
    }

    public void retrievePixDescription_async(
            AMD_IPixels_retrievePixDescription __cb, long pixId,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixId);
    }

    public void retrieveRndSettings_async(AMD_IPixels_retrieveRndSettings __cb,
            long pixId, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixId);
    }

    public void retrieveRndSettingsFor_async(AMD_IPixels_retrieveRndSettingsFor __cb,
            long pixId, long userId, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixId, userId);
    }
    
    public void retrieveAllRndSettings_async(AMD_IPixels_retrieveAllRndSettings __cb,
            long pixId, long userId, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixId, userId);
    }
    
    public void saveRndSettings_async(AMD_IPixels_saveRndSettings __cb,
            RenderingDef rndSettings, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, rndSettings);
    }

    public void setChannelGlobalMinMax_async(
            AMD_IPixels_setChannelGlobalMinMax __cb, long pixelsId,
            int channelIndex, double min, double max, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixelsId, channelIndex, min, max);
    }

}