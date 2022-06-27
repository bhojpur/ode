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

import loci.common.RandomAccessInputStream;
import loci.formats.tiff.TiffParser;
import loci.formats.tiff.TiffSaver;

/**
 * Performs "surgery" on a TIFF ImageDescription comment, particularly the
 * ODE-XML comment found in ODE-TIFF files. Note that this code must be
 * tailored to a specific need by editing the commented out code below to
 * make desired alterations to the comment.
 */
public class CommentSurgery {
  public static void main(String[] args) throws Exception {
    // the -test flag will print proposed changes to stdout
    // rather than actually changing the comment
    boolean test = args[0].equals("-test");

    for (int i=0; i<args.length; i++) {
      String id = args[i];
      if (!test) System.out.println(id + ": ");
      String xml = new TiffParser(id).getComment();
      if (xml == null) {
        System.out.println("ERROR: No ODE-XML comment.");
        return;
      }
      int len = xml.length();
      // do something to the comment; e.g.:
      //xml = xml.replaceAll("LogicalChannel:OWS", "LogicalChannel:OWS347-");

      if (test) System.out.println(xml);
      else {
        System.out.println(len + " -> " + xml.length());
        TiffSaver saver = new TiffSaver(id);
        RandomAccessInputStream in = new RandomAccessInputStream(id);
        saver.overwriteComment(in, xml);
        in.close();
        saver.close();
      }
    }
  }
}
