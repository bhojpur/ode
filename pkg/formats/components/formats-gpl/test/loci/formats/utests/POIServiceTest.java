package loci.formats.utests;

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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import loci.common.RandomAccessInputStream;
import loci.common.services.DependencyException;
import loci.common.services.ServiceFactory;
import loci.formats.services.POIService;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class POIServiceTest {

  private POIService service;

  private static final String TEST_XLS = "test.xls";

  private static final String WORKBOOK_DOCUMENT = "Root Entry" + File.separator + "Workbook";

  private static final int WORKBOOK_LENGTH = 9604;

  @BeforeMethod
  public void setUp() throws DependencyException, IOException {
    ServiceFactory sf = new ServiceFactory();
    service = sf.getInstance(POIService.class);
    URL file = this.getClass().getResource(TEST_XLS);
    service.initialize(file.getPath());
  }

  @Test
  public void testDocuments() {
    Vector<String> documents = service.getDocumentList();
    for (String document : documents) {
      if (document.equals(WORKBOOK_DOCUMENT)) {
        return;
      }
    }
    fail("Unable to find document: " + WORKBOOK_DOCUMENT);
  }

  @Test
  public void testWorkbookDocumentAsStream() throws IOException {
    RandomAccessInputStream stream = 
      service.getDocumentStream(WORKBOOK_DOCUMENT); 
    assertNotNull(stream);
    assertEquals(WORKBOOK_LENGTH, stream.length());
  }

  @Test
  public void testWorkbookDocumentBytes() throws IOException {
    byte[] bytes = service.getDocumentBytes(WORKBOOK_DOCUMENT); 
    assertNotNull(bytes);
    assertEquals(WORKBOOK_LENGTH, bytes.length);
  }

  @Test
  public void testWorkbookFileSize() {
    assertEquals(WORKBOOK_LENGTH, service.getFileSize(WORKBOOK_DOCUMENT));
  }
}
