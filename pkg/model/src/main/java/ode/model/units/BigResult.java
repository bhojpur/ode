package ode.model.units;

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

import java.math.BigDecimal;

/**
 * Checked exception which is thrown from unit methods which can possibly
 * overflow. Use of {@link BigDecimal} in the {@link ode.model.units.Conversion}
 * prevents the overflow from happening prematurely, but once the value is to
 * be returned to the client, the ode.model (or dependent objects) will be
 * forced to transform the {@link BigDecimal} to a {@link Double}. If that
 * {@link Double} is either {@link Double#POSITIVE_INFINITY} or
 * {@link Double#NEGATIVE_INFINITY}, then this exception will be thrown. The
 * internal {@link BigDecimal} will be returned in the {@link #result} field
 * for consumption by the client.
 */
public class BigResult extends Exception {

    private static final long serialVersionUID = 626976940908390756L;

    public final BigDecimal result;

    public BigResult(BigDecimal result, String message) {
        super(message);
        this.result = result;
    }

}