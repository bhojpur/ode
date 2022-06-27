/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.out;

import java.io.IOException;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.FormatWriter;
import loci.formats.codec.CompressionType;
import loci.formats.codec.JPEG2000Codec;
import loci.formats.codec.JPEG2000CodecOptions;
import loci.formats.meta.MetadataRetrieve;

/**
 * JPEG2000Writer is the file format writer for JPEG2000 files.
 */
public class JPEG2000Writer extends FormatWriter {

  // -- Fields --

  // -- Constructor --

  /** Creates a new instance. */
  public JPEG2000Writer() {
    super("JPEG-2000", "jp2");
    compressionTypes = new String[] {CompressionType.J2K_LOSSY.getCompression(), 
        CompressionType.J2K.getCompression()};
    //The default codec options
    options = JPEG2000CodecOptions.getDefaultOptions();
  }

  // -- IFormatWriter API methods --

  /**
   * @see loci.formats.IFormatWriter#saveBytes(int, byte[], int, int, int, int)
   */
  @Override
  public void saveBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    checkParams(no, buf, x, y, w, h);
    
    /*
    if (!isFullPlane(x, y, w, h)) {
      throw new FormatException(
        "JPEG2000Writer does not yet support saving image tiles.");
    }
    */
    //MetadataRetrieve retrieve = getMetadataRetrieve();
    //int width = retrieve.getPixelsSizeX(series).getValue().intValue();
    //int height = retrieve.getPixelsSizeY(series).getValue().intValue();
   
    out.write(compressBuffer(no, buf, x, y, w, h));
  }

  /**
   * Compresses the buffer.
   * 
   * @param no the plane index within the series.
   * @param buf the byte array that represents the image tile.
   * @param x the X coordinate of the upper-left corner of the image tile.
   * @param y the Y coordinate of the upper-left corner of the image tile.
   * @param w the width (in pixels) of the image tile.
   * @param h the height (in pixels) of the image tile.
   * @throws FormatException if one of the parameters is invalid.
   * @throws IOException if there was a problem writing to the file.
   */
  public byte[] compressBuffer(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    checkParams(no, buf, x, y, w, h);
    MetadataRetrieve retrieve = getMetadataRetrieve();
    boolean littleEndian = false;
    if (retrieve.getPixelsBigEndian(series) != null) {
      littleEndian = !retrieve.getPixelsBigEndian(series).booleanValue();
    }
    else if (retrieve.getPixelsBinDataCount(series) == 0) {
      littleEndian = !retrieve.getPixelsBinDataBigEndian(series, 0).booleanValue();
    }
    int bytesPerPixel = FormatTools.getBytesPerPixel(
      FormatTools.pixelTypeFromString(
      retrieve.getPixelsType(series).toString()));
    int nChannels = getSamplesPerPixel();

    //To be on the save-side
    if (options == null) options = JPEG2000CodecOptions.getDefaultOptions();
    options = new JPEG2000CodecOptions(options);
    options.width = w;
    options.height = h;
    options.channels = nChannels;
    options.bitsPerSample = bytesPerPixel * 8;
    options.littleEndian = littleEndian;
    options.interleaved = interleaved;
    options.lossless = compression == null || 
    compression.equals(CompressionType.J2K.getCompression());
    options.colorModel = getColorModel();

    return new JPEG2000Codec().compress(buf, options);
  }
    
  /**
   * Overridden to indicate that stacks are not supported. 
   * @see loci.formats.IFormatWriter#canDoStacks() 
   */
  @Override
  public boolean canDoStacks() { return false; }

  /**
   * Overridden to return the formats supported by the writer.
   * @see loci.formats.IFormatWriter#getPixelTypes(String) 
   */
  @Override
  public int[] getPixelTypes(String codec) {
    return new int[] {FormatTools.INT8, FormatTools.UINT8, FormatTools.INT16,
      FormatTools.UINT16, FormatTools.INT32, FormatTools.UINT32};
  }

}
