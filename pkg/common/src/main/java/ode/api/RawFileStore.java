package ode.api;

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

import ode.conditions.ResourceError;
import ode.model.core.OriginalFile;

/**
 * Raw file gateway which provides access to the Bhojpur ODE file repository.
 */
public interface RawFileStore extends StatefulServiceInterface {

    /**
     * Returns the current file id or null if none has been set.
     */
    public Long getFileId();

    /**
     * This method manages the state of the service. If the given file
     * is not considered DOWNLOADABLE, this method will throw a
     * {@link ode.conditions.SecurityViolation}.
     * 
     * @param fileId
     *            an {@link ode.model.core.OriginalFile} id.
     */
    public void setFileId(long fileId);
    
    /**
     * Checks to see if a raw file exists with the file ID that the service was
     * initialized with.
     * @return <code>true</code> if there is an accessible file within the
     * original file repository with the correct ID. Otherwise
     * <code>false</code>.
     * @throws ResourceError if there is a problem accessing the file due to
     * permissions errors within the repository or any other I/O error.
     */
    public boolean exists();

    /**
     * Reads {@code length} bytes of data at the {@code position} from the raw
     * file into an array of bytes
     */
    public byte[] read(long position, int length);

    /**
     * Returns the size of the file on disk (not as stored in the database since
     * that value will only be updated on {@link #save()}. If the file has not
     * yet been written to, and therefore does not exist, a
     * {@link ode.conditions.ResourceError} will be thrown.
     */
    public long size();

    /**
     * Limits the size of a file to the given length. If the file is already
     * shorter than length, no action is taken in which case false is returned.
     */
    public boolean truncate(long length);

    /**
     * Writes {@code length} bytes of data from the specified {@code buf} byte
     * array starting at at {@code position} to the raw file
     */
    public void write(byte[] buf, long position, int length);

    /**
     * Saves the {@link OriginalFile} associated with the service if it has
     * been modified. The returned valued should replace all instances of the
     * {@link OriginalFile} in the client.
     *
     * If save has not been called, {@link RawFileStore} instances will save the
     * {@link OriginalFile} object associated with it on {@link #close()}.
     */
    public OriginalFile save();
}