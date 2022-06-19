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

public class SimpleSessionStats implements SessionStats {

    private final ObjectsReadCounter read;
    private final ObjectsWrittenCounter written;
    private final MethodCounter methods;
    
    public SimpleSessionStats(ObjectsReadCounter read, ObjectsWrittenCounter written, MethodCounter methods) {
        this.read = read;
        this.written = written;
        this.methods = methods;
    }

    public void methodIn() {
        this.methods.increment(1);
    }

    public long methodCount() {
        return this.methods.count;
    }

    public void methodOut() {
        this.methods.increment(-1);
    }

    public void loadedObjects(int objects) {
        this.read.increment(objects);
    }

    public void readBytes(int bytes) {
        throw new UnsupportedOperationException();
    }

    public void updatedObjects(int objects) {
        this.written.increment(objects);
    }

    public void writtenBytes(int bytes) {
        throw new UnsupportedOperationException();   
    }

}
