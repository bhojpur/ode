package ode.formats.enums;

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

import ode.model.IObject;

public class EnumerationException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /** The enumeration class that was used in a failed enumeration lookup. */
    private Class<? extends IObject>  failureClass;

    /** The enumeration value that was used in a failed enumeration lookup. */
    private String value;

    public EnumerationException(String message, Class<? extends IObject> klass,
                                String value)
    {
        super(message);
        this.failureClass = klass;
        this.value = value;
    }

    public Class<? extends IObject> getFailureClass()
    {
        return failureClass;
    }

    public String getValue()
    {
        return value;
    }

    public String toString()
    {
        return getMessage() + "'" + value + "' in '" + failureClass + "'.";
    }
}