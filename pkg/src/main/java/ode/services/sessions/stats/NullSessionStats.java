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
 * Implementation of {@link SessionStats} which does nothing. This is used for
 * internal and privileged sessions which are not counted.
 */
public class NullSessionStats implements SessionStats {

    public void methodIn() {
        // nothing
    }

    public long methodCount() {
        return 0;
    }

    public void methodOut() {
        // nothing
    }

    public void loadedObjects(int objects) {
        // nothing
    }

    public void readBytes(int bytes) {
        // nothing
    }

    public void updatedObjects(int objects) {
        // nothing
    }

    public void writtenBytes(int bytes) {
        // nothing
    }

}
