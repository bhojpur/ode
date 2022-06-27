/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.in.LeicaMicrosystemsMetadata;

import org.w3c.dom.Node;

/**
 * This class loads and represents a Leica Microsystems image XML document that contains image metadata 
 */
public abstract class LMSImageXmlDocument extends LMSXmlDocument {
  public LMSImageXmlDocument(String xml) {
    super(xml);
  }

  public LMSImageXmlDocument(String filepath, LMSCollectionXmlDocument parent){
    super(filepath, parent);
  }


  /**
   * Returns the image node of the xml document which contains image metadata
   */
  public abstract Node getImageNode();

  /**
   * Returns the name of the image (it might be contained in the XML or otherwise e.g. in the file name)
   */
  public abstract String getImageName();
}
