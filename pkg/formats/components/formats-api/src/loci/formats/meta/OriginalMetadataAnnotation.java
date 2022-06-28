package loci.formats.meta;

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

import javax.xml.parsers.DocumentBuilder;

import loci.common.xml.XMLTools;
import ode.xml.model.XMLAnnotation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OriginalMetadataAnnotation extends XMLAnnotation {

  public static final String ORIGINAL_METADATA_NS;
  static {
    ORIGINAL_METADATA_NS = "bhojpur.net/OriginalMetadata";
  }

  private static final DocumentBuilder BUILDER = XMLTools.createBuilder();

  private String key, value;

  // -- OriginalMetadataAnnotation methods --

  /**
   * Set the key/value metadata pair for this annotation.
   * @param key the original metadata key
   * @param value the original metadata value
   */
  public void setKeyValue(String key, String value) {
    setNamespace(ORIGINAL_METADATA_NS);
    this.key = key;
    this.value = value; // Not XML value
    Document doc = BUILDER.newDocument();
    Element r = makeOriginalMetadata(doc);
    super.setValue(XMLTools.dumpXML(null,  doc, r, false));
  }

  // -- XMLAnnotation methods --

  /**
   * @return the original metadata key
   */
  public String getKey() {
    return key;
  }

  /**
   * Return just the value (i.e. v in k=v) as opposed to the XML
   * value which contains the entire block (e.g. &lt;originalmetadata&gt;...)
   */
  public String getValueForKey() {
    return value;
  }

  /**
   * Create a DOM element for this annotation with the given document
   * as the parent.
   * @param document the parent document for this annotation
   * @return the DOM element corresponding to this annotation
   */
  protected Element makeOriginalMetadata(Document document) {

    Element keyElement =
      document.createElement("Key");
    Element valueElement =
      document.createElement("Value");
    keyElement.setTextContent(key);
    valueElement.setTextContent(value);

    Element originalMetadata =
      document.createElement("OriginalMetadata");
    originalMetadata.appendChild(keyElement);
    originalMetadata.appendChild(valueElement);

    return originalMetadata;
  }

}
