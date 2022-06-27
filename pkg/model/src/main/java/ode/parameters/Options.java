package ode.parameters;

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
 * Simple options container.
 * Replaces the old PojoOptions, in order to support the new
 * {@link Parameters} passing defined.
 */
public class Options {
    
    public boolean acquisitionData;
    public boolean leaves;
    public boolean orphan;
    public boolean cacheable;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("O[");
        if (acquisitionData) {
            sb.append('A');
        }
        if (leaves) {
            sb.append('L');
        }
        if (orphan) {
            sb.append('O');
        }
        if (cacheable) {
            sb.append('C');
        }
        sb.append(']');
        return sb.toString();
    }
}