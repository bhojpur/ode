package loci.formats.out;

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

import loci.common.RandomAccessInputStream;
import loci.common.RandomAccessOutputStream;
import loci.formats.FormatException;
import loci.formats.meta.IPyramidStore;
import loci.formats.meta.MetadataRetrieve;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffParser;
import loci.formats.tiff.TiffSaver;

/**
 * PyramidODETiffWriter is the file format writer for pyramid ODE-TIFF files.
 */
public class PyramidODETiffWriter extends ODETiffWriter {

  // -- Constructor --

  // -- IFormatHandler API methods --

  /* @see IFormatHandler#isThisType(String) */
  @Override
  public boolean isThisType(String name) {
    if (!super.isThisType(name)) {
      return false;
    }
    if (resolutionData.size() > 0) {
      return true;
    }
    MetadataRetrieve r = getMetadataRetrieve();
    if (!(r instanceof IPyramidStore)) {
      return false;
    }
    return ((IPyramidStore) r).getResolutionCount(0) > 1;
  }

  // -- IFormatWriter API methods --

  @Override
  public void saveBytes(int no, byte[] buf, IFD ifd, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    if (ifd == null) {
      ifd = new IFD();
    }
    if (getResolution() > 0) {
      ifd.put(IFD.NEW_SUBFILE_TYPE, 1);
    }
    else {
      if (!ifd.containsKey(IFD.SUB_IFD)) {
        ifd.put(IFD.SUB_IFD, (long) 0);
      }
    }

    super.saveBytes(no, buf, ifd, x, y, w, h);
  }

  @Override
  public void close() throws IOException {
    String id = currentId;
    MetadataRetrieve r = getMetadataRetrieve();
    int[] planeCounts = new int[r.getImageCount()];
    int[] resCounts = new int[r.getImageCount()];
    for (int i=0; i<planeCounts.length; i++) {
      planeCounts[i] = getPlaneCount(i);
      try {
        setSeries(i);
      }
      catch (FormatException e) {
        throw new IOException(e);
      }
      resCounts[i] = getResolutionCount();
    }
    super.close();

    // post-processing step to fill in all SubIFD arrays
    try {
      boolean littleEndian = false;
      long[] allOffsets = null;
      try (RandomAccessInputStream in = new RandomAccessInputStream(id)) {
        TiffParser parser = new TiffParser(in);
        littleEndian = parser.checkHeader();
        allOffsets = parser.getIFDOffsets();
      }

      int mainIFDIndex = 0;
      int currentFullResolution = 0;
      for (int i=0; i<r.getImageCount(); i++) {
        setSeries(i);
        int resCount = resCounts[i];
        for (int p=0; p<planeCounts[i]; p++) {
          long[] subIFDOffsets = new long[resCount - 1];
          for (int res=0; res<subIFDOffsets.length; res++) {
            subIFDOffsets[res] = allOffsets[mainIFDIndex + (res + 1) * planeCounts[i]];
          }

          try (RandomAccessOutputStream out = new RandomAccessOutputStream(id);
               RandomAccessInputStream in = new RandomAccessInputStream(id)) {
            out.order(littleEndian);
            in.order(littleEndian);
            TiffSaver saver = new TiffSaver(out, id);
            saver.setBigTiff(isBigTiff);
            saver.setLittleEndian(littleEndian);
            int index = mainIFDIndex + 1;
            if (p == planeCounts[i] - 1) {
              index += (planeCounts[i] * (resCount - 1));
            }
            long nextPointer = index < allOffsets.length ? allOffsets[index] : 0;

            saver.overwriteIFDOffset(in, allOffsets[mainIFDIndex], nextPointer);
            saver.overwriteIFDValue(in, currentFullResolution, IFD.SUB_IFD, subIFDOffsets);
          }

          mainIFDIndex++;
          currentFullResolution++;
        }
        mainIFDIndex += (planeCounts[i] * (resCount - 1));
      }
      setSeries(0);
    }
    catch (FormatException e) {
      throw new IOException("Failed to assemble SubIFD offsets", e);
    }
  }

}
