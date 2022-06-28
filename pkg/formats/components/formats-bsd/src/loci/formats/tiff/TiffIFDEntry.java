package loci.formats.tiff;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

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
