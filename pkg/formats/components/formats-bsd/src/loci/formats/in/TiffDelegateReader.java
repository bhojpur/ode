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

import java.io.IOException;
import java.util.ArrayList;

import loci.formats.CoreMetadata;
import loci.formats.DelegateReader;
import loci.formats.FormatException;
import loci.formats.UnsupportedCompressionException;

/**
 * TiffDelegateReader is a file format reader for TIFF files.
 * It does not read files directly, but chooses which TIFF reader
 * is more appropriate.
 *
 * @see TiffReader
 * @see TiffJAIReader
 */
public class TiffDelegateReader extends DelegateReader {

  // -- Constructor --

  /** Constructs a new TIFF reader. */
  public TiffDelegateReader() {
    super("Tagged Image File Format", TiffReader.TIFF_SUFFIXES);
    nativeReader = new TiffReader();
    legacyReader = new TiffJAIReader();
    nativeReaderInitialized = false;
    legacyReaderInitialized = false;
    suffixNecessary = false;
  }

  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    if (!isLegacy()) {
      try {
        return nativeReader.openBytes(no, buf, x, y, w, h);
      }
      catch (UnsupportedCompressionException e) {
        LOGGER.debug("Could not open plane with native reader", e);
        if (!legacyReaderInitialized) {
          legacyReader.setId(getCurrentFile());
          legacyReaderInitialized = true;
          nativeReader.close();
          nativeReaderInitialized = false;
        }
        return legacyReader.openBytes(no, buf, x, y, w, h);
      }
    }
    return super.openBytes(no, buf, x, y, w, h);
  }

  @Override
  public void setId(String id) throws FormatException, IOException {
    if (isLegacy()) {
      super.setId(id);
    }
    nativeReader.setId(id);
    nativeReaderInitialized = true;
    currentId = nativeReader.getCurrentFile();
    core = new ArrayList<CoreMetadata>(nativeReader.getCoreMetadataList());
    metadata = nativeReader.getGlobalMetadata();
    metadataStore = nativeReader.getMetadataStore();
  }

}
