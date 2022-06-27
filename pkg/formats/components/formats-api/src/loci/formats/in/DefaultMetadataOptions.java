/*
 * Top-level reader and writer APIs
 */

package loci.formats.in;


/**
 * Default implementation of {@link loci.formats.in.MetadataOptions}.
 */
public class DefaultMetadataOptions implements MetadataOptions {

  private MetadataLevel metadataLevel;
  private boolean validate;

  /**
   * Construct a new {@code DefaultMetadataOptions}. Set the metadata level
   * to {@link MetadataLevel#ALL} and disable file validation.
   */
  public DefaultMetadataOptions() {
    this(MetadataLevel.ALL);
  }

  /**
   * Construct a new {@code DefaultMetadataOptions}. Set the metadata level
   * to the specified value and disable file validation.
   *
   * @param level the {@link loci.formats.in.MetadataLevel} to use.
   */
  public DefaultMetadataOptions(MetadataLevel level) {
    metadataLevel = level;
    validate = false;
  }

  @Override
  public MetadataLevel getMetadataLevel() {
    return metadataLevel;
  }

  @Override
  public void setMetadataLevel(MetadataLevel level) {
    metadataLevel = level;
  }

  @Override
  public boolean isValidate() {
    return validate;
  }

  @Override
  public void setValidate(boolean validateMetadata) {
    validate = validateMetadata;
  }

}
