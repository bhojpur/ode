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

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import loci.common.RandomAccessInputStream;
import loci.formats.tiff.*;

/**
 * Parses and outputs all IFDs for the given TIFF file(s).
 */
public class TiffDumper {

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      System.out.println("Usage: java TiffDumper file1 file2 ...");
      return;
    }
    for (int i=0; i<args.length; i++) dumpIFDs(args[i]);
  }

  public static void dumpIFDs(String path) throws IOException {
    RandomAccessInputStream in = new RandomAccessInputStream(path);
    TiffParser parser = new TiffParser(in);
    IFDList ifdList = parser.getMainIFDs();
    for (IFD ifd : ifdList) {
      for (Integer key : ifd.keySet()) {
        int k = key.intValue();
        String name = IFD.getIFDTagName(k);
        String value = prettyValue(ifd.getIFDValue(k), 0);
        System.out.println(name + " = " + value);
      }
    }
  }

  private static String prettyValue(Object value, int indent) {
    if (!value.getClass().isArray()) return value.toString();

    char[] spaceChars = new char[indent];
    Arrays.fill(spaceChars, ' ');
    String spaces = new String(spaceChars);

    StringBuilder sb = new StringBuilder();
    sb.append("{\n");
    for (int i=0; i<Array.getLength(value); i++) {
      sb.append(spaces);
      sb.append("  ");
      Object component = Array.get(value, i);
      sb.append(prettyValue(component, indent + 2));
      sb.append("\n");
    }
    sb.append(spaces);
    sb.append("}");
    return sb.toString();
  }

}
