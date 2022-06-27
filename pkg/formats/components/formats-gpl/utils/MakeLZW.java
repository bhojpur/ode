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

import loci.common.services.ServiceFactory;
import loci.formats.ImageReader;
import loci.formats.meta.IMetadata;
import loci.formats.out.TiffWriter;
import loci.formats.services.ODEXMLService;

/**
 * Converts the given image file to an LZW-compressed TIFF.
 */
public class MakeLZW {

  public static void main(String[] args) throws Exception {
    ImageReader reader = new ImageReader();

    ServiceFactory factory = new ServiceFactory();
    ODEXMLService service = factory.getInstance(ODEXMLService.class);
    IMetadata odexmlMeta = service.createODEXMLMetadata();

    reader.setMetadataStore(odexmlMeta);
    TiffWriter writer = new TiffWriter();
    for (int i=0; i<args.length; i++) {
      String inFile = args[i];
      String outFile = "lzw-" + inFile;
      System.out.print("Converting " + inFile + " to " + outFile);
      reader.setId(inFile);
      writer.setMetadataRetrieve(odexmlMeta);
      writer.setCompression("LZW");
      writer.setId(outFile);
      int planeCount = reader.getImageCount();
      for (int p=0; p<planeCount; p++) {
        System.out.print(".");
        byte[] plane = reader.openBytes(p);
        writer.saveBytes(p, plane);
      }
      System.out.println(" [done]");
    }
  }

}
