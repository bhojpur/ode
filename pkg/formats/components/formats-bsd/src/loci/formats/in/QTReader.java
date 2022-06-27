/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.in;

import loci.formats.DelegateReader;
import loci.formats.FormatTools;

/**
 * QTReader is the file format reader for QuickTime movie files.
 * It does not read files directly, but chooses which QuickTime reader is
 * more appropriate.
 *
 * @see NativeQTReader
 * @see LegacyQTReader
 */
public class QTReader extends DelegateReader {

  // -- Constructor --

  /** Constructs a new QuickTime reader. */
  public QTReader() {
    super("QuickTime", "mov");
    nativeReader = new NativeQTReader();
    legacyReader = new LegacyQTReader();
    nativeReaderInitialized = false;
    legacyReaderInitialized = false;
    domains = new String[] {FormatTools.GRAPHICS_DOMAIN};
  }

}
