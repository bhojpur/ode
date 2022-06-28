package loci.formats.utests.tiff;

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
import java.nio.ByteOrder;

import loci.common.ByteArrayHandle;
import loci.common.RandomAccessInputStream;
import loci.common.RandomAccessOutputStream;
import loci.formats.FormatException;
import loci.formats.tiff.IFD;
import loci.formats.tiff.PhotoInterp;
import loci.formats.tiff.TiffParser;
import loci.formats.tiff.TiffRational;
import loci.formats.tiff.TiffSaver;

public class BaseTiffMock {

  private ByteArrayHandle handle;
  
  private RandomAccessInputStream in;

  private RandomAccessOutputStream out;

  private TiffParser tiffParser;

  private TiffSaver tiffSaver;

  private static byte[] TIFF_HEADER = new byte[] {
    0x49, 0x49, 0x2A, 0x00, 0x08, 0x00, 0x00, 0x00,
  };

  private static final int ENTRY_VALUE_BEGIN_OFFSET = 65535;

  public BaseTiffMock() throws FormatException, IOException {
    handle = new ByteArrayHandle();
    handle.setOrder(ByteOrder.LITTLE_ENDIAN);
    out = new RandomAccessOutputStream(handle);
    tiffSaver = new TiffSaver(out, handle);
    tiffSaver.writeHeader();

    IFD ifd = new IFD();
    ifd.put(IFD.IMAGE_WIDTH, getImageWidth());
    ifd.put(IFD.IMAGE_LENGTH, getImageLength());
    ifd.put(IFD.BITS_PER_SAMPLE, getBitsPerSample());
    ifd.put(IFD.COMPRESSION, getCompression());
    ifd.put(IFD.PHOTOMETRIC_INTERPRETATION, PhotoInterp.RGB.getCode());
    ifd.put(IFD.STRIP_OFFSETS, getStripOffsets());
    ifd.put(IFD.SAMPLES_PER_PIXEL, getSamplesPerPixel());
    ifd.put(IFD.ROWS_PER_STRIP, getRowsPerStrip());
    ifd.put(IFD.X_RESOLUTION, getXResolution());
    ifd.put(IFD.Y_RESOLUTION, getYResolution());
    ifd.put(IFD.RESOLUTION_UNIT, getResolutionUnit());

    tiffSaver.writeIFD(ifd, 0);

    in = new RandomAccessInputStream(handle);
    tiffParser = new TiffParser(in);
  }

  /**
   * Closes the streams.
   *
   * @throws Exception Thrown if an error occurred while closing.
   */
  protected void close() throws IOException {
    if (in != null) in.close();
    if (tiffSaver != null) tiffSaver.close();
  }

  protected int getEntryCount() {
    return 11;
  }

  public TiffParser getTiffParser() {
    return tiffParser;
  }

  public int getImageWidth() {
    return 6;
  }

  public int getImageLength() {
    return 4;
  }

  public int[] getBitsPerSample() {
    return new int[] { 8 };
  }

  public int getCompression() {
    return 1;
  }

  public int[] getStripOffsets() {
    return new int[] { 0, 1, 2 };
  }

  public int[] getRowsPerStrip() {
    return new int[] { 2, 2, 2 };
  }

  public TiffRational getXResolution() {
    return new TiffRational(1, 4);
  }

  public TiffRational getYResolution() {
    return new TiffRational(1, 2);
  }

  public short getResolutionUnit() {
    return 1;
  }

  public int getSamplesPerPixel() {
    return 1;
  }

}
