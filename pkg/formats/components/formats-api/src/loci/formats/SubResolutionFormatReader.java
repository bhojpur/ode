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

import java.io.IOException;
import java.util.List;

/**
 * Abstract superclass of all biological file format readers
 * that can handle image pyramids.
 */
public abstract class SubResolutionFormatReader extends FormatReader {

  // -- Fields --

  /**
   * List of {@link CoreMetadata} objects for each series and resolution.
   */
  protected CoreMetadataList core;

  // -- Constructors --

  /** Constructs a format reader with the given name and default suffix. */
  public SubResolutionFormatReader(String format, String suffix) { super(format, suffix); }

  /** Constructs a format reader with the given name and default suffixes. */
  public SubResolutionFormatReader(String format, String[] suffixes) {
    super(format, suffixes);
  }

  // -- Internal FormatReader API methods --

  /**
   * Initializes the given file (parsing header information, etc.).
   * Most subclasses should override this method to perform
   * initialization operations such as parsing metadata.
   *
   * @throws FormatException if a parsing error occurs processing the file.
   * @throws IOException if an I/O error occurs processing the file
   */
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);

    core = new CoreMetadataList();
    CoreMetadata core0 = new CoreMetadata();
    core.add(core0);
    core0.orderCertain = true;
  }

  /**
   * Call {@link #updateMetadataLists(Hashtable)} on
   * all metadata hashtables.
   */
  protected void flattenHashtables() {
    updateMetadataLists(metadata);

    for (int s=0; s<core.size(); s++) {
      for (int r=0; r<core.size(s); r++) {
        if (core.get(s, r).seriesMetadata.size() > 0) {
          updateMetadataLists(core.get(s, r).seriesMetadata);
        }
      }
    }
  }

  // -- IFormatReader API methods --

  /* @see IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      core = null;
    }
  }

  /**
   * @see IFormatReader#getSeriesCount()
   * Note that the value can change depending upon whether resolutions are flattened,
   * and so reader implementations should use the series and resolutions fields internally.
   */
  @Override
  public int getSeriesCount() {
    FormatTools.assertId(currentId, true, 1);
    if (hasFlattenedResolutions()) {
      return core.flattenedSize();
    }
    else {
      return core.size();
    }
  }

  /**
   * @see IFormatReader#setSeries(int)
   * Note that the value can change depending upon whether resolutions are flattened,
   * and so reader implementations should use the series and resolutions fields internally.
   */
  @Override
  public void setSeries(int no) {
    if (hasFlattenedResolutions()) {
      int[] pos = core.flattenedIndexes(no);
      series = pos[0];
      resolution = pos[1];
    }
    else {
      series = no;
      resolution = 0;
    }
  }

  /* @see IFormatReader#getSeries() */
  @Override
  public int getSeries() {
    if (hasFlattenedResolutions()) {
      return core.flattenedIndex(series, resolution);
    }
    else {
      return series;
    }
  }

  /* @see IFormatReader#getcoredataList() */
  @Override
  public List<CoreMetadata> getCoreMetadataList() {
    FormatTools.assertId(currentId, true, 1);
    return core.getFlattenedList();
  }

  // -- Sub-resolution API methods --

  @Override
  public int seriesToCoreIndex(int s) {
    if (hasFlattenedResolutions()) {
      return s;
    }
    else {
      return core.flattenedIndex(s, 0);
    }
  }

  @Override
  public int coreIndexToSeries(int index) {
    if (hasFlattenedResolutions()) {
      return index;
    }
    else {
      return core.flattenedIndexes(index)[0];
    }
  }

  /* @see IFormatReader#getResolutionCount() */
  @Override
  public int getResolutionCount() {
    FormatTools.assertId(currentId, true, 1);

    if (hasFlattenedResolutions()) {
      return 1;
    }
    else {
      return core.size(series);
    }
  }

  /* @see IFormatReader#setResolution(int) */
  @Override
  public void setResolution(int no) {
    if (no < 0 || no >= getResolutionCount()) {
      throw new IllegalArgumentException("Invalid resolution: " + no);
    }
    if (!hasFlattenedResolutions()) {
      resolution = no;
    }
  }

  /* @see IFormatReader#getResolution() */
  @Override
  public int getResolution() {
    if (hasFlattenedResolutions()) {
      return 0;
    }
    else {
      return resolution;
    }
  }

  @Override
  public int getCoreIndex() {
    return core.flattenedIndex(series, resolution);
  }

  /* @see IFormatHandler#setCoreIndex(int) */
  @Override
  public void setCoreIndex(int no) {
    if (no < 0 || no >= core.flattenedSize()) {
      throw new IllegalArgumentException("Invalid series: " + no);
    }
    int[] pos = core.flattenedIndexes(no);
    series = pos[0];
    resolution = pos[1];
  }

  /**
   * Get the CoreMetadata corresponding to the current series and resolution
   *
   * @return the CoreMetadata
   */
  protected CoreMetadata getCurrentCore() {
    return core.get(series, resolution);
  }
}
