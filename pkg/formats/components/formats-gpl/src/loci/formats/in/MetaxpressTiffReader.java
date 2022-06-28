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
import java.util.Arrays;
import java.util.List;

import loci.common.Location;
import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.IFormatReader;
import loci.formats.MetadataTools;
import loci.formats.meta.IMetadata;
import loci.formats.meta.MetadataStore;
import loci.formats.ode.ODEXMLMetadata;
import loci.formats.services.ODEXMLService;

import ode.xml.meta.MetadataConverter;
import ode.xml.meta.ODEXMLMetadataRoot;
import ode.xml.model.Image;
import ode.xml.model.Instrument;
import ode.xml.model.primitives.NonNegativeInteger;
import ode.xml.model.primitives.PositiveInteger;
import ode.xml.model.primitives.Timestamp;
import ode.units.UNITS;
import ode.units.quantity.Length;

/**
 * MetaxpressTiffReader is the file format reader for MetaXpress .htd + TIFF files.
 */
public class MetaxpressTiffReader extends CellWorxReader {

  // -- Fields --

  // -- Constructor --

  /** Constructs a new MetaXpress TIFF reader. */
  public MetaxpressTiffReader() {
    super("MetaXpress TIFF", new String[] {"htd", "tif"});
    datasetDescription = "One .htd file plus one or more .tif files";
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(String, boolean) */
  @Override
  public boolean isThisType(String name, boolean open) {
    if (checkSuffix(name, "htd")) {
      return true;
    }
    if (!open) {
      return false;
    }
    return foundHTDFile(name);
  }

  /* @see loci.formats.IFormatReader#getSeriesUsedFiles(boolean) */
  @Override
  public String[] getSeriesUsedFiles(boolean noPixels) {
    FormatTools.assertId(currentId, true, 1);
    final List<String> files = new ArrayList<String>();
    files.add(currentId);

    int row = getWellRow(getSeries());
    int col = getWellColumn(getSeries());

    if (!noPixels) {
      for (String f : wellFiles[row][col]) {
        if (new Location(f).exists()) {
          files.add(f);
        }
      }
    }
    return files.toArray(new String[files.size()]);
  }

  /* @see loci.formats.IFormatReader#getUsedFiles(boolean) */
  @Override
  public String[] getUsedFiles(boolean noPixels) {
    String[] files = super.getUsedFiles(noPixels);

    List<String> allFiles = new ArrayList<String>();
    for (String f : files) {
      allFiles.add(f);
    }
    if (directoryList != null) {
      Location root = new Location(currentId).getParentFile();
      for (String f : directoryList) {
        if (f.toLowerCase().indexOf("_thumb") > 0) {
          String path = new Location(root, f).getAbsolutePath();
          if (!allFiles.contains(path)) {
            allFiles.add(path);
          }
        }
      }
    }
    return allFiles.toArray(new String[allFiles.size()]);
  }

  // -- Internal FormatReader API methods --

  // -- Helper methods --

  protected void findPixelsFiles() throws FormatException {
    // find pixels files
    String plateName = getPlateName(currentId);
    for (int row=0; row<wellFiles.length; row++) {
      for (int col=0; col<wellFiles[row].length; col++) {
        if (wellFiles[row][col] != null) {
          wellCount++;
          char rowLetter = (char) (row + 'A');
          wellFiles[row][col] = getTiffFiles(
            plateName, rowLetter, col, wavelengths.length, nTimepoints, zSteps);
        }
      }
    }
  }

  protected void parseWellLogFile(int wellIndex, MetadataStore store)
    throws IOException
  {
    return;
  }

  protected IFormatReader getReader(String file, boolean odexml)
    throws FormatException, IOException
  {
    IFormatReader reader = new MetamorphReader();
    initReader(reader, file, odexml);
    return reader;
  }

  private String[] getTiffFiles(String plateName, char rowLetter, int col,
    int channels, int nTimepoints, int zSteps)
    throws FormatException
  {
    String well = rowLetter + String.format("%02d", col + 1);
    String base = plateName + well;

    String[] files = new String[fieldCount * channels * nTimepoints * zSteps];

    int nextFile = 0;
    for (int field=0; field<fieldCount; field++) {
      for (int channel=0; channel<channels; channel++) {
        for (int t=0; t<nTimepoints; t++, nextFile++) {
          String file = base;
          if (fieldCount > 1) {
           file += "_s" + (field + 1);
          }
          if (doChannels || channels > 1) {
            file += "_w" + (channel + 1);
          }
          if (nTimepoints > 1) {
            file += "_t" + nTimepoints;
          }
          files[nextFile] = file + ".tif";

          if (!new Location(files[nextFile]).exists()) {
            files[nextFile] = file + ".TIF";
          }
        }
      }
    }

    boolean noneExist = true;
    for (String file : files) {
      if (file != null && new Location(file).exists()) {
        noneExist = false;
        break;
      }
    }

    if (noneExist) {
      nextFile = 0;
      Location parent =
        new Location(currentId).getAbsoluteFile().getParentFile();
      if (directoryList == null) {
        directoryList = parent.list(true);
        Arrays.sort(directoryList);
      }
      for (String f : directoryList) {
        if (checkSuffix(f, new String [] {"tif", "tiff"})) {
          String path = new Location(parent, f).getAbsolutePath();
          if (path.startsWith(base) && path.toLowerCase().indexOf("_thumb") < 0)
          {
            files[nextFile++] = path;
            noneExist = false;
          }
        }
      }

      if (noneExist) {
        subdirectories = true;

        // if all else fails, look for a directory structure:
        //  * file.htd
        //  * TimePoint_<t>
        //    * ZStep_<z>
        //      * file_<...>.tif
        base = base.substring(base.lastIndexOf(File.separator) + 1);
        LOGGER.debug("expected file prefix = {}", base);
        nextFile = 0;
        for (int i=0; i<nTimepoints; i++) {
          Location dir = new Location(parent, "TimePoint_" + (i + 1));
          if (dir.exists() && dir.isDirectory()) {
            for (int z=0; z<zSteps; z++) {
              Location file = new Location(dir, "ZStep_" + (z + 1));
              String[] zList = null;
              if (file.exists() && file.isDirectory()) {
                zList = file.list(true);
              }
              else if (zSteps == 1) {
                // if SizeZ == 1, the TIFF files may be in the
                // TimePoint_<t> directory
                file = dir;
                zList = file.list(true);
              }
              LOGGER.debug("parent directory = {}", file);

              if (zList != null) {
                Arrays.sort(zList);
                for (String f : zList) {
                  LOGGER.debug("  checking relative path = {}", f);
                  String path = new Location(file, f).getAbsolutePath();
                  if (f.startsWith(base) && path.toLowerCase().indexOf("_thumb") < 0) {
                    if (nextFile < files.length) {
                      files[nextFile] = path;
                    }
                    nextFile++;
                  }
                }
              }
            }
          }
        }
        if (nextFile != files.length) {
          LOGGER.warn("Well {} expected {} files; found {}",
            well, files.length, nextFile);
        }
      }
    }

    return files;
  }

}
