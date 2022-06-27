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

import loci.formats.ImageReader;
import loci.formats.MetadataTools;
import loci.formats.meta.DummyMetadata;
import loci.formats.meta.IMetadata;
import loci.formats.meta.IPyramidStore;
import loci.formats.out.PyramidODETiffWriter;
import loci.formats.tiff.IFD;

import ode.xml.model.primitives.PositiveInteger;

/**
 * Reads a set of pyramid resolutions (one per file) and converts to a single
 * pyramid ODE-TIFF.
 */
public class WritePyramid {

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.out.println("Specify one input file per resolution in " +
        "descending order and one output file");
      return;
    }
    loci.common.DebugTools.enableLogging("INFO");

    // read each input file's metadata to build an IPyramidStore
    // that represents the full image pyramid
    IMetadata meta = MetadataTools.createODEXMLMetadata();
    if (!(meta instanceof IPyramidStore)) {
      System.out.println("MetadataStore is not an IPyramidStore; " +
        "cannot write pyramid");
      return;
    }

    ImageReader reader = new ImageReader();
    reader.setMetadataStore(meta);
    reader.setId(args[0]);
    reader.close();
    reader.setMetadataStore(new DummyMetadata());
    for (int i=1; i<args.length-1; i++) {
      reader.setId(args[i]);
      int x = reader.getSizeX();
      int y = reader.getSizeY();
      reader.close();
      ((IPyramidStore) meta).setResolutionSizeX(new PositiveInteger(x), 0, i);
      ((IPyramidStore) meta).setResolutionSizeY(new PositiveInteger(y), 0, i);
    }

    // pass metadata to the writer so that a single file will be
    // written containing the whole image pyramid
    String output = args[args.length - 1];
    PyramidODETiffWriter writer = new PyramidODETiffWriter();
    writer.setWriteSequentially(true);
    writer.setMetadataRetrieve(meta);
    writer.setId(output);

    // save image tiles with dimensions 256x256
    // the largest resolution in a pyramid may be very large,
    // so working with whole planes at once doesn't make sense
    int tileSize = 256;
    for (int i=0; i<args.length-1; i++) {
      writer.setResolution(i);
      reader.setId(args[i]);
      writer.setInterleaved(reader.isInterleaved());

      for (int plane=0; plane<reader.getImageCount(); plane++) {
        IFD ifd = new IFD();
        ifd.put(IFD.TILE_WIDTH, tileSize);
        ifd.put(IFD.TILE_LENGTH, tileSize);
        for (int yy=0; yy<reader.getSizeY(); yy+=tileSize) {
          for (int xx=0; xx<reader.getSizeX(); xx+=tileSize) {
            int realWidth = (int) Math.min(tileSize, reader.getSizeX() - xx);
            int realHeight = (int) Math.min(tileSize, reader.getSizeY() - yy);
            byte[] tile = reader.openBytes(plane, xx, yy, realWidth, realHeight);
            writer.saveBytes(plane, tile, ifd, xx, yy, realWidth, realHeight);
          }
        }
      }
      reader.close();
    }
    writer.close();
  }

}
