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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CoreMetadataList extends MetadataList<CoreMetadata> {
  static final Comparator<CoreMetadata> comparator = new Comparator<CoreMetadata>() {
    @Override
    public int compare(CoreMetadata c1, CoreMetadata c2) {
      int result = Integer.compare(c1.sizeZ, c2.sizeZ);
      if (result == 0) {
        result = Integer.compare(c1.sizeY, c2.sizeY);
      }
      if (result == 0) {
        result = Integer.compare(c1.sizeX, c2.sizeX);
      }
      return -result; // descending order
    }
  };

  public CoreMetadataList() {
  }

  public CoreMetadataList(CoreMetadataList copy) {
    super(copy);
  }

  /**
   * Construct a list containing a specified number of primary elements.
   *
   * This may be used to specify e.g. the number of image series without any sub-resolutions.  The sub-resolutions,
   * including the full resolution, must be added afterward.
   *
   * @param size1 The number of primary list elements
   */
  public CoreMetadataList(int size1) {
    super(size1);
  }

  /**
   * Construct a list containing a specified number of primary elements and a fixed number of secondary elements.
   *
   * This may be used to specify e.g. the number of image series with a fixed number of sub-resolutions.
   *
   * @param size1 The number of primary list elements
   * @param size2 The number of secondary level list elements
   */
  public CoreMetadataList(int size1, int size2) { super(size1, size2); }

  /**
   * Construct a list containing a specified number of primary and secondary elements.
   *
   * This may be used to specify e.g. the number of image series including all sub-resolutions.
   *
   * @param sizes The number of primary and secondary list elements; the array elements are the secondary element sizes.
   */
  public CoreMetadataList(int[] sizes) { super(sizes); }

  public CoreMetadataList(List<CoreMetadata> list) {
    setFlattenedList(list);
  }

  /**
   * Add a secondary array element to the specified primary array
   * @param i1 The primary array index
   * @param value The element to set
   */
  @Override
  public void add(int i1, CoreMetadata value) {
    super.add(i1, value);
  }

  public void reorder() {
    for (List<CoreMetadata> s : data) {
      Collections.sort(s, comparator);
    }
  }

  public List<CoreMetadata> getFlattenedList() {
    List<CoreMetadata> l = new ArrayList<>();

    int[] sizes = sizes();

    for (int i = 0; i < sizes.length; ++i) {
      if (sizes[i] > 0) {
        get(i, 0).resolutionCount = sizes[i];
      }
      for (int j = 0; j < sizes[i]; ++j) {
        l.add(new CoreMetadata(get(i,j)));
      }
    }

    return l;
  }

  public void setFlattenedList(List<CoreMetadata> list) {
    clear();
    for (int i = 0; i < list.size(); i += list.get(i).resolutionCount) {
      List<CoreMetadata> sublist = new ArrayList<>();
      for (int j = 0; j < list.get(i).resolutionCount; ++j) {
        sublist.add(list.get(i + j));
      }
      add(sublist);
    }
  }

  public List<CoreMetadata> getSeriesList() {
    List<CoreMetadata> l = new ArrayList<>();

    for (int i = 0; i < size(); ++i) {
      l.add(new CoreMetadata(get(i, 0)));
    }
    for (CoreMetadata c : l) {
      c.resolutionCount = 1;
    }

    return l;
  }

  /**
   * Get flattened size (all resolutions in all series)
   * @return The number of all resolutions in all series
   */
  public int flattenedSize() {
    int ncore = 0;
    for (int v : sizes()) {
      ncore += v;
    }
    return ncore;
  }

  public int flattenedIndex(int series, int resolution) {
    int idx = 0;

    if (series < 0 || series >= size()) {
      throw new IllegalArgumentException("Invalid series: " + series);
    }

    for (int i = 0; i < series; ++i) {
      idx += size(i);
    }

    if (resolution < 0 || resolution >= size(series)) {
      throw new IllegalArgumentException("Invalid resolution: " + resolution);
    }

    idx += resolution;

    return idx;
  }

  public int[] flattenedIndexes(int flattenedIndex) {
    int series = 0;
    int resolution = 0;

    if (flattenedIndex >= flattenedSize()) {
      throw new IllegalArgumentException("Invalid flattened index: " + flattenedIndex);
    }

    int found = 0;
    for (int i = 0; i < size(); ++i) {
      if (size(i) <= flattenedIndex - found) {
        ++series;
        found += size(i);
        if (found == flattenedIndex) {
          break;
        }
      } else {
        resolution = flattenedIndex - found;
        break;
      }
    }

    return new int[] {series,resolution};
  }
}
