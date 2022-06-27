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
import loci.formats.FormatException;
import loci.formats.ImageReader;

/**
 * Simple example of how to open multiple files simultaneously.
 */
public class MultiFileExample {
  public static void main(String[] args) throws FormatException, IOException {
    if (args.length < 2) {
      System.out.println("You must specify two files.");
      System.exit(1);
    }
    ImageReader[] readers = new ImageReader[args.length];
    for (int i=0; i<readers.length; i++) {
      readers[i] = new ImageReader();
      readers[i].setId(args[i]);
    }

    // read plane #0 from file #0
    readers[0].openBytes(0);

    // read plane #0 from file #1
    readers[1].openBytes(0);

    // the other option is to use a single reader for all of the files
    // this will use a little less memory, but is substantially slower
    // unless you read all of the planes from one file before moving on
    // to the next file
    //
    // if you want one reader total, uncomment the following:

    /*
    ImageReader reader = new ImageReader();
    //read plane #0 from file #0
    reader.setId(args[0]);
    reader.openBytes(0);
    // read plane #0 from file #1
    reader.setId(args[1]);
    reader.openBytes(0);
    */

  }
}
