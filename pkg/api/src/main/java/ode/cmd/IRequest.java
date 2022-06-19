package ode.cmd;

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

import java.util.Map;

import ode.cmd.HandleI.Cancel;

/**
 * SPI Orthogonal interface hierarchy of types for working with the
 * {@link ode.cmd.Request} hierarchy. All request implementations
 * handled by the server <em>must</em> also be instances of {@link IRequest},
 * which defines the lifecycle methods needed for processing.
 */
public interface IRequest {

    /**
     * Returns the desired call context for this request. Some request
     * implementations will require "ode.group":"-1" for example and will
     * hard-code that value. Others may permit users to pass in the desired
     * values which will be merged into the static {@link Map} as desired.
     *
     * @return the call context for this request
     */
    Map<String, String> getCallContext();

    /**
     * Method called within the transaction boundaries before any processing occurs.
     * 
     * Implementations must properly initialize the "step" field of the
     * {@link Status} object by calling {@link Helper#setSteps(int)}. This count
     * will define how many times the {@link #step(int)} method will be called.
     * 
     * The {@link Helper} instance passed in contains those resources needed by
     * IRequests to interact with data and should be stored for later use.
     *
     * @param helper the helper for this request
     * @throws Cancel if this request is cancelled
     */
    void init(Helper helper) throws Cancel;

    /**
     * Single uncancellable action which will be performed by this IRequest.
     * 
     * The return value can be an ode.model object that is attached to the
     * current thread and transaction. After processing and detachment from
     * the transaction, the object will be passed to
     * {@link #buildResponse(int, Object)} for conversion and storage.
     *
     * @param step the step number
     * @return an object to be used in building the response
     * @throws Cancel if this request is cancelled
     */
    Object step(int step) throws Cancel;

    /**
     * Method within the transaction boundaries after all processing has
     * occurred. A thrown {@link Cancel} will still rollback the current
     * transaction.
     *
     * @throws Cancel if this request is cancelled
     */
    void finish() throws Cancel;

    /**
     * Post-transaction chance to map from the return value of
     * {@link #step(int)} to a {@link ode.cmd.Response} object.
     *
     * @param step the step number
     * @param object an object to be used in building the response
     */
    void buildResponse(int step, Object object);

    /**
     * Returns the current response value. This method should be protected
     * by synchronization where necessary, and should never raise an exception.
     * It is also guaranteed to be called so that any state cleanup that is
     * necessary can take place here.
     *
     * @return the response to this request
     */
    Response getResponse();

}