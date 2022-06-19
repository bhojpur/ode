package ode.log;

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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Writes a multi-line log message into a single string.
 * This class should be used to write a log message on multiple lines.  In fact,
 * writing text that spans multiple lines is a platform-dependent operation.
 * This class conveniently encapsulates this process while providing a 
 * platform-independent interface.  This class extends {@link PrintWriter} to
 * inherit all sort of useful <code>print</code> methods and adds another 
 * {@link #print(Throwable) print} method to write a stack trace into the log
 * message.
 */
public class LogMessage
    extends PrintWriter
{
    /**
     * The new line sequence.
     * You can use this to write a string that contains multiple lines.
     */
    public static final String	NEW_LINE = System.getProperty("line.separator");


    /** Creates a new empty message. */
    public LogMessage() 
    {
        super(new StringWriter());
    }

    /**
     * Creates a new instance.
     *
     * @param msg The message to log.
     * @param t The error to log.
     */
    public LogMessage(String msg, Throwable t) {
        this();
        print(msg);
        print(t);
    }

    /**
     * Writes a stack trace into this log message.
     * The information from the exception context is extracted and formatted
     * into a diagnostic message, then printed into the log message.
     * This information includes the exception class name, the exception
     * message, a snapshot of the current stack, and the name of the current
     * thread.
     *
     * @param t The exception.
     */
    public void print(Throwable t)
    {
        t.printStackTrace(this);
        print(out.toString());
        print("Exception in thread \"");
        print(Thread.currentThread().getName());
        println("\"");
    }

    /**
     * Returns the current content of the message.
     * After this call, the current content is flushed. This means that all
     * what has been written so far will be discarded.
     *
     * @return See above.
     */
    public String toString()
    {
        flush();
        return out.toString();
    }

}