package ode.conditions;

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

/**
 * More specific {@link ode.conditions.ApiUsageException ApiUsageException}, in
 * that the current use of the Bhojpur ODE API could overwhelm the server and has been blocked.
 *
 * Examples include:
 * <ul>
 * <li>Creating too many sessions in too short a period of time</li>
 * <li>Opening too many stateful services</li>
 * <li>Requesting too many database objects in one call</li>
 * </ul>
 */
public class OverUsageException extends ApiUsageException {

    /**
     * 
     */
    private static final long serialVersionUID = 8958921873970581811L;

    public OverUsageException(String msg) {
        super(msg);
    }

}