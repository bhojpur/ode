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

import ode.api.IMetadata;
import ode.services.server.util.ServerExecutor;
import ode.ServerError;
import ode.api.AMD_IMetadata_countAnnotationsUsedNotOwned;
import ode.api.AMD_IMetadata_countSpecifiedAnnotations;
import ode.api.AMD_IMetadata_getTaggedObjectsCount;
import ode.api.AMD_IMetadata_loadAnnotation;
import ode.api.AMD_IMetadata_loadAnnotations;
import ode.api.AMD_IMetadata_loadAnnotationsUsedNotOwned;
import ode.api.AMD_IMetadata_loadChannelAcquisitionData;
import ode.api.AMD_IMetadata_loadInstrument;
import ode.api.AMD_IMetadata_loadLogFiles;
import ode.api.AMD_IMetadata_loadSpecifiedAnnotations;
import ode.api.AMD_IMetadata_loadSpecifiedAnnotationsLinkedTo;
import ode.api.AMD_IMetadata_loadTagContent;
import ode.api.AMD_IMetadata_loadTagSets;
import ode.api._IMetadataOperations;
import ode.sys.Parameters;
import ode.util.IceMapper;
import Ice.Current;

/** 
 * Implementation of the <code>IMetadata</code> service.
 */
public class MetadataI 
	extends AbstractAmdServant 
	implements _IMetadataOperations
{

	/**
	 * Creates a new instance.
	 * 
	 * @param service Reference to the service.
	 * @param be      The executor.
	 */
	 public MetadataI(IMetadata service, ServerExecutor be)
	 {
		 super(service, be);
	 }

	 public void loadChannelAcquisitionData_async(
			 AMD_IMetadata_loadChannelAcquisitionData __cb, 
			 List<Long> ids, Current __current)
	 throws ServerError 
	 {
		 callInvokerOnRawArgs(__cb, __current, ids);
	 }

	 public void loadAnnotations_async(AMD_IMetadata_loadAnnotations __cb,
			 String rootType, List<Long> rootIds, List<String> annotationTypes, 
			 List<Long> annotatorIds, Parameters options, Current __current) 
	 throws ServerError {
		 try {
			 map(annotationTypes);
		 } catch (ServerError sr) {
			 __cb.ice_exception(sr);
			 return;
		 }
		 callInvokerOnRawArgs(__cb, __current, rootType, rootIds, 
				 annotationTypes, annotatorIds, options);
	 }
	    
	 public void loadSpecifiedAnnotations_async(AMD_IMetadata_loadSpecifiedAnnotations __cb,
			 String annotationType, List<String> include, List<String> exclude,
			Parameters options, Current __current) 
	 throws ServerError {
		 try
		 {
			 annotationType = map(annotationType);
		 } catch (ServerError sr) {
			 __cb.ice_exception(sr);
			 return;
		 }
		 callInvokerOnRawArgs(__cb, __current, annotationType, include, exclude, 
				 options);
	 }
	 
	 public void countSpecifiedAnnotations_async(AMD_IMetadata_countSpecifiedAnnotations __cb,
			 String annotationType, List<String> include, List<String> exclude,
			Parameters options, Current __current) 
	 throws ServerError {
		 try
		 {
			 annotationType = map(annotationType);
		 } catch (ServerError sr) {
			 __cb.ice_exception(sr);
			 return;
		 }
		 callInvokerOnRawArgs(__cb, __current, annotationType, include, exclude, 
				 options);
	 }
	 
	 public void loadTagSets_async(AMD_IMetadata_loadTagSets __cb,  Parameters options,
			 Current __current) 
	 throws ServerError 
	 {
		 callInvokerOnRawArgs(__cb, __current, options);
	 }
	 
	 public void loadTagContent_async(AMD_IMetadata_loadTagContent __cb, 
			 List<Long> ids, Parameters options, Current __current) 
	 throws ServerError 
	 {
		 callInvokerOnRawArgs(__cb, __current, ids, options);
	 }
	 
	 public void getTaggedObjectsCount_async(AMD_IMetadata_getTaggedObjectsCount __cb, 
			 List<Long> ids, Parameters options, Current __current) 
	 throws ServerError 
	 {
		 callInvokerOnRawArgs(__cb, __current, ids, options);
	 }
	 
	 public void loadAnnotation_async(AMD_IMetadata_loadAnnotation __cb, 
			 List<Long> ids, Current __current) 
	 throws ServerError 
	 {
		 callInvokerOnRawArgs(__cb, __current, ids);
	 }

	 public void loadInstrument_async(AMD_IMetadata_loadInstrument __cb, 
			 long id, Current __current) 
	 throws ServerError 
	 {
		 callInvokerOnRawArgs(__cb, __current, id);
	 }
	 
	 public void countAnnotationsUsedNotOwned_async(AMD_IMetadata_countAnnotationsUsedNotOwned __cb,
			 String annotationType, long userID, Current __current) 
	 throws ServerError {
		 try
		 {
			 annotationType = map(annotationType);
		 } catch (ServerError sr) {
			 __cb.ice_exception(sr);
			 return;
		 }
		 callInvokerOnRawArgs(__cb, __current, annotationType, userID);
	 }
	 
	 public void loadAnnotationsUsedNotOwned_async(AMD_IMetadata_loadAnnotationsUsedNotOwned __cb,  
			 String annotationType, long userID, Current __current) 
	 throws ServerError 
	 {
		 try
		 {
			 annotationType = map(annotationType);
		 } catch (ServerError sr) {
			 __cb.ice_exception(sr);
			 return;
		 }
		 callInvokerOnRawArgs(__cb, __current, annotationType, userID);
	 }


	 public void loadSpecifiedAnnotationsLinkedTo_async(AMD_IMetadata_loadSpecifiedAnnotationsLinkedTo __cb,
			 String annotationType, List<String> include, List<String> exclude,
			String rootNodeType, List<Long> nodeIds, Parameters options, Current __current) 
	 throws ServerError {
		 try
		 {
			 annotationType = map(annotationType);
		 } catch (ServerError sr) {
			 __cb.ice_exception(sr);
			 return;
		 }
		 callInvokerOnRawArgs(__cb, __current, annotationType, include, exclude, 
				 rootNodeType, nodeIds, options);
	 }

    @Override
    public void loadLogFiles_async(AMD_IMetadata_loadLogFiles __cb,
            String rootType, List<Long> ids, Current __current)
                    throws ServerError {
        callInvokerOnRawArgs(__cb, __current, rootType, ids);
    }

	protected void map(List<String> annotationTypes) throws ServerError
	{
		if (annotationTypes == null)
		{
			return; // No result is fine
		}

		for (int i = 0; i < annotationTypes.size(); i++)
		{
			String in = annotationTypes.get(i);
			Class<?> out = IceMapper.odeClass(in, true);
			annotationTypes.set(i, out.getName());
		}
	}

	protected String map(String annotationType) throws ServerError
	{
		Class<?> out = IceMapper.odeClass(annotationType, true);
		return out.getName();
	}
}