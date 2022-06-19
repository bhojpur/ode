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

/** 
 * Defines the log service interface.
 * Operations are defined to log messages according the severity of the event:
 * <ul>
 *  <li><i>DEBUG</i>: all debug messages.</li>
 *  <li><i>INFO</i>: regular log messages that inform about normal application
 *  	workflow.</li>
 *  <li><i>WARN</i>: messages emitted in case of abnormal or suspect
 * 		application behavior.</li>
 *  <li><i>ERROR</i>: all error conditions and failures that can be
 * 		recovered.</li>
 *  <li><i>FATAL</i>: severe failures that require the application to
 * 		terminate.</li>
 * <ul>
 * <p>Every method takes in two parameters: the originator of the log message
 * and the log message itself.  If the message spans multiple lines, then
 * a {@link LogMessage} object should be used to construct it.</p>  
 * <p>A configuration file (in the configuration directory under the
 * installation directory) provides for fine-tuning of the log settings on a
 * per-class basis.  Those settings include the choice of output locations and
 * verbosity based on priority levels &#151; <i>DEBUG</i> has a lower priority
 * than <i>INFO</i>, which, in turn, is lower priority than <i>WARN</i>, and so
 * on.</p>
 * <p>The implementation of the service is thread-safe.  Methods can be called
 * from different threads without compromising the integrity of the log records.
 * </p>
 */
public interface Logger
{

    /**
     * Logs a debug message.
     *
     * @param originator The originator of the message.
     * @param logMsg The log message.
     */
    public void debug(Object originator, String logMsg);

    /**
     * Logs a debug message.
     *
     * @param originator The originator of the message.
     * @param msg The log message.
     */
    public void debug(Object originator, LogMessage msg);

    /**
     * Logs an info message.
     *
     * @param originator The originator of the message.
     * @param logMsg The log message.
     */
    public void info(Object originator, String logMsg);

    /**
     * Logs an info message.
     *
     * @param originator The originator of the message.
     * @param msg The log message.
     */
    public void info(Object originator, LogMessage msg);

    /**
     * Logs a warn message.
     *
     * @param originator The originator of the message.
     * @param logMsg The log message.
     */
    public void warn(Object originator, String logMsg);

    /**
     * Logs a warn message.
     *
     * @param originator The originator of the message.
     * @param msg The log message.
     */
    public void warn(Object originator, LogMessage msg);

    /**
     * Logs an error message.
     *
     * @param originator The originator of the message.
     * @param logMsg The log message.
     */
    public void error(Object originator, String logMsg);

    /**
     * Logs an error message.
     *
     * @param originator The originator of the message.
     * @param msg The log message.
     */
    public void error(Object originator, LogMessage msg);

    /**
     * Logs a fatal message.
     *
     * @param originator The originator of the message.
     * @param logMsg The log message.
     */
    public void fatal(Object originator, String logMsg);

    /**
     * Logs a fatal message.
     *
     * @param originator The originator of the message.
     * @param msg The log message.
     */
    public void fatal(Object originator, LogMessage msg);

    /** 
     * Returns the log file.
     *
     * @return See above.
     */
    public String getLogFile();

}