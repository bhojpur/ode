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

import ode.api.IRenderingSettings;
import ode.services.server.util.ServerExecutor;
import ode.ServerError;
import ode.api.AMD_IRenderingSettings_applySettingsToDataset;
import ode.api.AMD_IRenderingSettings_applySettingsToImage;
import ode.api.AMD_IRenderingSettings_applySettingsToImages;
import ode.api.AMD_IRenderingSettings_applySettingsToPixels;
import ode.api.AMD_IRenderingSettings_applySettingsToProject;
import ode.api.AMD_IRenderingSettings_applySettingsToSet;
import ode.api.AMD_IRenderingSettings_createNewRenderingDef;
import ode.api.AMD_IRenderingSettings_getRenderingSettings;
import ode.api.AMD_IRenderingSettings_resetDefaults;
import ode.api.AMD_IRenderingSettings_resetDefaultsInDataset;
import ode.api.AMD_IRenderingSettings_resetDefaultsInImage;
import ode.api.AMD_IRenderingSettings_resetDefaultsForPixels;
import ode.api.AMD_IRenderingSettings_resetDefaultsInSet;
import ode.api.AMD_IRenderingSettings_resetDefaultsByOwnerInSet;
import ode.api.AMD_IRenderingSettings_resetDefaultsNoSave;
import ode.api.AMD_IRenderingSettings_resetMinMaxInSet;
import ode.api.AMD_IRenderingSettings_sanityCheckPixels;
import ode.api.AMD_IRenderingSettings_setOriginalSettingsInDataset;
import ode.api.AMD_IRenderingSettings_setOriginalSettingsInImage;
import ode.api.AMD_IRenderingSettings_setOriginalSettingsForPixels;
import ode.api.AMD_IRenderingSettings_setOriginalSettingsInSet;
import ode.api._IRenderingSettingsOperations;
import ode.model.Pixels;
import ode.model.RenderingDef;

import Ice.Current;

/**
 * Implementation of the IRenderingSettings service.
 * @see ode.api.IRenderingSettings
 */
public class RenderingSettingsI extends AbstractAmdServant implements
        _IRenderingSettingsOperations {

    public RenderingSettingsI(IRenderingSettings service, ServerExecutor be) {
        super(service, be);
    }

    // Interface methods
    // =========================================================================

    public void applySettingsToDataset_async(
            AMD_IRenderingSettings_applySettingsToDataset __cb, long from,
            long to, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, from, to);

    }

    public void applySettingsToImage_async(
            AMD_IRenderingSettings_applySettingsToImage __cb, long from,
            long to, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, from, to);

    }

    public void applySettingsToPixels_async(
            AMD_IRenderingSettings_applySettingsToPixels __cb, long from,
            long to, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, from, to);

    }

    public void applySettingsToProject_async(
            AMD_IRenderingSettings_applySettingsToProject __cb, long from,
            long to, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, from, to);

    }

    public void applySettingsToImages_async(
            AMD_IRenderingSettings_applySettingsToImages __cb, long from,
            List<Long> to, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, from, to);

    }
    
    public void applySettingsToSet_async(
            AMD_IRenderingSettings_applySettingsToSet __cb, long from,
            String toType, List<Long> to, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, from, toType, to);

    }

    public void createNewRenderingDef_async(
            AMD_IRenderingSettings_createNewRenderingDef __cb, Pixels pixels,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixels);

    }

    public void getRenderingSettings_async(
            AMD_IRenderingSettings_getRenderingSettings __cb, long pixelsId,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixelsId);

    }

    public void resetDefaultsInDataset_async(
            AMD_IRenderingSettings_resetDefaultsInDataset __cb, long dataSetId,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, dataSetId);

    }

    public void resetDefaultsInImage_async(
            AMD_IRenderingSettings_resetDefaultsInImage __cb, long imageId,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, imageId);

    }
    
    public void resetDefaultsForPixels_async(
            AMD_IRenderingSettings_resetDefaultsForPixels __cb, long pixelsId,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixelsId);

    }

    public void resetDefaultsInSet_async(
            AMD_IRenderingSettings_resetDefaultsInSet __cb, String type,
            List<Long> nodeIds, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, type, nodeIds);

    }

    public void resetDefaultsByOwnerInSet_async(
            AMD_IRenderingSettings_resetDefaultsByOwnerInSet __cb, String type,
            List<Long> nodeIds, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, type, nodeIds);

    }

    public void resetDefaultsNoSave_async(
            AMD_IRenderingSettings_resetDefaultsNoSave __cb, RenderingDef def,
            Pixels pixels, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, def, pixels);

    }

    public void resetDefaults_async(AMD_IRenderingSettings_resetDefaults __cb,
            RenderingDef def, Pixels pixels, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, def, pixels);

    }

    public void resetMinMaxInSet_async(
            AMD_IRenderingSettings_resetMinMaxInSet __cb, String type,
            List<Long> nodeIds, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, type, nodeIds);

    }

    public void sanityCheckPixels_async(
            AMD_IRenderingSettings_sanityCheckPixels __cb, Pixels from,
            Pixels to, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, from, to);

    }

    public void setOriginalSettingsInDataset_async(
            AMD_IRenderingSettings_setOriginalSettingsInDataset __cb,
            long dataSetId, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, dataSetId);

    }

    public void setOriginalSettingsInImage_async(
            AMD_IRenderingSettings_setOriginalSettingsInImage __cb,
            long imageId, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, imageId);

    }
    
    public void setOriginalSettingsForPixels_async(
            AMD_IRenderingSettings_setOriginalSettingsForPixels __cb,
            long pixelsId, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixelsId);

    }

    public void setOriginalSettingsInSet_async(
            AMD_IRenderingSettings_setOriginalSettingsInSet __cb, String type,
            List<Long> nodeIds, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, type, nodeIds);

    }

}