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

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;


/**
 * A utility class with convenience methods for logback.
 */
public final class LogbackTools {

  // -- Constructor --

  private static final String CALLER = "Bio-Formats";

  private LogbackTools() { }

  /**
   * Checks whether logback has been enabled.
   *
   * This method will check if the root logger has been initialized via either
   * a configuration file or a previous call to {@link #enableLogging()}. The
   * logger context property will be used to discriminate the latter case from
   * other initializations.
   *
   * @return {@code true} if logging was successfully enabled
   */
  public static synchronized boolean isEnabled() {
    Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    LoggerContext loggerContext = root.getLoggerContext();
    return (ConfigurationWatchListUtil.getMainWatchURL(loggerContext) != null
            || (loggerContext.getProperty("caller") == CALLER));
  }

  /**
   * Sets the level of the root logger
   *
   * @param level A string indicating the desired level
   *   (i.e.: ALL, DEBUG, ERROR, FATAL, INFO, OFF, WARN).
   */
  public static synchronized void setRootLevel(String level) {
    Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(Level.toLevel(level));
  }

  /**
   * Initializes logback without an external configuration file.
   *
   * The logging initialization also sets a logger context property to record
   * the initalization provenance.
   *
   * @return {@code true} if logging was successfully enabled
   */
  public static synchronized boolean enableLogging() {
    Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    LoggerContext context = root.getLoggerContext();
    if (!root.iteratorForAppenders().hasNext()) {
      context.reset();
      context.putProperty("caller", CALLER);
      PatternLayoutEncoder layout = new PatternLayoutEncoder();
      layout.setContext(context);
      layout.setPattern("%m%n");
      layout.start();

      ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<ILoggingEvent>();
      appender.setContext(context);
      appender.setEncoder(layout);
      appender.start();
      root.addAppender(appender);
    }
    else {
      Appender defaultAppender = root.iteratorForAppenders().next();
      if (defaultAppender instanceof ConsoleAppender) {
        context.reset();
        context.putProperty("caller", CALLER);

        PatternLayoutEncoder layout = new PatternLayoutEncoder();
        layout.setContext(context);
        layout.setPattern("%m%n");
        layout.start();

        defaultAppender.setContext(context);
        ((ConsoleAppender) defaultAppender).setEncoder(layout);
        defaultAppender.start();
        root.addAppender(defaultAppender);
      }
    }
    return true;
  }

  public static synchronized void enableIJLogging(boolean debug,
    Appender<ILoggingEvent> appender) {
    try {

      Object logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
      if (!(logger instanceof Logger)) return;

      Logger root = (Logger) logger;

      if (debug) {
        root.setLevel(Level.DEBUG);
      }
      appender.setContext(root.getLoggerContext());
      root.addAppender(appender);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}