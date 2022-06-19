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

import java.util.StringTokenizer;

import org.xml.sax.Attributes;

/**
 * Used by validateXML to parse the XML block's schema path using SAX.
 */
/**  */
class ValidationSAXHandler extends BaseHandler {
  private String schemaPath;
  private boolean first;
  public String getSchemaPath() { return schemaPath; }
  @Override
  public void startDocument() {
    schemaPath = null;
    first = true;
  }
  @Override
  public void startElement(String uri,
    String localName, String qName, Attributes attributes)
  {
    if (!first) return;
    first = false;

    String namespace_attribute = "xmlns" ;
    final int colon = qName.indexOf( ':' ) ;
    if (colon > 0)
    {
        final String namespace_prefix = qName.substring( 0, colon ) ;
        namespace_attribute += ':' + namespace_prefix ;
    }
    int len = attributes.getLength();
    String xmlns = null, xsiSchemaLocation = null;
    for (int i=0; i<len; i++) {
      String name = attributes.getQName(i);
      if (name.equals(namespace_attribute)) xmlns = attributes.getValue(i);
      else if (name.equals("schemaLocation") ||
        name.endsWith(":schemaLocation"))
      {
        xsiSchemaLocation = attributes.getValue(i);
      }
    }
    if (xmlns == null || xsiSchemaLocation == null) return; // not found

    StringTokenizer st = new StringTokenizer(xsiSchemaLocation);
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      if (xmlns.equals(token)) {
        // next token is the actual schema path
        if (st.hasMoreTokens()) schemaPath = st.nextToken();
        break;
      }
    }
  }
}