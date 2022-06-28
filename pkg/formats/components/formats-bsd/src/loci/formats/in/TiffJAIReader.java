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

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import loci.common.FileHandle;
import loci.common.IRandomAccess;
import loci.common.Location;
import loci.common.ReflectException;
import loci.common.ReflectedUniverse;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.MissingLibraryException;
import loci.formats.gui.AWTImageTools;
import loci.formats.meta.MetadataStore;

/**
 * TiffJAIReader is a file format reader for TIFF images. It uses the
 * Java Advanced Imaging library (javax.media.jai) to read the data.
 *
 * Much of this code was adapted from
 * <a href="http://java.sun.com/products/java-media/jai/forDevelopers/samples/MultiPageRead.java">this example</a>.
 */
public class TiffJAIReader extends BIFormatReader {

  // -- Constants --

  private static final String NO_JAI_MSG =
    "Java Advanced Imaging (JAI) is required to read some TIFF files. " +
    "Please install JAI from https://jai.dev.java.net/";

  // -- Fields --

  /** Reflection tool for JAI calls. */
  protected ReflectedUniverse r;

  // -- Constructor --

  /** Constructs a new TIFF reader that uses Java Image I/O. */
  public TiffJAIReader() {
    super("Tagged Image File Format", TiffReader.TIFF_SUFFIXES);
    domains = new String[] {FormatTools.GRAPHICS_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#openPlane(int, int, int, int, int int) */
  @Override
  public Object openPlane(int no, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, -1, x, y, w, h);
    BufferedImage img = openBufferedImage(no);
    return AWTImageTools.getSubimage(img, isLittleEndian(), x, y, w, h);
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    LOGGER.info("Checking for JAI");
    try {
      r = new ReflectedUniverse();
      r.exec("import javax.media.jai.NullOpImage");
      r.exec("import javax.media.jai.OpImage");
      r.exec("import com.sun.media.jai.codec.FileSeekableStream");
      r.exec("import com.sun.media.jai.codec.ImageDecoder");
      r.exec("import com.sun.media.jai.codec.ImageCodec");
    }
    catch (ReflectException exc) {
      throw new MissingLibraryException(NO_JAI_MSG, exc);
    }

    super.initFile(id);

    LOGGER.info("Reading movie dimensions");

    // map Location to File or RandomAccessFile, if possible
    IRandomAccess ira = Location.getMappedFile(id);
    if (ira != null) {
      if (ira instanceof FileHandle) {
        FileHandle fh = (FileHandle) ira;
        r.setVar("file", fh.getRandomAccessFile());
      }
      else {
        throw new FormatException(
          "Unsupported handle type" + ira.getClass().getName());
      }
    }
    else {
      String mapId = Location.getMappedId(id);
      File file = new File(mapId);
      if (file.exists()) {
        r.setVar("file", file);
      }
      else throw new FileNotFoundException(id);
    }
    r.setVar("tiff", "tiff");
    r.setVar("param", null);

    // create TIFF decoder
    int numPages;
    try {
      r.exec("s = new FileSeekableStream(file)");
      r.exec("dec = ImageCodec.createImageDecoder(tiff, s, param)");
      numPages = ((Integer) r.exec("dec.getNumPages()")).intValue();
    }
    catch (ReflectException exc) {
      throw new FormatException(exc);
    }
    if (numPages < 0) {
      throw new FormatException("Invalid page count: " + numPages);
    }

    // decode first image plane
    BufferedImage img = openBufferedImage(0);
    if (img == null) throw new FormatException("Invalid image stream");

    LOGGER.info("Populating metadata");

    CoreMetadata m = core.get(0);

    m.imageCount = numPages;

    m.sizeX = img.getWidth();
    m.sizeY = img.getHeight();
    m.sizeZ = 1;
    m.sizeC = img.getSampleModel().getNumBands();
    m.sizeT = numPages;

    m.rgb = m.sizeC > 1;

    m.dimensionOrder = "XYCZT";
    m.pixelType = AWTImageTools.getPixelType(img);
    m.interleaved = true;
    m.littleEndian = false;
    m.metadataComplete = true;
    m.indexed = false;
    m.falseColor = false;

    // populate the metadata store
    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

  /** Obtains a BufferedImage from the given data source using JAI. */
  protected BufferedImage openBufferedImage(int no) throws FormatException {
    r.setVar("no", no);
    RenderedImage img;
    try {
      r.exec("img = dec.decodeAsRenderedImage(no)");
      img = (RenderedImage)
        r.exec("new NullOpImage(img, null, OpImage.OP_IO_BOUND, null)");
    }
    catch (ReflectException exc) {
      throw new FormatException(exc);
    }
    return AWTImageTools.convertRenderedImage(img);
  }

}
