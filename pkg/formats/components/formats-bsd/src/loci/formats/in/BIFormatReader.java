/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.in;

import java.awt.image.BufferedImage;
import java.io.IOException;

import loci.common.DataTools;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.gui.AWTImageTools;

/**
 * BIFormatReader is the superclass for file format readers
 * that use java.awt.image.BufferedImage as the native data type.
 */
public abstract class BIFormatReader extends FormatReader {

  // -- Constructors --

  /** Constructs a new BIFormatReader. */
  public BIFormatReader(String name, String suffix) {
    super(name, suffix);
  }

  /** Constructs a new BIFormatReader. */
  public BIFormatReader(String name, String[] suffixes) {
    super(name, suffixes);
  }

  // -- IFormatReader API methods --

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    BufferedImage data = (BufferedImage) openPlane(no, x, y, w, h);
    switch (data.getColorModel().getComponentSize(0)) {
      case 8:
        byte[] t = AWTImageTools.getBytes(data, false);
        System.arraycopy(t, 0, buf, 0, (int) Math.min(t.length, buf.length));
        break;
      case 16:
        short[][] ts = AWTImageTools.getShorts(data);
        for (int c=0; c<ts.length; c++) {
          int offset = c * ts[c].length * 2;
          for (int i=0; i<ts[c].length && offset < buf.length; i++) {
            DataTools.unpackBytes(ts[c][i], buf, offset, 2, isLittleEndian());
            offset += 2;
          }
        }
        break;
    }
    return buf;
  }

  // -- IFormatHandler API methods --

  /* @see loci.formats.IFormatHandler#getNativeDataType() */
  @Override
  public Class<?> getNativeDataType() {
    return BufferedImage.class;
  }

}
