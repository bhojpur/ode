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

import java.io.*;
import java.util.Hashtable;
import loci.common.RandomAccessInputStream;
import loci.formats.tiff.*;

/**
 * Allows raw user TIFF comment editing for the given TIFF files.
 */
public class EditTiffComment {

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.out.println("Usage: java EditTiffComment file1 file2 ...");
      return;
    }
    BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
    for (int i=0; i<args.length; i++) {
      String f = args[i];
      // read comment
      System.out.println("Reading " + f + " ");
      String comment = new TiffParser(f).getComment();
      // or if you already have the file open for random access, you can use:
      // RandomAccessInputStream fin = new RandomAccessInputStream(f);
      // TiffParser tiffParser = new TiffParser(fin);
      // String comment = tiffParser.getComment();
      // fin.close();
      System.out.println("[done]");
      // display comment, and prompt for changes
      System.out.println("Comment =");
      System.out.println(comment);
      System.out.println("Enter new comment (no line breaks):");
      String xml = cin.readLine();
      System.out.print("Saving " + f);
      // save results back to the TIFF file
      TiffSaver saver = new TiffSaver(f);
      RandomAccessInputStream in = new RandomAccessInputStream(f);
      saver.overwriteComment(in, xml);
      in.close();
      saver.close();
      System.out.println(" [done]");
    }
  }

}
