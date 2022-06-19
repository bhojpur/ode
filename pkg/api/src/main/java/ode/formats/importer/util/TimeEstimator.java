package ode.formats.importer.util;

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
 * Estimates the time left (ETA) during image resource upload to the server.
 * Uses a exponential moving average as the calculation algorithm. Internally,
 * the object keeps track of previous estimations in a circular buffer.
 *
 * The user of this API is responsible for instantiating a new object every
 * time the state has to be reset.
 */
public interface TimeEstimator {

    /**
     * Starts the time counting.
     */
    void start();

    /**
     * Stops the time counting and updates the internal time counter.
     */
    void stop();

    /**
     * Stops the time counting and updates the internal updates the internal
     * time counter and counter of total number of transmitted bytes.
     *
     * @param uploadedBytes
     *            Number of bytes uploaded in a single time frame that is being
     *            sampled.
     */
    void stop(long uploadedBytes);

    /**
     * Return the estimated time left in milliseconds based on the calls to
     * {@link TimeEstimator#start() start} and {@link TimeEstimator#stop(long)
     * stop} methods.
     *
     * @return The estimated time remaining. The value 0 is returned if
     *         {@link TimeEstimator#stop(long) stop} hasn't been called at least
     *         once before calling this method.
     */
    long getUploadTimeLeft();
}