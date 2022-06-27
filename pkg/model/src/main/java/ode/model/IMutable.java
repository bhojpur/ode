package ode.model;

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
 * interface for all mutable domain objects. Provides access to the version
 * property which the backend uses for optimistic locking. An object with an id
 * but without a version passed to the backend is considered an error, since
 * some backends will silently create a new object in the database.
 */
public interface IMutable extends IObject {

    /** optimistic-lock version. Usually managed by the backend. */
    public Integer getVersion();

    /**
     * use with caution. In general, the version should only be altered by the
     * backend. In the best case, an exception will be thrown for a version not
     * equal to the current DB value. In the worst (though rare) case, the new
     * version could match the database, and override optimistic lock checks
     * that are supposed to prevent data loss.
     * 
     * @param version
     *            Value for this objects version.
     */
    public void setVersion(Integer version);
    // TODO public Event getUpdateEvent();
    // TODO public void setUpdateEvent(Event e);

}