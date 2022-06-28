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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.meta.MetadataStore;
import loci.formats.tiff.IFD;
import loci.formats.tiff.PhotoInterp;
import loci.formats.tiff.TiffIFDEntry;
import loci.formats.tiff.TiffParser;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;

/** IonpathMIBITiffReader is the file format reader for Ionpath MIBI TIFF files. */
public class IonpathMIBITiffReader extends BaseTiffReader {

  // -- Constants --

  /** TIFF software tag prefix for Ionpath MIBI files. */
  public static final String IONPATH_MIBI_SOFTWARE_PREFIX = "IonpathMIBI";

  // -- Fields --

  private HashMap<String, Integer> seriesTypes = new HashMap<String, Integer>();
  private List<Integer> seriesIFDs = new ArrayList<Integer>();
  private List<String> channelIDs = new ArrayList<String>();
  private List<String> channelNames = new ArrayList<String>();
  private Hashtable<String, Object> simsDescription = new Hashtable<String, Object>();

  // -- Constructor --

  /** Constructs a new Ionpath MIBI reader. */
  public IonpathMIBITiffReader() {
    super("Ionpath MIBI", new String[] {"tif, tiff"});
    domains = new String[] {FormatTools.UNKNOWN_DOMAIN};
    suffixSufficient = false;
    canSeparateSeries = false;
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    TiffParser tiffParser = new TiffParser(stream);
    IFD ifd = tiffParser.getFirstIFD();
    if (ifd == null) return false;
    String software = ifd.getIFDTextValue(IFD.SOFTWARE);
    if (software == null) return false;
    return software.startsWith(IONPATH_MIBI_SOFTWARE_PREFIX);
  }

  /** @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int) */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
      throws FormatException, IOException {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);
    if (tiffParser == null) {
      initTiffParser();
    }
    tiffParser.getSamples(ifds.get(seriesIFDs.get(getSeries()) + no), buf, x, y, w, h);
    return buf;
  }

  // -- Internal BaseTiffReader API methods --

  /* @see loci.formats.BaseTiffReader#initStandardMetadata() */
  @Override
  protected void initStandardMetadata() throws FormatException, IOException {
    super.initStandardMetadata();

    ifds = tiffParser.getMainIFDs();
    int seriesIndex;
    String imageType = null;

    core.clear();

    for (int i = 0; i < ifds.size(); i++) {
      IFD ifd = ifds.get(i);
      Object description = ifd.get(IFD.IMAGE_DESCRIPTION);
      if (description == null) {
        throw new FormatException("Image description is mandatory.");
      }
      String imageDescription = null;
      if (description instanceof TiffIFDEntry) {
        Object value = tiffParser.getIFDValue((TiffIFDEntry) description);
        if (value != null) {
          imageDescription = value.toString();
        }
      } else if (description instanceof String) {
        imageDescription = (String) description;
      }

      JSONObject jsonDescription;
      try {
        jsonDescription = new JSONObject(imageDescription);
        imageType = jsonDescription.getString("image.type");
        if (imageType.equals("SIMS")) {
          String mass = jsonDescription.getString("channel.mass");
          if (mass == null) {
            throw new FormatException("Channel masses are mandatory.");
          }
          String target = jsonDescription.getString("channel.target");
          channelIDs.add(mass);
          channelNames.add(target != null && target != "null" ? target : mass);
        }
      } catch (JSONException e) {
        throw new FormatException("Unexpected format in SIMS description JSON.");
      }

      if (seriesTypes.containsKey(imageType)) {
        if (!imageType.equals("SIMS")) {
          throw new FormatException("Only type 'SIMS' can have >1 image per file.");
        }
        seriesIndex = seriesTypes.get(imageType);
        CoreMetadata ms = core.get(seriesIndex, 0);
        ms.sizeC += 1;
        ms.imageCount += 1;
      } else {
        seriesIndex = seriesTypes.size();
        seriesTypes.put(imageType, seriesIndex);
        seriesIFDs.add(i);

        core.add(new CoreMetadata());
        setSeries(seriesIndex);
        tiffParser.setDoCaching(true);
        tiffParser.fillInIFD(ifd);

        if (imageType.equals("SIMS")) {
          try {
            Iterator<?> keySet = jsonDescription.keys();
            while (keySet.hasNext()) {
              String key = (String) keySet.next();
              if (key.startsWith("mibi.")) {
                simsDescription.put(key, jsonDescription.getString(key));
              }
            }
          } catch (JSONException e) {
            throw new FormatException("Unexpected format in SIMS description JSON.");
          }
        }

        CoreMetadata ms = core.get(seriesIndex, 0);
        PhotoInterp p = ifd.getPhotometricInterpretation();
        int samples = ifd.getSamplesPerPixel();
        ms.rgb = samples > 1 || p == PhotoInterp.RGB;
        ms.sizeX = (int) ifd.getImageWidth();
        ms.sizeY = (int) ifd.getImageLength();
        ms.sizeZ = 1;
        ms.sizeT = 1;
        ms.sizeC = ms.rgb ? samples : 1;
        ms.littleEndian = ifd.isLittleEndian();
        ms.indexed =
            p == PhotoInterp.RGB_PALETTE
                && (get8BitLookupTable() != null || get16BitLookupTable() != null);
        ms.imageCount = 1;
        ms.pixelType = ifd.getPixelType();
        ms.metadataComplete = true;
        ms.interleaved = false;
        ms.falseColor = false;
        ms.dimensionOrder = "XYCZT";
        ms.thumbnail = false;
        ms.imageCount = 1;
        if (imageType.equals("SIMS")) {
          ms.seriesMetadata = simsDescription;
        }
      }
    }
  }

  /* @see loci.formats.BaseTiffReader#initMetadataStore() */
  @Override
  protected void initMetadataStore() throws FormatException {
    super.initMetadataStore();

    MetadataStore store = makeFilterMetadata();
    int simsIndex = seriesTypes.get("SIMS");
    String instrument = (String) simsDescription.get("mibi.instrument");
    store.setInstrumentID(formatMetadata("Instrument", instrument), simsIndex);
    store.setImageDescription((String) simsDescription.get("mibi.description"), simsIndex);

    String currentFile = FilenameUtils.getName(this.getCurrentFile());
    for (Map.Entry<String, Integer> entry : seriesTypes.entrySet()) {
      String key = entry.getKey();
      if (key != null) {
        store.setImageID(formatMetadata("Image", entry.getKey()), entry.getValue());
        store.setImageName(currentFile + " " + entry.getKey(), entry.getValue());
      }
    }

    for (int j = 0; j < channelIDs.size(); j++) {
      store.setChannelID(formatMetadata("Channel", channelIDs.get(j)), simsIndex, j);
      if (channelNames.get(j) != null) {
        store.setChannelName(formatMetadata("Target", channelNames.get(j)), simsIndex, j);
      }
    }
  }

  private static String formatMetadata(String key, String value) {
    return key + ":" + value.replaceAll("\\s", "_");
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      seriesTypes.clear();
      seriesIFDs.clear();
      channelIDs.clear();
      channelNames.clear();
      simsDescription .clear();
    }
  }
}
