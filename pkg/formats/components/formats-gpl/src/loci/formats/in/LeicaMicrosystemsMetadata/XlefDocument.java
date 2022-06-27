/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.in.LeicaMicrosystemsMetadata;

import java.util.List;
import org.w3c.dom.Node;

/**
 * This class represents a Leica Microsystems XLEF xml document,
 * a project file which references xlifs and optionally xlcfs
 */
public class XlefDocument extends LMSCollectionXmlDocument {

  // -- Constructor --
  public XlefDocument(String filepath) {
    super(filepath, null);
    initChildren();
  }

  // -- Getters --

  /**
   * Returns number of images which are referenced by xlifs
   * 
   * @return image number
   */
  public int getImageCount() {
    List<XlifDocument> xlifs = getXlifs();

    int imgCount = 0;
    for (LMSXmlDocument xlif : xlifs) {
      imgCount += ((XlifDocument)xlif).getImagePaths().size();
    }
    return imgCount;
  }

  // -- Methods --

  public void printReferences() {
    List<XlifDocument> xlifs = getXlifs();
    LOGGER.info("-------- XLEF INFO: " + xlifs.size() + " images found -------- ");
    for (XlifDocument xlif : xlifs) {
      xlif.printXlifInfo();
    }
    LOGGER.info("-------------------------------------------");
  }
}
