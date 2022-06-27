/*
 * Top-level reader and writer APIs
 */

package loci.formats;

import java.io.Closeable;
import java.io.IOException;

/**
 * Interface for all biological file format readers and writers.
 */
public interface IFormatHandler extends Closeable, IMetadataConfigurable {

  /** Checks if the given string is a valid filename for this file format. */
  boolean isThisType(String name);

  /** Gets the name of this file format. */
  String getFormat();

  /** Gets the default file suffixes for this file format. */
  String[] getSuffixes();

  /**
   * Returns the native data type of image planes for this reader, as returned
   * by {@link IFormatReader#openPlane} or {@link IFormatWriter#savePlane}.
   * For most readers this type will be a byte array; however, some readers
   * call external APIs that work with other types such as
   * {@link java.awt.image.BufferedImage}.
   */
  Class<?> getNativeDataType();

  /** Sets the current file name. */
  void setId(String id) throws FormatException, IOException;

}
