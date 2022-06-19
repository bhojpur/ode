package loci.common;

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

import java.util.Enumeration;

/**
 * A utility class with convenience methods for log4j.
 */
public final class Log4jTools {

  // -- Constructor --

  private Log4jTools() { }

  /**
   * Checks whether the log4j framework was successfully enabled
   *
   * @return {@code true} if logging was successfully enabled
   */
  public static synchronized boolean isEnabled() {
    try {
      ReflectedUniverse r = new ReflectedUniverse();
      r.exec("import org.apache.log4j.Level");
      r.exec("import org.apache.log4j.Logger");
      r.exec("root = Logger.getRootLogger()");
      Enumeration en = (Enumeration) r.exec("root.getAllAppenders()");
      return en.hasMoreElements();
    } catch (ReflectException exc) {
      return false;
    }
  }

  /**
   * Sets the level of the root logger
   *
   * @param level A string indicating the desired level
   *   (i.e.: ALL, DEBUG, ERROR, FATAL, INFO, OFF, TRACE, WARN).
   */
  public static synchronized void setRootLevel(String level) {
    try {
      ReflectedUniverse r = new ReflectedUniverse();
      r.exec("import org.apache.log4j.Level");
      r.exec("import org.apache.log4j.Logger");
      r.exec("root = Logger.getRootLogger()");
      r.exec("root.setLevel(Level." + level + ")");
    } catch (ReflectException exc) {
      return;
    }
    return;
  }
  
  /**
   * Attempts to enable SLF4J logging via log4j
   * without an external configuration file.
   *
   * @return {@code true} if logging was successfully enabled
   */
  public static synchronized boolean enableLogging() {
    try {
      ReflectedUniverse r = new ReflectedUniverse();
      r.exec("import org.apache.log4j.Level");
      r.exec("import org.apache.log4j.Logger");
      r.exec("root = Logger.getRootLogger()");
      Enumeration en = (Enumeration) r.exec("root.getAllAppenders()");
      if (!en.hasMoreElements()) {
        // no appenders yet; attach a simple console appender
        r.exec("import org.apache.log4j.ConsoleAppender");
        r.exec("import org.apache.log4j.PatternLayout");
        r.setVar("pattern", "%m%n");
        r.exec("layout = new PatternLayout(pattern)");
        r.exec("appender = new ConsoleAppender(layout)");
        r.exec("root.addAppender(appender)");
      }
    } catch (ReflectException exc) {
      return false;
    }
    return true;
  }
}