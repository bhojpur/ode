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

import loci.common.Location;
import loci.common.RandomAccessInputStream;
import loci.common.services.ServiceFactory;
import loci.formats.FilePattern;
import loci.formats.in.TiffReader;
import loci.formats.meta.IMetadata;
import loci.formats.out.TiffWriter;
import loci.formats.services.ODEXMLService;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffParser;

/**
 * Stitches the first plane from a collection of TIFFs into a single file.
 */
public class SewTiffs {

  private static final int DOTS = 50;

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.out.println(
        "Usage: java SewTiffs base_name channel_num [time_count]");
      System.exit(1);
    }
    String base = args[0];
    int c = Integer.parseInt(args[1]);
    int num;
    if (args.length < 3) {
      FilePattern fp = new FilePattern(
        new Location(base + "_C" + c + "_TP1.tiff"));
      int[] count = fp.getCount();
      num = count[count.length - 1];
    }
    else num = Integer.parseInt(args[2]);
    System.out.println("Fixing " + base + "_C" + c + "_TP<1-" + num + ">.tiff");
    TiffReader in = new TiffReader();
    TiffWriter out = new TiffWriter();
    String outId = base + "_C" + c + ".tiff";
    System.out.println("Writing " + outId);
    out.setId(outId);
    System.out.print("   ");
    boolean comment = false;

    for (int t=0; t<num; t++) {
      String inId = base + "_C" + c + "_TP" + (t + 1) + ".tiff";
      ServiceFactory factory = new ServiceFactory();
      ODEXMLService service = factory.getInstance(ODEXMLService.class);
      IMetadata meta = service.createODEXMLMetadata();
      in.setMetadataStore(meta);
      in.setId(inId);
      out.setMetadataRetrieve(meta);

      // read first image plane
      byte[] image = in.openBytes(0);
      in.close();

      if (t == 0) {
        // read first IFD
        RandomAccessInputStream ras = new RandomAccessInputStream(inId);
        TiffParser parser = new TiffParser(ras);
        IFD ifd = parser.getFirstIFD();
        ras.close();

        // preserve TIFF comment
        String desc = ifd.getComment();

        if (desc != null) {
          ifd = new IFD();
          ifd.putIFDValue(IFD.IMAGE_DESCRIPTION, desc);
          comment = true;
          out.saveBytes(t, image, ifd);
          System.out.print(".");
          continue;
        }
      }

      // write image plane
      out.saveBytes(t, image);

      // update status
      System.out.print(".");
      if (t % DOTS == DOTS - 1) {
        System.out.println(" " + (t + 1));
        System.out.print("   ");
      }
    }
    System.out.println();
    if (comment) System.out.println("ODE-TIFF comment saved.");
    else System.out.println("No ODE-TIFF comment found.");
  }

}
