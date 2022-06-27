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
import loci.common.RandomAccessOutputStream;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.ImageReader;
import loci.formats.MetadataTools;
import loci.formats.meta.IMetadata;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffCompression;
import loci.formats.tiff.TiffSaver;

import ode.xml.model.primitives.PositiveInteger;

/**
 * Writes the pixels from a set of JPEG files to a single TIFF.
 * The pixel data is used as-is, so no decompression or re-compression is
 * performed.
 */
public class WritePrecompressedPlanes {
  public static void main(String[] args) throws FormatException, IOException {
    // print usage if no args specified
    if (args.length == 0) {
      System.out.println("Usage: java WritePrecompressedPlanes " +
        "[input file 1] ... [input file n] [output file]");
      System.exit(0);
    }

    // first n - 1 args are the input files
    String[] inputFiles = new String[args.length - 1];
    System.arraycopy(args, 0, inputFiles, 0, inputFiles.length);

    // last arg is the output file
    String outputFile = args[args.length - 1];

    // open up one of the input files so that we can read the metadata
    // this assumes that the dimensions of the input files are the same
    ImageReader reader = new ImageReader();
    reader.setId(inputFiles[0]);
    int pixelType = reader.getPixelType();

    // write the pixel data to the output file
    RandomAccessOutputStream out = new RandomAccessOutputStream(outputFile);
    TiffSaver saver = new TiffSaver(out, outputFile);
    saver.setWritingSequentially(true);
    saver.setLittleEndian(reader.isLittleEndian());
    saver.setBigTiff(false);
    saver.writeHeader();

    for (int i=0; i<inputFiles.length; i++) {
      RandomAccessInputStream in = new RandomAccessInputStream(inputFiles[i]);
      byte[] buf = new byte[(int) in.length()];
      in.readFully(buf);
      in.close();

      IFD ifd = new IFD();
      ifd.put(IFD.IMAGE_WIDTH, reader.getSizeX());
      ifd.put(IFD.IMAGE_LENGTH, reader.getSizeY());
      ifd.put(IFD.LITTLE_ENDIAN, reader.isLittleEndian());
      ifd.put(IFD.SAMPLE_FORMAT, FormatTools.isSigned(pixelType) ? 2 :
        FormatTools.isFloatingPoint(pixelType) ? 3 : 1);
      ifd.put(IFD.PLANAR_CONFIGURATION, 1);
      ifd.put(IFD.REUSE, out.length());
      out.seek(out.length());

      // this is very important
      // the data is already compressed in a single chunk, so setting the
      // number of rows per strip to something smaller than the full height
      // will require us to re-compress the data
      ifd.put(IFD.ROWS_PER_STRIP, reader.getSizeY());

      saver.writeImage(buf, ifd, i, pixelType, 0, 0, reader.getSizeX(),
        reader.getSizeY(), i == inputFiles.length - 1,
        reader.getRGBChannelCount(), true);
    }

    reader.close();
    out.close();

    // reset the TIFF file's compression flag
    // you cannot do this before the pixel data is written, otherwise
    // the pixels will be re-compressed

    saver = new TiffSaver(outputFile);
    for (int i=0; i<inputFiles.length; i++) {
      RandomAccessInputStream in = new RandomAccessInputStream(outputFile);
      saver.overwriteLastIFDOffset(in);
      saver.overwriteIFDValue(in, i, IFD.COMPRESSION,
        TiffCompression.JPEG.getCode());
      in.close();
    }
    saver.getStream().close();
  }
}
