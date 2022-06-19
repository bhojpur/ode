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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import ode.services.server.util.ServerExecutor;
import ode.services.server.util.ServiceFactoryAware;
import odeis.providers.re.RenderingEngine;
import ode.InternalException;
import ode.RLong;
import ode.ServerError;
import ode.api.AMD_RenderingEngine_addCodomainMap;
import ode.api.AMD_RenderingEngine_addCodomainMapToChannel;
import ode.api.AMD_RenderingEngine_getAvailableFamilies;
import ode.api.AMD_RenderingEngine_getAvailableModels;
import ode.api.AMD_RenderingEngine_getChannelCurveCoefficient;
import ode.api.AMD_RenderingEngine_getChannelFamily;
import ode.api.AMD_RenderingEngine_getChannelLookupTable;
import ode.api.AMD_RenderingEngine_getChannelNoiseReduction;
import ode.api.AMD_RenderingEngine_getChannelStats;
import ode.api.AMD_RenderingEngine_getChannelWindowEnd;
import ode.api.AMD_RenderingEngine_getChannelWindowStart;
import ode.api.AMD_RenderingEngine_getCodomainMapContext;
import ode.api.AMD_RenderingEngine_getCompressionLevel;
import ode.api.AMD_RenderingEngine_getDefaultT;
import ode.api.AMD_RenderingEngine_getDefaultZ;
import ode.api.AMD_RenderingEngine_getModel;
import ode.api.AMD_RenderingEngine_getPixels;
import ode.api.AMD_RenderingEngine_getPixelsTypeLowerBound;
import ode.api.AMD_RenderingEngine_getPixelsTypeUpperBound;
import ode.api.AMD_RenderingEngine_getQuantumDef;
import ode.api.AMD_RenderingEngine_getRGBA;
import ode.api.AMD_RenderingEngine_getRenderingDefId;
import ode.api.AMD_RenderingEngine_isActive;
import ode.api.AMD_RenderingEngine_isPixelsTypeSigned;
import ode.api.AMD_RenderingEngine_load;
import ode.api.AMD_RenderingEngine_loadRenderingDef;
import ode.api.AMD_RenderingEngine_lookupPixels;
import ode.api.AMD_RenderingEngine_lookupRenderingDef;
import ode.api.AMD_RenderingEngine_removeCodomainMap;
import ode.api.AMD_RenderingEngine_removeCodomainMapFromChannel;
import ode.api.AMD_RenderingEngine_render;
import ode.api.AMD_RenderingEngine_renderAsPackedInt;
import ode.api.AMD_RenderingEngine_renderCompressed;
import ode.api.AMD_RenderingEngine_renderProjectedAsPackedInt;
import ode.api.AMD_RenderingEngine_renderProjectedCompressed;
import ode.api.AMD_RenderingEngine_resetDefaultSettings;
import ode.api.AMD_RenderingEngine_saveAsNewSettings;
import ode.api.AMD_RenderingEngine_saveCurrentSettings;
import ode.api.AMD_RenderingEngine_setActive;
import ode.api.AMD_RenderingEngine_setChannelLookupTable;
import ode.api.AMD_RenderingEngine_setChannelWindow;
import ode.api.AMD_RenderingEngine_setCodomainInterval;
import ode.api.AMD_RenderingEngine_setCompressionLevel;
import ode.api.AMD_RenderingEngine_setDefaultT;
import ode.api.AMD_RenderingEngine_setDefaultZ;
import ode.api.AMD_RenderingEngine_setModel;
import ode.api.AMD_RenderingEngine_setOverlays;
import ode.api.AMD_RenderingEngine_setQuantizationMap;
import ode.api.AMD_RenderingEngine_setQuantumStrategy;
import ode.api.AMD_RenderingEngine_setRGBA;
import ode.api.AMD_RenderingEngine_updateCodomainMap;
import ode.api.AMD_RenderingEngine_updateSettings;
import ode.api.IRoiPrx;
import ode.api._RenderingEngineOperations;
import ode.constants.projection.ProjectionType;
import ode.grid.Column;
import ode.grid.Data;
import ode.grid.MaskColumn;
import ode.grid.TablePrx;
import ode.model.Family;
import ode.model.RenderingDef;
import ode.model.RenderingModel;
import ode.romio.CodomainMapContext;
import ode.romio.PlaneDef;
import ode.util.IceMapper;

import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;

import Ice.Current;

/**
 * Implementation of the RenderingEngine service.
 * @see odeis.providers.re.RenderingEngine
 */
