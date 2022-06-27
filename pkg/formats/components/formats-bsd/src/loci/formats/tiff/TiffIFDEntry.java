/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.tiff;

/**
 * This class represents a single raw TIFF IFD entry. It does not retrieve or
 * store the values from the entry's specific offset and is based on the TIFF
 * 6.0 specification of an IFD entry.
 */
public class TiffIFDEntry implements Comparable<Object> {

  /** The <i>Tag</i> that identifies the field. */
  private int tag;

  /** The field <i>Type</i>. */
  private IFDType type;

  /** The number of values, <i>Count</i> of the indicated <i>Type</i>. */
  private int valueCount;

  /**
   * The <i>Value Offset</i>, the file offset (in bytes) of the <i>Value</i>
   * for the field.
   */
  private long valueOffset;

  public TiffIFDEntry(int tag, IFDType type, int valueCount, long valueOffset) {
    this.tag = tag;
    this.type = type;
    this.valueCount = valueCount;
    this.valueOffset = valueOffset;
  }

  /**
   * Retrieves the entry's <i>Tag</i> value.
   * @return the entry's <i>Tag</i> value.
   */
  public int getTag() { return tag; }

  /**
   * Retrieves the entry's <i>Type</i> value.
   * @return the entry's <i>Type</i> value.
   */
  public IFDType getType() { return type; }

  /**
   * Retrieves the entry's <i>ValueCount</i> value.
   * @return the entry's <i>ValueCount</i> value.
   */
  public int getValueCount() { return valueCount; }

  /**
   * Retrieves the entry's <i>ValueOffset</i> value.
   * @return the entry's <i>ValueOffset</i> value.
   */
  public long getValueOffset() { return valueOffset; }

  @Override
  public String toString() {
    return "tag = " + tag + ", type = " + type + ", count = " + valueCount +
      ", offset = " + valueOffset;
  }

  // -- Comparable API methods --

  @Override
  public int compareTo(Object o) {
    if (!(o instanceof TiffIFDEntry)) return 1;
    long offset = ((TiffIFDEntry) o).getValueOffset();

    if (offset == getValueOffset()) return 0;
    return offset < getValueOffset() ? 1 : -1;
  }

}
