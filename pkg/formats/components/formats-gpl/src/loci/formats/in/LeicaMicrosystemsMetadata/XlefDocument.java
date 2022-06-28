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