package ode.services.util;

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

import java.lang.reflect.Method;

/**
 * Methods for working with Ice.
 */
public class IceUtil {

    private static final Object ICE_ENCODING;

    private static final Method ICE_OUTPUT_STREAM_METHOD;

    private static final Method ICE_INPUT_STREAM_METHOD;

    static {
        Class<Ice.Communicator> ic = Ice.Communicator.class;
        Class<byte[]> b = byte[].class;
        Method os = null;
        Method is = null;
        Object enc = null;
        try {
            try {
                Class<?> ev = Class.forName("Ice.EncodingVersion");
                os = Ice.Util.class.getMethod("createOutputStream", ic, ev);
                is = Ice.Util.class.getMethod("createInputStream", ic, b, ev);
                enc = Ice.Util.class.getField("Encoding_1_0").get(null);
            } catch (ClassNotFoundException e) {
                // Then this is pre Ice3.5
                os = Ice.Util.class.getMethod("createOutputStream", ic);
                is = Ice.Util.class.getMethod("createInputStream", ic, b);
            }
        } catch (Exception e) {
            // This shouldn't be able to happen unless there's been
            // a breaking change in Ice.
            throw new RuntimeException("Cannot configure Ice", e);
        }
        ICE_ENCODING = enc;
        ICE_OUTPUT_STREAM_METHOD = os;
        ICE_INPUT_STREAM_METHOD = is;
    }

    /**
     * Creates an {@link Ice.OutputStream} with the appropriate encoding.
     * This should only be used parsing objects/streams between Ice versions,
     * for example when the data is or will be stored in the database.
     */
    public static Ice.OutputStream createSafeOutputStream(Ice.Communicator ic) {
        Object[] args;
        if (ICE_ENCODING != null) {
            args = new Object[]{ic, ICE_ENCODING};
        } else {
            args = new Object[]{ic};
        }

        try {
            return (Ice.OutputStream) ICE_OUTPUT_STREAM_METHOD.invoke(null, args);
        } catch (Exception e) {
            throw new RuntimeException("ICE_INPUT_STREAM_METHOD failed", e);
        }
    }

    /**
     * Creates an {@link Ice.InputStream} with the appropriate encoding.
     * This should only be used parsing objects/streams between Ice versions,
     * for example when the data is or will be stored in the database.
     */
    public static Ice.InputStream createSafeInputStream(Ice.Communicator ic, byte[] data) {
        Object[] args;
        if (ICE_ENCODING != null) {
            args = new Object[]{ic, data, ICE_ENCODING};
        } else {
            args = new Object[]{ic, data};
        }
        try {
            return (Ice.InputStream) ICE_INPUT_STREAM_METHOD.invoke(null, args);
        } catch (Exception e) {
            throw new RuntimeException("ICE_INPUT_STREAM_METHOD failed", e);
        }
    }
}