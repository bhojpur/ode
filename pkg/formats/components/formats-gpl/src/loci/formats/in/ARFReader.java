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
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;

/**
 * ARFReader is the file format reader for Axon Raw Format (ARF) files,
 * produced by INDEC BioSystems's
 * <a href="http://www.imagingworkbench.com/">Imaging Workbench</a>
 * software.
 */
public class ARFReader extends FormatReader {

  // -- Constants --

  private static final long PIXELS_OFFSET = 524;

  // -- Constructor --

  /** Constructs a new ARF reader. */
  public ARFReader() {
    super("ARF", "arf");
    domains = new String[] {FormatTools.UNKNOWN_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 4;
    if (!FormatTools.validStream(stream, blockLen, false)) return false;
    byte endian1 = stream.readByte();
    byte endian2 = stream.readByte();
    return ((endian1 == 1 && endian2 == 0) || (endian1 == 0 && endian2 == 1)) &&
      stream.readString(2).equals("AR");
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    in.seek(PIXELS_OFFSET + no * FormatTools.getPlaneSize(this));
    readPlane(in, x, y, w, h, buf);

    return buf;
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);

    // parse file header

    LOGGER.info("Reading file header");

    byte endian1 = in.readByte();
    byte endian2 = in.readByte();
    boolean little;
    if (endian1 == 1 && endian2 == 0) little = true;
    else if (endian1 == 0 && endian2 == 1) little = false;
    else throw new FormatException("Undefined endianness");
    in.order(little);

    in.skipBytes(2); // 'AR' signature
    int version = in.readUnsignedShort();
    int width = in.readUnsignedShort();
    int height = in.readUnsignedShort();
    int bitsPerPixel = in.readUnsignedShort();
    int numImages = version == 2 ? in.readUnsignedShort() : 1;
    // NB: The next 510 bytes are unused 'application dependent' data,
    // followed by raw image data with no padding.

    // populate core metadata
    CoreMetadata m = core.get(0);

    m.sizeX = width;
    m.sizeY = height;
    m.sizeZ = 1;
    m.sizeC = 1;
    m.sizeT = numImages;

    int bpp = bitsPerPixel / 8;
    if ((bitsPerPixel % 8) != 0) bpp++;
    m.pixelType = FormatTools.pixelTypeFromBytes(bpp, false, false);

    m.bitsPerPixel = bitsPerPixel;
    m.imageCount = numImages;
    m.dimensionOrder = "XYCZT";
    m.orderCertain = true;
    m.littleEndian = little;
    m.rgb = false;
    m.interleaved = false;
    m.indexed = false;
    m.metadataComplete = true;

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      // populate original metadata

      addGlobalMeta("Endianness", little ? "little" : "big");
      addGlobalMeta("Version", version);
      addGlobalMeta("Width", width);
      addGlobalMeta("Height", height);
      addGlobalMeta("Bits per pixel", bitsPerPixel);
      addGlobalMeta("Image count", numImages);
    }

    // populate ODE metadata

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

}
