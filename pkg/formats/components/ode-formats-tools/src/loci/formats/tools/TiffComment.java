package loci.formats.tools;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

import loci.common.Constants;
import loci.common.DataTools;
import loci.common.RandomAccessInputStream;
import loci.common.RandomAccessOutputStream;
import loci.formats.FormatException;
import loci.formats.tiff.TiffParser;
import loci.formats.tiff.TiffSaver;

/**
 * Extracts the comment from the first IFD of the given TIFF file(s).
 */
public class TiffComment {

  public static void main(String[] args) throws FormatException, IOException {
    if (args.length == 0) {
      System.out.println("Usage:");
      System.out.println(
        "tiffcomment [-set comment] [-edit] file1 [file2 ...]");
      System.out.println();

      System.out.println("This tool requires an ImageDescription tag to be " +
        "present in the TIFF file. ");
      System.out.println();
      System.out.println("If using the '-set' option, the new TIFF comment " +
        "must be specified and may take any of the following forms:");
      System.out.println();
      System.out.println("  * the text of the comment, e.g. 'new comment!'");
      System.out.println("  * the name of the file containing the text of " +
        "the comment, e.g. 'file.xml'");
      System.out.println("  * '-', to enter the comment using stdin.  " +
        "Entering a blank line will");
      System.out.println("    terminate reading from stdin.");
      return;
    }

    CommandLineTools.runUpgradeCheck(args);

    // parse flags
    boolean edit = false;
    String newComment = null;
    ArrayList<String> files = new ArrayList<String>();
    for (int i=0; i<args.length; i++) {
      if (!args[i].startsWith("-")) {
        files.add(args[i]);
        continue;
      }

      if (args[i].equals("-edit")) edit = true;
      else if (args[i].equals("-set")) {
        newComment = args[++i];
        if (new File(newComment).exists()) {
          newComment = DataTools.readFile(newComment);
        }
        else if (newComment.equals("-")) {
          newComment = null;
          try (BufferedReader reader = new BufferedReader(
                      new InputStreamReader(System.in, Constants.ENCODING))) {
            String line = reader.readLine();
            while (line != null && line.length() > 0) {
              if (newComment == null) newComment = line;
              else {
                newComment += "\n" + line;
              }
              line = reader.readLine();
            }
          }
        }
      }
      else System.out.println("Warning: unknown flag: " + args[i]);
    }

    // process files
    for (String file : files) {
      if (edit) {
        try (RandomAccessInputStream in = EditTiffG.open(file)) {}
      }
      else if (newComment != null) {
        overwriteComment(file, newComment);
      }
      else {
        try (RandomAccessInputStream in = new RandomAccessInputStream(file)) {
          TiffParser parser = new TiffParser(in);
          String comment = parser.getComment();
          System.out.println(comment == null ?
                file + ": no TIFF comment found." : comment);
        }
      }
    }
  }

  /**
   * Overwrites the comment.
   *
   * @param file The path to the file to handle.
   * @param comment The new comment to write.
   */
  private static void overwriteComment(String file, String comment)
  {
    try (RandomAccessInputStream in = new RandomAccessInputStream(file);
         RandomAccessOutputStream out = new RandomAccessOutputStream(file)) {
      TiffSaver saver = new TiffSaver(out, file);
      saver.overwriteComment(in, comment);
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }
}
