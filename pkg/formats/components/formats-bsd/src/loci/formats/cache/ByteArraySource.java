/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.cache;

import java.io.IOException;

import loci.formats.FormatException;
import loci.formats.IFormatReader;

/**
 * Retrieves byte arrays from a data source
 * (e.g., a file) using ODE-Formats.
 */
public class ByteArraySource extends CacheSource {

  // -- Constructors --

  /** Constructs a byte array source from the given ODE-Formats reader. */
  public ByteArraySource(IFormatReader r) { super(r); }

  /** Constructs a byte array source that draws from the given file. */
  public ByteArraySource(String id) throws CacheException { super(id); }

  // -- ICacheSource API methods --

  /* @see loci.formats.cache.ICacheSource#getObject(int) */
  @Override
  public Object getObject(int index) throws CacheException {
    try { return reader.openBytes(index); }
    catch (FormatException exc) { throw new CacheException(exc); }
    catch (IOException exc) { throw new CacheException(exc); }
  }

}
