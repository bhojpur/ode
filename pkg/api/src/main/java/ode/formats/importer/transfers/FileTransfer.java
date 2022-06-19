package ode.formats.importer.transfers;

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

import java.io.IOException;
import java.util.List;

import ode.ServerError;

/**
 * Abstracted concept of "getting the file to the server". A single
 * {@link FileTransfer} instance should be used for all the instances in
 * a single "import".
 *
 * Implementations are responsible for making sure that when
 * the server accesses the remote (i.e. server-side) location
 * that a file-like object (file, hard-link, soft-link, etc.) is
 * present with the right size and checksum.
 *
 * Transfer implementations have a number of responsibilities such as
 * reporting on progress and estimating remaining time which make them not
 * completely trivial to implement. Sub-classing an existing implementation is
 * likely the easiest way to modify behavior.
 *
 * Implementations should be thread-safe, i.e. callable from multiple
 * threads, and blocking should be avoided if at all possible.
 */
public interface FileTransfer {

   /**
    * Transfers a file and returns the appropriate checksum string for
    * the source file. The {@link TransferState} instance should be unique
    * for this invocation, i.e. not used by any other threads. After
    * execution, the fields can be inspected to see, e.g., the newly created
    * file.
    */
    String transfer(TransferState state)
        throws IOException, ServerError;

    /**
     * Callback which must be invoked after a related set of files has been
     * processed. This provides the {@link FileTransfer} instance a chance to
     * free resources. If any errors have occurred, then no destructive changes
     * should be made, though the user may should be given the option to react.
     */
    void afterTransfer(int errors, List<String> transferredFiles) throws CleanupFailure;

}