package ode.services.server;

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
 * Status check for Bhojpur ODE server. Uses the "StatusCheck" proxy as defined in the
 * Ice properties passed to main method.
 */
public class Status implements Runnable {

    Ice.Communicator ic;

    /**
     * Uses the passed args to create an {@link Ice.Communicator}.
     */
    public static void main(final String[] args) {
        Status status = new Status(args);
        try {
            status.run();
        } catch (StatusException se) {
            se.printStackTrace();
            System.exit(se.exit);
        }
        System.exit(0);
    }

    public Status(String[] args) {
        ic = Ice.Util.initialize(args);
    }

    public void run() {
        Ice.ObjectPrx base = ic.propertyToProxy("StatusCheck");
        try {
            Glacier2.SessionManagerPrx mgr = Glacier2.SessionManagerPrxHelper
                    .checkedCast(base);
            if (mgr == null) {
                throw new StatusException("Null proxy.", 1);
            }
        } catch (Ice.ConnectionRefusedException cre) {
            throw new StatusException("Connection refused.", 2);
        }

    }

}

class StatusException extends RuntimeException {
    int exit = Integer.MAX_VALUE;

    StatusException(String msg, int code) {
        super(msg);
        exit = code;
    }
}