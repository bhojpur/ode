/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;

import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.cache.CacheException;
import loci.formats.cache.ICacheSource;


/**
 * Retrieves BufferedImages from a data source using ODE-Formats.
 */
public class BufferedImageSource implements ICacheSource {

  // -- Fields --

  /** Image reader from which to draw BufferedImages. */
  protected BufferedImageReader reader;

  // -- Constructors --

  public BufferedImageSource(IFormatReader reader) throws CacheException {
    if (reader instanceof BufferedImageReader) {
      this.reader = (BufferedImageReader) reader;
    }
    else {
      this.reader = new BufferedImageReader(reader);
    }
  }

  // -- ICacheSource API methods --

  /* @see loci.formats.cache.ICacheSource#getObject(int) */
  @Override
  public int getObjectCount() { return reader.getImageCount(); }

  /* @see loci.formats.cache.ICacheSource#getObject(int) */
  @Override
  public Object getObject(int index) throws CacheException {
    BufferedImage bi = null;
    try {
      bi = reader.openImage(index);
    }
    catch (FormatException exc) {
      throw new CacheException(exc);
    }
    catch (IOException exc) {
      throw new CacheException(exc);
    }
    return bi;
  }

}
