/*
 * Top-level reader and writer APIs
 */

package loci.formats;

import java.util.Set;

import loci.formats.in.MetadataLevel;
import loci.formats.in.MetadataOptions;

public interface IMetadataConfigurable{

  Set<MetadataLevel> getSupportedMetadataLevels();

  void setMetadataOptions(MetadataOptions options);

  MetadataOptions getMetadataOptions();

}
