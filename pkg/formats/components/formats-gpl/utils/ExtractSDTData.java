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
import loci.formats.in.SDTReader;

/**
 * Reads SDT files and creates text files containing histogram data.
 */
public class ExtractSDTData {

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.out.println("Usage: java ExtractSDTData file.sdt");
      System.exit(1);
    }
    String id = args[0];
    String tid = id.substring(0, id.indexOf('.'));
    SDTReader r = new SDTReader();
    r.setId(id);
    int bins = r.getTimeBinCount();
    int chan = r.getChannelCount();
    int w = r.getSizeX();
    int h = r.getSizeY();
    System.out.println("Data is " + w + " x " + h +
      ", bins=" + bins + ", channels=" + chan);
    byte[][] data = new byte[chan][h * w * bins * 2];
    for (int c=0; c<chan; c++) {
      System.out.println("Reading channel #" + c + "...");
      r.openBytes(c, data[c]);
      int i = 0;
      for (int y=0; y<h; y++) {
        for (int x=0; x<w; x++) {
          String oid = tid + "-c" + c + "-row" + y + "-col" + x;
          System.out.println(oid);
          PrintWriter out = new PrintWriter(new FileWriter(oid));
          for (int b=0; b<bins; b++) {
            i += 2;
            int v0 = data[c][i];
            int v1 = data[c][i + 1];
            int v = (v0 << 8) & v1;
            out.println(b + " " + v);
          }
          out.close();
        }
      }
    }
  }

}