public class RenderingEngineI extends AbstractPyramidServant implements
        _RenderingEngineOperations, ServiceFactoryAware {
	
	private ServiceFactoryI sf;
	
	private IRoiPrx roiService;
	
    public RenderingEngineI(RenderingEngine service, ServerExecutor be) {
        super(service, be);
    }
    
	public void setServiceFactory(ServiceFactoryI sf) throws ServerError {
		this.sf = sf;
		this.roiService = sf.getRoiService(null);
	}

    // Interface methods
    // =========================================================================

    public void addCodomainMap_async(AMD_RenderingEngine_addCodomainMap __cb,
            CodomainMapContext mapCtx, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, mapCtx);

    }

    public void addCodomainMapToChannel_async(AMD_RenderingEngine_addCodomainMapToChannel __cb,
            CodomainMapContext mapCtx, int w, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, mapCtx, w);

    }

    public void getAvailableFamilies_async(
            AMD_RenderingEngine_getAvailableFamilies __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getAvailableModels_async(
            AMD_RenderingEngine_getAvailableModels __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getChannelCurveCoefficient_async(
            AMD_RenderingEngine_getChannelCurveCoefficient __cb, int w,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w);
    }

    public void getChannelFamily_async(
            AMD_RenderingEngine_getChannelFamily __cb, int w, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w);
    }

    public void getChannelNoiseReduction_async(
            AMD_RenderingEngine_getChannelNoiseReduction __cb, int w,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w);
    }

    public void getChannelStats_async(AMD_RenderingEngine_getChannelStats __cb,
            int w, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w);
    }

    public void getChannelWindowEnd_async(
            AMD_RenderingEngine_getChannelWindowEnd __cb, int w,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w);
    }

    public void getChannelWindowStart_async(
            AMD_RenderingEngine_getChannelWindowStart __cb, int w,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w);
    }

    public void getCompressionLevel_async(
            AMD_RenderingEngine_getCompressionLevel __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getDefaultT_async(AMD_RenderingEngine_getDefaultT __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getDefaultZ_async(AMD_RenderingEngine_getDefaultZ __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getModel_async(AMD_RenderingEngine_getModel __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getPixelsTypeLowerBound_async(
            AMD_RenderingEngine_getPixelsTypeLowerBound __cb, int w,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w);

    }

    public void getPixelsTypeUpperBound_async(
            AMD_RenderingEngine_getPixelsTypeUpperBound __cb, int w,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w);
    }

    public void getPixels_async(AMD_RenderingEngine_getPixels __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getQuantumDef_async(AMD_RenderingEngine_getQuantumDef __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getRGBA_async(AMD_RenderingEngine_getRGBA __cb, int w,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w);
    }

    public void isActive_async(AMD_RenderingEngine_isActive __cb, int w,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w);
    }

    public void isPixelsTypeSigned_async(
            AMD_RenderingEngine_isPixelsTypeSigned __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void loadRenderingDef_async(
            AMD_RenderingEngine_loadRenderingDef __cb, long renderingDefId,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, renderingDefId);
    }
    
    public void setOverlays_async(AMD_RenderingEngine_setOverlays __cb,
    		RLong tableId, RLong imageId, Map<Long, Integer> rowColorMap,
    		Current __current) throws ServerError {
    	try
    	{
    		final IceMapper mapper = new IceMapper(IceMapper.VOID);
    		// Sanity check so that we do not attempt to slice the entire table
    		if (rowColorMap.size() == 0)
    		{
    			callInvokerOnMappedArgs(mapper, __cb, __current, 
    					new Object[] { null});
    			return;
    		}
    		// Translate our set of rows to an array for table slicing
    		Set<Long> rowsAsSet = rowColorMap.keySet();
    		long[] rows = new long[rowsAsSet.size()];
    		int rowIndex = 0;
    		for (Long row : rowsAsSet)
    		{
    			rows[rowIndex] = row.longValue();
    			rowIndex++;
    		}

    		// Load the table and find the index of the mask column, throwing an
    		// exception if the mask column does not exist.
    		StopWatch s1 = new Slf4JStopWatch("ode.getTable");
    		TablePrx table = roiService.getTable(tableId.getValue());
    		s1.stop();
    		s1 = new Slf4JStopWatch("ode.getHeaders");
    		Column[] columns = table.getHeaders();
    		s1.stop();
    		int maskColumnIndex = 0;
    		for (; maskColumnIndex < columns.length; maskColumnIndex++)
    		{
    			if (columns[maskColumnIndex] instanceof MaskColumn)
    			{
    				break;
    			}
    		}
    		if (maskColumnIndex == columns.length)
    		{
    			throw new IllegalArgumentException(
    					"Unable to find mask column in table: " + 
    					tableId.getValue());
    		}

    		// Slice the table and feed the byte array encoded bit masks to the
    		// rendering engine servant.
    		s1 = new Slf4JStopWatch("ode.sliceAndBuildREMap");
    		Data data = table.slice(new long[] { maskColumnIndex }, rows);
    		MaskColumn maskColumn = (MaskColumn) data.columns[0];
    		final Map<byte[], Integer> forRenderingEngine = 
    			new LinkedHashMap<byte[], Integer>();
    		for (int i = 0; i < rows.length; i++)
    		{
    			forRenderingEngine.put(maskColumn.bytes[i],
    					rowColorMap.get(rows[i]));
    		}
    		s1.stop();
    		callInvokerOnMappedArgs(mapper, __cb, __current,
    				                forRenderingEngine);
    	}
    	catch (Throwable t)
    	{
    		if (!(t instanceof Exception))
    		{
    			__cb.ice_exception(new InternalException());
    		}
    		else
    		{
    			__cb.ice_exception((Exception) t);
    		}
    	}
    }

    public void load_async(AMD_RenderingEngine_load __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void lookupPixels_async(AMD_RenderingEngine_lookupPixels __cb,
            long pixelsId, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixelsId);
    }

    public void lookupRenderingDef_async(
            AMD_RenderingEngine_lookupRenderingDef __cb, long pixelsId,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, pixelsId);

    }

    public void removeCodomainMap_async(
            AMD_RenderingEngine_removeCodomainMap __cb,
            CodomainMapContext mapCtx, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, mapCtx);

    }

    public void removeCodomainMapFromChannel_async(
            AMD_RenderingEngine_removeCodomainMapFromChannel __cb,
            CodomainMapContext mapCtx, int w, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, mapCtx, w);

    }

    public void renderAsPackedInt_async(
            AMD_RenderingEngine_renderAsPackedInt __cb, PlaneDef def,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, def);
    }

    public void renderCompressed_async(
            AMD_RenderingEngine_renderCompressed __cb, PlaneDef def,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, def);
    }

    public void renderProjectedAsPackedInt_async(
            AMD_RenderingEngine_renderProjectedAsPackedInt __cb, 
            ProjectionType algorithm, int timepoint, int stepping, int start, 
            int end, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, algorithm.ordinal(), timepoint, 
                stepping, start, end);
    }

    public void renderProjectedCompressed_async(
            AMD_RenderingEngine_renderProjectedCompressed __cb, 
            ProjectionType algorithm, int timepoint, int stepping, int start, 
            int end, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, algorithm.ordinal(), timepoint,
                stepping, start, end);
    }

    public void render_async(AMD_RenderingEngine_render __cb, PlaneDef def,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, def);
    }

    public void resetDefaultSettings_async(AMD_RenderingEngine_resetDefaultSettings __cb,
            boolean save, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, save);
    }
    
    public void saveAsNewSettings_async(
            AMD_RenderingEngine_saveAsNewSettings __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void saveCurrentSettings_async(
            AMD_RenderingEngine_saveCurrentSettings __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void setActive_async(AMD_RenderingEngine_setActive __cb, int w,
            boolean active, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w, active);
    }

    public void setChannelWindow_async(
            AMD_RenderingEngine_setChannelWindow __cb, int w, double start,
            double end, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w, start, end);
    }

    public void setCodomainInterval_async(
            AMD_RenderingEngine_setCodomainInterval __cb, int start, int end,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, start, end);
    }

    public void setCompressionLevel_async(
            AMD_RenderingEngine_setCompressionLevel __cb, float percentage,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, percentage);
    }

    public void setDefaultT_async(AMD_RenderingEngine_setDefaultT __cb, int t,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, t);
    }

    public void setDefaultZ_async(AMD_RenderingEngine_setDefaultZ __cb, int z,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, z);

    }

    public void setModel_async(AMD_RenderingEngine_setModel __cb,
            RenderingModel model, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, model);
    }

    public void setQuantizationMap_async(
            AMD_RenderingEngine_setQuantizationMap __cb, int w, Family fam,
            double coefficient, boolean noiseReduction, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w, fam, coefficient,
                noiseReduction);
    }

    public void setQuantumStrategy_async(
            AMD_RenderingEngine_setQuantumStrategy __cb, int bitResolution,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, bitResolution);
    }

    public void setRGBA_async(AMD_RenderingEngine_setRGBA __cb, int w, int red,
            int green, int blue, int alpha, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w, red, green, blue, alpha);
    }

    @Override
    public void updateSettings_async(AMD_RenderingEngine_updateSettings __cb, RenderingDef settings, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, settings);
    }

    public void updateCodomainMap_async(
            AMD_RenderingEngine_updateCodomainMap __cb,
            CodomainMapContext mapCtx, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, mapCtx);
    }

    public void getRenderingDefId_async(AMD_RenderingEngine_getRenderingDefId __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    @Override
    public void getChannelLookupTable_async(
            AMD_RenderingEngine_getChannelLookupTable __cb, int w,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w);
    }

    @Override
    public void setChannelLookupTable_async(
            AMD_RenderingEngine_setChannelLookupTable __cb, int w,
            String lookup, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w, lookup);
    }

    @Override
    public void getCodomainMapContext_async(
            AMD_RenderingEngine_getCodomainMapContext __cb, int w, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, w);
    }
}