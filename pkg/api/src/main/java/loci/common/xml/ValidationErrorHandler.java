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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * Used by validateXML to handle XML validation errors.
 */
public class ValidationErrorHandler implements ErrorHandler {

  // -- Fields --

  private int errors = 0;

  // -- ValidatorErrorHandler API --

  public boolean ok() {
    return errors == 0;
  }

  public int getErrorCount() {
    return errors;
  }

  @Override
  public void error(SAXParseException e) {
    XMLTools.LOGGER.error(e.getMessage());
    errors++;
  }

  @Override
  public void fatalError(SAXParseException e) {
    XMLTools.LOGGER.error(e.getMessage());
    errors++;
  }

  @Override
  public void warning(SAXParseException e) {
    XMLTools.LOGGER.error(e.getMessage());
    errors++;
  }

}