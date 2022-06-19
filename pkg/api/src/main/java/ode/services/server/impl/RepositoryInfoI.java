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

import ode.api.IRepositoryInfo;
import ode.services.server.util.ServerExecutor;
import ode.ServerError;
import ode.api.AMD_IRepositoryInfo_getFreeSpaceInKilobytes;
import ode.api.AMD_IRepositoryInfo_getUsageFraction;
import ode.api.AMD_IRepositoryInfo_getUsedSpaceInKilobytes;
import ode.api.AMD_IRepositoryInfo_removeUnusedFiles;
import ode.api.AMD_IRepositoryInfo_sanityCheckRepository;
import ode.api._IRepositoryInfoOperations;

import Ice.Current;

/**
 * Implementation of the IRepositoryInfo service.
 *
 * @see ode.api.IRepositoryInfo
 */
public class RepositoryInfoI extends AbstractAmdServant implements
        _IRepositoryInfoOperations {

    public RepositoryInfoI(IRepositoryInfo service, ServerExecutor be) {
        super(service, be);
    }

    // Interface methods
    // =========================================================================

    public void getFreeSpaceInKilobytes_async(
            AMD_IRepositoryInfo_getFreeSpaceInKilobytes __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void getUsageFraction_async(
            AMD_IRepositoryInfo_getUsageFraction __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void getUsedSpaceInKilobytes_async(
            AMD_IRepositoryInfo_getUsedSpaceInKilobytes __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void removeUnusedFiles_async(
            AMD_IRepositoryInfo_removeUnusedFiles __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void sanityCheckRepository_async(
            AMD_IRepositoryInfo_sanityCheckRepository __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

}