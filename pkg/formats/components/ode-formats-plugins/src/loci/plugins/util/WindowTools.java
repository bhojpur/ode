package loci.plugins.util;

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

import ij.IJ;
import ij.ImageJ;
import ij.gui.GenericDialog;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.List;
import java.util.StringTokenizer;

import loci.common.DebugTools;
import loci.plugins.BF;

/**
 * Utility methods for managing ImageJ dialogs and windows.
 */
public final class WindowTools {

  // -- Constructor --

  private WindowTools() { }

  // -- Utility methods --

  /** Adds AWT scroll bars to the given container. */
  public static void addScrollBars(Container pane) {
    GridBagLayout layout = (GridBagLayout) pane.getLayout();

    // extract components
    int count = pane.getComponentCount();
    Component[] c = new Component[count];
    GridBagConstraints[] gbc = new GridBagConstraints[count];
    for (int i=0; i<count; i++) {
      c[i] = pane.getComponent(i);
      gbc[i] = layout.getConstraints(c[i]);
    }

    // clear components
    pane.removeAll();
    layout.invalidateLayout(pane);

    // create new container panel
    Panel newPane = new Panel();
    GridBagLayout newLayout = new GridBagLayout();
    newPane.setLayout(newLayout);
    for (int i=0; i<count; i++) {
      newLayout.setConstraints(c[i], gbc[i]);
      newPane.add(c[i]);
    }

    // HACK - get preferred size for container panel
    // NB: don't know a better way:
    // - newPane.getPreferredSize() doesn't work
    // - newLayout.preferredLayoutSize(newPane) doesn't work
    Frame f = new Frame();
    f.setLayout(new BorderLayout());
    f.add(newPane, BorderLayout.CENTER);
    f.pack();
    final Dimension size = newPane.getSize();
    f.remove(newPane);
    f.dispose();

    // compute best size for scrollable viewport
    size.width += 25;
    size.height += 15;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    int maxWidth = 7 * screen.width / 8;
    int maxHeight = 3 * screen.height / 4;
    if (size.width > maxWidth) size.width = maxWidth;
    if (size.height > maxHeight) size.height = maxHeight;

    // create scroll pane
    ScrollPane scroll = new ScrollPane() {
      @Override
      public Dimension getPreferredSize() {
        return size;
      }
    };
    scroll.add(newPane);

    // add scroll pane to original container
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    constraints.fill = GridBagConstraints.BOTH;
    constraints.weightx = 1.0;
    constraints.weighty = 1.0;
    layout.setConstraints(scroll, constraints);
    pane.add(scroll);
  }

  /**
   * Places the given window at a nice location on screen, either centered
   * below the ImageJ window if there is one, or else centered on screen.
   */
  public static void placeWindow(Window w) {
    Dimension size = w.getSize();

    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    ImageJ ij = IJ.getInstance();

    Point p = new Point();

    if (ij == null) {
      // center config window on screen
      p.x = (screen.width - size.width) / 2;
      p.y = (screen.height - size.height) / 2;
    }
    else {
      // place config window below ImageJ window
      Rectangle ijBounds = ij.getBounds();
      p.x = ijBounds.x + (ijBounds.width - size.width) / 2;
      p.y = ijBounds.y + ijBounds.height + 5;
    }

    // nudge config window away from screen edges
    final int pad = 10;
    if (p.x < pad) p.x = pad;
    else if (p.x + size.width + pad > screen.width) {
      p.x = screen.width - size.width - pad;
    }
    if (p.y < pad) p.y = pad;
    else if (p.y + size.height + pad > screen.height) {
      p.y = screen.height - size.height - pad;
    }

    w.setLocation(p);
  }

  /** Reports the given exception with stack trace in an ImageJ error dialog. */
  public static void reportException(Throwable t) {
    reportException(t, false, null);
  }

  /** Reports the given exception with stack trace in an ImageJ error dialog. */
  public static void reportException(Throwable t, boolean quiet) {
    reportException(t, quiet, null);
  }

  /** Reports the given exception with stack trace in an ImageJ error dialog. */
  public static void reportException(Throwable t, boolean quiet, String msg) {
    if (quiet) return;
    BF.status(quiet, "");
    if (t != null) {
      String s = DebugTools.getStackTrace(t);
      StringTokenizer st = new StringTokenizer(s, "\n\r");
      while (st.hasMoreTokens()) IJ.log(st.nextToken());
    }
    if (msg != null) IJ.error("ODE-Formats Importer", msg);
  }

  @SuppressWarnings("unchecked")
  public static List<TextField> getNumericFields(GenericDialog gd) {
    return gd.getNumericFields();
  }

  @SuppressWarnings("unchecked")
  public static List<Checkbox> getCheckboxes(GenericDialog gd) {
    return gd.getCheckboxes();
  }

  @SuppressWarnings("unchecked")
  public static List<Choice> getChoices(GenericDialog gd) {
    return gd.getChoices();
  }

}
