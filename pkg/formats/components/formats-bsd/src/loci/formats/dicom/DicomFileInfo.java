/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.dicom;

import java.io.IOException;
import java.util.List;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.in.DicomReader;

import ode.xml.model.primitives.Timestamp;
import ode.units.quantity.Length;

/**
 * Structure containing metadata for one DICOM file (instance).
 */
public class DicomFileInfo implements Comparable<DicomFileInfo> {
  public CoreMetadata coreMetadata;
  public String file;
  public int concatenationIndex = 0;
  public List<DicomTile> tiles;
  public String imageType;
  public List<Double> zOffsets;
  public boolean edf = false;
  public Timestamp timestamp;
  public Length pixelSizeX;
  public Length pixelSizeY;
  public Length pixelSizeZ;
  public List<Double> positionX;
  public List<Double> positionY;
  public List<Double> positionZ;
  public List<String> channelNames;

  /**
   * Construct an empty object to be populated later.
   */
  public DicomFileInfo() {
  }

  /**
   * Construct an object from the given file.
   * A DicomReader will be used to initialize the file and extract metadata.
   *
   * @param filePath DICOM file
   * @throws FormatException if file initialization fails
   * @throws IOException if file initialization fails
   */
  public DicomFileInfo(String filePath) throws FormatException, IOException {
    file = filePath;

    try (DicomReader reader = new DicomReader()) {
      reader.setGroupFiles(false);
      reader.setId(file);
      coreMetadata = reader.getCoreMetadataList().get(0);
      tiles = reader.getTiles();
      imageType = reader.getImageType();
      zOffsets = reader.getZOffsets();
      concatenationIndex = reader.getConcatenationIndex();
      edf = reader.isExtendedDepthOfField();
      pixelSizeX = reader.getPixelSizeX();
      pixelSizeY = reader.getPixelSizeY();
      pixelSizeZ = reader.getPixelSizeZ();
      positionX = reader.getPositionX();
      positionY = reader.getPositionY();
      positionZ = reader.getPositionZ();
      channelNames = reader.getChannelNames();
      timestamp = reader.getTimestamp();
    }
  }

  @Override
  public int compareTo(DicomFileInfo info) {
    if (info.edf != edf) {
      return edf ? 1 : -1;
    }

    String[] infoTypeTokens = info.imageType.split("\\\\");
    String[] thisTypeTokens = this.imageType.split("\\\\");
    int endIndex = (int) Math.min(infoTypeTokens.length, thisTypeTokens.length) - 1;
    for (int i=2; i<endIndex; i++) {
      if (!infoTypeTokens[i].equals(thisTypeTokens[i])) {
        // this logic is intentional, the idea is to sort like this:
        //   ORIGINAL\PRIMARY\VOLUME\NONE
        //   DERIVED\PRIMARY\VOLUME\RESAMPLED
        //   DERIVED\PRIMARY\LABEL\NONE
        if (i < endIndex) {
          return infoTypeTokens[i].compareTo(thisTypeTokens[i]);
        }
        else {
          return thisTypeTokens[i].compareTo(infoTypeTokens[i]);
        }
      }
    }
    // if the image types match (e.g. all DERIVED\PRIMARY\VOLUME\RESAMPLED),
    // then sort in descending order by image width and height
    int infoX = info.coreMetadata.sizeX;
    int infoY = info.coreMetadata.sizeY;
    int thisX = this.coreMetadata.sizeX;
    int thisY = this.coreMetadata.sizeY;
    if (infoX != thisX) {
      return infoX - thisX;
    }
    if (infoY != thisY) {
      return infoY - thisY;
    }
    if (this.zOffsets.size() > 0 && info.zOffsets.size() > 0) {
      return this.zOffsets.get(0).compareTo(info.zOffsets.get(0));
    }

    return this.concatenationIndex - info.concatenationIndex;
  }
}
