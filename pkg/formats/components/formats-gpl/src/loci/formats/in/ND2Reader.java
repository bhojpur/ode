/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.in;

import loci.formats.DelegateReader;
import loci.formats.FormatTools;

/**
 * ND2Reader is the file format reader for Nikon ND2 files.
 * It does not read files directly, but chooses which ND2 reader is
 * more appropriate.
 *
 * @see NativeND2Reader
 * @see LegacyND2Reader
 */
public class ND2Reader extends DelegateReader {

  // -- Constructor --

  /** Constructs a new ND2 reader. */
  public ND2Reader() {
    super("Nikon ND2", "nd2");
    nativeReader = new NativeND2Reader();
    legacyReader = new LegacyND2Reader();
    nativeReaderInitialized = false;
    legacyReaderInitialized = false;
    domains = new String[] {FormatTools.LM_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#getOptimalTileHeight() */
  @Override
  public int getOptimalTileHeight() {
    FormatTools.assertId(currentId, true, 1);
    return getSizeY();
  }

}
