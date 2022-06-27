package loci.plugins.shortcut;

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
import ij.plugin.PlugIn;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import loci.common.Constants;
import loci.plugins.util.WindowTools;

/**
 * A plugin that displays a small window containing shortcuts to the
 * plugins, including the ODE-Formats Importer, ODE-Formats Exporter,
 * Stack Colorizer and Stack Slicer.
 *
 * Files dragged and dropped onto the window will be opened using the
 * ODE-Formats Importer plugin.
 */
public class ShortcutPanel extends JPanel implements ActionListener, PlugIn {

  // -- Constants --

  /** Name of plugin to use for opening dropped files. */
  protected static final String OPENER_PLUGIN = "ODE-Formats Importer";

  /** Name of this plugin, ignored when parsing the plugins list. */
  protected static final String SHORTCUT_PLUGIN =
    "ODE-Formats Plugins Shortcut Window";

  /** Name of the 'ODE-Formats' submenu. */
  protected static final String NORMAL_MENU = "Plugins>ODE-Formats";

  /** Name of the 'About Plugins' submenu. */
  protected static final String HELP_MENU = "Help>About Plugins";

  /** Number of pixels for window border. */
  protected static final int BORDER = 10;

  // -- Fields --

  /** Index of plugin to use to open dropped files. */
  protected static int openerIndex;

  /** Details for installed plugins. */
  protected static String[] names, plugins, args;

  static {
    // load list of plugins
    int index = -1;
    ArrayList<String> vNames = new ArrayList<String>();
    ArrayList<String> vPlugins = new ArrayList<String>();
    ArrayList<String> vArgs = new ArrayList<String>();

    // read from configuration file
    try {
      URL url = ShortcutPanel.class.getResource("ShortcutPanel.class");
      String path = url.toString();
      path = path.substring(0, path.indexOf('!')) + "!/plugins.config";
      url = new URL(path);
      try (BufferedReader in = new BufferedReader(
        new InputStreamReader(url.openStream(), Constants.ENCODING))) {
        while (true) {
          String line = in.readLine();
          if (line == null) break;

          // determine plugin type
          boolean normal = line.startsWith(NORMAL_MENU);
          boolean help = line.startsWith(HELP_MENU);
          if (!normal && !help) continue;

          // parse plugin information
          int quote1 = line.indexOf("\"");
          if (quote1 < 0) continue;
          int quote2 = line.indexOf("\"", quote1 + 1);
          if (quote2 < 0) continue;
          int quote3 = line.indexOf("\"", quote2 + 1);
          if (quote3 < 0) continue;
          int quote4 = line.indexOf("\"", quote3 + 1);
          if (quote4 < 0) continue;
          String name = line.substring(quote1 + 1, quote2);
          if (help) name = "About " + name.substring(0, name.length() - 3);
          String plugin = line.substring(quote2 + 2, quote3 - 1).trim();
          String arg = line.substring(quote3 + 1, quote4);
          if (name.equals(OPENER_PLUGIN)) index = vNames.size();
          if (!name.equals(SHORTCUT_PLUGIN)) {
            vNames.add(name);
            vPlugins.add(plugin);
            vArgs.add(arg);
          }
        }
      }
    }
    catch (IOException exc) {
      exc.printStackTrace();
    }

    openerIndex = index;
    names = vNames.toArray(new String[0]);
    plugins = vPlugins.toArray(new String[0]);
    args = vArgs.toArray(new String[0]);
  }

  // -- Constructor --

  /** Constructs a shortcut panel. */
  public ShortcutPanel() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(new EmptyBorder(BORDER, BORDER, BORDER, BORDER));
    setTransferHandler(new ShortcutTransferHandler(this));
    JButton[] b = new JButton[names.length];
    Dimension prefSize = new Dimension(-1, -1);
    Dimension maxSize = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    for (int i=0; i<names.length; i++) {
      b[i] = new JButton(names[i]);
      Dimension d = b[i].getPreferredSize();
      if (d.width > prefSize.width) prefSize.width = d.width;
      if (d.height > prefSize.height) prefSize.height = d.height;
    }
    for (int i=0; i<b.length; i++) {
      b[i].setPreferredSize(prefSize);
      b[i].setMaximumSize(maxSize);
      b[i].addActionListener(this);
      add(b[i]);
    }
  }

  // -- ShortcutPanel API methods --

  /**
   * Opens the given location (e.g., file)
   * with the ODE-Formats Importer plugin.
   */
  public void open(String id) {
    String arg = args[openerIndex] + "open=[" + id + "] ";
    runPlugIn(plugins[openerIndex], arg);
  }

  /** Executes the given plugin, in a separate thread. */
  public static void runPlugIn(final String className, final String arg) {
    // NB: If we don't run in a separate thread, there are GUI update
    //     problems with the ImageJ status bar and log window.
    new Thread() {
      @Override
      public void run() {
        try {
          IJ.runPlugIn(className, arg);
        }
        catch (Throwable t) {
          WindowTools.reportException(t);
        }
      }
    }.start();
  }

  // -- ActionListener API methods --

  /** Handles button presses. */
  @Override
  public void actionPerformed(ActionEvent e) {
    JButton b = (JButton) e.getSource();
    String name = b.getText();
    for (int i=0; i<names.length; i++) {
      if (name.equals(names[i])) {
        runPlugIn(plugins[i], args[i]);
        break;
      }
    }
  }

  // -- PlugIn API methods --

  /** Executes the plugin. */
  @Override
  public void run(String arg) {
    JFrame frame = new JFrame("ODE-Formats Plugins Shortcut Window");
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setContentPane(new ShortcutPanel());
    frame.pack();
    WindowTools.placeWindow(frame);
    frame.setVisible(true);
  }

}
