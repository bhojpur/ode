
package loci.tests;

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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import loci.formats.FormatException;
import loci.formats.ImageReader;

/**
 * A class for testing {@link loci.common.RandomAccessInputStream}'s
 * ability to handle files compressed with gz, zip or bz2.
 */
public class ZipTester {

  private static ImageReader reader = new ImageReader();

  public static void main(String[] args)
    throws IOException, FormatException, InterruptedException
  {
    if (args.length < 2) {
      System.out.println("Usage: java loci.tests.ZipTester " +
        "/path/to/input-file /path/to/output-folder");
      System.exit(1);
    }
    File in = new File(args[0]);
    if (!in.exists()) {
      System.out.println("Input file '" + in + "' does not exist.");
      System.exit(2);
    }
    File out = new File(args[1]);
    if (!out.isDirectory()) {
      System.out.println("Output folder '" + out + "' is not a directory.");
      System.exit(3);
    }

    // create temporary working directory
    File tmp = new File(out, "ZipTester.tmp");
    if (!tmp.exists()) tmp.mkdir();

    // copy original file into working directory
    String name = in.getName();
    String id = new File(tmp, name).getPath();
    System.out.println("cp '" + in.getPath() + "' '" + id + "'");
    FileInputStream fin = new FileInputStream(in);
    FileOutputStream fout = new FileOutputStream(id);
    byte[] buf = new byte[8192];
    while (true) {
      int r = fin.read(buf);
      if (r <= 0) break;
      fout.write(buf, 0, r);
    }
    fout.close();
    fin.close();

    time(id);

    Runtime r = Runtime.getRuntime();
    String[] cmd;

    // test zip archive
    String zip = id + ".zip";
    cmd = new String[] {"zip", zip, id};
    System.out.println("zip '" + zip + "' '" + id + "'");
    r.exec(cmd).waitFor();
    time(zip);

    // test bz2 archive
    String bz2 = id + ".bz2";
    cmd = new String[] {"bzip2", "-k", id};
    System.out.println("bzip2 -k '" + id + "'");
    r.exec(cmd).waitFor();
    time(bz2);

    // create gz archive
    String gz = id + ".gz";
    cmd = new String[] {"gzip", id};
    System.out.println("gzip '" + id + "'");
    r.exec(cmd).waitFor();
    time(gz);

    // clean up
    new File(zip).delete();
    new File(bz2).delete();
    new File(gz).delete();
    tmp.delete();
  }

  public static void time(String id) throws IOException, FormatException {
    System.out.print("Timing " + new File(id).getName() + ": ");
    long t1 = System.currentTimeMillis();
    boolean result = reader.isThisType(id);
    long t2 = System.currentTimeMillis();
    System.out.print((t2 - t1) + " ms to check type (" + result + "); ");
    long t3 = System.currentTimeMillis();
    reader.setId(id);
    long t4 = System.currentTimeMillis();
    System.out.println((t4 - t3) + " ms to initialize");
    reader.close();
  }

}
