/*
 * Top-level reader and writer APIs
 */

package loci.formats;

import java.awt.image.ColorModel;
import java.io.IOException;
import java.util.List;

import loci.common.Region;
import loci.formats.codec.CodecOptions;
import loci.formats.meta.MetadataRetrieve;

/**
 * Interface for all biological file format writers.
 */
public interface IFormatWriter extends IFormatHandler, IPyramidHandler {

  /**
   * Saves the given image to the current series in the current file.
   *
   * @param no the plane index within the series.
   * @param buf the byte array that represents the image.
   * @throws FormatException if one of the parameters is invalid.
   * @throws IOException if there was a problem writing to the file.
   */
  void saveBytes(int no, byte[] buf) throws FormatException, IOException;

  /**
   * Saves the given image tile to the current series in the current file.
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
  void saveBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException;

  /**
   * Saves the given image tile to the current series in the current file.
   *
   * @param no the plane index within the series.
   * @param buf the byte array that represents the image tile.
   * @param tile the Region representing the image tile to be read.
   * @throws FormatException if one of the parameters is invalid.
   * @throws IOException if there was a problem writing to the file.
   */
  void saveBytes(int no, byte[] buf, Region tile)
    throws FormatException, IOException;

  /**
   * Saves the given image plane to the current series in the current file.
   *
   * @param no the plane index within the series.
   * @param plane the image plane.
   * @throws FormatException if one of the parameters is invalid.
   * @throws IOException if there was a problem writing to the file.
   */
  void savePlane(int no, Object plane) throws FormatException, IOException;

  /**
   * Saves the given image plane to the current series in the current file.
   *
   * @param no the plane index within the series.
   * @param plane the image plane.
   * @param x the X coordinate of the upper-left corner of the image tile.
   * @param y the Y coordinate of the upper-left corner of the image tile.
   * @param w the width (in pixels) of the image tile.
   * @param h the height (in pixels) of the image tile.
   * @throws FormatException if one of the parameters is invalid.
   * @throws IOException if there was a problem writing to the file.
   */
  void savePlane(int no, Object plane, int x, int y, int w, int h)
    throws FormatException, IOException;

  /**
   * Saves the given image plane to the current series in the current file.
   *
   * @param no the plane index within the series.
   * @param plane the image plane.
   * @param tile the Region representing the image tile to be read.
   * @throws FormatException if one of the parameters is invalid.
   * @throws IOException if there was a problem writing to the file.
   */
  void savePlane(int no, Object plane, Region tile)
    throws FormatException, IOException;

  /**
   * Sets the current series.
   *
   * @param series the series index, starting from 0.
   * @throws FormatException if the specified series is invalid.
   */
  void setSeries(int series) throws FormatException;

  /** Returns the current series. */
  int getSeries();

  /** Sets whether or not the channels in an image are interleaved. */
  void setInterleaved(boolean interleaved);

  /** Sets the number of valid bits per pixel. */
  void setValidBitsPerPixel(int bits);

  /** Gets whether or not the channels in an image are interleaved. */
  boolean isInterleaved();

  /** Reports whether the writer can save multiple images to a single file. */
  boolean canDoStacks();

  /**
   * Sets the metadata retrieval object from
   * which to retrieve standardized metadata.
   */
  void setMetadataRetrieve(MetadataRetrieve r);

  /**
   * Retrieves the current metadata retrieval object for this writer. You can
   * be assured that this method will <b>never</b> return a <code>null</code>
   * metadata retrieval object.
   * @return A metadata retrieval object.
   */
  MetadataRetrieve getMetadataRetrieve();

  /** Sets the color model. */
  void setColorModel(ColorModel cm);

  /** Gets the color model. */
  ColorModel getColorModel();

  /** Sets the frames per second to use when writing. */
  void setFramesPerSecond(int rate);

  /** Gets the frames per second to use when writing. */
  int getFramesPerSecond();

  /** Gets the available compression types. */
  String[] getCompressionTypes();

  /** Gets the supported pixel types. */
  int[] getPixelTypes();

  /** Gets the supported pixel types for the given codec. */
  int[] getPixelTypes(String codec);

  /** Checks if the given pixel type is supported. */
  boolean isSupportedType(int type);

  /** Sets the current compression type. */
  void setCompression(String compress) throws FormatException;

  /**
   * Sets the codec options.
   * @param options The options to set.
   */
  void setCodecOptions(CodecOptions options) ;

  /** Gets the current compression type. */
  String getCompression();

  /** Switch the output file for the current dataset. */
  void changeOutputFile(String id) throws FormatException, IOException;

  /**
   * Sets whether or not we know that planes will be written sequentially.
   * If planes are written sequentially and this flag is set, then performance
   * will be slightly improved.
   */
  void setWriteSequentially(boolean sequential);

  /**
   * Retrieves the current tile width
   * Defaults to 0 if not supported
   * @return The current tile width being used
   * @throws FormatException Image metadata including Pixels Size X must be set prior to calling getTileSizeX()
   */
  int getTileSizeX() throws FormatException;

  /**
   * Will attempt to set the tile width to the desired value and return the actual value which will be used
   * @param tileSize The tile width you wish to use. Setting to 0 will disable tiling
   * @return The tile width which will actually be used, this may differ from the value requested.
   *         If the requested value is not supported the writer will return and use the closest appropriate value.
   * @throws FormatException Tile size must be greater than or equal to 0 and less than the image width
   */
  int setTileSizeX(int tileSize) throws FormatException;

  /**
   * Retrieves the current tile height
   * Defaults to 0 if not supported
   * @return The current tile height being used
   * @throws FormatException Image metadata including Pixels Size Y must be set prior to calling getTileSizeY()
   */
  int getTileSizeY() throws FormatException;

  /**
   * Will attempt to set the tile height to the desired value and return the actual value which will be used
   * @param tileSize The tile height you wish to use. Setting to 0 will disable tiling
   * @return The tile height which will actually be used, this may differ from the value requested.
   *         If the requested value is not supported the writer will return and use the closest appropriate value.
   * @throws FormatException Tile size must be greater than or equal to 0 and less than the image height
   */
  int setTileSizeY(int tileSize) throws FormatException;

  /**
   * Specify a list of resolution objects for the current series.
   * If resolutions are specified using this method, then any resolution
   * data supplied via the MetadataRetrieve will be ignored.
   */
  void setResolutions(List<Resolution> resolutions);

  /**
   * Get a list of resolution objects for the current series.
   */
  List<Resolution> getResolutions();

}
