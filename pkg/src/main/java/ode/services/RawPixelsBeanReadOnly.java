package ode.services;

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

import ode.annotations.RolesAllowed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the RawPixelsStore stateful service.
 * @see RawPixelsBean
 */
@Transactional(readOnly = true)
public class RawPixelsBeanReadOnly extends RawPixelsBean {

    /** The logger for this particular class */
    private static Logger log = LoggerFactory.getLogger(RawPixelsBeanReadOnly.class);

    private static final long serialVersionUID = -5121611405890224414L;

    /**
     * default constructor
     */
    public RawPixelsBeanReadOnly() {
    }

    /**
     * overridden to allow Spring to set boolean
     */
    public RawPixelsBeanReadOnly(boolean checking, String odeDataDir) {
        super(checking, odeDataDir);
    }

    @RolesAllowed("user")
    @Override
    public void close() {
        /* omits save() */
        super.clean();
    }
}