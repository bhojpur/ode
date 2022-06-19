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

import ode.api.IQuery;
import ode.services.server.util.ServerExecutor;
import ode.ServerError;
import ode.api.AMD_IQuery_find;
import ode.api.AMD_IQuery_findAll;
import ode.api.AMD_IQuery_findAllByExample;
import ode.api.AMD_IQuery_findAllByFullText;
import ode.api.AMD_IQuery_findAllByQuery;
import ode.api.AMD_IQuery_findAllByString;
import ode.api.AMD_IQuery_findByExample;
import ode.api.AMD_IQuery_findByQuery;
import ode.api.AMD_IQuery_findByString;
import ode.api.AMD_IQuery_get;
import ode.api.AMD_IQuery_projection;
import ode.api.AMD_IQuery_refresh;
import ode.api._IQueryOperations;
import ode.model.IObject;
import ode.sys.Filter;
import ode.sys.Parameters;
import ode.util.IceMapper;

import Ice.Current;

/**
 * Implementation of the IQuery service.
 * 
 * @see ode.api.IQuery
 */
public class QueryI extends AbstractAmdServant implements _IQueryOperations {

    public QueryI(IQuery service, ServerExecutor be) {
        super(service, be);
    }

    // Interface methods
    // =========================================================================

    public void findAllByExample_async(AMD_IQuery_findAllByExample __cb,
            IObject example, Filter filter, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, example, filter);

    }

    public void findAllByFullText_async(AMD_IQuery_findAllByFullText __cb,
            String klass, String query, Parameters params, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, klass, query, params);

    }

    public void findAllByQuery_async(AMD_IQuery_findAllByQuery __cb,
            String query, Parameters params, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, query, params);

    }

    public void findAllByString_async(AMD_IQuery_findAllByString __cb,
            String klass, String field, String value, boolean caseSensitive,
            Filter filter, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, klass, field, value,
                caseSensitive, filter);

    }

    public void findAll_async(AMD_IQuery_findAll __cb, String klass,
            Filter filter, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, klass, filter);

    }

    public void findByExample_async(AMD_IQuery_findByExample __cb,
            IObject example, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, example);

    }

    public void findByQuery_async(AMD_IQuery_findByQuery __cb, String query,
            Parameters params, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, query, params);

    }

    public void findByString_async(AMD_IQuery_findByString __cb, String klass,
            String field, String value, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, klass, field, value);

    }

    public void find_async(AMD_IQuery_find __cb, String klass, long id,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, klass, id);

    }

    public void get_async(AMD_IQuery_get __cb, String klass, long id,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, klass, id);

    }

    public void refresh_async(AMD_IQuery_refresh __cb, IObject object,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, object);

    }

    public void projection_async(AMD_IQuery_projection __cb, String query,
            Parameters params, Current __current) throws ServerError {
        IceMapper mapper = new IceMapper(IceMapper.LISTOBJECTARRAY_TO_RTYPESEQSEQ);
        ode.parameters.Parameters p = mapper.convert(params);
        callInvokerOnMappedArgs(mapper, __cb, __current, query, p);
    }

}