/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_CORE_RAWFILESTORE_ICE
#define ODE_CORE_RAWFILESTORE_ICE

#include <ode/ModelF.ice>
#include <ode/ServicesF.ice>
#include <ode/Collections.ice>

module ode {

    module core {

        /**
         * Raw file gateway which provides access to the ODE file repository.
         *
         * Note: methods on this service are protected by a <i>DOWNLOAD</i>
         * restriction.
         */
        ["ami", "amd"] interface RawFileStore extends StatefulServiceInterface
            {

                /**
                 * This method manages the state of the service. This method
                 * will throw a {@link ode.SecurityViolation} if for the
                 * current user context either the file is not readable or a
                 * {@code ode.constants.permissions.BINARYACCESS} restriction is
                 * in place.
                 */
                void setFileId(long fileId) throws ServerError;

                /**
                 * Returns the current file id or null if none has been set.
                 */
                idempotent ode::RLong getFileId() throws ServerError;

                /**
                 * Reads <code>length</code> bytes of data at the
                 * <code>position</code> from the raw file into an array of
                 * bytes.
                 */
                idempotent Ice::ByteSeq read(long position, int length) throws ServerError;

                /**
                 * Returns the size of the file on disk (not as stored in the
                 * database since that value will only be updated on
                 * {@code save}. If the file has not yet been written to, and
                 * therefore does not exist, a {@link ode.ResourceError}
                 * will be thrown.
                 */
                idempotent long size() throws ServerError;

                /**
                 * Limits the size of a file to the given length. If the file
                 * is already shorter than length, no action is taken in which
                 * case false is returned.
                 */
                idempotent bool truncate(long length) throws ServerError;

                /**
                 * Writes <code>length</code> bytes of data from the specified
                 * <code>buf</code> byte array starting at at
                 * <code>position</code> to the raw file.
                 */
                idempotent void write(Ice::ByteSeq buf, long position, int length) throws ServerError;

                /**
                 * Checks to see if a raw file exists with the file ID that
                 * the service was initialized with.
                 *
                 * @return <code>true</code> if there is an accessible file
                 *         within the original file repository with the
                 *         correct ID. Otherwise <code>false</code>.
                 * @throws ResourceError if there is a problem accessing the
                 *         file due to permissions errors within the
                 *         repository or any other I/O error.
                 */
                idempotent bool exists() throws ServerError;

                /**
                 * Saves the {@link ode.model.OriginalFile} associated with
                 * the service if it has been modified. The returned valued
                 * should replace all instances of the
                 * {@link ode.model.OriginalFile} in the client.
                 *
                 * If save has not been called, {@link ode.api.RawFileStore}
                 * instances will save the {@link ode.model.OriginalFile}
                 * object associated with it on {@code close}.
                 */
                idempotent ode::model::OriginalFile save() throws ServerError;

            };
    };
};

#endif