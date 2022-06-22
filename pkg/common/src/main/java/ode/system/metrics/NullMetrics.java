package ode.system.metrics;

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
 * No-op version of {@link Metrics}
 */
public class NullMetrics implements Metrics {

    private static class NullSnapshots implements Snapshot {

        @Override
        public double get75thPercentile() {
            return 0;
        }

        @Override
        public double get95thPercentile() {
            return 0;
        }

        @Override
        public double get98thPercentile() {
            return 0;
        }

        @Override
        public double get999thPercentile() {
            return 0;
        }

        @Override
        public double get99thPercentile() {
            return 0;
        }

        @Override
        public long getMax() {
            return 0;
        }

        @Override
        public double getMean() {
            return 0;
        }

        @Override
        public double getMedian() {
            return 0;
        }

        @Override
        public long getMin() {
            return 0;
        }

        @Override
        public double getStdDev() {
            return 0;
        }

        @Override
        public double getValue(double quantile) {
            return 0;
        }

        @Override
        public long[] getValues() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

    }

    private static class NullHistogram implements Histogram {

        @Override
        public void update(int done) {
            // no-op
        }

        @Override
        public Snapshot getSnapshot() {
            return S;
        }
    }

    private static class NullTimerContext implements Timer.Context {

        @Override
        public long stop() {
            return -1;
        }
    }

    private static class NullTimer implements Timer {

        private final NullTimerContext c;

        public NullTimer(NullTimerContext c) {
            this.c = c;
        }

        @Override
        public Context time() {
            return c;
        }

        @Override
        public long getCount() {
            return -1;
        }
    }

    private static class NullCounter implements Counter {

        @Override
        public void inc() {
            // no-op
        }

        public void dec() {
            // no-op
        }

        @Override
        public long getCount() {
            return -1l;
        }
    }

    private final static NullSnapshots S = new NullSnapshots();

    private final static NullTimerContext X = new NullTimerContext();

    private final static NullHistogram H = new NullHistogram();

    private final static NullCounter C = new NullCounter();

    private final static NullTimer T = new NullTimer(X);

    @Override
    public Histogram histogram(Object obj, String name) {
        return H;
    }

    @Override
    public Timer timer(Object obj, String name) {
        return T;
    }

    @Override
    public Counter counter(Object obj, String name) {
        return C;
    }

}