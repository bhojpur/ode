/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.gui;

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
