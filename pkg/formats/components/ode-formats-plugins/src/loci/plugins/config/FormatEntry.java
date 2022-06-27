package loci.plugins.config;

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

import java.awt.Component;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * A list entry for the configuration window's Formats tab.
 */
public class FormatEntry implements Comparable<Object> {

  // -- Fields --

  private Object reader;
  private String formatName;
  protected String readerName;
  protected String[] suffixes;
  protected String[] labels;
  protected Component[] widgets;

  // -- Constructor --

  public FormatEntry(PrintWriter log, Object reader) {
    this.reader = reader;
    Class<?> readerClass = reader.getClass();
    String n = readerClass.getName();
    readerName = n.substring(n.lastIndexOf(".") + 1, n.length() - 6);
    try {
      Method getFormat = readerClass.getMethod("getFormat");
      formatName = (String) getFormat.invoke(this.reader);
      Method getSuffixes = readerClass.getMethod("getSuffixes");
      suffixes = (String[]) getSuffixes.invoke(this.reader);
      log.println("Successfully queried " + readerName + " reader.");
    }
    catch (Throwable t) {
      log.println("Error querying " + readerName + " reader:");
      t.printStackTrace(log);
      log.println();
      suffixes = new String[0];
    }
    // create any extra widgets for this format, if any
    IFormatWidgets fw = null;
    String fwClassName = "loci.plugins.config." + readerName + "Widgets";
    try {
      Class<?> fwClass = Class.forName(fwClassName);
      fw = (IFormatWidgets) fwClass.newInstance();
      log.println("Initialized extra widgets for " + readerName + " reader.");
    }
    catch (Throwable t) {
      if (t instanceof ClassNotFoundException) {
        // no extra widgets for this reader
      }
      else {
        log.println("Error constructing widgets for " +
          readerName + " reader:");
        t.printStackTrace(log);
      }
    }
    labels = fw == null ? new String[0] : fw.getLabels();
    widgets = fw == null ? new Component[0] : fw.getWidgets();
  }

  // -- Comparable API methods --

  @Override
  public int compareTo(Object o) {
    return toString().compareTo(o.toString());
  }

  // -- Object API methods --

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) return false;
    if (!(o instanceof FormatEntry)) return false;
    return compareTo(o) == 0;
  }

  @Override
  public String toString() {
    return "<html>" + formatName;
  }

}
