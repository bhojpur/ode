package loci.formats.gui;

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
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import loci.common.Constants;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * A window containing syntax highlighted XML as a tree.
 */
public class XMLWindow extends JFrame {

  // -- Fields --

  private Document doc;

  // -- Constructors --

  public XMLWindow() {
    super();
  }

  public XMLWindow(String title) {
    super(title);
  }

  // -- XMLWindow methods --

  /** Displays XML from the given string. */
  public void setXML(String xml)
    throws ParserConfigurationException, SAXException, IOException
  {
    setDocument(null);

    // parse XML from string into DOM structure
    DocumentBuilderFactory docFact = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = docFact.newDocumentBuilder();
    ByteArrayInputStream is =
      new ByteArrayInputStream(xml.getBytes(Constants.ENCODING));
    Document doc = db.parse(is);
    is.close();

    setDocument(doc);
  }

  /** Displays XML from the given file. */
  public void setXML(File file)
    throws ParserConfigurationException, SAXException, IOException
  {
    setDocument(null);

    // parse XML from file into DOM structure
    DocumentBuilderFactory docFact = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = docFact.newDocumentBuilder();
    Document doc = db.parse(file);

    setDocument(doc);
  }

  /** Displays XML from the given document. */
  public void setDocument(Document doc) {
    this.doc = doc;
    getContentPane().removeAll();
    if (doc == null) setVisible(false);
    else {
      // populate metadata window and size intelligently
      JTree tree = XMLCellRenderer.makeJTree(doc);
      for (int i=0; i<tree.getRowCount(); i++) tree.expandRow(i);
      getContentPane().add(new JScrollPane(tree));
      pack();
      Dimension dim = getSize();
      final int pad = 20;
      dim.width += pad;
      dim.height += pad;
      Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
      int maxWidth = 3 * ss.width / 4;
      int maxHeight = 3 * ss.height / 4;
      if (dim.width > maxWidth) dim.width = maxWidth;
      if (dim.height > maxHeight) dim.height = maxHeight;
      setSize(dim);
    }
  }

  /** Gets the XML document currently being displayed within the window. */
  public Document getDocument() {
    return doc;
  }

  // -- Main method --

  public static void main(String[] args) throws Exception {
    XMLWindow xmlWindow = new XMLWindow();
    xmlWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    if (args.length > 0) {
      String filename = args[0];
      xmlWindow.setXML(new File(filename));
      xmlWindow.setTitle("XML Window - " + filename);
    }
    else {
      BufferedReader in = new BufferedReader(
        new InputStreamReader(System.in, Constants.ENCODING));
      final StringBuilder sb = new StringBuilder();
      while (true) {
        String line = in.readLine();
        if (line == null) break;
        sb.append(line);
        sb.append("\n");
      }
      xmlWindow.setXML(sb.toString());
      xmlWindow.setTitle("XML Window - <stdin>");
    }
    xmlWindow.setLocation(200, 200);
    xmlWindow.setVisible(true);
  }

}
