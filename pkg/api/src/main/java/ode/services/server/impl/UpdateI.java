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

import ode.api.IUpdate;
import ode.services.server.util.ServerExecutor;
import ode.ServerError;
import ode.api.AMD_IUpdate_deleteObject;
import ode.api.AMD_IUpdate_indexObject;
import ode.api.AMD_IUpdate_saveAndReturnArray;
import ode.api.AMD_IUpdate_saveAndReturnIds;
import ode.api.AMD_IUpdate_saveAndReturnObject;
import ode.api.AMD_IUpdate_saveArray;
import ode.api.AMD_IUpdate_saveCollection;
import ode.api.AMD_IUpdate_saveObject;
import ode.api._IUpdateOperations;
import ode.model.IObject;

import Ice.Current;

/**
 * Implementation of the IUpdate service.
 * @see ode.api.IUpdate
 */
public class UpdateI extends AbstractAmdServant implements _IUpdateOperations {

    public UpdateI(IUpdate service, ServerExecutor be) {
        super(service, be);
    }

    // Interface methods
    // =========================================================================

    public void deleteObject_async(AMD_IUpdate_deleteObject __cb, IObject row,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, row);

    }

    public void indexObject_async(AMD_IUpdate_indexObject __cb, IObject row,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, row);

    }

    public void saveAndReturnArray_async(AMD_IUpdate_saveAndReturnArray __cb,
            List<IObject> graph, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, graph);

    }

    public void saveAndReturnObject_async(AMD_IUpdate_saveAndReturnObject __cb,
            IObject obj, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, obj);

    }

    public void saveArray_async(AMD_IUpdate_saveArray __cb,
            List<IObject> graph, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, graph);

    }

    public void saveCollection_async(AMD_IUpdate_saveCollection __cb,
            List<IObject> objs, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, objs);
    }

    public void saveObject_async(AMD_IUpdate_saveObject __cb, IObject obj,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, obj);

    }

    public void saveAndReturnIds_async(AMD_IUpdate_saveAndReturnIds __cb,
            List<IObject> graph, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, graph);
    }

}