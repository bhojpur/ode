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
 * Thin wrapper around {@link com.codahale.metrics.Timer}
 */
public class DefaultTimer implements Timer {

    /**
     * @see com.codahale.metrics.Timer.Context
     */
    public static class Context implements Timer.Context {

        private final com.codahale.metrics.Timer.Context c;

        public Context(com.codahale.metrics.Timer.Context c) {
            this.c = c;
        }

        public long stop() {
            return this.c.stop();
        }

    }

    private final com.codahale.metrics.Timer t;

    public DefaultTimer(com.codahale.metrics.Timer t) {
        this.t = t;
    }

    /**
     * @see com.codahale.metrics.Timer#time
     */
    public Timer.Context time() {
        return new Context(t.time());
    }

    /**
     * @see com.codahale.metrics.Timer#getCount()
     */
    public long getCount() {
        return this.t.getCount();
    }
}