/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.in.LeicaMicrosystemsMetadata;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NodeList;

/**
 * This class represents an LMS xml file that references other LMS xml files
 * such as xlcfs and xlifs.
 */
public class LMSCollectionXmlDocument extends LMSXmlDocument {

  // -- Fields --
  private List<LMSXmlDocument> children = new ArrayList<>();

  // -- Constructor --

  public LMSCollectionXmlDocument(String filepath, LMSCollectionXmlDocument parent) {
    super(filepath, parent);
  }

  // -- Getters --

  /**
   * Returns all XlifDocuments referenced by this and by all referenced
   * XlcfDocuments
   * 
   * @return list of xlifs as XlifDocuments
   */
  public List<XlifDocument> getXlifs() {
    List<XlifDocument> xlifs = new ArrayList<>();
    for (LMSXmlDocument child : children) {
      if (child instanceof XlifDocument) {
        xlifs.add((XlifDocument) child);
      } else if (child instanceof XlcfDocument) {
        xlifs.addAll(((XlcfDocument) child).getXlifs());
      }
    }
    return xlifs;
  }

  /**
   * Returns filepaths of all files which are referenced by this document.
   * 
   * @param pixels if true, image files in which pixels are stored are included
   * @return
   */
  public List<String> getChildrenFiles(boolean pixels) {
    List<String> files = new ArrayList<>();
    for (LMSXmlDocument child : children) {
      files.add(child.filepath);
      if (child instanceof XlcfDocument) {
        files.addAll(((XlcfDocument) child).getChildrenFiles(pixels));
      } else if (child instanceof XlifDocument && pixels) {
        files.addAll(((XlifDocument) child).getImagePaths());
      }
    }
    return files;
  }

  // -- Methods --

  /** Adds all referenced xlcfs and valid xlifs as children */
  protected void initChildren() {
    NodeList references = xPath("//Reference");
    LOGGER.info("References Found: " + references.getLength());
    for (int i = 0; i < references.getLength(); i++) {
      String path = parseFilePath(getAttr(references.item(i), "File"));
      String correctedPath = fileExists(path);
      if (correctedPath != null) {
        if (correctedPath.endsWith(".xlif")) {
          XlifDocument xlif = new XlifDocument(correctedPath, this);
          if (xlif.isValid()) {
            children.add(xlif);
          }
          else {
            LOGGER.warn("XLIF file is invalid: " + correctedPath);
          }
        } else if (correctedPath.endsWith(".xlcf")) {
          children.add(new XlcfDocument(correctedPath, this));
        }
      }
      else {
        LOGGER.warn("Expected file at image path does not exist: " + path);
      }
    }
  }
  
}
