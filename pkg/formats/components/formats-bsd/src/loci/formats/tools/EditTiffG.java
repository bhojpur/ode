/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.tools;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import loci.common.Constants;
import loci.common.RandomAccessInputStream;
import loci.common.RandomAccessOutputStream;
import loci.formats.FormatException;
import loci.formats.tiff.TiffParser;
import loci.formats.tiff.TiffSaver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a GUI for editing TIFF file comments.
 */
public class EditTiffG extends JFrame implements ActionListener {

  // -- Constants --

  private static final String TITLE = "EditTiffG";

  private static final Logger LOGGER = LoggerFactory.getLogger(EditTiffG.class);

  // -- Fields --

  private JTextArea textArea;
  private JFileChooser fileBox;
  private File file;

  // -- Constructor --

  public EditTiffG() {
    setTitle(TITLE);
    setLayout(new BorderLayout());
    textArea = new JTextArea(25, 80);
    add(new JScrollPane(textArea), BorderLayout.CENTER);
    //textArea.setWrapStyleWord(true);
    textArea.setLineWrap(true);

    JMenuBar menubar = new JMenuBar();
    setJMenuBar(menubar);

    JMenu file = new JMenu("File");
    menubar.add(file);

    JMenuItem fileOpen = new JMenuItem("Open");
    file.add(fileOpen);
    fileOpen.addActionListener(this);
    fileOpen.setActionCommand("open");

    JMenuItem fileSave = new JMenuItem("Save");
    file.add(fileSave);
    fileSave.addActionListener(this);
    fileSave.setActionCommand("save");

    JMenuItem fileExit = new JMenuItem("Exit");
    file.add(fileExit);
    fileExit.addActionListener(this);
    fileExit.setActionCommand("exit");

    fileBox = new JFileChooser(System.getProperty("user.dir"));

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    pack();
    setVisible(true);
  }

  // -- EditTiffG methods --

  public String getXML() {
    return textArea.getText();
  }

  public void setXML(String xml) {
    textArea.setText(xml);
  }

  public void open() {
    int rval = fileBox.showOpenDialog(this);
    if (rval != JFileChooser.APPROVE_OPTION) return;
    File f = fileBox.getSelectedFile();
    openFile(f);
  }

  public void save() {
    saveFile(file);
  }

  public void exit() {
    System.exit(0);
  }

  public void openFile(File f) {
    try {
      String id = f.getAbsolutePath();
      String xml = new TiffParser(id).getComment();
      setXML(xml);
      file = f;
      setTitle(TITLE + " - " + id);
    }
    catch (IOException exc) {
      showError(exc);
    }
  }

  public void openFile(File f, RandomAccessInputStream in) {
    try {
      String id = f.getAbsolutePath();
      String xml = new TiffParser(in).getComment();
      setXML(xml);
      file = f;
      setTitle(TITLE + " - " + id);
    }
    catch (IOException exc) {
      showError(exc);
    }
  }
 
  public void saveFile(File f) {
      String path = f.getAbsolutePath();
    try (RandomAccessInputStream in = new RandomAccessInputStream(path);
         RandomAccessOutputStream out = new RandomAccessOutputStream(path)){
      String xml = getXML();
      TiffSaver saver = new TiffSaver(out, path);
      saver.overwriteComment(in, xml);
    }
    catch (FormatException exc) {
      showError(exc);
    }
    catch (IOException exc) {
      showError(exc);
    }
  }

  public void showError(Throwable t) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String error = "";
    try {
      t.printStackTrace(
        new PrintWriter(new OutputStreamWriter(out, Constants.ENCODING)));
      error = new String(out.toByteArray(), Constants.ENCODING);
    }
    catch (UnsupportedEncodingException e) {
      LOGGER.warn("Failed to show error", e);
    } finally {
      try {
        out.close();
      } catch (Exception ex) {}
    }
    JOptionPane.showMessageDialog(this, "Sorry, there was an error: " + error,
      TITLE, JOptionPane.ERROR_MESSAGE);
  }

  @Deprecated
  public static void openFile(String filename) {
    EditTiffG etg = new EditTiffG();
    File f = new File(filename);
    if (f.exists()) etg.openFile(f);
  }

  public static RandomAccessInputStream open(String filename)
    throws IOException {
    EditTiffG etg = new EditTiffG();
    File f = new File(filename);
    if (f.exists()) {
      RandomAccessInputStream in = new RandomAccessInputStream(filename);
      etg.openFile(f, in);
      return in;
    }
    return null;
  }
  // -- ActionListener methods --

  @Override
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    if ("open".equals(cmd)) open();
    else if ("save".equals(cmd)) save();
    else if ("exit".equals(cmd)) exit();
  }

  // -- Main method --

  public static void main(String[] args) throws Exception {
    try (RandomAccessInputStream in = EditTiffG.open(args[0])) {
    }
  }

}
