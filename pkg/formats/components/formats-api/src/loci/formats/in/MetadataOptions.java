/*
 * Top-level reader and writer APIs
 */

package loci.formats.in;


/**
 * Holds metadata-related options.
 */
public interface MetadataOptions {

  /**
   * Set the metadata level.
   *
   * @param level a {@link loci.formats.in.MetadataLevel}.
   */
  void setMetadataLevel(MetadataLevel level);

  /**
   * Get the configured metadata level.
   *
   * @return the configured {@link loci.formats.in.MetadataLevel}.
   */
  MetadataLevel getMetadataLevel();

  /**
   * Specifies whether or not to validate files when reading.
   *
   * @param validate {@code true} if files should be validated, {@code
   * false} otherwise.
   */
  void setValidate(boolean validate);

  /**
   * Checks whether file validation has been set.
   *
   * @return {@code true} if files are validated when read, {@code
   * false} otherwise.
   */
  boolean isValidate();

}
