package loci.common.xml;

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

import java.util.Hashtable;

import org.xml.sax.Attributes;

/**
 * Used to retrieve key/value pairs from XML.
 */
class MetadataHandler extends BaseHandler {
  private String currentQName;
  private Hashtable<String, String> metadata =
    new Hashtable<String, String>();

  // -- MetadataHandler API methods --

  public Hashtable<String, String> getMetadata() {
    return metadata;
  }

  // -- DefaultHandler API methods --

  @Override
  public void characters(char[] data, int start, int len) {
    metadata.put(currentQName, new String(data, start, len));
  }

  @Override
  public void startElement(String uri, String localName, String qName,
    Attributes attributes)
  {
    if (attributes.getLength() == 0) currentQName += " - " + qName;
    else currentQName = qName;
    for (int i=0; i<attributes.getLength(); i++) {
      metadata.put(qName + " - " + attributes.getQName(i),
        attributes.getValue(i));
    }
  }
}