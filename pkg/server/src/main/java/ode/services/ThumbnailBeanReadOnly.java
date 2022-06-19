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

import java.util.Map;
import java.util.Set;

import ode.annotations.RolesAllowed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides methods for directly querying object graphs. This read-only variant of the service
 * does not support rendering engine lazy object creation where rendering settings are missing.
 * @see ThumbnailBean
 */
@Transactional(readOnly = true)
public class ThumbnailBeanReadOnly extends ThumbnailBean {

    private static final long serialVersionUID = 7297115245954897138L;

    /** The logger for this class. */
    private transient static Logger log = LoggerFactory.getLogger(ThumbnailBeanReadOnly.class);

    /**
     * overridden to allow Spring to set boolean
     */
    public ThumbnailBeanReadOnly(boolean checking) {
        super(checking);
    }

    /*
     * (non-Javadoc)
     *
     * @see ode.api.ThumbnailStore#setPixelsId(long)
     */
    @RolesAllowed("user")
    @Override
    public boolean setPixelsId(long id)
    {
        return super.setPixelsId(id);
    }

    @RolesAllowed("user")
    @Override
    public Map<Long, byte[]> getThumbnailByLongestSideSet(Integer size, Set<Long> pixelsIds)
    {
        return super.getThumbnailByLongestSideSet(size, pixelsIds);
    }

    /*
     * (non-Javadoc)
     *
     * @see ode.api.ThumbnailStore#getThumbnail(ode.model.core.Pixels,
     *      ode.model.display.RenderingDef, java.lang.Integer,
     *      java.lang.Integer)
     */
    @RolesAllowed("user")
    @Override
    public byte[] getThumbnail(Integer sizeX, Integer sizeY) {
        return super.getThumbnail(sizeX, sizeY);
    }

    /*
     * (non-Javadoc)
     *
     * @see ode.api.ThumbnailStore#getThumbnailByLongestSide(ode.model.core.Pixels,
     *      ode.model.display.RenderingDef, java.lang.Integer)
     */
    @RolesAllowed("user")
    @Override
    public byte[] getThumbnailByLongestSide(Integer size) {
        return super.getThumbnailByLongestSide(size);
    }
}