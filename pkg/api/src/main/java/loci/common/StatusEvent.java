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

/**
 * An event indicating a status update.
 */
public class StatusEvent {

  // -- Fields --

  /** Current progress value. */
  protected int progress;

  /** Current progress maximum. */
  protected int maximum;

  /** Current status message. */
  protected String status;

  /** Whether or not this is a warning event. */
  protected boolean warning;

  // -- Constructor --

  /**
   * Constructs a non-warning status event.
   * The initial progress and maximum progress are set to -1.
   *
   * @param message the initial status message
   */
  public StatusEvent(String message) {
    this(-1, -1, message);
  }

  /**
   * Constructs a status event.
   * The initial progress and maximum progress are set to -1.
   *
   * @param message the initial status message
   * @param warn true if this is a warning event
   */
  public StatusEvent(String message, boolean warn) {
    this(-1, -1, message, warn);
  }

  /**
   * Constructs a non-warning status event.
   *
   * @param progress the current progress value
   * @param maximum the maximum progress value
   * @param message the initial status message
   */
  public StatusEvent(int progress, int maximum, String message) {
    this(progress, maximum, message, false);
  }

  /**
   * Constructs a status event.
   *
   * @param progress the current progress value
   * @param maximum the maximum progress value
   * @param message the initial status message
   * @param warn true if this is a warning event
   */
  public StatusEvent(int progress, int maximum, String message, boolean warn) {
    this.progress = progress;
    this.maximum = maximum;
    status = message;
    warning = warn;
  }

  // -- StatusEvent API methods --

  /**
   * @return the progress value. Returns -1 if progress is unknown.
   */
  public int getProgressValue() {
    return progress;
  }

  /**
   * @return progress maximum. Returns -1 if progress is unknown.
   */
  public int getProgressMaximum() {
    return maximum;
  }

  /**
   * @return status message.
   */
  public String getStatusMessage() {
    return status;
  }

  /**
   * @return true if this is a warning event.
   */
  public boolean isWarning() {
    return warning;
  }

  // -- Object API methods --

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Status");
    sb.append(": progress=" + progress);
    sb.append(", maximum=" + maximum);
    sb.append(", warning=" + warning);
    sb.append(", status='" + status + "'");
    return sb.toString();
  }

}