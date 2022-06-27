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

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;

import loci.formats.cache.Cache;
import loci.formats.gui.CacheComponent;

/**
 * Extension of JFrame that allows the user to adjust caching settings.
 */
public class BrowserOptionsWindow extends JFrame {

  // -- Constructor --

  public BrowserOptionsWindow(String title, Cache cache, String[] axes) {
    super(title);

    CacheComponent panel = new CacheComponent(cache, axes);

    panel.setBorder(new EmptyBorder(15, 15, 15, 15));
    panel.setMinimumSize(new Dimension(300, 500));
    setContentPane(panel);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    pack();
  }

}
