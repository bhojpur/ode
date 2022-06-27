/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.out;

import loci.formats.FormatTools;

/**
 * JPEGWriter is the file format writer for JPEG files.
 */
public class JPEGWriter extends ImageIOWriter {

  // -- Constructor --

  public JPEGWriter() {
    super("JPEG", new String[] {"jpg", "jpeg", "jpe"}, "jpeg");
  }

  // -- IFormatWriter API methods --

  /* @see loci.formats.IFormatWriter#getPixelTypes(String) */
  @Override
  public int[] getPixelTypes(String codec) {
    return new int[] {FormatTools.UINT8};
  }

}
