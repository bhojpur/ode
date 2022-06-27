/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.dicom;

import loci.common.Region;

/**
 * Represents a tile stored in a DICOM file.
 * This may be a tile in a larger image, or a full plane.
 */
public class DicomTile {
  public Region region;
  public String file;
  public int fileIndex;
  public long fileOffset;
  public long endOffset;
  public Double zOffset;
  public int channel;
  public boolean last = false;

  public boolean isJP2K = false;
  public boolean isJPEG = false;
  public boolean isRLE = false;
  public boolean isDeflate = false;
}
