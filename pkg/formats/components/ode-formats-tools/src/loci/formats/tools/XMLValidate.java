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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import loci.common.Constants;
import loci.common.RandomAccessInputStream;
import loci.common.xml.XMLTools;
import loci.formats.in.TiffReader;
import loci.formats.tiff.TiffParser;

/**
 * Attempts to validate the given XML files.
 */
public class XMLValidate {

  public static boolean validate(BufferedReader in, String label)
    throws IOException
  {
    StringBuffer sb = new StringBuffer();
    try {
       while (true) {
        String line = in.readLine();
        if (line == null) break;
        sb.append(line);
      }
    } finally {
      in.close();
    }
    return XMLTools.validateXML(sb.toString(), label);
  }

  @Deprecated
  public static void process(String label, BufferedReader in)
    throws IOException
  {
      validate(in, label);
  }

  public static boolean validate(String file)
    throws IOException
  {
    String[] files = new String[1];
    files[0] = file;
    return validate(files)[0];
  }

  public static boolean[] validate(String[] files)
    throws IOException
  {
    if (files == null || files.length == 0) {
        throw new IllegalArgumentException("No files to validate");
    }
    boolean[] results = new boolean[files.length];
    List<String> extensions = Arrays.asList(TiffReader.TIFF_SUFFIXES);
    for (int i = 0; i < files.length; i++) {
        String file = files[i];
        if (file == null || file.trim().length() == 0) {
          results[i] = false;
        } else{
          String extension = file.substring(file.lastIndexOf(".")+1);
          if (extensions.contains(extension.toLowerCase())) {
            String comment = "";
            try (RandomAccessInputStream stream = new RandomAccessInputStream(file)) {
              comment = new TiffParser(stream).getComment();
            }
            results[i] = validate(new BufferedReader(new StringReader(comment)), file);
          } else {
            results[i] = validate(new BufferedReader(new InputStreamReader(
                      new FileInputStream(file), Constants.ENCODING)), file);
          }
        }
    }
    return results;
  }

  public static void main(String[] args) throws Exception {
    CommandLineTools.runUpgradeCheck(args);

    boolean result = true;
    if (args.length == 0) {
      // read from stdin
      result = validate(new BufferedReader(
                new InputStreamReader(System.in, Constants.ENCODING)), "<stdin>");
    }
    else {
      // read from file(s)
      boolean[] results = validate(args);
      int count = 0;
      for (int i = 0; i < results.length; i++) {
        if (results[i]) {
          count++;
        }
      }
      //Check if all files are valid
      result = (count == results.length);
    }
    if (result) {
      System.exit(0);
    } else {
      System.exit(1); 
    }
  }

}
