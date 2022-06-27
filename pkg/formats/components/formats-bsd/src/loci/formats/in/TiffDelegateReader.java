/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.in;

import java.io.IOException;
import java.util.ArrayList;

import loci.formats.CoreMetadata;
import loci.formats.DelegateReader;
import loci.formats.FormatException;
import loci.formats.UnsupportedCompressionException;

/**
 * TiffDelegateReader is a file format reader for TIFF files.
 * It does not read files directly, but chooses which TIFF reader
 * is more appropriate.
 *
 * @see TiffReader
 * @see TiffJAIReader
 */
public class TiffDelegateReader extends DelegateReader {

  // -- Constructor --

  /** Constructs a new TIFF reader. */
  public TiffDelegateReader() {
    super("Tagged Image File Format", TiffReader.TIFF_SUFFIXES);
    nativeReader = new TiffReader();
    legacyReader = new TiffJAIReader();
    nativeReaderInitialized = false;
    legacyReaderInitialized = false;
    suffixNecessary = false;
  }

  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    if (!isLegacy()) {
      try {
        return nativeReader.openBytes(no, buf, x, y, w, h);
      }
      catch (UnsupportedCompressionException e) {
        LOGGER.debug("Could not open plane with native reader", e);
        if (!legacyReaderInitialized) {
          legacyReader.setId(getCurrentFile());
          legacyReaderInitialized = true;
          nativeReader.close();
          nativeReaderInitialized = false;
        }
        return legacyReader.openBytes(no, buf, x, y, w, h);
      }
    }
    return super.openBytes(no, buf, x, y, w, h);
  }

  @Override
  public void setId(String id) throws FormatException, IOException {
    if (isLegacy()) {
      super.setId(id);
    }
    nativeReader.setId(id);
    nativeReaderInitialized = true;
    currentId = nativeReader.getCurrentFile();
    core = new ArrayList<CoreMetadata>(nativeReader.getCoreMetadataList());
    metadata = nativeReader.getGlobalMetadata();
    metadataStore = nativeReader.getMetadataStore();
  }

}
