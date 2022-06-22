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
 * Thin wrapper around {@link com.codahale.metrics.Snapshot}
 */
public class DefaultSnapshot implements Snapshot {

    private final com.codahale.metrics.Snapshot s;

    public DefaultSnapshot(com.codahale.metrics.Snapshot s) {
        this.s = s;
    }

    public double get75thPercentile() {
        return s.get75thPercentile();
    }

    public double get95thPercentile() {
        return s.get95thPercentile();
    }

    public double get98thPercentile() {
        return s.get98thPercentile();
    }

    public double get999thPercentile() {
        return s.get999thPercentile();
    }

    public double get99thPercentile() {
        return s.get99thPercentile();
    }

    public long getMax() {
        return s.getMax();
    }

    public double getMean() {
        return s.getMean();
    }

    public double getMedian() {
        return s.getMedian();
    }

    public long getMin() {
        return s.getMin();
    }

    public double getStdDev() {
        return s.getStdDev();
    }

    public double getValue(double quantile) {
        return s.getValue(quantile);
    }

    public long[] getValues() {
        return s.getValues();
    }

    public int size() {
        return s.size();
    }

}