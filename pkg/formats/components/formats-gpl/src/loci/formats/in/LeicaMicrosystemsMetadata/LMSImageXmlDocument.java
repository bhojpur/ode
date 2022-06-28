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
