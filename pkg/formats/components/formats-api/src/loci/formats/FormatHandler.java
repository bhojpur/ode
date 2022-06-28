package loci.formats;

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

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import loci.formats.in.DynamicMetadataOptions;
import loci.formats.in.MetadataLevel;
import loci.formats.in.MetadataOptions;

/**
 * Abstract superclass of all biological file format readers and writers.
 */
public abstract class FormatHandler implements IFormatHandler {

  // -- Constants --

  protected static final Logger LOGGER =
    LoggerFactory.getLogger(FormatHandler.class);

  /** Suffixes for supported compression types. */
  public static final String[] COMPRESSION_SUFFIXES = {"bz2", "gz"};

  // -- Fields --

  /** Name of this file format. */
  protected String format;

  /** Valid suffixes for this file format. */
  protected String[] suffixes;

  /** Name of current file. */
  protected String currentId;

  /** Metadata parsing options. */
  protected MetadataOptions metadataOptions = new DynamicMetadataOptions();

  // -- Constructors --

  /** Constructs a format handler with the given name and default suffix. */
  public FormatHandler(String format, String suffix) {
    this(format, suffix == null ? null : new String[] {suffix});
  }

  /** Constructs a format handler with the given name and default suffixes. */
  public FormatHandler(String format, String[] suffixes) {
    this.format = format;
    this.suffixes = suffixes == null ? new String[0] : suffixes;
  }

  // -- IMetadataConfigurable API methods --

  /* (non-Javadoc)
   * @see loci.formats.IMetadataConfigurable#getSupportedMetadataLevels()
   */
  @Override
  public Set<MetadataLevel> getSupportedMetadataLevels() {
    Set<MetadataLevel> supportedLevels = new HashSet<MetadataLevel>();
    supportedLevels.add(MetadataLevel.ALL);
    supportedLevels.add(MetadataLevel.NO_OVERLAYS);
    supportedLevels.add(MetadataLevel.MINIMUM);
    return supportedLevels;
  }

  /* (non-Javadoc)
   * @see loci.formats.IMetadataConfigurable#getMetadataOptions()
   */
  @Override
  public MetadataOptions getMetadataOptions() {
    return metadataOptions;
  }

  /* (non-Javadoc)
   * @see loci.formats.IMetadataConfigurable#setMetadataOptions(loci.formats.in.MetadataOptions)
   */
  @Override
  public void setMetadataOptions(MetadataOptions options) {
    this.metadataOptions = options;
  }

  // -- IFormatHandler API methods --

  /**
   * Checks if a file matches the type of this format handler.
   * The default implementation checks filename suffixes against
   * those known for this format.
   */
  @Override
  public boolean isThisType(String name) {
    return checkSuffix(name, suffixes);
  }

  /* @see IFormatHandler#getFormat() */
  @Override
  public String getFormat() { return format; }

  /* @see IFormatHandler#getSuffixes() */
  @Override
  public String[] getSuffixes() { return suffixes; }

  /* @see IFormatHandler#getNativeDataType() */
  @Override
  public Class<?> getNativeDataType() {
    // NB: Handlers use byte arrays by default as the native type.
    return byte[].class;
  }

  // -- Utility methods --

  /** Performs suffix matching for the given filename. */
  public static boolean checkSuffix(String name, String suffix) {
    return checkSuffix(name, new String[] {suffix});
  }

  /** Performs suffix matching for the given filename. */
  public static boolean checkSuffix(String name, String[] suffixList) {
    String lname = name.toLowerCase();
    for (int i=0; i<suffixList.length; i++) {
      String s = "." + suffixList[i];
      if (lname.endsWith(s)) return true;
      for (int j=0; j<COMPRESSION_SUFFIXES.length; j++) {
        if (lname.endsWith(s + "." + COMPRESSION_SUFFIXES[j])) return true;
      }
    }
    return false;
  }

}
