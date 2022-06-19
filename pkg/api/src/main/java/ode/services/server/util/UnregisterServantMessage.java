package ode.services.server.util;

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

import ode.util.messages.InternalMessage;
import ode.util.ServantHolder;

/**
 * {@link InternalMessage} raised when a servant should be removed from the
 * {@link Ice.ObjectAdapter adapter}. This is most likely a result of a call to
 * "service.close()" from within
 * {@link ode.services.server.util.IceMethodInvoker}
 *
 * Though this instance is {@link java.io.Serializable} through inheritance, it
 * is not intended to be stored anywhere, but should be acted upon and discarded
 * immediately. The {@link Ice.Current} instance is not
 * {@link java.io.Serializable}
 */
public class UnregisterServantMessage extends InternalMessage {

    private static final long serialVersionUID = 3409582093802L;

    private final transient Ice.Current curr;

    private final transient ServantHolder holder;

    public UnregisterServantMessage(Object source, Ice.Current current,
        ServantHolder holder) {
        super(source);
        this.curr = current;
        this.holder = holder;
    }

    public Ice.Current getCurrent() {
        return this.curr;
    }

    public ServantHolder getHolder() {
        return this.holder;
    }
}
