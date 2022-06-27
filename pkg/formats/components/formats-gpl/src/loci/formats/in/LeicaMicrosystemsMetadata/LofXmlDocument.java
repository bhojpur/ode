/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.in.LeicaMicrosystemsMetadata;

import org.w3c.dom.Node;

/**
 * This class loads and represents a Leica Microsystems XML document that has been extracted from a LOF file
 */
public class LofXmlDocument extends LMSImageXmlDocument {
  private String name;

  public LofXmlDocument(String xml, String name) {
    super(xml);
    this.name = name;
  }

  @Override
  public Node getImageNode() {
    Node child = GetChildWithName(doc.getDocumentElement(), "Image");
    if (child != null) return child;
    child = GetChildWithName(doc.getDocumentElement(), "Element");
    if (child != null){
      Node elementChild = GetChildWithName(child, "Image");
      if (elementChild != null) return elementChild;
      elementChild = GetChildWithName(child, "Data");
      if (elementChild != null){
        Node dataChild = GetChildWithName(elementChild, "Image");
        if (dataChild != null) return dataChild;
      }
    }
    return null;
  }

  @Override
  public String getImageName(){
    return name;
  }
}
