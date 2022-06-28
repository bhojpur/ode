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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import loci.common.IRandomAccess;
import loci.common.Location;
import loci.common.RandomAccessInputStream;
import loci.common.ZipHandle;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.ImageReader;

/**
 * Reader for Zip files.
 */
public class ZipReader extends FormatReader {

  // -- Fields --

  private transient ImageReader reader;
  private String entryName;

  private ArrayList<String> mappedFiles = new ArrayList<String>();

  // -- Constructor --

  public ZipReader() {
    super("Zip", "zip");
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#get8BitLookupTable() */
  @Override
  public byte[][] get8BitLookupTable() throws FormatException, IOException {
    return reader.get8BitLookupTable();
  }

  /* @see loci.formats.IFormatReader#get16BitLookupTable() */
  @Override
  public short[][] get16BitLookupTable() throws FormatException, IOException {
    return reader.get16BitLookupTable();
  }

  /* @see loci.formats.IFormatReader#setGroupFiles(boolean) */
  @Override
  public void setGroupFiles(boolean groupFiles) {
    super.setGroupFiles(groupFiles);
    if (reader != null) reader.setGroupFiles(groupFiles);
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    if (Location.getMappedFile(entryName) == null) {
      reader.close();
      findZipEntries();
    }
    reader.setId(entryName);
    return reader.openBytes(no, buf, x, y, w, h);
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (reader != null) reader.close(fileOnly);
    if (!fileOnly) reader = null;
    for (String name : mappedFiles) {
      IRandomAccess handle = Location.getMappedFile(name);
      Location.mapFile(name, null);
      if (handle != null) {
        handle.close();
      }
    }
    mappedFiles.clear();
    entryName = null;
  }

  @Override
  public void reopenFile() throws IOException {
    if (reader != null) {
      reader.close();
    }
    else {
      reader = new ImageReader();
    }
    findZipEntries();
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    if (reader != null) {
      reader.close();
    }
    reader = new ImageReader();

    reader.setMetadataOptions(getMetadataOptions());
    reader.setMetadataFiltered(isMetadataFiltered());
    reader.setOriginalMetadataPopulated(isOriginalMetadataPopulated());
    reader.setNormalized(isNormalized());
    reader.setMetadataStore(getMetadataStore());

    findZipEntries();

    if (entryName == null) {
      throw new FormatException("Zip file does not contain any valid files");
    }

    reader.setId(entryName);

    metadataStore = reader.getMetadataStore();
    core = new ArrayList<CoreMetadata>(reader.getCoreMetadataList());
    metadata = reader.getGlobalMetadata();
  }

  private void findZipEntries() throws IOException {
    String innerFile = currentId;
    if (checkSuffix(currentId, "zip")) {
      innerFile = currentId.substring(0, currentId.length() - 4);
    }
    int sep = innerFile.lastIndexOf(File.separator);
    if (sep < 0) {
      sep = innerFile.lastIndexOf("/");
    }
    if (sep >= 0) {
      innerFile = innerFile.substring(sep + 1);
    }

    // NB: We need a raw handle on the ZIP data itself, not a ZipHandle.
    IRandomAccess rawHandle = Location.getHandle(currentId, false, false);
    try (RandomAccessInputStream in = new RandomAccessInputStream(rawHandle, currentId)) {
      ZipInputStream zip = new ZipInputStream(in);
      ZipEntry ze = null;
      entryName = null;
      boolean matchFound = false;
      while (true) {
        ze = zip.getNextEntry();
        if (ze == null) break;

        if (entryName == null) {
          entryName = ze.getName();
        }

        if (!matchFound && ze.getName().startsWith(innerFile)) {
          entryName = ze.getName();
          matchFound = true;
        }

        if (Location.getMappedFile(ze.getName()) != null) {
          Location.getMappedFile(ze.getName()).close();
        }
        ZipHandle handle = new ZipHandle(currentId, ze);
        Location.mapFile(ze.getName(), handle);
        mappedFiles.add(ze.getName());
      }
    }
  }

}
