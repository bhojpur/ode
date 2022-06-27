/*
 * Top-level reader and writer APIs
 */

package loci.formats.meta;

/**
 *
 */
public class FilterMetadata extends ode.xml.meta.FilterMetadata implements MetadataStore {

  public FilterMetadata(MetadataStore store, boolean filter) {
    super(store, filter);
  }

}
