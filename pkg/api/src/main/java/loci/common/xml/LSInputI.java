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

import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;

/**
 * A simple bean implementation of the {@link LSInput} interface.
 * Entity resolvers use these instances to provide XML schema definitions.
 */
public class LSInputI implements LSInput {

  private Reader characterStream;
  private InputStream byteStream;
  private String stringData;
  private String systemId;
  private String publicId;
  private String baseURI;
  private String encoding;
  private boolean certifiedText;

  @Override
  public Reader getCharacterStream() {
    return characterStream;
  }

  @Override
  public void setCharacterStream(Reader characterStream) {
    this.characterStream = characterStream;
  }

  @Override
  public InputStream getByteStream() {
    return byteStream;
  }

  @Override
  public void setByteStream(InputStream byteStream) {
    this.byteStream = byteStream;
  }

  @Override
  public String getStringData() {
    return stringData;
  }

  @Override
  public void setStringData(String stringData) {
    this.stringData = stringData;
  }

  @Override
  public String getSystemId() {
    return systemId;
  }

  @Override
  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }

  @Override
  public String getPublicId() {
    return publicId;
  }

  @Override
  public void setPublicId(String publicId) {
    this.publicId = publicId;
  }

  @Override
  public String getBaseURI() {
    return baseURI;
  }

  @Override
  public void setBaseURI(String baseURI) {
    this.baseURI = baseURI;
  }

  @Override
  public String getEncoding() {
    return encoding;
  }

  @Override
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  @Override
  public boolean getCertifiedText() {
    return certifiedText;
  }

  @Override
  public void setCertifiedText(boolean certifiedText) {
    this.certifiedText = certifiedText;
  }
}