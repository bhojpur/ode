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

import java.io.File;
import java.io.FileWriter;

import loci.common.RandomAccessInputStream;
import loci.formats.in.FlexReader;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffParser;

/**
 * Convenience method to extract the metadata from
 * all the Flex files present in a directory.
 */
public class ExtractFlexMetadata {

  public static void main(String[] args) throws Exception {
    File dir;
    if (args.length != 1 || !(dir = new File(args[0])).canRead()) {
      System.out.println("Usage: java ExtractFlexMetadata dir");
      return;
    }
    for (File file:dir.listFiles()) {
      if (file.getName().endsWith(".flex")) {
        String id = file.getPath();
        int dot = id.lastIndexOf(".");
        String outId = (dot >= 0 ? id.substring(0, dot) : id) + ".xml";
        RandomAccessInputStream in = new RandomAccessInputStream(id);
        TiffParser parser = new TiffParser(in);
        IFD firstIFD = parser.getMainIFDs().get(0);
        String xml = firstIFD.getIFDTextValue(FlexReader.FLEX);
        in.close();
        FileWriter writer = new FileWriter(new File(outId));
        writer.write(xml);
        writer.close();
        System.out.println("Writing header of: " + id);
      }
    }
    System.out.println("Done");
  }
}
