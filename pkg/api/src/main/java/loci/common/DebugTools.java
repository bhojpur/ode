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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.LinkageError;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * A utility class with convenience methods for debugging.
 */
public final class DebugTools {

  final static String[][] TOOL_CLASSES = new String[][] {
    new String[] {"loci.common.", "LogbackTools"},
    new String[] {"loci.common.", "Log4jTools"}
  };

  // -- Constructor --
  private DebugTools() { }

  // -- DebugTools methods --

  /**
   * Extracts the given exception's corresponding stack trace to a string.
   *
   * @param t the Throwable from which to extract a stack trace
   * @return the complete stack trace as a String
   */
  public static String getStackTrace(Throwable t) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      t.printStackTrace(new PrintStream(out, false, Constants.ENCODING));
      return new String(out.toByteArray(), Constants.ENCODING);
    }
    catch (IOException e) { }
    return null;
  }

  /**
   * Checks whether SLF4J logging has been enabled via logback or log4j.
   *
   * @return {@code true} if logging has been successfully enabled
   */
  public static synchronized boolean isEnabled() {
    for (String[] toolClass : TOOL_CLASSES) {
      try {
        Class<?> k = Class.forName(toolClass[0] + toolClass[1]);
        Method m = k.getMethod("isEnabled");
        return (Boolean) m.invoke(null);
      }
      catch (ReflectiveOperationException|LinkageError t) {
        // no-op. Ignore error and try the next class.
      }
    }
    return false;
  }

  /**
   * Sets the root logger level.
   *
   * This method will override the root logger level independently of the way
   * the logging framework has been enabled.
   *
   * @param level A string indicating the desired level
   */
  public static synchronized void setRootLevel(String level) {
    for (String[] toolClass : TOOL_CLASSES) {
      try {
        Class<?> k = Class.forName(toolClass[0] + toolClass[1]);
        Method m = k.getMethod("setRootLevel", String.class);
        m.invoke(null, level);
        return;
      }
      catch (ReflectiveOperationException|LinkageError t) {
        // no-op. Ignore error and try the next class.
      }
    }
    return;
  }

  /**
   * Attempts to enable SLF4J logging via logback or log4j.
   *
   * This will first check whether the logging has been enabled using the
   * return value of {@link #isEnabled()}.
   *
   * @return {@code true} if logging was successfully enabled by this method
   */
  public static synchronized boolean enableLogging() {
    if (isEnabled()) return false;
    for (String[] toolClass : TOOL_CLASSES) {
      try {
        Class<?> k = Class.forName(toolClass[0] + toolClass[1]);
        Method m = k.getMethod("enableLogging");
        m.invoke(null);
        return true;
      }
      catch (ReflectiveOperationException|LinkageError t) {
        // no-op. Ignore error and try the next class.
      }
    }
    return false;
  }
  
  /**
   * Attempts to enable SLF4J logging and set the root logger level.
   *
   * This method will first try to initialize the logging using
   * {@link #enableLogging()}. If this method returns {@code true}, the root
   * logger level is also set via {@link #setRootLevel(String)} using the
   * input level.
   *
   * @param level A string indicating the desired level
   * @return {@code true} if logging was successfully enabled by this method
   */
  public static synchronized boolean enableLogging(String level) {
    boolean status = enableLogging();
    if (status) setRootLevel(level);
    return status;
  }

  /**
   * Enable SLF4J logging using logback, in the context of ImageJ.
   *
   * This allows logging events to be echoed to the ImageJ status bar,
   * regardless of how the logging configuration file was set up.
   *
   * @param debug true if debug-level output should be shown
   * @return whether or not ImageJ log enabling was successful
   */
  public static synchronized boolean enableIJLogging(boolean debug) {
    ReflectedUniverse r = new ReflectedUniverse();
    try {
      r.exec("import loci.common.LogbackTools");
      r.exec("import loci.plugins.util.IJStatusEchoer");
      r.exec("appender = new IJStatusEchoer()");
      r.setVar("debug", debug);
      r.exec("LogbackTools.enableIJLogging(debug, appender)");
    }
    catch (ReflectException exc) {
      return false;
    }
    return true;
  }

  /**
   * This method uses reflection to scan the values of the given class's
   * static fields, returning the first matching field's name.
   *
   * @param c the class to scan
   * @param value the int value of the field to find
   * @return the name of the field, or the string representation of
   *         <code>value</code> if a matching field is not found
   */
  public static String getFieldName(Class<?> c, int value) {
    Field[] fields = c.getDeclaredFields();
    for (int i=0; i<fields.length; i++) {
      if (!Modifier.isStatic(fields[i].getModifiers())) continue;
      fields[i].setAccessible(true);
      try {
        if (fields[i].getInt(null) == value) return fields[i].getName();
      }
      catch (IllegalAccessException exc) { }
      catch (IllegalArgumentException exc) { }
    }
    return "" + value;
  }

}