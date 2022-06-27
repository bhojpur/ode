/*
 * Top-level reader and writer APIs
 */

package loci.formats.meta;

import ode.xml.meta.MetadataStore;
import ode.xml.meta.MetadataRetrieve;

/**
 * A utility class containing a method for piping a source
 * {@link MetadataRetrieve} object into a destination {@link MetadataStore}.
 *
 * <p>This technique allows conversion between two different storage media.
 * For example, it can be used to convert an <code>ODEMetadataStore</code>
 * (Bhojpur ODE's metadata store implementation) into an
 * {@link loci.formats.ode.ODEXMLMetadata}, thus generating ODE-XML from
 * information in a Bhojpur ODE database.
 *
 * @deprecated Use ode.xml.meta.MetadataConverter.
 */
@Deprecated
public final class MetadataConverter {

  // -- Constructor --

  /**
   * Private constructor; all methods in MetadataConverter
   * are static, so this should not be called.
   */
  private MetadataConverter() { }

  // -- MetadataConverter API methods --

  /**
   * Copies information from a metadata retrieval object
   * (source) into a metadata store (destination).
   */
  public static void convertMetadata(MetadataRetrieve src, MetadataStore dest) {
    ode.xml.meta.MetadataConverter.convertMetadata(src, dest);
  }

}
