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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import loci.common.Constants;

/**
 * Drag and drop transfer handler for ODE-Formats Plugins Shortcut Window.
 */
public class ShortcutTransferHandler extends TransferHandler {

  // -- Fields --

  /** Associated shortcut panel. */
  protected ShortcutPanel shortcutPanel;

  // -- Constructor --

  /** Constructs a new shortcut panel drag and drop transfer handler. */
  public ShortcutTransferHandler(ShortcutPanel shortcutPanel) {
    this.shortcutPanel = shortcutPanel;
  }

  // -- TransferHandler API methods --

  @Override
  public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
    return true;
  }

  @Override
  public boolean importData(JComponent comp, Transferable t) {
    try {
      // search for compatible data flavors (lists, files and strings)
      DataFlavor[] flavors = t.getTransferDataFlavors();
      int fileIndex = -1, stringIndex = -1, listIndex = -1;
      for (int i=0; i<flavors.length; i++) {
        if (fileIndex >= 0 && stringIndex >= 0 && listIndex >= 0) break;
        Class<?> c = flavors[i].getRepresentationClass();
        if (fileIndex < 0 && c == File.class) fileIndex = i;
        if (stringIndex < 0 && c == String.class) stringIndex = i;
        if (listIndex < 0 && c == List.class) listIndex = i;
      }
      // convert data into list of objects
      List<?> list = null;
      if (listIndex >= 0) {
        list = (List<?>) t.getTransferData(flavors[listIndex]);
      }
      else if (fileIndex >= 0) {
        File f = (File) t.getTransferData(flavors[fileIndex]);
        list = Arrays.asList(f);
      }
      else if (stringIndex >= 0) {
        String s = (String) t.getTransferData(flavors[stringIndex]);
        list = Arrays.asList(s.split("[ \t\n\r\f]"));
        //StringTokenizer st = new StringTokenizer(s);
        //while (st.hasMoreTokens()) list.add(st.nextToken());
      }
      if (list == null) {
        // no compatible data flavors found
        return false;
      }
      // process each item on the list
      final String[] ids = new String[list.size()];
      for (int i=0; i<ids.length; i++) {
        Object item = list.get(i);
        String id = null;
        if (item instanceof File) {
          File f = (File) item;
          id = f.getAbsolutePath();
        }
        else if (item instanceof String) {
          id = (String) item;
        }
        if (id == null) {
          System.err.println("Warning: ignoring item #" + i + ": " + item);
        }
        else {
          // convert "file://" URLs into path names
          ids[i] = id.replaceAll("^file:/*", "/");
          if (!new File(ids[i]).exists()) {
            ids[i] = URLDecoder.decode(ids[i], Constants.ENCODING);
          }
        }
      }

      // open each item
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          for (int i=0; i<ids.length; i++) shortcutPanel.open(ids[i]);
        }
      });
    }
    catch (UnsupportedFlavorException e) {
      e.printStackTrace();
      // dump list of supported flavors, for debugging
      DataFlavor[] df = t.getTransferDataFlavors();
      System.err.println("Supported flavors:");
      for (int i=0; i<df.length; i++) {
        System.err.println("\t#" + i + ": " + df[i]);
      }
      ij.IJ.error(e.toString());
      return false;
    }
    catch (IOException e) {
      e.printStackTrace();
      ij.IJ.error(e.toString());
      return false;
    }
    return true;
  }

}
