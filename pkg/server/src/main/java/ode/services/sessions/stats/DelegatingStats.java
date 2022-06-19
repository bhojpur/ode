package ode.services.sessions.stats;

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
 * Delegates to a {@link SessionStats} which is acquired from {@link #stats()}.
 * Also intended for subclassing.
 */
public class DelegatingStats implements SessionStats {

    private final SessionStats[] stats;
    
    public DelegatingStats() {
        this.stats = new SessionStats[0];
    }
    
    public DelegatingStats(SessionStats[] stats) {
        if (stats == null) {
            this.stats = new SessionStats[0];
        } else {
            this.stats = new SessionStats[stats.length];
            System.arraycopy(stats, 0, this.stats, 0, stats.length);
        }
    }
    
    /**
     * Intended to be overwritten by subclasses.
     */
    protected SessionStats[] stats() {
        return stats;
    }

    public void methodIn() {
        for (SessionStats stats : stats()) {
            stats.methodIn();
        }
    }

    public long methodCount() {
        long count = 0;
        for (SessionStats stats : stats()) {
            count = Math.max(count, stats.methodCount());
        }
        return count;
    }

    public void methodOut() {
        for (SessionStats stats : stats()) {
            stats.methodOut();
        }
    }

    public final void loadedObjects(int objects) {
        for (SessionStats stats : stats()) {
            stats.loadedObjects(objects);
        }
    }

    public final void readBytes(int bytes) {
        for (SessionStats stats : stats()) {
            stats.readBytes(bytes);
        }
    }

    public final void updatedObjects(int objects) {
        for (SessionStats stats : stats()) {
            stats.updatedObjects(objects);
        }
    }

    public final void writtenBytes(int bytes) {
        for (SessionStats stats : stats()) {
            stats.writtenBytes(bytes);
        }
    }

}
