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
