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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import loci.common.RandomAccessInputStream;
import loci.formats.FormatTools;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffIFDEntry;
import loci.formats.tiff.TiffParser;

/**
 * MikroscanTiffReader is the file format reader for Mikroscan TIFF files.
 */
public class MikroscanTiffReader extends SVSReader {
   
  // -- Constants --
    
  /** Logger for this class. */
  private static final Logger LOGGER =
    LoggerFactory.getLogger(MikroscanTiffReader.class);

  /** TIFF image description prefix for Mikroscan TIF files. */
  private static final String MIKROSCAN_IMAGE_DESCRIPTION_PREFIX = "Mikroscan Image"; 

  // -- Constructor --  
  
  /** Constructs a new Mikroscan TIF reader. */
  public MikroscanTiffReader() {
    super("Mikroscan TIFF", new String[] {"tif", "tiff"});
    domains = new String[] {FormatTools.HISTOLOGY_DOMAIN, FormatTools.LM_DOMAIN};
    suffixNecessary = false;
    suffixSufficient = false;
  }

  // -- IFormatReader API methods --
  
  /* (non-Javadoc)
   * @see loci.formats.FormatReader#isThisType(java.lang.String, boolean)
   */
  @Override
  public boolean isThisType(String name, boolean open) {
    boolean isThisType = super.isThisType(name, open);
    if (isThisType && open) {
      try (RandomAccessInputStream stream = new RandomAccessInputStream(name)) {
        TiffParser tiffParser = new TiffParser(stream);
        tiffParser.setDoCaching(false);
        if (!tiffParser.isValidHeader()) {
          return false;
        }
        IFD ifd = tiffParser.getFirstIFD();
        if (ifd == null) {
          return false;
        }
        Object description = ifd.get(IFD.IMAGE_DESCRIPTION);
        if (description != null) {
          String imageDescription = null;

          if (description instanceof TiffIFDEntry) {
            Object value = tiffParser.getIFDValue((TiffIFDEntry) description);
            if (value != null) {
              imageDescription = value.toString();
            }
          }
          else if (description instanceof String) {
            imageDescription = (String) description;
          }
          if (imageDescription != null
              && imageDescription.startsWith(MIKROSCAN_IMAGE_DESCRIPTION_PREFIX)) {
            return true;
          }
        }
        return false;
      }
      catch (IOException e) {
        LOGGER.debug("I/O exception during isThisType() evaluation.", e);
        return false;
      }
    }
    return isThisType;
  }
}
