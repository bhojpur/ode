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

import java.io.IOException;
import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.ImageReader;
import loci.formats.meta.IMetadata;
import loci.formats.services.ODEXMLService;

/**
 * Extracts and prints out the ODE-XML for a given file.
 */
public class DumpODEXML {

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.out.println("Usage: java DumpODEXML file1 file2 ...");
      return;
    }
    for (int i=0; i<args.length; i++) dumpODEXML(args[i]);
  }

  public static void dumpODEXML(String path) throws FormatException,
    IOException, DependencyException, ServiceException
  {
    ServiceFactory serviceFactory = new ServiceFactory();
    ODEXMLService odexmlService =
      serviceFactory.getInstance(ODEXMLService.class);
    IMetadata meta = odexmlService.createODEXMLMetadata();

    ImageReader r = new ImageReader();
    r.setMetadataStore(meta);
    r.setOriginalMetadataPopulated(true);
    r.setId(path);
    r.close();
    String xml = odexmlService.getODEXML(meta);
    System.out.println(xml);
  }

}
