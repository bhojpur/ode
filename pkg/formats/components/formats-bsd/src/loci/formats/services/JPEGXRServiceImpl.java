/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.services;

import java.io.IOException;

import loci.common.RandomAccessInputStream;
import loci.common.services.AbstractService;
import loci.formats.FormatException;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffParser;

import ode.jxrlib.Decode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface defining methods for working with JPEG-XR data
 */
public class JPEGXRServiceImpl extends AbstractService implements JPEGXRService {

  // see table A.4 in ITU-T T.832
  private static final int PIXEL_FORMAT_TAG = 0xbc01;

  // see table A.6 in ITU-T T.832
  private static final short BGR_24 = 0x0c;
  private static final short BGR_32 = 0x0e;
  private static final short BGRA_32 = 0x0f;
  private static final short PBGRA_32 = 0x10;

  private static final Logger LOGGER =
    LoggerFactory.getLogger(JPEGXRServiceImpl.class);

  public JPEGXRServiceImpl() {
    checkClassDependency(ode.jxrlib.Decode.class);
  }

  /**
   * @see JPEGXRServiceImpl#decompress(byte[])
   */
  public byte[] decompress(byte[] compressed) throws FormatException {
      LOGGER.trace("begin tile decode; compressed size = {}", compressed.length);
      try {
        byte[] raw = Decode.decodeFirstFrame(compressed, 0, compressed.length);
        short[] format = getPixelFormat(compressed);

        if (isBGR(format)) {
          int bpp = getBGRComponents(format);
          // only happens with 8 bits per channel,
          // 3 (BGR) or 4 (BGRA) channel data
          for (int p=0; p<raw.length; p+=bpp) {
            byte tmp = raw[p];
            raw[p] = raw[p + 2];
            raw[p + 2] = tmp;
          }
        }
        return raw;
      }
      // really only want to catch ode.jxrlib.FormatError, but that doesn't compile
      catch (Exception e) {
        throw new FormatException(e);
      }
  }

  private short[] getPixelFormat(byte[] stream) throws FormatException, IOException {
    try (RandomAccessInputStream s = new RandomAccessInputStream(stream)) {
      s.order(true);
      s.seek(4);
      long ifdPointer = s.readInt();
      TiffParser p = new TiffParser(s);
      IFD ifd = p.getIFD(ifdPointer);
      return ifd.getIFDShortArray(PIXEL_FORMAT_TAG);
    }
  }

  private boolean isBGR(short[] format) {
    short lastByte = format[format.length - 1];
    return lastByte == BGR_24 || lastByte == BGR_32 ||
      lastByte == BGRA_32 || lastByte == PBGRA_32;
  }

  private int getBGRComponents(short[] format) {
    short lastByte = format[format.length - 1];
    return lastByte == BGR_24 || lastByte == BGR_32 ? 3 : 4;
  }

}
