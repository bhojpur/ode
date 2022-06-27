/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.in.LeicaMicrosystemsMetadata;

/**
 * This class loads and represents a Leica Microsystems XLCF xml document
 */
public class XlcfDocument extends LMSCollectionXmlDocument {

  // -- Constructor --
  public XlcfDocument(String filepath, LMSCollectionXmlDocument parent) {
    super(filepath, parent);
    initChildren();
  }
}
