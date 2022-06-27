/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.cache;

/**
 * Interface for cache sources. A cache source returns objects
 * in a defined order, from 0 to getObjectCount() - 1.
 * The actual source of the objects is implementation-dependent;
 * ODE-Formats uses {@link loci.formats.IFormatReader} to obtain image planes,
 * but any ordered collection of objects can conceivably be cached.
 */
public interface ICacheSource {

  /** Get the total number of objects that this source can retrieve. */
  int getObjectCount() throws CacheException;

  /** Get the object corresponding to the given index. */
  Object getObject(int index) throws CacheException;

}
