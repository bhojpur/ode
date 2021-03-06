package loci.formats.in;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import loci.common.DataTools;
import loci.common.Location;
import loci.formats.AxisGuesser;
import loci.formats.ClassList;
import loci.formats.CoreMetadata;
import loci.formats.FileStitcher;
import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.ReaderWrapper;
import loci.formats.WrappedReader;
import loci.formats.meta.MetadataStore;

/**
 *
 */
public class FilePatternReader extends WrappedReader {

  // -- Fields --

  private FileStitcher helper;

  // -- Constructor --

  /** Constructs a new pattern reader. */
  public FilePatternReader() {
    super("File pattern", new String[] {"pattern"});

    ClassList<IFormatReader> classes = ImageReader.getDefaultReaderClasses();
    Class<? extends IFormatReader>[] classArray = classes.getClasses();
    ClassList<IFormatReader> newClasses =
      new ClassList<IFormatReader>(IFormatReader.class);
    for (Class<? extends IFormatReader> c : classArray) {
      if (!c.equals(FilePatternReader.class)) {
        newClasses.addClass(c);
      }
    }
    helper = new FileStitcher(new ImageReader(newClasses));

    suffixSufficient = true;
  }

  // -- WrappedReader methods --

  protected ReaderWrapper getHelper() {
    return helper;
  }

  // -- FormatReader methods that are not overridden by WrappedReader --

  @Override
  public void setGroupFiles(boolean group) {
    getHelper().setGroupFiles(group);
  }

  @Override
  public boolean isGroupFiles() {
    return getHelper().isGroupFiles();
  }

  @Override
  public void setNormalized(boolean normalize) {
    getHelper().setNormalized(normalize);
  }

  @Override
  public boolean isNormalized() { return getHelper().isNormalized(); }

  @Override
  public void setOriginalMetadataPopulated(boolean populate) {
    getHelper().setOriginalMetadataPopulated(populate);
  }

  @Override
  public boolean isOriginalMetadataPopulated() {
    return getHelper().isOriginalMetadataPopulated();
  }

  @Override
  public void setMetadataFiltered(boolean filter) {
    getHelper().setMetadataFiltered(filter);
  }

  @Override
  public boolean isMetadataFiltered() { return getHelper().isMetadataFiltered(); }

  @Override
  public void setMetadataStore(MetadataStore store) {
    getHelper().setMetadataStore(store);
  }

  @Override
  public MetadataStore getMetadataStore() {
    return getHelper().getMetadataStore();
  }

  @Override
  public boolean hasFlattenedResolutions() {
    return getHelper().hasFlattenedResolutions();
  }

  @Override
  public void setFlattenedResolutions(boolean flattened) {
    getHelper().setFlattenedResolutions(flattened);
  }

  // -- IFormatReader methods --

  @Override
  public byte[][] get8BitLookupTable() throws FormatException, IOException {
    if (getCurrentFile() == null) {
      return null;
    }
    return helper.get8BitLookupTable();
  }

  @Override
  public short[][] get16BitLookupTable() throws FormatException, IOException {
    if (getCurrentFile() == null) {
      return null;
    }
    return helper.get16BitLookupTable();
  }

  @Override
  public String[] getSeriesUsedFiles(boolean noPixels) {
    if (noPixels) {
      return new String[] {currentId};
    }
    String[] helperFiles = helper.getSeriesUsedFiles(noPixels);
    String[] allFiles = new String[helperFiles.length + 1];
    allFiles[0] = currentId;
    System.arraycopy(helperFiles, 0, allFiles, 1, helperFiles.length);
    return allFiles;
  }

  @Override
  public String[] getUsedFiles(boolean noPixels) {
    if (noPixels) {
      return new String[] {currentId};
    }
    String[] helperFiles = helper.getUsedFiles(noPixels);
    String[] allFiles = new String[helperFiles.length + 1];
    allFiles[0] = currentId;
    System.arraycopy(helperFiles, 0, allFiles, 1, helperFiles.length);
    return allFiles;
  }

  @Override
  public List<CoreMetadata> getCoreMetadataList() {
    // Only used for determining the object type.
    List<CoreMetadata> oldcore = helper.getCoreMetadataList();
    List<CoreMetadata> newcore = new ArrayList<CoreMetadata>();

    for (int s=0; s<oldcore.size(); s++) {
      CoreMetadata newMeta = oldcore.get(s).clone(this, s);
      newMeta.resolutionCount = oldcore.get(s).resolutionCount;
      newcore.add(newMeta);
    }

    return newcore;
  }

  @Override
  public boolean isSingleFile(String id) throws FormatException, IOException {
    return false;
  }

  @Override
  public boolean hasCompanionFiles() {
    return true;
  }

  // -- Internal FormatReader methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    // read the pattern from the file
    // the file should just contain a single line with the relative or
    // absolute file pattern

    currentId = new Location(id).getAbsolutePath();
    String pattern = DataTools.readFile(id).trim();
    String dir = new Location(id).getAbsoluteFile().getParent();
    if (new Location(pattern).getParent() == null) {
      pattern = dir + File.separator + pattern;
    }

    helper.setUsingPatternIds(true);
    helper.setCanChangePattern(false);
    helper.setId(pattern);
    core = helper.getCoreMetadataList();

    if (getEffectiveSizeC() > 1) {
      MetadataStore store = makeFilterMetadata();
      String[][] elements = helper.getFilePattern().getElements();
      int[] axisTypes = helper.getAxisTypes();
      int nextChannel = 0;
      for (int i=0; i<axisTypes.length; i++) {
        if (axisTypes[i] == AxisGuesser.C_AXIS) {
          for (int c=0; c<elements[i].length; c++) {
            if (nextChannel < getEffectiveSizeC()) {
              store.setChannelName(elements[i][c], 0, nextChannel++);
            }
          }
        }
      }
    }
  }

}
