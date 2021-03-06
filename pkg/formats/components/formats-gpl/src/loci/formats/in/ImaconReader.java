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

import loci.common.DateTools;
import loci.common.RandomAccessInputStream;
import loci.common.xml.BaseHandler;
import loci.common.xml.XMLTools;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;
import loci.formats.tiff.IFD;
import loci.formats.tiff.PhotoInterp;
import loci.formats.tiff.TiffParser;

import ode.xml.model.primitives.Timestamp;

import org.xml.sax.Attributes;

/**
 * ImaconReader is the file format reader for Imacon .fff (TIFF) files.
 */
public class ImaconReader extends BaseTiffReader {

  // -- Constants --

  private static final int CREATOR_TAG = 34377;
  private static final int XML_TAG = 50457;
  private static final int PIXELS_TAG = 46275;

  // -- Fields --

  private String experimenterName;
  private String creationDate;
  private String imageName;

  // -- Constructor --

  /** Constructs a new Imacon reader. */
  public ImaconReader() {
    super("Imacon", new String[] {"fff"});
    domains = new String[] {FormatTools.GRAPHICS_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    TiffParser parser = new TiffParser(stream);
    parser.setDoCaching(false);
    IFD ifd = parser.getFirstIFD();
    if (ifd == null) return false;
    return ifd.containsKey(XML_TAG);
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      experimenterName = null;
      creationDate = null;
      imageName = null;
    }
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    if (tiffParser == null) {
      initTiffParser();
    }

    tiffParser.getSamples(ifds.get(getSeries()), buf, x, y, w, h);
    return buf;
  }

  // -- Internal BaseTiffReader API methods --

  /* @see BaseTiffReader#initStandardMetadata() */
  @Override
  protected void initStandardMetadata() throws FormatException, IOException {
    super.initStandardMetadata();

    ifds = tiffParser.getMainIFDs();

    core.clear();
    for (int i=0; i<ifds.size(); i++) {
      CoreMetadata ms = new CoreMetadata();
      core.add(ms);
      ms.imageCount = 1;
      IFD ifd = ifds.get(i);
      ifd.remove(PIXELS_TAG);
      tiffParser.fillInIFD(ifd);

      PhotoInterp photo = ifd.getPhotometricInterpretation();
      int samples = ifd.getSamplesPerPixel();
      ms.rgb = samples > 1 || photo == PhotoInterp.RGB ||
        photo == PhotoInterp.CFA_ARRAY;
      if (photo == PhotoInterp.CFA_ARRAY) samples = 3;

      ms.sizeX = (int) ifd.getImageWidth();
      ms.sizeY = (int) ifd.getImageLength();
      ms.sizeZ = 1;
      ms.sizeC = isRGB() ? samples : 1;
      ms.sizeT = 1;
      ms.pixelType = ifd.getPixelType();
      ms.indexed = photo == PhotoInterp.RGB_PALETTE;
      ms.dimensionOrder = "XYCZT";
      ms.interleaved = false;
    }

    IFD firstIFD = ifds.get(0);

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      String xml = firstIFD.getIFDTextValue(XML_TAG).trim();
      xml = xml.substring(xml.indexOf('<'));
      XMLTools.parseXML(xml, new ImaconHandler());
    }

    String[] creationInfo = firstIFD.getIFDTextValue(CREATOR_TAG).split("\n");

    if (creationInfo.length > 4) {
      experimenterName = creationInfo[4].trim();
    }
    if (creationInfo.length > 6) {
      imageName = creationInfo[6].trim();
    }
    if (creationInfo.length > 8) {
      creationDate = creationInfo[8].trim();
    }
    if (creationInfo.length > 10) {
      creationDate += " " + creationInfo[10].trim();
    }
  }

  /* @see BaseTiffReader#initMetadataStore() */
  @Override
  protected void initMetadataStore() throws FormatException {
    super.initMetadataStore();

    MetadataStore store = makeFilterMetadata();

    if (creationDate != null) {
      creationDate = DateTools.formatDate(creationDate, "yyyyMMdd HHmmSSZ");
    }

    for (int i=0; i<getSeriesCount(); i++) {
      String name = imageName;
      if (imageName.length() == 0) {
        name = "#" + (i + 1);
      }
      else {
        name += " #" + (i + 1);
      }
      store.setImageName(name, i);
      if (creationDate != null) {
        store.setImageAcquisitionDate(new Timestamp(creationDate), i);
      }
    }

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      if (experimenterName == null) experimenterName = "";

      int nameSpace = experimenterName.indexOf(' ');
      String firstName =
        nameSpace == -1 ? "" : experimenterName.substring(0, nameSpace);
      String lastName = nameSpace == -1 ? experimenterName :
        experimenterName.substring(nameSpace + 1);

      String experimenter = MetadataTools.createLSID("Experimenter", 0);

      store.setExperimenterID(experimenter, 0);
      store.setExperimenterFirstName(firstName, 0);
      store.setExperimenterLastName(lastName, 0);

      for (int i=0; i<getSeriesCount(); i++) {
        store.setImageExperimenterRef(experimenter, i);
      }
    }
  }

  // -- Helper class --

  class ImaconHandler extends BaseHandler {
    private String key, value;
    private String qName;

    // -- DefaultHandler API methods --

    @Override
    public void characters(char[] data, int start, int len) {
      if (qName.equals("key")) {
        key = new String(data, start, len);
        value = null;
      }
      else value = new String(data, start, len);

      if (key != null && value != null) {
        addGlobalMeta(key, value);
      }
    }

    @Override
    public void startElement(String uri, String localName, String qName,
      Attributes attributes)
    {
      this.qName = qName;
    }
  }

}
