/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.cache;

import java.io.IOException;

import loci.formats.FileStitcher;
import loci.formats.FormatException;
import loci.formats.IFormatReader;

/**
 * Superclass of cache sources that retrieve image planes
 * from a data source (e.g., a file) using ODE-Formats.
 */
public abstract class CacheSource implements ICacheSource {

  // -- Fields --

  /** Reader from which to draw image planes. */
  protected IFormatReader reader;

  // -- Constructors --

  /** Constructs a cache source from the given ODE-Formats reader. */
  public CacheSource(IFormatReader r) { reader = r; }

  /** Constructs a cache source that draws from the given file. */
  public CacheSource(String id) throws CacheException {
    this(new FileStitcher());
    try { reader.setId(id); }
    catch (FormatException exc) { throw new CacheException(exc); }
    catch (IOException exc) { throw new CacheException(exc); }
  }

  // -- ICacheSource API methods --

  /* @see loci.formats.cache.ICacheSource#getObjectCount() */
  @Override
  public int getObjectCount() { return reader.getImageCount(); }

  /* @see ICacheSource#getObject(int) */
  @Override
  public abstract Object getObject(int index) throws CacheException;

}
