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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ode.api.ITypes;
import ode.services.server.util.ServerExecutor;
import ode.ServerError;
import ode.api.AMD_ITypes_allEnumerations;
import ode.api.AMD_ITypes_createEnumeration;
import ode.api.AMD_ITypes_deleteEnumeration;
import ode.api.AMD_ITypes_getAnnotationTypes;
import ode.api.AMD_ITypes_getEnumeration;
import ode.api.AMD_ITypes_getEnumerationTypes;
import ode.api.AMD_ITypes_getEnumerationsWithEntries;
import ode.api.AMD_ITypes_getOriginalEnumerations;
import ode.api.AMD_ITypes_resetEnumerations;
import ode.api.AMD_ITypes_updateEnumeration;
import ode.api.AMD_ITypes_updateEnumerations;
import ode.api._ITypesOperations;
import ode.model.IObject;
import ode.util.IceMapper;
import Ice.Current;
import Ice.UserException;

/**
 * Implementation of the ITypes service.
 * @see ode.api.ITypes
 */
public class TypesI extends AbstractAmdServant implements _ITypesOperations {
    
	public TypesI(ITypes service, ServerExecutor be) {
		super(service, be);
	}

    // Interface methods
    // =========================================================================

    public void allEnumerations_async(AMD_ITypes_allEnumerations __cb,
            String type, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, type);

    }

    public void createEnumeration_async(AMD_ITypes_createEnumeration __cb,
            IObject newEnum, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, newEnum);

    }

    public void deleteEnumeration_async(AMD_ITypes_deleteEnumeration __cb,
            IObject oldEnum, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, oldEnum);

    }

    public void getAnnotationTypes_async(AMD_ITypes_getAnnotationTypes __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void getEnumerationTypes_async(AMD_ITypes_getEnumerationTypes __cb,
            Current __current) throws ServerError {
        
        IceMapper mapper = new IceMapper(new IceMapper.ReturnMapping(){

            public Object mapReturnValue(IceMapper mapper, Object value)
                    throws UserException {
                
                if (value == null) {
                    return null;
                }
                List<Class> iv = (List<Class>) value;
                List<String> rv = new ArrayList<String>(iv.size());
                for (Class i : iv) {
                    rv.add(i.getSimpleName());
                }
                return rv;
            }});

        callInvokerOnMappedArgs(mapper, __cb, __current);

    }

    public void getEnumeration_async(AMD_ITypes_getEnumeration __cb,
            String type, String value, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, type, value);

    }

    private final static Logger log = LoggerFactory.getLogger(TypesI.class);

    public void getEnumerationsWithEntries_async(
            AMD_ITypes_getEnumerationsWithEntries __cb, Current __current)
            throws ServerError {
        
        IceMapper mapper = new IceMapper(new IceMapper.ReturnMapping(){

            public Object mapReturnValue(IceMapper mapper, Object value)
                    throws UserException {
                
            	if (value == null) {
                    return null;
                } 
            	Map<Class, List<ode.model.IEnum>> map = (Map<Class, List<ode.model.IEnum>>) value;
                Map<String, List<IObject>> rv = new HashMap<String, List<IObject>>();
                for (Class key : map.keySet()) {
                    Object v = map.get(key);
                    String kr = key.getSimpleName();
                    List<IObject> vr = (List<IObject>) IceMapper.FILTERABLE_COLLECTION.mapReturnValue(mapper, v);
                    rv.put(kr, vr);
                }
                return rv;
                
            }});

        callInvokerOnMappedArgs(mapper, __cb, __current);

    }

    public void getOriginalEnumerations_async(
            AMD_ITypes_getOriginalEnumerations __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void resetEnumerations_async(AMD_ITypes_resetEnumerations __cb,
            String enumClass, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, enumClass);

    }

    public void updateEnumeration_async(AMD_ITypes_updateEnumeration __cb,
            IObject oldEnum, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, oldEnum);

    }

    public void updateEnumerations_async(AMD_ITypes_updateEnumerations __cb,
            List<IObject> oldEnums, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, oldEnums);

    }

}