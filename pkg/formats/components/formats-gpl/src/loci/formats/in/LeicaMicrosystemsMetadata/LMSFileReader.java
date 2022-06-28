package loci.formats.in.LeicaMicrosystemsMetadata;

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

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.Logger;

import loci.formats.FormatReader;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.meta.MetadataStore;
import loci.formats.in.MetadataOptions;
import loci.formats.in.DynamicMetadataOptions;

/**
 * This class can be extended by readers of Leica Microsystems file formats that
 * include LASX generated XML (e.g. XLEF, LOF)com
 */
public abstract class LMSFileReader extends FormatReader {
  // -- Constants --
  public static final String OLD_PHYSICAL_SIZE_KEY = "leicalif.old_physical_size";
  public static final boolean OLD_PHYSICAL_SIZE_DEFAULT = false;

  // -- Fields --
  public static Logger log;
  public MetadataTempBuffer metaTemp;
  public LMSXmlDocument associatedXmlDoc; //an optional LMS xml file that references the file(s) that are read by this reader

  /** file format in which actual image bytes are stored */
  public enum ImageFormat {
    LOF, TIF, BMP, JPEG, PNG, UNKNOWN
  }

  // -- Constructor --
  protected LMSFileReader(String format, String suffix) {
    super(format, suffix);
    LMSFileReader.log = LOGGER;
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);

    if (!fileOnly){
      metaTemp = null;
    }
  }

  // -- Methods --

  /**
   * Returns the file format in which actual image bytes are stored
   */
  public abstract ImageFormat getImageFormat();

  public List<CoreMetadata> getCore() {
    return core;
  }

  public void setCore(ArrayList<CoreMetadata> core) {
    this.core = core;
  }

  public boolean useOldPhysicalSizeCalculation() {
    MetadataOptions options = getMetadataOptions();
    if (options instanceof DynamicMetadataOptions) {
      return ((DynamicMetadataOptions) options).getBoolean(OLD_PHYSICAL_SIZE_KEY, OLD_PHYSICAL_SIZE_DEFAULT);
    }
    return OLD_PHYSICAL_SIZE_DEFAULT;
  }

  public void addSeriesMeta(String key, Object value) {
    super.addMeta(key, value, getCurrentCore().seriesMetadata);
  }

  public void addSeriesMetaList(String key, Object value) {
    super.addSeriesMetaList(key, value);
  }

  public MetadataStore makeFilterMetadata() {
    return super.makeFilterMetadata();
  }

  /**
   * Extracts metadata from a list of Leica image XML Documents and writes them to
   * the reader's CoreMetadata, {@link MetadataTempBuffer} and MetadataStore
   * 
   * @param docs List of document nodes representing XML documents
   * @throws FormatException
   * @throws IOException
   */
  public void translateMetadata(List<LMSImageXmlDocument> docs) throws FormatException, IOException {
    int len = docs.size();
    metaTemp = new MetadataTempBuffer(len);

    LMSMetadataExtractor extractor = new LMSMetadataExtractor(this);
    extractor.translateMetadata(docs);

    MetadataStoreInitializer initializer = new MetadataStoreInitializer(this);
    initializer.initMetadataStore();
  }

  public void translateMetadata(LMSImageXmlDocument doc) throws FormatException, IOException{
    List<LMSImageXmlDocument> docs = new ArrayList<>();
    docs.add(doc);
    translateMetadata(docs);
  }

  // -- Helper functions --

  /**
   * Checks if file at path exists
   * 
   * @param path whole file path
   */
  public static boolean fileExists(String path) {
    if (path != null && !path.trim().isEmpty()) {
      try {
        File f = new File(path);
        if (f.exists()) {
          return true;
        }
      } catch (Exception e) {
        return false;
      }
    }
    return false;
  }
}
