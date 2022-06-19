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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ode.api.IContainer;
import ode.services.server.util.ServerExecutor;
import ode.ApiUsageException;
import ode.RClass;
import ode.ServerError;
import ode.rtypes;
import ode.api.AMD_IContainer_createDataObject;
import ode.api.AMD_IContainer_createDataObjects;
//import ode.api.AMD_IContainer_findAnnotations;
import ode.api.AMD_IContainer_findContainerHierarchies;
import ode.api.AMD_IContainer_getCollectionCount;
import ode.api.AMD_IContainer_getImages;
import ode.api.AMD_IContainer_getImagesByOptions;
import ode.api.AMD_IContainer_getImagesBySplitFilesets;
import ode.api.AMD_IContainer_getUserImages;
import ode.api.AMD_IContainer_link;
import ode.api.AMD_IContainer_loadContainerHierarchy;
import ode.api.AMD_IContainer_retrieveCollection;
import ode.api.AMD_IContainer_unlink;
import ode.api.AMD_IContainer_updateDataObject;
import ode.api.AMD_IContainer_updateDataObjects;
import ode.api._IContainerOperations;
import ode.model.IObject;
import ode.sys.Parameters;
import ode.util.IceMapper;
import Ice.Current;
import Ice.UserException;

/**
 * Implementation of the IContainer service.
 * @see ode.api.IContainer
 */
public class ContainerI extends AbstractAmdServant implements _IContainerOperations {

    public ContainerI(IContainer service, ServerExecutor be) {
        super(service, be);
    }

    // Interface methods
    // =========================================================================

    public void createDataObject_async(AMD_IContainer_createDataObject __cb,
            IObject obj, Parameters options, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, obj, options);

    }

    public void createDataObjects_async(AMD_IContainer_createDataObjects __cb,
            List<IObject> dataObjects, Parameters options,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, dataObjects, options);

    }

    /*
    public void findAnnotations_async(AMD_IContainer_findAnnotations __cb,
            String rootType, List<Long> rootIds, List<Long> annotatorIds,
            Parameters options, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, rootType, rootIds, annotatorIds,
                options);

    }
*/
    public void findContainerHierarchies_async(
            AMD_IContainer_findContainerHierarchies __cb, String rootType,
            List<Long> imageIds, Parameters options, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, rootType, imageIds, options);

    }

    public void getCollectionCount_async(AMD_IContainer_getCollectionCount __cb,
            String type, String property, List<Long> ids,
            Parameters options, Current __current) throws ServerError {

        // This is a bit weird. The CountMap type in ode/Collections.ice
        // specifies <Long, Long> which makes sense, but ContainerImpl is returning,
        // Long, Integer. So we're working around that here with the hope that
        // it'll eventually get fixed. :)
        
        IceMapper mapper = new IceMapper(new IceMapper.ReturnMapping(){

            public Object mapReturnValue(IceMapper mapper, Object value)
                    throws UserException {
                Map<Long, Integer> map = (Map<Long, Integer>) value;
                Map<Long, Long> rv = new HashMap<Long, Long>();
                for (Long k : map.keySet()) {
                    Integer v = map.get(k);
                    rv.put(k, Long.valueOf(v.longValue()));
                }
                return rv;
            }});

        Class<?> odeType = IceMapper.odeClass(type, false);
        String odeStr = odeType == null ? null : odeType.getName();
        Set<Long> _ids = new HashSet<Long>(ids);
        callInvokerOnMappedArgs(mapper, __cb, __current, odeStr, property, _ids, null);

    }

    public void getImagesByOptions_async(AMD_IContainer_getImagesByOptions __cb,
            Parameters options, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, options);

    }

    public void getImages_async(AMD_IContainer_getImages __cb, String rootType,
            List<Long> rootIds, Parameters options, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, rootType, rootIds, options);

    }

    public void getImagesBySplitFilesets_async(
            AMD_IContainer_getImagesBySplitFilesets __cb,
            Map<java.lang.String, List<Long>> included, Parameters options,
            Current __current) throws ServerError {
        final Map<RClass, List<Long>> includedWithClasses =
                new HashMap<RClass, List<Long>>(included.size());
        for (final Map.Entry<String, List<Long>> entry : included.entrySet()) {
            includedWithClasses.put(rtypes.rclass(entry.getKey()), entry.getValue());
        }
        callInvokerOnRawArgs(__cb, __current, includedWithClasses, options);
    }

    public void getUserImages_async(AMD_IContainer_getUserImages __cb,
            Parameters options, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, options);

    }

    public void link_async(AMD_IContainer_link __cb, List<IObject> links,
            Parameters options, Current __current) throws ServerError {

        IceMapper mapper = new IceMapper(IceMapper.FILTERABLE_ARRAY);
        ode.model.ILink[] array;
        if (links == null) {
            array = new ode.model.ILink[0];
        } else {
            array = new ode.model.ILink[links.size()];
            for (int i = 0; i < array.length; i++) {
                try {
                    mapToLinkArrayOrThrow(links, mapper, array, i);
                } catch (Exception e) {
                    __cb.ice_exception(e);
                    return; // EARLY EXIT !
                }
            }
        }
        Object map = mapper.reverse(options);
        callInvokerOnMappedArgs(mapper, __cb, __current, array, map);
    }

    public void loadContainerHierarchy_async(
            AMD_IContainer_loadContainerHierarchy __cb, String rootType,
            List<Long> rootIds, Parameters options, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, rootType, rootIds, options);

    }

    public void retrieveCollection_async(AMD_IContainer_retrieveCollection __cb,
            IObject obj, String collectionName, Parameters options,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, obj, collectionName, options);

    }

    public void unlink_async(AMD_IContainer_unlink __cb, List<IObject> links,
            Parameters options, Current __current) throws ServerError {
        IceMapper mapper = new IceMapper(IceMapper.VOID);
        ode.model.ILink[] array;
        if (links == null) {
            array = new ode.model.ILink[0];
        } else {
            array = new ode.model.ILink[links.size()];
            for (int i = 0; i < array.length; i++) {
                try {
                    mapToLinkArrayOrThrow(links, mapper, array, i);
                } catch (Exception e) {
                    __cb.ice_exception(e);
                    return; // EARLY EXIT!
                }
            }
        }
        Object map = mapper.reverse(options);
        callInvokerOnMappedArgs(mapper, __cb, __current, array, map);
    }

    public void updateDataObject_async(AMD_IContainer_updateDataObject __cb,
            IObject obj, Parameters options, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, obj, options);

    }

    public void updateDataObjects_async(AMD_IContainer_updateDataObjects __cb,
            List<IObject> objs, Parameters options, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, objs, options);

    }

    // Helpers
    // =========================================================================

    private void mapToLinkArrayOrThrow(
            List<IObject> links, IceMapper mapper, ode.model.ILink[] array,
            int i) throws ApiUsageException {
        try {
            array[i] = (ode.model.ILink) mapper.reverse(links.get(i));
        } catch (ClassCastException cce) {
            ode.ApiUsageException aue = new ode.ApiUsageException();
            IceMapper.fillServerError(aue, cce);
            aue.message = "ClassCastException: " + cce.getMessage();
            throw aue;
        }
    }
}