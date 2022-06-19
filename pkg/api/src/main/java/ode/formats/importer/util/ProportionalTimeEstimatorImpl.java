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

import org.apache.commons.lang.time.StopWatch;

/**
 * Class implementing the {@link TimeEstimator} interface. Uses the Exponential
 * Moving Average equation to provide an estimate of the remaining upload time
 * of binary data. A correction factor is used for minimal overestimation.
 */
public class ProportionalTimeEstimatorImpl implements TimeEstimator {

    private final ThreadLocal<StopWatch> swGetter = new ThreadLocal<StopWatch>() {
        @Override
        protected StopWatch initialValue() {
            return new StopWatch();
        }
    };

    private long imageContainerSize = 0, timeLeft = 0;

    private long totalBytes = 0;

    private long totalTime = 0;

    /**
     * Creates a new object of this class with a defined internal buffer size.
     *
     * @param imageContainerSize
     *            The total size in bytes of the data container for which upload
     *            time is being estimated.
     */
    public ProportionalTimeEstimatorImpl(long imageContainerSize) {
        this.imageContainerSize = imageContainerSize;
    }

    /**
     * @see TimeEstimator#start()
     */
    public void start() {
        final StopWatch sw = swGetter.get();
        sw.reset();
        sw.start();
    }

    /**
     * @see TimeEstimator#stop()
     */
    public void stop() {
        final StopWatch sw = swGetter.get();
        sw.stop();
        updateStats(sw);
    }

    /**
     * @see TimeEstimator#stop(long)
     */
    public void stop(long uploadedBytes) {
        final StopWatch sw = swGetter.get();
        sw.stop();
        updateStats(sw, uploadedBytes);
    }

    private synchronized void updateStats(StopWatch sw) {
        totalTime += sw.getTime();
    }

    private synchronized void updateStats(StopWatch sw, long uploadedBytes) {
        totalTime += sw.getTime();
        totalBytes += uploadedBytes;
        imageContainerSize -= uploadedBytes;

        if (totalTime > 0) {
            float averageBps = totalBytes / ((float) totalTime / 1000);
            timeLeft = (long) Math
                    .ceil((imageContainerSize / averageBps) * 1000);
        }
    }

    /**
     * @see TimeEstimator#getUploadTimeLeft()
     */
    public synchronized long getUploadTimeLeft() {
        return timeLeft;
    }
}