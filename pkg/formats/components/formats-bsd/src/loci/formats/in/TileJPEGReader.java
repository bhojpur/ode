package loci.formats.in;

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

import loci.common.RandomAccessInputStream;
import loci.common.services.ServiceException;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.codec.JPEGTileDecoder;
import loci.formats.meta.MetadataStore;
import loci.formats.services.JPEGTurboService;
import loci.formats.services.JPEGTurboServiceImpl;

/**
 * Reader for decoding JPEG images using java.awt.Toolkit and TurboJPEG.
 * This reader is useful for reading very large JPEG images, as it supports
 * tile-based access.
 */

public class TileJPEGReader extends FormatReader {

  // -- Fields --

  private transient JPEGTurboService service;

  // -- Constructor --

  public TileJPEGReader() {
    super("Tile JPEG", new String[] {"jpg", "jpeg"});
    domains = new String[] {FormatTools.GRAPHICS_DOMAIN};
  }

  // -- IFormatReader API methods --

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    service.getTile(buf, x, y, w, h);

    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      if (service != null) {
        service.close();
      }
      service = null;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  public void initFile(String id) throws FormatException, IOException {
    super.initFile(id);

    in = new RandomAccessInputStream(id);
    int[] dimensions;
    try (JPEGTileDecoder decoder = new JPEGTileDecoder()) {
      dimensions = decoder.preprocess(in);
    }

    CoreMetadata m = core.get(0);

    m.interleaved = true;
    m.littleEndian = false;

    m.sizeX = dimensions[0];
    m.sizeY = dimensions[1];
    m.sizeZ = 1;
    m.sizeT = 1;

    reopenFile();

    m.sizeC = 3;
    m.rgb = getSizeC() > 1;
    m.imageCount = 1;
    m.pixelType = FormatTools.UINT8;
    m.dimensionOrder = "XYCZT";
    m.metadataComplete = true;
    m.indexed = false;

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

  @Override
  public void reopenFile() throws IOException {
    if (in != null) {
      in.close();
    }
    in = new RandomAccessInputStream(currentId);
    in.seek(0);
    service = new JPEGTurboServiceImpl();
    try {
      service.initialize(in, getSizeX(), getSizeY());
    }
    catch (ServiceException se) {
      service = null;
      throw new IOException("Could not initialize JPEG service", se);
    }
  }

}
